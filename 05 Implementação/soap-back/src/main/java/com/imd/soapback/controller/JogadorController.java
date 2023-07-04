package main.java.com.imd.soapback.controller;

import java.sql.SQLException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import main.java.com.imd.soapback.dao.JogadorDAO;
import main.java.com.imd.soapback.interfaceDAO.IJogador;
import main.java.com.imd.soapback.model.Jogador;

@Controller
@RequestMapping(value = "/jogador")
public class JogadorController {
    
    @GetMapping("/getAll")
    public ResponseEntity<String> getAll() throws SQLException {
		IJogador jogDao = new JogadorDAO("jdbc:mysql://localhost/soap?useTimezone=true&serverTimezone=America/Fortaleza","root","root");

		List<Jogador> listjogadores = jogDao.search();
        String resultString = "";
		for (Jogador jog : listjogadores) {
			resultString += jog.getId() + " " + jog.getNome() + " " + jog.getDinheiro() + " / ";
		}
                
        return new ResponseEntity<String>(resultString, null, HttpStatus.OK);
    }
}
