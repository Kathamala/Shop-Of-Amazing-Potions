package main.java.com.imd.soapback.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Vector;

import main.java.com.imd.soapback.connection.ConFactory;
import main.java.com.imd.soapback.interfaceDAO.IJogador;
import main.java.com.imd.soapback.model.Ingrediente;
import main.java.com.imd.soapback.model.Jogador;

public class JogadorDAO implements IJogador {

	private String 	URL;
	private String NOME;
	private String SENHA;

	private Connection con;  
    private Statement comando;
	
	public JogadorDAO(String server, String user, String password) throws SQLException {
		this.URL = server;
		this.NOME = user;
		this.SENHA = password;
	}

	@Override
	public Jogador search(Integer id) {
        Jogador jogador = new Jogador();

		try {
			conectar();
            String sql = "SELECT * FROM JOGADOR WHERE ID=" + id;
            ResultSet rs = comando.executeQuery(sql);
            if (rs.next()) {
				jogador = this.buildJogador(rs);
            }
        } catch (SQLException SQLe) {
            SQLe.printStackTrace();
        } catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
        return jogador;
	}

	@Override
	public List<Jogador> searchAll() {
		synchronized (this) {
            ResultSet rs = null;
            
	        List<Jogador> jogadores = new Vector<Jogador>();
	        try {
	        	conectar();
				
	            try {
	                rs = comando.executeQuery("SELECT * FROM JOGADOR");
	                while (rs.next()) {
	    				Jogador e = this.buildJogador(rs);
	    				jogadores.add(e);
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
	        return jogadores;
        }
	}
	
	@Override
	public Jogador searchByName(String name) {
        Jogador jogador = new Jogador();

		try {
			conectar();
            String sql = "SELECT * FROM JOGADOR WHERE NOME=" + retornarValorStringBD(name);
            ResultSet rs = comando.executeQuery(sql);
            if (rs.next()) {
				jogador = this.buildJogador(rs);
            }
        } catch (SQLException SQLe) {
            SQLe.printStackTrace();
        } catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
        return jogador;
	}

	@Override
	public void remove(Jogador jogador) {
        
        	String sql ="DELETE FROM JOGADOR WHERE id=" + jogador.getId() + ";";
			
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
    		String sql ="DELETE FROM JOGADOR WHERE id=" + this.retornarValorStringBD(id.toString());
    		System.out.println("%"+sql+"%");
			comando.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void update(Jogador jogador) {
			StringBuffer buffer = new StringBuffer();
	        buffer.append("UPDATE JOGADOR SET ");
	        buffer.append(returnFieldValuesBD(jogador));
	        buffer.append(" WHERE id=");
	        buffer.append(jogador.getId());
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
	public void insert(Jogador jogador) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("INSERT INTO JOGADOR (");
		buffer.append(this.retornarCamposBD());
		buffer.append(") VALUES (");
		buffer.append(this.retornarValoresBD(jogador));
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
    	return "id, nome, dinheiro";
    }
    
    protected String returnFieldValuesBD(Jogador j) {

        StringBuffer buffer = new StringBuffer();
        buffer.append("id=");
        buffer.append(retornarValorStringBD(j.getId().toString()));
        buffer.append(", nome=");
        buffer.append(retornarValorStringBD(j.getNome()));
        buffer.append(", dinheiro=");
        buffer.append(retornarValorStringBD(j.getDinheiro().toString()));

        return buffer.toString();
    }
    
    protected String retornarValoresBD(Jogador j) {
    	return
	        j.getId()
	        + ", "
	        + retornarValorStringBD(j.getNome())
	        + ", "
	        + j.getDinheiro();
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

	@Override
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
    
    private Jogador buildJogador(ResultSet rs) {
		Jogador jogador = new Jogador();
		try {  
			jogador.setId(rs.getInt("id"));
			jogador.setNome(rs.getString("nome"));
			jogador.setDinheiro(rs.getFloat("dinheiro"));

		} catch (SQLException e) { 
			e.printStackTrace();
		}  
		return jogador;
    }

	public Integer getNextId(){
		try{
			conectar();
			String sql = "SELECT MAX(id) as id FROM JOGADOR";
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

	public void adicionarIngredienteInventario(Jogador jogador, Ingrediente ingrediente){
		try {
			conectar();
			String sql = "SELECT * FROM JOGADOR_POSSUI_INGREDIENTE WHERE JOGADOR_id=" + jogador.getId() + " AND INGREDIENTE_id=" + ingrediente.getId();
			ResultSet rs = comando.executeQuery(sql);
			if (rs.next()) {
				//AJEITAR AQUI!
				StringBuffer buffer = new StringBuffer();
				buffer.append("UPDATE JOGADOR_POSSUI_INGREDIENTE SET quantidade = quantidade + 1 WHERE");
				buffer.append("JOGADOR_id=" + jogador.getId() + " AND INGREDIENTE_id=" + ingrediente.getId());
				sql = buffer.toString();
			} else{
				StringBuffer buffer = new StringBuffer();
				buffer.append("INSERT INTO JOGADOR_POSSUI_INGREDIENTE (JOGADOR_id, INGREDIENTE_id, quantidade) VALUES (");
				buffer.append(jogador.getId() + ", " + ingrediente.getId() + ", 1");
				buffer.append(")");
				sql = buffer.toString();
			}

			comando.execute(sql);

			//TODO: Reduzir dinheiro do jogador (transaction).
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
