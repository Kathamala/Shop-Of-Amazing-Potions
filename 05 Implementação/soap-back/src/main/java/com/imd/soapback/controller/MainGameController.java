package main.java.com.imd.soapback.controller;

import java.sql.SQLException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import main.java.com.imd.soapback.connection.ConFactory;
import main.java.com.imd.soapback.dao.JogadorDAO;
import main.java.com.imd.soapback.interfaceDAO.IJogador;
import main.java.com.imd.soapback.model.Jogador;

@Controller
@RequestMapping(value = "/maingame")
public class MainGameController {

    //JOGADOR
    @GetMapping("/login")
    public ResponseEntity<?> login(@RequestParam String name) throws SQLException {
        try{
		    IJogador dao = new JogadorDAO(ConFactory.DAO_PATH, ConFactory.USER, ConFactory.PASSWORD);
		    Jogador obj = dao.searchByName(name);

            if(obj.getId() == null){
                return new ResponseEntity<String>("Jogador " + name + " não encontrado.", null, HttpStatus.FORBIDDEN);    
            }

            return new ResponseEntity<Jogador>(obj, null, HttpStatus.OK);
        } catch(Exception e){
            return new ResponseEntity<String>("Exception: " + e.getStackTrace(), null, HttpStatus.FORBIDDEN);
        }
    }
}
