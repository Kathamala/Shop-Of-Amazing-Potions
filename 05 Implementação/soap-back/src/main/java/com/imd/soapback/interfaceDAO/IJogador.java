package main.java.com.imd.soapback.interfaceDAO;

import java.util.List;

import main.java.com.imd.soapback.model.Jogador;

public interface IJogador {

	 public Jogador search(Integer id);
	 public List<Jogador> searchAll();
	 public void remove(Jogador jogador);
	 public void remove(Integer id);
	 public void update(Jogador jogador);
	 public void insert(Jogador jogador);
}
