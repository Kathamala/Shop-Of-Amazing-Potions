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
}
