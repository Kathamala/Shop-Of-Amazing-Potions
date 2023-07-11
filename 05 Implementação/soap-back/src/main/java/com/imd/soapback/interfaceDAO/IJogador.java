package main.java.com.imd.soapback.interfaceDAO;

import java.util.List;

import main.java.com.imd.soapback.model.Ingrediente;
import main.java.com.imd.soapback.model.Jogador;

public interface IJogador {

	 public Jogador search(Integer id);
	 public List<Jogador> searchAll();
	 public Jogador searchByName(String name);
	 public void remove(Jogador jogador);
	 public void remove(Integer id);
	 public void update(Jogador jogador);
	 public void insert(Jogador jogador);
	 public Integer getNextId();
	 public void adicionarIngredienteInventario(Jogador jogador, Ingrediente ingrediente);
	 public void adicionarPocaoInventario(Integer pocaoId, Integer jogadorId, List<Integer> ingredientesId);
	 public void venderPocao(Integer pocaoId, Integer npcId, Integer jogadorId, Float verba);
	 public void commit() throws Exception;
}
