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
            String sql = "SELECT * FROM POCAO WHERE ID=" + id;
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
					//TODO: CHANGE IMPLEMENTATION TO BRING PLAYER POTIONS
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
		try {  
			obj.setId(rs.getInt("id"));
			obj.setDescricao(rs.getString("descricao"));

		} catch (SQLException e) { 
			e.printStackTrace();
		}  
		return obj;
    }
}
