	package dao;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class DAO 
{
	protected Connection connection;
	
	public DAO()
	{
		connection = null;
	}
	
	public boolean connect()
	{
		String driverName = "org.postgresql.Driver";
		String serverName = "localhost";
		String databaseName = "test";
		int port = 5432;
		String url = "jdbc:postgresql://" + serverName + ":" + port + "/" + databaseName;
		String username = "postgres";
		String password = "123";
		boolean status = false;
		
		try
		{
			Class.forName(driverName);
			connection = DriverManager.getConnection(url, username, password);
			status = connection == null;
			System.out.println("Connection with database succeeded!");
		}
		catch(ClassNotFoundException ex)
		{
			System.err.println("Connection with database failed -- Driver not found:\n" + ex.getMessage());
		}
		catch(SQLException ex)
		{
			System.err.println("Connection with database failed --\n" + ex.getMessage());
		}
		
		return status;
	}
	
	public boolean close()
	{
		boolean status = false;
		
		try
		{
			connection.close();
			status = true;
		}
		catch(SQLException ex)
		{
			System.err.println("Atempt to close connection failed --\n" + ex.getMessage());
		}
		
		return status;
	}
	
	public static String toMD5(String password) throws Exception
	{
		MessageDigest m = MessageDigest.getInstance("MD5");
		m.update(password.getBytes(), 0, password.length());
		return new BigInteger(1, m.digest()).toString(); 
	}
}
