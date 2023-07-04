package conexao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConFactory {

	public static final int MYSQL = 0;  
	public static final int POSTGRES = 1;  
	   private static final String MySQLDriver = "com.mysql.cj.jdbc.Driver"; 
	  
	   public static Connection conexao(String url, String nome, String senha) throws ClassNotFoundException, SQLException {                
	    	Class.forName(MySQLDriver);  

	    	return DriverManager.getConnection(url, nome, senha);  
	   }
}
