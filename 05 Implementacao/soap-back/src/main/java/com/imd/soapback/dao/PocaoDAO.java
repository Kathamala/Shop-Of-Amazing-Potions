package main.java.com.imd.soapback.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Vector;

import main.java.com.imd.soapback.connection.ConFactory;
import main.java.com.imd.soapback.interfaceDAO.IPocao;
import main.java.com.imd.soapback.model.Pocao;

public class PocaoDAO implements IPocao {

	private String 	URL;
	private String NOME;
	private String SENHA;

	private Connection con;  
    private Statement comando;
	
	public PocaoDAO(String server, String user, String password) throws SQLException {
		this.URL = server;
		this.NOME = user;
		this.SENHA = password;
	}

	@Override
	public Pocao search(Integer id) {
        Pocao obj = new Pocao();

		try {
			conectar();
            String sql = "SELECT id, descricao, quantidade FROM POCAO, JOGADOR_POSSUI_POCAO WHERE ID= " + id + "\n" + //
            		"AND date_add(inicio_preparo, interval (SELECT SUM(tempo_necessario)\n" + //
            		"FROM INGREDIENTE_COMPOE_POCAO, INGREDIENTE\n" + //
            		"WHERE id = INGREDIENTE_id AND POCAO_id = POCAO.id) minute) < NOW();";
            ResultSet rs = comando.executeQuery(sql);
            if (rs.next()) {
				obj = this.buildPocao(rs);
            }
        } catch (SQLException SQLe) {
            SQLe.printStackTrace();
        } catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
        return obj;
	}

	@Override
	public List<Pocao> searchAllStore(Integer jogadorId) {
		synchronized (this) {
            ResultSet rs = null;
            
	        List<Pocao> list = new Vector<Pocao>();
	        try {
	        	conectar();
				
	            try {
	                rs = comando.executeQuery("(SELECT id, descricao, quantidade FROM JOGADOR_POSSUI_POCAO, POCAO\n" + //
	                		"WHERE JOGADOR_POSSUI_POCAO.POCAO_id = POCAO.id AND JOGADOR_id = " + jogadorId + ")\n" + //
	                		"UNION (SELECT id, descricao, 0 FROM POCAO WHERE id NOT IN\n" + //
	                		"(SELECT id FROM JOGADOR_POSSUI_POCAO, POCAO\n" + //
	                		"WHERE JOGADOR_POSSUI_POCAO.POCAO_id = POCAO.id AND JOGADOR_id = " + jogadorId + "))");
	                while (rs.next()) {
	    				Pocao e = this.buildPocao(rs);
	    				list.add(e);
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
	        return list;
        }
	}	

	@Override
	public Boolean jogadorPossuiPocao(Integer pocaoId, Integer jogadorId) {
        Pocao obj = new Pocao();

		try {
			conectar();
            String sql = "SELECT id, descricao FROM POCAO, JOGADOR_POSSUI_POCAO\n" + //
            		"WHERE POCAO.id = JOGADOR_POSSUI_POCAO.POCAO_id\n" + //
            		"AND POCAO.id = " + pocaoId + " AND JOGADOR_id = " + jogadorId;
            ResultSet rs = comando.executeQuery(sql);
            if (rs.next()) {
				obj = this.buildPocao(rs);
            }
        } catch (SQLException SQLe) {
            SQLe.printStackTrace();
        } catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
        return obj.getId() != null;
	}


	@Override
	public Boolean pocaoCuraNPC(Integer pocaoId, Integer npcId) {
		synchronized (this) {
            ResultSet rs = null;
            
	        List<Pocao> list = new Vector<Pocao>();
	        try {
	        	conectar();
				
	            try {
	                rs = comando.executeQuery("SELECT INGREDIENTE_id FROM INGREDIENTE_TRATA_CONDICOES, NPC_ACOMETIDO_POR_CONDICOES\n" + //
	                		"WHERE INGREDIENTE_TRATA_CONDICOES.CONDICOES_id = NPC_ACOMETIDO_POR_CONDICOES.CONDICOES_id\n" + //
	                		"AND NPC_id = " + npcId + " AND INGREDIENTE_id NOT IN (SELECT INGREDIENTE_id FROM INGREDIENTE_COMPOE_POCAO \n" + //
	                		"WHERE POCAO_id = " + pocaoId + ")");
	                while (rs.next()) {
	    				Pocao e = this.buildPocao(rs);
	    				list.add(e);
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
	        return list.isEmpty();
        }
	}	

	@Override
	public Boolean pocaoContemAlergiaNPC(Integer pocaoId, Integer npcId) {
		synchronized (this) {
            ResultSet rs = null;
            
	        List<Pocao> list = new Vector<Pocao>();
	        try {
	        	conectar();
				
	            try {
	                rs = comando.executeQuery("SELECT * FROM NPC_ALERGICO_A_INGREDIENTE, INGREDIENTE_COMPOE_POCAO\n" + //
	                		"WHERE NPC_ALERGICO_A_INGREDIENTE.INGREDIENTE_id = INGREDIENTE_COMPOE_POCAO.INGREDIENTE_id\n" + //
	                		"AND NPC_id = " + npcId + " AND POCAO_id = " + pocaoId);
	                while (rs.next()) {
	    				Pocao e = this.buildPocao(rs);
	    				list.add(e);
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
	        return !list.isEmpty();
        }
	}	

	@Override
	public List<Pocao> searchAll() {
		synchronized (this) {
            ResultSet rs = null;
            
	        List<Pocao> list = new Vector<Pocao>();
	        try {
	        	conectar();
				
	            try {
	                rs = comando.executeQuery("SELECT * FROM POCAO");
	                while (rs.next()) {
	    				Pocao e = this.buildPocao(rs);
	    				list.add(e);
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
	        return list;
        }
	}
	
	@Override
	public List<Pocao> searchAllByJogadorId(Integer jogadorId) {
		synchronized (this) {
            ResultSet rs = null;
            
	        List<Pocao> list = new Vector<Pocao>();
	        try {
	        	conectar();
				
	            try {
	                rs = comando.executeQuery("SELECT id, descricao, quantidade FROM POCAO, JOGADOR_POSSUI_POCAO\n" + //
	                		"WHERE POCAO.id = JOGADOR_POSSUI_POCAO.POCAO_id AND quantidade > 0\n" + //
	                		"AND date_add(inicio_preparo, interval (SELECT SUM(tempo_necessario) \n" + //
	                		"FROM INGREDIENTE_COMPOE_POCAO, INGREDIENTE\n" + //
	                		"WHERE id = INGREDIENTE_id AND POCAO_id = POCAO.id) minute) < NOW()\n" + //
	                		"AND JOGADOR_id = " + jogadorId);
	                while (rs.next()) {
	    				Pocao e = this.buildPocao(rs);
	    				list.add(e);
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
	        return list;
        }
	}

	@Override
	public List<Pocao> searchAllByJogadorIdMaking(Integer jogadorId) {
		synchronized (this) {
            ResultSet rs = null;
            
	        List<Pocao> list = new Vector<Pocao>();
	        try {
	        	conectar();
				
	            try {
	                rs = comando.executeQuery("SELECT id, descricao, quantidade FROM POCAO, JOGADOR_POSSUI_POCAO\n" + //
	                		"WHERE POCAO.id = JOGADOR_POSSUI_POCAO.POCAO_id AND quantidade > 0\n" + //
	                		"AND date_add(inicio_preparo, interval (SELECT SUM(tempo_necessario) \n" + //
	                		"FROM INGREDIENTE_COMPOE_POCAO, INGREDIENTE\n" + //
	                		"WHERE id = INGREDIENTE_id AND POCAO_id = POCAO.id) minute) > NOW()\n" + //
	                		"AND JOGADOR_id = " + jogadorId);
	                while (rs.next()) {
	    				Pocao e = this.buildPocao(rs);
	    				list.add(e);
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
	        return list;
        }
	}


	@Override
	public void remove(Pocao obj) {
        
        	String sql ="DELETE FROM POCAO WHERE id=" + obj.getId() + ";";
			
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
    		String sql ="DELETE FROM POCAO WHERE id=" + this.retornarValorStringBD(id.toString());
    		System.out.println("%"+sql+"%");
			comando.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void update(Pocao obj) {
			StringBuffer buffer = new StringBuffer();
	        buffer.append("UPDATE POCAO SET ");
	        buffer.append(returnFieldValuesBD(obj));
	        buffer.append(" WHERE id=");
	        buffer.append(obj.getId());
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
	public void insert(Pocao obj) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("INSERT INTO POCAO (");
		buffer.append(this.retornarCamposBD());
		buffer.append(") VALUES (");
		buffer.append(this.retornarValoresBD(obj));
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
    	return "id, descricao";
    }
    
    protected String returnFieldValuesBD(Pocao obj) {

        StringBuffer buffer = new StringBuffer();
        buffer.append("id=");
        buffer.append(retornarValorStringBD(obj.getId().toString()));
        buffer.append(", descricao=");
        buffer.append(retornarValorStringBD(obj.getDescricao()));
        return buffer.toString();
    }
    
    protected String retornarValoresBD(Pocao obj) {
    	return
	        obj.getId()
	        + ", "
	        + retornarValorStringBD(obj.getDescricao());
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
    
    private Pocao buildPocao(ResultSet rs) {
		Pocao obj = new Pocao();
		try{
			obj.setId(rs.getInt("id"));
		} catch(Exception e){}
		try{
			obj.setDescricao(rs.getString("descricao"));
		} catch(Exception e){}
		try{
			obj.setQuantidade(rs.getInt("quantidade"));
		} catch(Exception e){}
		return obj;
    }

	public Integer getNextId(){
		try{
			conectar();
			String sql = "SELECT MAX(id) as id FROM POCAO";
			ResultSet rs = comando.executeQuery(sql);
			if (rs.next()) {
				return rs.getInt("id") + 1;
			}
		} catch (SQLException SQLe) {
            SQLe.printStackTrace();
        } catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return 0;
	}

	@Override
	public void criarPocao(List<Integer> ingredientesId, String descricaoPocao, Integer jogadorId) throws ClassNotFoundException, SQLException{
		Integer novoId = getNextId();

		//Criar pocao
		StringBuffer buffer = new StringBuffer();
		buffer.append("INSERT INTO POCAO VALUES (");
		buffer.append(novoId + ", " + retornarValorStringBD(descricaoPocao));
		buffer.append(")");
		String sql = buffer.toString();

		//Atribuir ingredientes a pocao
		StringBuffer buffer2 = new StringBuffer();
		buffer2.append("INSERT INTO INGREDIENTE_COMPOE_POCAO VALUES ");
		for(Integer i : ingredientesId){
			buffer2.append("(" + i + ", " + novoId + "),");
		}
		String sql2 = buffer2.toString().substring(0, buffer2.toString().length()-1);

		//Remover ingredientes do jogador
		StringBuffer buffer3 = new StringBuffer();
		buffer3.append("UPDATE JOGADOR_POSSUI_INGREDIENTE SET quantidade=quantidade-1 WHERE JOGADOR_id = " + jogadorId 
		+ " AND (INGREDIENTE_id = " + ingredientesId.get(0) + " ");
		for(Integer i : ingredientesId){
			buffer3.append("OR INGREDIENTE_id = " + i + " ");
		}
		buffer3.append(")");
		String sql3 = buffer3.toString();

		//Atribuir pocao ao jogador
		StringBuffer buffer4 = new StringBuffer();
		buffer4.append("INSERT INTO JOGADOR_POSSUI_POCAO VALUES (");
		buffer4.append(jogadorId + ", " + novoId + ", 1, NOW()");
		buffer4.append(")");
		String sql4 = buffer4.toString();

		conectar();
		comando.execute(sql);
		comando.execute(sql2);
		comando.execute(sql3);
		comando.execute(sql4);
	}
}
