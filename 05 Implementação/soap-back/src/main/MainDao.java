package main;

import interfaceDAO.IJogador;

import java.util.List;

import dao.JogadorDAO;
import entidade.Jogador;

public class MainDao {

	public static void main(String[] args) throws Exception { 
		IJogador jogDao = new JogadorDAO("jdbc:mysql://localhost/soap?useTimezone=true&serverTimezone=America/Fortaleza","root","root");

		// LISTA OS EMPREGADOS
		List<Jogador> listjogadores = jogDao.search();
		for (Jogador jog : listjogadores) {
			System.out.println(jog.getId() + " " + jog.getNome() + " " + jog.getDinheiro());
		}
		
		// INSERE EMPREGADO OMNIGLITH
		Jogador jogador = new Jogador(2, "Omniglith", 50f);
		jogDao.insert(jogador);

		listjogadores = jogDao.search();
		for (Jogador jog : listjogadores) {
			System.out.println(jog.getId() + " " + jog.getNome() + " " + jog.getDinheiro());
		}

		((JogadorDAO)jogDao).commit();
	}
}

