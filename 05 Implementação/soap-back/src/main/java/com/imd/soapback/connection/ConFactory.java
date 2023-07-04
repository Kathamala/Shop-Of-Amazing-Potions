package main.java.com.imd.soapback.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConFactory {

	private static final String MySQLDriver = "com.mysql.cj.jdbc.Driver"; 
	public static final String DAO_PATH = "jdbc:mysql://localhost/soap?useTimezone=true&serverTimezone=America/Fortaleza";
	public static final String USER = "root";
	public static final String PASSWORD = "root";

	public static Connection conexao(String url, String nome, String senha) throws ClassNotFoundException, SQLException {                
		Class.forName(MySQLDriver);  

		return DriverManager.getConnection(url, nome, senha);  
	}
}
