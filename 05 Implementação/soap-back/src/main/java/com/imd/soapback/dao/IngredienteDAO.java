package main.java.com.imd.soapback.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Vector;

import main.java.com.imd.soapback.connection.ConFactory;
import main.java.com.imd.soapback.interfaceDAO.IIngrediente;
import main.java.com.imd.soapback.model.Ingrediente;

public class IngredienteDAO implements IIngrediente {

	private String 	URL;
	private String NOME;
	private String SENHA;

	private Connection con;  
    private Statement comando;
	
	public IngredienteDAO(String server, String user, String password) throws SQLException {
		this.URL = server;
		this.NOME = user;
		this.SENHA = password;
	}

	@Override
	public Ingrediente search(Integer id) {
        Ingrediente obj = new Ingrediente();

		try {
			conectar();
            String sql = "SELECT * FROM INGREDIENTE WHERE ID=" + id;
            ResultSet rs = comando.executeQuery(sql);
            if (rs.next()) {
				obj = this.buildIngrediente(rs);
            }
        } catch (SQLException SQLe) {
            SQLe.printStackTrace();
        } catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
        return obj;
	}

	@Override
	public List<Ingrediente> searchAll() {
		synchronized (this) {
            ResultSet rs = null;
            
	        List<Ingrediente> list = new Vector<Ingrediente>();
	        try {
	        	conectar();
				
	            try {
	                rs = comando.executeQuery("SELECT * FROM INGREDIENTE");
	                while (rs.next()) {
	    				Ingrediente e = this.buildIngrediente(rs);
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
	public List<Ingrediente> searchAllByJogadorId(Integer jogadorId) {
		synchronized (this) {
            ResultSet rs = null;
            
	        List<Ingrediente> list = new Vector<Ingrediente>();
	        try {
	        	conectar();
				
	            try {
					//TODO: CHANGE IMPLEMENTATION TO BRING PLAYER INGREDIENTS
	                rs = comando.executeQuery("SELECT * FROM INGREDIENTE");
	                while (rs.next()) {
	    				Ingrediente e = this.buildIngrediente(rs);
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
	public void remove(Ingrediente obj) {
        
        	String sql ="DELETE FROM INGREDIENTE WHERE id=" + obj.getId() + ";";
			
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
    		String sql ="DELETE FROM INGREDIENTE WHERE id=" + this.retornarValorStringBD(id.toString());
    		System.out.println("%"+sql+"%");
			comando.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void update(Ingrediente obj) {
			StringBuffer buffer = new StringBuffer();
	        buffer.append("UPDATE INGREDIENTE SET ");
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
	public void insert(Ingrediente obj) {
			StringBuffer buffer = new StringBuffer();
	        buffer.append("INSERT INTO INGREDIENTE (");
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
    	return "id, valor, nome, tempo_necessario";
    }
    
    protected String returnFieldValuesBD(Ingrediente obj) {

        StringBuffer buffer = new StringBuffer();
        buffer.append("id=");
        buffer.append(retornarValorStringBD(obj.getNome()));
        buffer.append(", valor=");		
        buffer.append(retornarValorStringBD(obj.getValor().toString()));
        buffer.append(", nome=");
        buffer.append(retornarValorStringBD(obj.getNome()));
        buffer.append(", tempo_necessario=");
        buffer.append(retornarValorStringBD(obj.getTempoNecessario().toString()));;

        return buffer.toString();
    }
    
    protected String retornarValoresBD(Ingrediente obj) {
    	return
	        obj.getId()
	        + ", "
	        + obj.getValor()
	        + ", "
	        + retornarValorStringBD(obj.getNome())
	        + ", "
	        + obj.getTempoNecessario();
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
    
    private Ingrediente buildIngrediente(ResultSet rs) {
		Ingrediente obj = new Ingrediente();
		try {  
			obj.setId(rs.getInt("id"));
			obj.setValor(rs.getFloat("valor"));
			obj.setNome(rs.getString("nome"));
			obj.setTempoNecessario(rs.getInt("tempo_necessario"));

		} catch (SQLException e) { 
			e.printStackTrace();
		}  
		return obj;
    }
}
