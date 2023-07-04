package interfaceDAO;

import java.util.List;

import entidade.Jogador;

public interface IJogador {

	 public Jogador search(Integer id);
	 public List<Jogador> search();
	 public void remove(Jogador jogador);
	 public void remove(Integer id);
	 public void update(Jogador jogador);
	 public void insert(Jogador jogador);
}
