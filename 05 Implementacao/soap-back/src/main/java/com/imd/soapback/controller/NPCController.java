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
import main.java.com.imd.soapback.dao.NPCDAO;
import main.java.com.imd.soapback.interfaceDAO.INPC;
import main.java.com.imd.soapback.model.NPC;

@Controller
@RequestMapping(value = "/npc")
public class NPCController {
    
    @GetMapping("/getAll")
    public ResponseEntity<?> getAll() throws SQLException {
        try{
            INPC dao = new NPCDAO(ConFactory.DAO_PATH, ConFactory.USER, ConFactory.PASSWORD);
            List<NPC> list = dao.searchAll();

            return new ResponseEntity<List<NPC>>(list, null, HttpStatus.OK);
        } catch(Exception e){
            return new ResponseEntity<String>("Exception: " + e.getStackTrace(), null, HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/newNPC")
    public ResponseEntity<?> newNPC(@RequestBody NPC newData) throws Exception {
        try{
            INPC dao = new NPCDAO(ConFactory.DAO_PATH, ConFactory.USER, ConFactory.PASSWORD);

            if(newData.getId() == null){
                return new ResponseEntity<String>("NPC id must not be null.", null, HttpStatus.FORBIDDEN);
            } else if(dao.search(newData.getId()).getId() != null){
                return new ResponseEntity<String>("NPC with id=" + newData.getId() + " already exists.", null, HttpStatus.FORBIDDEN);
            }

            
            dao.insert(newData);
            ((NPCDAO)dao).commit();

            return new ResponseEntity<NPC>(newData, null, HttpStatus.OK);            
        } catch(Exception e){
            return new ResponseEntity<String>("Exception: " + e.getStackTrace(), null, HttpStatus.FORBIDDEN);
        }
    }
}
