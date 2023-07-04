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
import main.java.com.imd.soapback.dao.CondicoesDAO;
import main.java.com.imd.soapback.interfaceDAO.ICondicoes;
import main.java.com.imd.soapback.model.Condicoes;

@Controller
@RequestMapping(value = "/condicoes")
public class CondicaoController {
    
    @GetMapping("/getAll")
    public ResponseEntity<?> getAll() throws SQLException {
        try{
            ICondicoes dao = new CondicoesDAO(ConFactory.DAO_PATH, ConFactory.USER, ConFactory.PASSWORD);
            List<Condicoes> list = dao.searchAll();

            return new ResponseEntity<List<Condicoes>>(list, null, HttpStatus.OK);
        } catch(Exception e){
            return new ResponseEntity<String>("Exception: " + e.getStackTrace(), null, HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/newCondicoes")
    public ResponseEntity<?> newCondicoes(@RequestBody Condicoes newData) throws Exception {
        try{
            ICondicoes dao = new CondicoesDAO(ConFactory.DAO_PATH, ConFactory.USER, ConFactory.PASSWORD);

            if(newData.getId() == null){
                return new ResponseEntity<String>("Condition id must not be null.", null, HttpStatus.FORBIDDEN);
            } else if(dao.search(newData.getId()).getId() != null){
                return new ResponseEntity<String>("Condition with id=" + newData.getId() + " already exists.", null, HttpStatus.FORBIDDEN);
            }

            dao.insert(newData);
            ((CondicoesDAO)dao).commit();

            return new ResponseEntity<Condicoes>(newData, null, HttpStatus.OK);           
        } catch(Exception e){
            return new ResponseEntity<String>("Exception: " + e.getStackTrace(), null, HttpStatus.FORBIDDEN);
        }
    }
}
