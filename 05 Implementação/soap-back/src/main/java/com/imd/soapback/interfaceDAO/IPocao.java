package main.java.com.imd.soapback.interfaceDAO;

import java.sql.SQLException;
import java.util.List;

import main.java.com.imd.soapback.model.Pocao;

public interface IPocao {

	 public Pocao search(Integer id);
	 public List<Pocao> searchAll();
	 public void remove(Pocao jogador);
	 public void remove(Integer id);
	 public void update(Pocao jogador);
	 public void insert(Pocao jogador);
	 public List<Pocao> searchAllByJogadorId(Integer jogadorId);
	 public List<Pocao> searchAllByJogadorIdMaking(Integer jogadorId);
	 public List<Pocao> searchAllStore(Integer jogadorId);
	 public Boolean pocaoCuraNPC(Integer pocaoId, Integer npcId);
	 public Boolean pocaoContemAlergiaNPC(Integer pocaoId, Integer npcId);
	 public Boolean jogadorPossuiPocao(Integer pocaoId, Integer jogadorId);
	 public void criarPocao(List<Integer> ingredientesId, String descricaoPocao, Integer jogadorId) throws ClassNotFoundException, SQLException;
	 public void commit() throws Exception;
}
