package main.java.com.imd.soapback.interfaceDAO;

import java.util.List;

import main.java.com.imd.soapback.model.Condicoes;

public interface ICondicoes {

	 public Condicoes search(Integer id);
	 public List<Condicoes> searchAll();
	 public void remove(Condicoes jogador);
	 public void remove(Integer id);
	 public void update(Condicoes jogador);
	 public void insert(Condicoes jogador);
	 public List<Condicoes> searchAllByNPCAcometido(Integer npcId);
}
