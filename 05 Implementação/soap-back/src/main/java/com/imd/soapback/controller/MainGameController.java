package main.java.com.imd.soapback.controller;

import java.sql.SQLException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import main.java.com.imd.soapback.connection.ConFactory;
import main.java.com.imd.soapback.dao.IngredienteDAO;
import main.java.com.imd.soapback.dao.JogadorDAO;
import main.java.com.imd.soapback.dao.NPCDAO;
import main.java.com.imd.soapback.dao.PocaoDAO;
import main.java.com.imd.soapback.interfaceDAO.IIngrediente;
import main.java.com.imd.soapback.interfaceDAO.IJogador;
import main.java.com.imd.soapback.interfaceDAO.INPC;
import main.java.com.imd.soapback.interfaceDAO.IPocao;
import main.java.com.imd.soapback.model.Ingrediente;
import main.java.com.imd.soapback.model.Jogador;
import main.java.com.imd.soapback.model.NPC;
import main.java.com.imd.soapback.model.Pocao;

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

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestParam String name) throws SQLException {
        try{
		    IJogador dao = new JogadorDAO(ConFactory.DAO_PATH, ConFactory.USER, ConFactory.PASSWORD);
		    Jogador obj = dao.searchByName(name);

            if(obj.getId() != null){
                return new ResponseEntity<String>("Jogador " + name + " já existe.", null, HttpStatus.FORBIDDEN);    
            }

            obj = new Jogador(dao.getNextId(), name, 100f);
            if(obj.getId() == 0){
                return new ResponseEntity<String>("Não foi possível cadastrar o novo jogador.", null, HttpStatus.FORBIDDEN);    
            }

            dao.insert(obj);

            return new ResponseEntity<Jogador>(obj, null, HttpStatus.OK);
        } catch(Exception e){
            return new ResponseEntity<String>("Exception: " + e.getStackTrace(), null, HttpStatus.FORBIDDEN);
        }
    }

    //MAIN MENU
    @GetMapping("/visualizarInventario")
    public ResponseEntity<?> visualizarInventario(@RequestParam Integer jogadorId) throws SQLException {
        try{
            IPocao daoPocao = new PocaoDAO(ConFactory.DAO_PATH, ConFactory.USER, ConFactory.PASSWORD);
            IIngrediente daoIngrediente = new IngredienteDAO(ConFactory.DAO_PATH, ConFactory.USER, ConFactory.PASSWORD);

            String resultText = "";

            List<Pocao> pocoes = daoPocao.searchAllByJogadorId(jogadorId);
            List<Ingrediente> ingredientes = daoIngrediente.searchAllByJogadorId(jogadorId);
            
            resultText += "\n\n######### POÇÕES #########";

            for(Pocao p : pocoes){
                resultText += p.getId() + ": " + p.getDescricao() + "\n";
            }

            resultText += "\n\n###### INGREDIENTES #######";

            for(Ingrediente i : ingredientes){
                resultText += i.getId() + ": " + i.getNome() + " | $" + i.getValor() + " | " + i.getTempoNecessario() + "\n";
            }            

            return new ResponseEntity<String>(resultText, null, HttpStatus.OK);
        } catch(Exception e){
            return new ResponseEntity<String>("Exception: " + e.getStackTrace(), null, HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/npcsAtendimento")
    public ResponseEntity<?> npcsAtendimento(@RequestParam Integer jogadorId) throws SQLException {
        try{
            INPC daoNPC = new NPCDAO(ConFactory.DAO_PATH, ConFactory.USER, ConFactory.PASSWORD);

            String resultText = "";

            List<NPC> npcs = daoNPC.searchAllByJogadorId(jogadorId);
            
            resultText += "\n\n######### CLIENTES #########";

            for(NPC n : npcs){
                resultText += n.getId() + ": " + n.getNome() + "\n";
            }       

            return new ResponseEntity<String>(resultText, null, HttpStatus.OK);
        } catch(Exception e){
            return new ResponseEntity<String>("Exception: " + e.getStackTrace(), null, HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/abrirLoja")
    public ResponseEntity<?> abrirLoja(@RequestParam Integer jogadorId) throws SQLException {
        try{
            IIngrediente daoIngrediente = new IngredienteDAO(ConFactory.DAO_PATH, ConFactory.USER, ConFactory.PASSWORD);
		    IJogador dao = new JogadorDAO(ConFactory.DAO_PATH, ConFactory.USER, ConFactory.PASSWORD);
		    Jogador jogador = dao.search(jogadorId);

            String resultText = "Saldo Disponível: $" + jogador.getDinheiro() + "\n";

            List<Ingrediente> ingredientes = daoIngrediente.searchAll();

            for(Ingrediente i : ingredientes){
                resultText += i.getId() + ": " + i.getNome() + " | $" + i.getValor() + " | " + i.getTempoNecessario() + "\n";
            }

            return new ResponseEntity<String>(resultText, null, HttpStatus.OK);
        } catch(Exception e){
            return new ResponseEntity<String>("Exception: " + e.getStackTrace(), null, HttpStatus.FORBIDDEN);
        }
    }

    //NPC
    @PostMapping("/venderPocao")
    public ResponseEntity<?> venderPocao(@RequestParam Integer npcId, @RequestParam Integer jogadorId) throws SQLException {
        try{ 
            //TODO
            return new ResponseEntity<String>("OK", null, HttpStatus.OK);
        } catch(Exception e){
            return new ResponseEntity<String>("Exception: " + e.getStackTrace(), null, HttpStatus.FORBIDDEN);
        }
    }

    //LOJA
    @PostMapping("/comprarIngrediente")
    public ResponseEntity<?> comprarIngrediente(@RequestParam Integer ingredienteId, @RequestParam Integer jogadorId) throws SQLException {
        try{ 
            IJogador dao = new JogadorDAO(ConFactory.DAO_PATH, ConFactory.USER, ConFactory.PASSWORD);
            IIngrediente daoIngrediente = new IngredienteDAO(ConFactory.DAO_PATH, ConFactory.USER, ConFactory.PASSWORD);

            Jogador jogador = dao.search(jogadorId);
            Ingrediente ingrediente = daoIngrediente.search(ingredienteId);

            if(jogador == null){
                return new ResponseEntity<String>("Jogador não encontrado.", null, HttpStatus.FORBIDDEN);
            } else if(ingrediente == null){
                return new ResponseEntity<String>("Ingrediente não encontrado.", null, HttpStatus.FORBIDDEN);
            } else if(jogador.getDinheiro() < ingrediente.getValor()){
                return new ResponseEntity<String>("Saldo Insuficiente.", null, HttpStatus.FORBIDDEN);
            }

            dao.adicionarIngredienteInventario(jogador, ingrediente);
            return new ResponseEntity<String>("OK", null, HttpStatus.OK);
        } catch(Exception e){
            return new ResponseEntity<String>("Exception: " + e.getStackTrace(), null, HttpStatus.FORBIDDEN);
        }
    }
}
