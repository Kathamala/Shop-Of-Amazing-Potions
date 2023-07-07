package main.java.com.imd.soapback.controller;

import java.sql.SQLException;
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
                String qnt = " (" + p.getQuantidade() + " no inventario)";
                resultText += p.getId() + ": " + p.getDescricao() + qnt + "\n";
            }

            if(pocoes.size() == 0){
                resultText += "NENHUMA POCAO FABRICADA\n";
            }

            resultText += "\n\n###### INGREDIENTES #######\n\n";

            for(Ingrediente i : ingredientes){
                String qnt = "(" + i.getQuantidade() + " no inventario)";
                resultText += i.getId() + ": " + i.getNome() + " | $" + i.getValor() + " | " + i.getTempoNecessario() + " horas " + qnt + "\n";
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
            
            resultText += "\n\n######### CLIENTES #########\n\n";

            for(NPC n : npcs){
                resultText += n.getId() + ": " + n.getNome() + "\n\n";

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

                System.out.println();
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
                resultText += i.getId() + ": $" + i.getValor() + " | " + i.getNome() + " | " + i.getTempoNecessario() + " horas " + qnt + "\n";
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
        
    @PostMapping("/venderPocao")
    public ResponseEntity<?> venderPocao(@RequestParam Integer pocaoId, @RequestParam Integer npcId, @RequestParam Integer jogadorId) throws SQLException {
        try{ 
            IPocao daoPocap = new PocaoDAO(ConFactory.DAO_PATH, ConFactory.USER, ConFactory.PASSWORD);
            Pocao pocao = daoPocap.search(pocaoId);

            if(pocao.getId() == null){
                return new ResponseEntity<String>("FORBIDDEN: Pocao inexistente.", null, HttpStatus.FORBIDDEN);
            }

            INPC daoNPC = new NPCDAO(ConFactory.DAO_PATH, ConFactory.USER, ConFactory.PASSWORD);
            NPC npc = daoNPC.search(npcId);

            if(npc.getId() == null){
                return new ResponseEntity<String>("FORBIDDEN: Cliente inexistente.", null, HttpStatus.FORBIDDEN);
            }

            //Verificar se a pocao de fato possui ingredientes para tratar as condicoes do npc.
            //Verificar se a pocao não possui ingredientes que o npc seja alergico

            //Tirar pocao do user
            //Remover relação user-npc
            //Adicionar 100 moedas na conta do usuario

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

            jogador.setDinheiro(jogador.getDinheiro() - ingrediente.getValor());
            dao.update(jogador);

            dao.commit();

            return new ResponseEntity<Jogador>(jogador, null, HttpStatus.OK);
        } catch(Exception e){
            return new ResponseEntity<String>("FORBIDDEN: " + e.getStackTrace(), null, HttpStatus.FORBIDDEN);
        }
    }

    //POCAO
    @GetMapping("/listarPocoesVenda")
    public ResponseEntity<?> listarPocoesVenda(@RequestParam Integer jogadorId) throws SQLException {
        try{ 
            IIngrediente daoIngrediente = new IngredienteDAO(ConFactory.DAO_PATH, ConFactory.USER, ConFactory.PASSWORD);
            IPocao daoPocao = new PocaoDAO(ConFactory.DAO_PATH, ConFactory.USER, ConFactory.PASSWORD);

            List<Pocao> pocoes = daoPocao.searchAllStore(jogadorId);
            String resultText = "";

            resultText += "\n\n######### POCOES #########\n\n";

            for(Pocao p : pocoes){
                String qnt = "(" + p.getQuantidade() + " no inventario)";
                resultText += p.getId() + ": " + p.getDescricao() + " " + qnt + "\n\n";

                resultText += "===> Ingredientes necessarios: \n";
                List<Ingrediente> ingredientes = daoIngrediente.searchAllByPocaoIdAndJogadorId(p.getId(), jogadorId); 

                for(Ingrediente i : ingredientes){
                    String qnti = "(" + i.getQuantidade() + " no inventario)";
                    resultText += "======> " + i.getId() + ": $" + i.getValor() + " | " + i.getNome() + " | " + i.getTempoNecessario() + " horas " + qnti + "\n";
                }

                resultText += "\n";
            }

            return new ResponseEntity<String>(resultText, null, HttpStatus.OK);
        } catch(Exception e){
            return new ResponseEntity<String>("FORBIDDEN: " + e.getStackTrace(), null, HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/fabricarPocao")
    public ResponseEntity<?> fabricarPocao(@RequestParam Integer pocaoId, @RequestParam Integer jogadorId) throws SQLException {
        try{ 
            IIngrediente daoIngrediente = new IngredienteDAO(ConFactory.DAO_PATH, ConFactory.USER, ConFactory.PASSWORD);
            IPocao daoPocao = new PocaoDAO(ConFactory.DAO_PATH, ConFactory.USER, ConFactory.PASSWORD);

            Pocao pocao = daoPocao.search(pocaoId);
            if(pocao.getId() == null){
                return new ResponseEntity<String>("FORBIDDEN: Esta pocao nao existe.", null, HttpStatus.FORBIDDEN);
            }

            List<Ingrediente> ingredientes = daoIngrediente.searchAllByPocaoIdAndJogadorId(pocaoId, jogadorId); 
            List<Integer> ingredientesId = new ArrayList<Integer>();
            
            for(Ingrediente i : ingredientes){
                if(i.getQuantidade() == 0){
                    return new ResponseEntity<String>("FORBIDDEN: Voce nao possui a quantidade necessaria de ingredientes para fabricar a pocao.", null, HttpStatus.FORBIDDEN);
                }
                ingredientesId.add(i.getId());
            }

            IJogador dao = new JogadorDAO(ConFactory.DAO_PATH, ConFactory.USER, ConFactory.PASSWORD);
            dao.adicionarPocaoInventario(pocaoId, jogadorId, ingredientesId);

            dao.commit();

            return new ResponseEntity<String>("Pocao fabricada com sucesso!", null, HttpStatus.OK);
        } catch(Exception e){
            return new ResponseEntity<String>("FORBIDDEN: " + e.getStackTrace(), null, HttpStatus.FORBIDDEN);
        }
    }    
}
