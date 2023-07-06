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
                return new ResponseEntity<String>("FORBIDDEN: Jogador " + name + " inexistente.", null, HttpStatus.FORBIDDEN);    
            }

            return new ResponseEntity<Jogador>(obj, null, HttpStatus.OK);
        } catch(Exception e){
            return new ResponseEntity<String>("FORBIDDEN: " + e.getStackTrace(), null, HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestParam String name) throws SQLException {
        try{
		    IJogador dao = new JogadorDAO(ConFactory.DAO_PATH, ConFactory.USER, ConFactory.PASSWORD);
		    Jogador obj = dao.searchByName(name);

            if(obj.getId() != null){
                return new ResponseEntity<String>("FORBIDDEN: Jogador " + name + " ja existe.", null, HttpStatus.FORBIDDEN);    
            }

            obj = new Jogador(dao.getNextId(), name, 100f);
            if(obj.getId() == 0){
                return new ResponseEntity<String>("FORBIDDEN: Não foi possível cadastrar o novo jogador.", null, HttpStatus.FORBIDDEN);    
            }

            dao.insert(obj);

            return new ResponseEntity<Jogador>(obj, null, HttpStatus.OK);
        } catch(Exception e){
            return new ResponseEntity<String>("FORBIDDEN: Exception: " + e.getStackTrace(), null, HttpStatus.FORBIDDEN);
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
            
            resultText += "\n\n######### POCOES #########\n\n";

            for(Pocao p : pocoes){
                resultText += p.getId() + ": " + p.getDescricao() + "\n";
            }

            if(pocoes.size() == 0){
                resultText += "NENHUMA POCAO FABRICADA\n";
            }

            resultText += "\n\n###### INGREDIENTES #######\n\n";

            for(Ingrediente i : ingredientes){
                resultText += i.getId() + ": " + i.getNome() + " | $" + i.getValor() + " | " + i.getTempoNecessario() + " horas\n";
            }        

            if(ingredientes.size() == 0){
                resultText += "NENHUM INGREDIENTE COMPRADO\n";
            }

            return new ResponseEntity<String>(resultText, null, HttpStatus.OK);
        } catch(Exception e){
            return new ResponseEntity<String>("FORBIDDEN: " + e.getStackTrace(), null, HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/npcsAtendimento")
    public ResponseEntity<?> npcsAtendimento(@RequestParam Integer jogadorId) throws SQLException {
        try{
            INPC daoNPC = new NPCDAO(ConFactory.DAO_PATH, ConFactory.USER, ConFactory.PASSWORD);

            String resultText = "";

            List<NPC> npcs = daoNPC.searchAllByJogadorId(jogadorId);
            
            resultText += "\n\n######### CLIENTES #########\n\n";

            for(NPC n : npcs){
                resultText += n.getId() + ": " + n.getNome() + "\n";
            }

            if(npcs.size() == 0){
                resultText = "NENHUM CLIENTE EM ATENDIMENTO.";
            }

            return new ResponseEntity<String>(resultText, null, HttpStatus.OK);
        } catch(Exception e){
            return new ResponseEntity<String>("FORBIDDEN: " + e.getStackTrace(), null, HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/abrirLoja")
    public ResponseEntity<?> abrirLoja(@RequestParam Integer jogadorId) throws SQLException {
        try{
            IIngrediente daoIngrediente = new IngredienteDAO(ConFactory.DAO_PATH, ConFactory.USER, ConFactory.PASSWORD);
		    IJogador dao = new JogadorDAO(ConFactory.DAO_PATH, ConFactory.USER, ConFactory.PASSWORD);
		    Jogador jogador = dao.search(jogadorId);

            String resultText = "Saldo Atual: $" + jogador.getDinheiro() + "\n\n";

            List<Ingrediente> ingredientes = daoIngrediente.searchAllStore(jogadorId);

            for(Ingrediente i : ingredientes){
                String qnt = i.getQuantidade() > 0 ? "(" + i.getQuantidade() + " no inventario)" : "";
                resultText += i.getId() + ": " + i.getValor() + " | $" + i.getNome() + " | " + i.getTempoNecessario() + " horas " + qnt + "\n";
            }

            return new ResponseEntity<String>(resultText, null, HttpStatus.OK);
        } catch(Exception e){
            return new ResponseEntity<String>("FORBIDDEN: " + e.getStackTrace(), null, HttpStatus.FORBIDDEN);
        }
    }

    //NPC
    @PostMapping("/venderPocao")
    public ResponseEntity<?> venderPocao(@RequestParam Integer npcId, @RequestParam Integer jogadorId) throws SQLException {
        try{ 
            //TODO
            return new ResponseEntity<String>("OK", null, HttpStatus.OK);
        } catch(Exception e){
            return new ResponseEntity<String>("FORBIDDEN: " + e.getStackTrace(), null, HttpStatus.FORBIDDEN);
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

            if(jogador.getId() == null){
                return new ResponseEntity<String>("FORBIDDEN: Jogador inexistente.", null, HttpStatus.FORBIDDEN);
            } else if(ingrediente.getId() == null){
                return new ResponseEntity<String>("FORBIDDEN: Ingrediente inexistente.", null, HttpStatus.FORBIDDEN);
            } else if(jogador.getDinheiro() < ingrediente.getValor()){
                return new ResponseEntity<String>("FORBIDDEN: Saldo insuficiente.", null, HttpStatus.FORBIDDEN);
            }

            dao.adicionarIngredienteInventario(jogador, ingrediente);
            dao.commit();
            
            return new ResponseEntity<String>("Compra realizada com sucesso!", null, HttpStatus.OK);
        } catch(Exception e){
            return new ResponseEntity<String>("FORBIDDEN: " + e.getStackTrace(), null, HttpStatus.FORBIDDEN);
        }
    }
}
