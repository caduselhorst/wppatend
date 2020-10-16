package br.com.wppatend.gixdb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
	
	private static final String DRIVER = "org.postgresql.Driver";
	private static final String URL = "jdbc:postgresql://192.168.0.1:5432/GIX";
	///private static final String URL = "jdbc:postgresql://pj.centerkennedy.com.br:5432/GIX";
	private static final String USER = "postgres";
	private static final String PASS= "#nFbt";
	
	
	public Connection getConnection() throws ClassNotFoundException, SQLException {
		Class.forName(DRIVER);
		return DriverManager.getConnection(URL, USER, PASS);
	}
	
	public void closeConnection(Connection conn) throws SQLException {
		if(conn != null) {
			conn.close();
		}
	}

}
