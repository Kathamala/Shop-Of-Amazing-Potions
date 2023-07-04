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
}
