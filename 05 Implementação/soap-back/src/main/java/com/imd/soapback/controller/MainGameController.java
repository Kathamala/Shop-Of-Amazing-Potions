package main.java.com.imd.soapback.controller;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import main.java.com.imd.soapback.connection.ConFactory;
import main.java.com.imd.soapback.dao.CondicoesDAO;
import main.java.com.imd.soapback.dao.IngredienteDAO;
import main.java.com.imd.soapback.dao.JogadorDAO;
import main.java.com.imd.soapback.dao.NPCDAO;
import main.java.com.imd.soapback.dao.PocaoDAO;
import main.java.com.imd.soapback.interfaceDAO.ICondicoes;
import main.java.com.imd.soapback.interfaceDAO.IIngrediente;
import main.java.com.imd.soapback.interfaceDAO.IJogador;
import main.java.com.imd.soapback.interfaceDAO.INPC;
import main.java.com.imd.soapback.interfaceDAO.IPocao;
import main.java.com.imd.soapback.model.Condicoes;
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
            dao.commit();

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
            List<Pocao> pocoesEmPreparo = daoPocao.searchAllByJogadorIdMaking(jogadorId);
            List<Ingrediente> ingredientes = daoIngrediente.searchAllByJogadorId(jogadorId);
            
            resultText += "\n\n######### POCOES #########\n";

            for(Pocao p : pocoes){
                String qnt = " (" + p.getQuantidade() + " no inventario)";
                resultText += p.getId() + ": " + p.getDescricao() + qnt + "\n";
            }

            if(pocoesEmPreparo.size() != 0){
                for(Pocao p : pocoesEmPreparo){
                    resultText += p.getId() + ": " + p.getDescricao() + " (em preparo) \n";
                }
            }            

            if(pocoes.size() + pocoesEmPreparo.size() == 0){
                resultText += "\nNENHUMA POCAO FABRICADA\n";
            }

            resultText += "\n\n###### INGREDIENTES #######\n\n";

            for(Ingrediente i : ingredientes){
                String qnt = "(" + i.getQuantidade() + " no inventario)";
                resultText += i.getId() + ": " + i.getNome() + " | $" + i.getValor() + " | " + i.getTempoNecessario() + " minuto(s) " + qnt + "\n";
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
            ICondicoes daoCondicoes = new CondicoesDAO(ConFactory.DAO_PATH, ConFactory.USER, ConFactory.PASSWORD);
            IIngrediente daoIngrediente = new IngredienteDAO(ConFactory.DAO_PATH, ConFactory.USER, ConFactory.PASSWORD);

            String resultText = "";

            List<NPC> npcs = daoNPC.searchAllByJogadorId(jogadorId);

            for(NPC n : npcs){
                String pattern = "[dd/MM] HH:mm";
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

                resultText += "################# " + n.getId() + ": " + n.getNome() + " #################\n\n";

                resultText += "===> Chegada: " + simpleDateFormat.format(n.getChegada()) + " / Tempo de espera: " + n.getTempoDeEspera() + " minuto(s).\n\n";

                List<Condicoes> condicoes = daoCondicoes.searchAllByNPCAcometido(n.getId());

                resultText += "===> Condicoes: \n";

                for(Condicoes c : condicoes){
                    resultText += "======> " + c.getId() + ": " + c.getNome() + " | " + c.getDescricao() + " | Intensidade: " + c.getIntensidade() + "\n";
                }

                List<Ingrediente> alergias = daoIngrediente.searchAllAlergiasNPC(n.getId());
                
                resultText += "\n===> Alergias: \n";

                for(Ingrediente i : alergias){
                    resultText += "======> " + i.getId() + ": " + i.getNome() + "\n";
                }

                resultText += "\n";
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
                String qnt = "(" + i.getQuantidade() + " no inventario)";
                resultText += i.getId() + ": $" + i.getValor() + " | " + i.getNome() + " | " + i.getTempoNecessario() + " minuto(s) " + qnt + "\n";
            }

            return new ResponseEntity<String>(resultText, null, HttpStatus.OK);
        } catch(Exception e){
            return new ResponseEntity<String>("FORBIDDEN: " + e.getStackTrace(), null, HttpStatus.FORBIDDEN);
        }
    }

    //NPC
    @GetMapping("/detalharCondicao")
    public ResponseEntity<?> detalharCondicao(@RequestParam Integer condicaoId) throws SQLException {
        try{ 
            ICondicoes daoCondicoes = new CondicoesDAO(ConFactory.DAO_PATH, ConFactory.USER, ConFactory.PASSWORD);
            IIngrediente daoIngrediente = new IngredienteDAO(ConFactory.DAO_PATH, ConFactory.USER, ConFactory.PASSWORD);

            Condicoes c = daoCondicoes.search(condicaoId);

            String resultText = "\n\n\n\n# " + c.getId() + ": " + c.getNome() + " | " + c.getDescricao() + " | Intensidade: " + c.getIntensidade() + "\n";

            List<Ingrediente> ingredientes = daoIngrediente.searchAllTrataCondicao(c.getId());
            
            resultText += "\n===> Ingrediente(s) para tratar a condicao: \n";
            for(Ingrediente i : ingredientes){
                resultText += "======> " + i.getId() + ": " + i.getNome() + "\n";
            }         

            return new ResponseEntity<String>(resultText, null, HttpStatus.OK);
        } catch(Exception e){
            return new ResponseEntity<String>("FORBIDDEN: " + e.getStackTrace(), null, HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/listarPocoesVendaNPC")
    public ResponseEntity<?> listarPocoesVendaNPC(@RequestParam Integer jogadorId) throws SQLException {
        try{ 
            IPocao daoPocao = new PocaoDAO(ConFactory.DAO_PATH, ConFactory.USER, ConFactory.PASSWORD);

            String resultText = "";

            List<Pocao> pocoes = daoPocao.searchAllByJogadorId(jogadorId);
            
            resultText += "\n\n######### POCOES #########\n";

            for(Pocao p : pocoes){
                String qnt = " (" + p.getQuantidade() + " no inventario)";
                resultText += p.getId() + ": " + p.getDescricao() + qnt + "\n";
            }

            if(pocoes.size() == 0){
                resultText += "\nNENHUMA POCAO FABRICADA\n";
            }

            return new ResponseEntity<String>(resultText, null, HttpStatus.OK);
        } catch(Exception e){
            return new ResponseEntity<String>("FORBIDDEN: " + e.getStackTrace(), null, HttpStatus.FORBIDDEN);
        }
    }    
        
    @PostMapping("/venderPocao")
    public ResponseEntity<?> venderPocao(@RequestParam Integer pocaoId, @RequestParam Integer npcId, @RequestParam Integer jogadorId) throws SQLException {
        try{ 
            IPocao daoPocao = new PocaoDAO(ConFactory.DAO_PATH, ConFactory.USER, ConFactory.PASSWORD);
            Pocao pocao = daoPocao.search(pocaoId);

            if(pocao.getId() == null){
                return new ResponseEntity<String>("FORBIDDEN: Pocao inexistente.", null, HttpStatus.FORBIDDEN);
            }

            INPC daoNPC = new NPCDAO(ConFactory.DAO_PATH, ConFactory.USER, ConFactory.PASSWORD);
            NPC npc = daoNPC.searchAtendimento(npcId);

            if(npc.getId() == null){
                return new ResponseEntity<String>("FORBIDDEN: Cliente inexistente.", null, HttpStatus.FORBIDDEN);
            }

            if(!daoNPC.jogadorAtendeNPC(jogadorId, npcId)){
                return new ResponseEntity<String>("FORBIDDEN: Este jogador nao atende este cliente.", null, HttpStatus.FORBIDDEN);
            }            

            if(!daoPocao.jogadorPossuiPocao(pocaoId, jogadorId)){
                return new ResponseEntity<String>("FORBIDDEN: Este jogador nao possui esta pocao.", null, HttpStatus.FORBIDDEN);
            }  

            if(!daoPocao.pocaoCuraNPC(pocaoId, npcId)){
                return new ResponseEntity<String>("FORBIDDEN: Essa pocao nao atende aos requisitos para curar este cliente.", null, HttpStatus.FORBIDDEN);
            }

            if(daoPocao.pocaoContemAlergiaNPC(pocaoId, npcId)){
                return new ResponseEntity<String>("FORBIDDEN: Essa pocao contem ingredientes que o cliente possui alergia.", null, HttpStatus.FORBIDDEN);
            }

            IJogador daoJogador = new JogadorDAO(ConFactory.DAO_PATH, ConFactory.USER, ConFactory.PASSWORD);

            daoJogador.venderPocao(pocaoId, npcId, jogadorId);

            daoJogador.commit();

            return new ResponseEntity<String>("Pocao vendida para o cliente com sucesso!", null, HttpStatus.OK);
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

            jogador.setDinheiro(jogador.getDinheiro() - ingrediente.getValor());
            dao.update(jogador);

            dao.commit();

            return new ResponseEntity<Jogador>(jogador, null, HttpStatus.OK);
        } catch(Exception e){
            return new ResponseEntity<String>("FORBIDDEN: " + e.getStackTrace(), null, HttpStatus.FORBIDDEN);
        }
    }

    //POCAO
    @PostMapping("/fabricarPocao")
    public ResponseEntity<?> fabricarPocao(@RequestParam String ingredientesString, @RequestParam String descricaoPocao, @RequestParam Integer jogadorId) throws SQLException {
        try{ 
            IIngrediente daoIngrediente = new IngredienteDAO(ConFactory.DAO_PATH, ConFactory.USER, ConFactory.PASSWORD);
            IPocao daoPocao = new PocaoDAO(ConFactory.DAO_PATH, ConFactory.USER, ConFactory.PASSWORD);

            descricaoPocao = descricaoPocao.replaceAll("DspaceD", " ");

            String[] ingredientesStringId = ingredientesString.split(",");
            List<Integer> ingredientesId = new ArrayList<Integer>();

            for(String s : ingredientesStringId){
                try{
                    Integer i = Integer.parseInt(s);
                    if(!ingredientesId.contains(i)) {
                        ingredientesId.add(i);
                    }
                    if(daoIngrediente.search(i).getId() == null){
                        return new ResponseEntity<String>("FORBIDDEN: Ingrediente nao existe.", null, HttpStatus.FORBIDDEN);    
                    }                    
                    if(!daoIngrediente.jogadorPossuiIngrediente(i, jogadorId)){
                        return new ResponseEntity<String>("FORBIDDEN: O jogador nao possui o ingrediente.", null, HttpStatus.FORBIDDEN);    
                    }
                } catch(Exception e){
                    return new ResponseEntity<String>("FORBIDDEN: Valor invalido inserido.", null, HttpStatus.FORBIDDEN);
                }                
            }

            daoPocao.criarPocao(ingredientesId, descricaoPocao, jogadorId);
            daoPocao.commit();

            return new ResponseEntity<String>("Pocao fabricada com sucesso!", null, HttpStatus.OK);
        } catch(Exception e){
            return new ResponseEntity<String>("FORBIDDEN: " + e.getStackTrace(), null, HttpStatus.FORBIDDEN);
        }
    }    
}
