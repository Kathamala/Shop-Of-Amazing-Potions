package main.java.com.imd.soapback.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Vector;

import main.java.com.imd.soapback.connection.ConFactory;
import main.java.com.imd.soapback.interfaceDAO.ICondicoes;
import main.java.com.imd.soapback.model.Condicoes;

public class CondicoesDAO implements ICondicoes {

	private String 	URL;
	private String NOME;
	private String SENHA;

	private Connection con;  
    private Statement comando;
	
	public CondicoesDAO(String server, String user, String password) throws SQLException {
		this.URL = server;
		this.NOME = user;
		this.SENHA = password;
	}

	@Override
	public Condicoes search(Integer id) {
        Condicoes obj = new Condicoes();

		try {
			conectar();
            String sql = "SELECT * FROM CONDICOES WHERE ID=" + id;
            ResultSet rs = comando.executeQuery(sql);
            if (rs.next()) {
				obj = this.buildCondicoes(rs);
            }
        } catch (SQLException SQLe) {
            SQLe.printStackTrace();
        } catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
        return obj;
	}

	@Override
	public List<Condicoes> searchAllByNPCAcometido(Integer npcId) {
		synchronized (this) {
            ResultSet rs = null;
            
	        List<Condicoes> list = new Vector<Condicoes>();
	        try {
	        	conectar();
				
	            try {
	                rs = comando.executeQuery("SELECT DISTINCT id, nome, descricao, intensidade FROM NPC_ACOMETIDO_POR_CONDICOES, CONDICOES\n" + //
	                		"WHERE NPC_ACOMETIDO_POR_CONDICOES.CONDICOES_id = CONDICOES.id\n" + //
	                		"AND NPC_id = " + npcId);
	                while (rs.next()) {
	    				Condicoes e = this.buildCondicoes(rs);
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
	public List<Condicoes> searchAll() {
		synchronized (this) {
            ResultSet rs = null;
            
	        List<Condicoes> list = new Vector<Condicoes>();
	        try {
	        	conectar();
				
	            try {
	                rs = comando.executeQuery("SELECT * FROM CONDICOES");
	                while (rs.next()) {
	    				Condicoes e = this.buildCondicoes(rs);
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
	public void remove(Condicoes obj) {
        
        	String sql ="DELETE FROM CONDICOES WHERE id=" + obj.getId() + ";";
			
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
    		String sql ="DELETE FROM CONDICOES WHERE id=" + this.retornarValorStringBD(id.toString());
    		System.out.println("%"+sql+"%");
			comando.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void update(Condicoes obj) {
			StringBuffer buffer = new StringBuffer();
	        buffer.append("UPDATE CONDICOES SET ");
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
	public void insert(Condicoes obj) {
			StringBuffer buffer = new StringBuffer();
	        buffer.append("INSERT INTO CONDICOES (");
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
    	return "id, nome, descricao, intensidade";
    }
    
    protected String returnFieldValuesBD(Condicoes obj) {

        StringBuffer buffer = new StringBuffer();
        buffer.append("id=");
        buffer.append(retornarValorStringBD(obj.getId().toString()));
        buffer.append(", nome=");
        buffer.append(retornarValorStringBD(obj.getNome()));
        buffer.append(", descricao=");
        buffer.append(retornarValorStringBD(obj.getDescricao()));
        buffer.append(", intensidade=");
        buffer.append(retornarValorStringBD(obj.getIntensidade().toString()));;

        return buffer.toString();
    }
    
    protected String retornarValoresBD(Condicoes obj) {
    	return
	        obj.getId()
	        + ", "
	        + retornarValorStringBD(obj.getNome())
	        + ", "
	        + retornarValorStringBD(obj.getDescricao())
	        + ", "
	        + obj.getIntensidade();
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
    
    private Condicoes buildCondicoes(ResultSet rs) {
		Condicoes obj = new Condicoes();
		try {  
			obj.setId(rs.getInt("id"));
			obj.setNome(rs.getString("nome"));
			obj.setDescricao(rs.getString("descricao"));
			obj.setIntensidade(rs.getInt("intensidade"));

		} catch (SQLException e) { 
			e.printStackTrace();
		}  
		return obj;
    }
}
