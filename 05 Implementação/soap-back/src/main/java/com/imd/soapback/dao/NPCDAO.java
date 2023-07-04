package main.java.com.imd.soapback.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Vector;

import main.java.com.imd.soapback.connection.ConFactory;
import main.java.com.imd.soapback.interfaceDAO.INPC;
import main.java.com.imd.soapback.model.NPC;

public class NPCDAO implements INPC {

	private String 	URL;
	private String NOME;
	private String SENHA;

	private Connection con;  
    private Statement comando;
	
	public NPCDAO(String server, String user, String password) throws SQLException {
		this.URL = server;
		this.NOME = user;
		this.SENHA = password;
	}

	@Override
	public NPC search(Integer id) {
        NPC npc = new NPC();

		try {
			conectar();
            String sql = "SELECT * FROM NPC WHERE ID=" + id;
            ResultSet rs = comando.executeQuery(sql);
            if (rs.next()) {
				npc = this.buildNPC(rs);
            }
        } catch (SQLException SQLe) {
            SQLe.printStackTrace();
        } catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
        return npc;
	}

	@Override
	public List<NPC> searchAll() {
		synchronized (this) {
            ResultSet rs = null;
            
	        List<NPC> npcs = new Vector<NPC>();
	        try {
	        	conectar();
				
	            try {
	                rs = comando.executeQuery("SELECT * FROM NPC");
	                while (rs.next()) {
	    				NPC e = this.buildNPC(rs);
	    				npcs.add(e);
	                }
	            } finally {
        			if (rs != null) {
        				try {
        					rs.close();
        				} catch (SQLException sqlEx) { 
        				} 
        				rs = null;
        			}
        			if (comando != null) {
        				try {
        					comando.close();
        				} catch (SQLException sqlEx) { 
        				}
        				comando = null;
        			}
	            }
	        } catch (SQLException SQLe) {
	            SQLe.printStackTrace();
	        } catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
	        return npcs;
        }
	}

	@Override
	public List<NPC> searchAllByJogadorId(Integer jogadorId) {
		synchronized (this) {
            ResultSet rs = null;
            
	        List<NPC> npcs = new Vector<NPC>();
	        try {
	        	conectar();
				
	            try {
					//TODO: CHANGE IMPLEMENTATION TO BRING PLAYER NPCs
	                rs = comando.executeQuery("SELECT * FROM NPC");
	                while (rs.next()) {
	    				NPC e = this.buildNPC(rs);
	    				npcs.add(e);
	                }
	            } finally {
        			if (rs != null) {
        				try {
        					rs.close();
        				} catch (SQLException sqlEx) { 
        				} 
        				rs = null;
        			}
        			if (comando != null) {
        				try {
        					comando.close();
        				} catch (SQLException sqlEx) { 
        				}
        				comando = null;
        			}
	            }
	        } catch (SQLException SQLe) {
	            SQLe.printStackTrace();
	        } catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
	        return npcs;
        }
	}

	@Override
	public void remove(NPC npc) {
        
        	String sql ="DELETE FROM NPC WHERE id=" + npc.getId() + ";";
			
	    	try {
				conectar();
	    		comando.execute(sql);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}

	@Override
	public void remove(Integer id) {

        try {
        	conectar();
    		String sql ="DELETE FROM NPC WHERE id=" + this.retornarValorStringBD(id.toString());
    		System.out.println("%"+sql+"%");
			comando.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void update(NPC npc) {
			StringBuffer buffer = new StringBuffer();
	        buffer.append("UPDATE NPC SET ");
	        buffer.append(returnFieldValuesBD(npc));
	        buffer.append(" WHERE id=");
	        buffer.append(npc.getId());
	        String sql = buffer.toString();
	        
	    	try {
				conectar();
	    		comando.execute(sql);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	        
	}

	@Override
	public void insert(NPC npc) {
			StringBuffer buffer = new StringBuffer();
	        buffer.append("INSERT INTO NPC (");
	        buffer.append(this.retornarCamposBD());
	        buffer.append(") VALUES (");
	        buffer.append(this.retornarValoresBD(npc));
	        buffer.append(")");
	        String sql = buffer.toString();

	    	try {
				conectar();
	    		comando.execute(sql);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}
		
	protected String retornarCamposBD() {
    	return "id, nome, tempo_de_espera, verba_periodicidade, verba_valor_base, verba_multiplicador, jogador_id";
    }
    
    protected String returnFieldValuesBD(NPC j) {

        StringBuffer buffer = new StringBuffer();
        buffer.append("id=");
        buffer.append(retornarValorStringBD(j.getId().toString()));
        buffer.append(", nome=");
        buffer.append(retornarValorStringBD(j.getNome()));
        buffer.append(", tempoDeEspera=");
        buffer.append(retornarValorStringBD(j.getTempoDeEspera().toString()));
        buffer.append(", verbaPeriodicidade=");
        buffer.append(retornarValorStringBD(j.getVerbaPeriodicidade().toString()));
        buffer.append(", verbaValorBase=");
        buffer.append(retornarValorStringBD(j.getVerbaValorBase().toString()));
        buffer.append(", verbaMultiplicador=");
        buffer.append(retornarValorStringBD(j.getVerbaMultiplicador().toString()));
        buffer.append(", jogadorId=");
        buffer.append(retornarValorStringBD(j.getJogadorId().toString()));

        return buffer.toString();
    }
    
    protected String retornarValoresBD(NPC npc) {
    	return
	        npc.getId()
	        + ", "
	        + retornarValorStringBD(npc.getNome())
	        + ", "
	        + npc.getTempoDeEspera()
	        + ", "
	        + npc.getVerbaPeriodicidade()
			+ ", "
	        + npc.getVerbaValorBase()
	        + ", "
	        + npc.getVerbaMultiplicador()
			+ ", "
	        + npc.getJogadorId();
    }
    
    private String retornarValorStringBD(String valor) {
        String retorno = "";
        if (valor == null) {
        	retorno = "null";
        } else {
            retorno = "'" + valor + "'";
        }
        return retorno;
    }
    
    private void conectar() throws ClassNotFoundException, SQLException  {
        con = ConFactory.conexao(URL, NOME, SENHA);  
        con.setAutoCommit(false);
        comando = con.createStatement();   
	}	  

    public void commit() throws Exception {
    	try {
			this.commitTransaction();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
    	
    }
    
	public void cancelTransaction() throws Exception {
	    if (con == null) {
	        throw new Exception("There is no opened connection");
	    }
	    try {
	        con.rollback();
	    } finally {
	        con.close();
	        con = null;
	    }
	}

	public void commitTransaction() throws Exception {
	    if (con == null) {
	        throw new Exception("There is no opened connection");
	    }
	    try {
	        con.commit();
	    } finally {
	        con.close();
	        con = null;
	    }
	}
    
    private NPC buildNPC(ResultSet rs) {
		NPC npc = new NPC();
		try {  
			npc.setId(rs.getInt("id"));
			npc.setNome(rs.getString("nome"));
			npc.setJogadorId(rs.getInt("jogador_id"));
			npc.setTempoDeEspera(rs.getInt("tempo_de_espera"));
			npc.setVerbaMultiplicador(rs.getFloat("verba_multiplicador"));
			npc.setVerbaPeriodicidade(rs.getInt("verba_periodicidade"));
			npc.setVerbaValorBase(rs.getFloat("verba_valor_base"));

		} catch (SQLException e) { 
			e.printStackTrace();
		}  
		return npc;
    }
}
