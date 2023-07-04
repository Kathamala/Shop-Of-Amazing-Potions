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
import main.java.com.imd.soapback.dao.PocaoDAO;
import main.java.com.imd.soapback.interfaceDAO.IPocao;
import main.java.com.imd.soapback.model.Pocao;


@Controller
@RequestMapping(value = "/pocao")
public class PocaoController {
    
    @GetMapping("/getAll")
    public ResponseEntity<?> getAll() throws SQLException {
        try{
            IPocao dao = new PocaoDAO(ConFactory.DAO_PATH, ConFactory.USER, ConFactory.PASSWORD);
            List<Pocao> list = dao.searchAll();

            return new ResponseEntity<List<Pocao>>(list, null, HttpStatus.OK);
        } catch(Exception e){
            return new ResponseEntity<String>("Exception: " + e.getStackTrace(), null, HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/newPocao")
    public ResponseEntity<?> newPocao(@RequestBody Pocao newData) throws Exception {
        try{
            IPocao dao = new PocaoDAO(ConFactory.DAO_PATH, ConFactory.USER, ConFactory.PASSWORD);

            if(newData.getId() == null){
                return new ResponseEntity<String>("Potion id must not be null.", null, HttpStatus.FORBIDDEN);
            } else if(dao.search(newData.getId()).getId() != null){
                return new ResponseEntity<String>("Potion with id=" + newData.getId() + " already exists.", null, HttpStatus.FORBIDDEN);
            }

            dao.insert(newData);
            ((PocaoDAO)dao).commit();

            return new ResponseEntity<Pocao>(newData, null, HttpStatus.OK);           
        } catch(Exception e){
            return new ResponseEntity<String>("Exception: " + e.getStackTrace(), null, HttpStatus.FORBIDDEN);
        }
    }
}
