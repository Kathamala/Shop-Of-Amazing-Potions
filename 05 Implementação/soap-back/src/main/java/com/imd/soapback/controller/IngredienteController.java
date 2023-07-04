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
import main.java.com.imd.soapback.dao.IngredienteDAO;
import main.java.com.imd.soapback.interfaceDAO.IIngrediente;
import main.java.com.imd.soapback.model.Ingrediente;


@Controller
@RequestMapping(value = "/ingrediente")
public class IngredienteController {
    
    @GetMapping("/getAll")
    public ResponseEntity<?> getAll() throws SQLException {
        try{
            IIngrediente dao = new IngredienteDAO(ConFactory.DAO_PATH, ConFactory.USER, ConFactory.PASSWORD);
            List<Ingrediente> list = dao.searchAll();

            return new ResponseEntity<List<Ingrediente>>(list, null, HttpStatus.OK);
        } catch(Exception e){
            return new ResponseEntity<String>("Exception: " + e.getStackTrace(), null, HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/newIngrediente")
    public ResponseEntity<?> newingrediente(@RequestBody Ingrediente newData) throws Exception {
        try{
            IIngrediente dao = new IngredienteDAO(ConFactory.DAO_PATH, ConFactory.USER, ConFactory.PASSWORD);

            if(newData.getId() == null){
                return new ResponseEntity<String>("Ingredient id must not be null.", null, HttpStatus.FORBIDDEN);
            } else if(dao.search(newData.getId()).getId() != null){
                return new ResponseEntity<String>("Ingredient with id=" + newData.getId() + " already exists.", null, HttpStatus.FORBIDDEN);
            }

            dao.insert(newData);
            ((IngredienteDAO)dao).commit();

            return new ResponseEntity<Ingrediente>(newData, null, HttpStatus.OK);           
        } catch(Exception e){
            return new ResponseEntity<String>("Exception: " + e.getStackTrace(), null, HttpStatus.FORBIDDEN);
        }
    }
}
