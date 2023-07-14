package main.java.com.imd.soapback.interfaceDAO;

import java.util.List;

import main.java.com.imd.soapback.model.NPC;

public interface INPC {

	 public NPC search(Integer id);
	 public List<NPC> searchAll();
	 public void remove(NPC jogador);
	 public void remove(Integer id);
	 public void update(NPC jogador);
	 public void insert(NPC jogador);
	 public List<NPC> searchAllByJogadorId(Integer jogadorId);
	 public List<NPC> searchAll(Integer jogadorId);
	 public NPC searchAtendimento(Integer id);
	 public Boolean jogadorAtendeNPC(Integer jogadorId, Integer npcId);
}
