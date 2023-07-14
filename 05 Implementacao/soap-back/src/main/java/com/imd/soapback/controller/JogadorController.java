package main.java.com.imd.soapback.controller;

import java.sql.SQLException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import main.java.com.imd.soapback.connection.ConFactory;
import main.java.com.imd.soapback.dao.JogadorDAO;
import main.java.com.imd.soapback.interfaceDAO.IJogador;
import main.java.com.imd.soapback.model.Jogador;

@Controller
@RequestMapping(value = "/jogador")
public class JogadorController {
    
    @GetMapping("/getAll")
    public ResponseEntity<?> getAll() throws SQLException {
        try{
		    IJogador dao = new JogadorDAO(ConFactory.DAO_PATH, ConFactory.USER, ConFactory.PASSWORD);
		    List<Jogador> list = dao.searchAll();

            return new ResponseEntity<List<Jogador>>(list, null, HttpStatus.OK);
        } catch(Exception e){
            return new ResponseEntity<String>("Exception: " + e.getStackTrace(), null, HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/newPlayer")
    public ResponseEntity<?> newPlayer(@RequestBody Jogador newData) throws Exception {
        try{
            IJogador dao = new JogadorDAO(ConFactory.DAO_PATH, ConFactory.USER, ConFactory.PASSWORD);

            if(newData.getId() == null){
                return new ResponseEntity<String>("Player id must not be null.", null, HttpStatus.FORBIDDEN);
            } else if(dao.search(newData.getId()).getId() != null){
                return new ResponseEntity<String>("Player with id=" + newData.getId() + " already exists.", null, HttpStatus.FORBIDDEN);
            }

            dao.insert(newData);
            ((JogadorDAO)dao).commit();

            return new ResponseEntity<Jogador>(newData, null, HttpStatus.OK);
        } catch(Exception e){
            return new ResponseEntity<String>("Exception: " + e.getStackTrace(), null, HttpStatus.FORBIDDEN);
        }
    }
}
