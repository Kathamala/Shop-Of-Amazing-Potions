package main.java.com.imd.soapback.interfaceDAO;

import java.util.List;

import main.java.com.imd.soapback.model.Ingrediente;

public interface IIngrediente {

	 public Ingrediente search(Integer id);
	 public List<Ingrediente> searchAll();
	 public void remove(Ingrediente jogador);
	 public void remove(Integer id);
	 public void update(Ingrediente jogador);
	 public void insert(Ingrediente jogador);
	 public List<Ingrediente> searchAllByJogadorId(Integer jogadorId);
	 public List<Ingrediente> searchAllStore(Integer jogadorId);
	 public List<Ingrediente> searchAllByPocaoIdAndJogadorId(Integer pocaoId, Integer jogadorId);
	 public List<Ingrediente> searchAllAlergiasNPC(Integer npcId);
	 public List<Ingrediente> searchAllTrataCondicao(Integer condicaoId);
	 public Boolean jogadorPossuiIngrediente(Integer ingredienteId, Integer jogadorId);
}
