/**
 * File containing the JdbcConnection class.
 * 
 * @version 1.0.0
 * @since 12/04/2024
 * @author MABILLE Alice
 */
package queries;

import java.sql.Connection;
import java.sql.DriverManager;

public class JdbcConnection {
	
	private static String host;
	private static String base;
	private static String user;
	private static String password;
	private static String url;
	/**
	 * Lazy singleton instance.
	 */
	private static Connection connection;
	
	// Method to set the static elements 
	public static void setDbConnection(String newHost, String newBase, String newUser, String newPassword)
	{
		host = newHost;
		base = newBase;
		user = newUser;
		password = newPassword;
		url = "jdbc:mysql://" + host + "/" + base + "?characterEncoding=latin1&useSSL=false&serverTimezone=UTC";
	}

	// Method to get a connection with the database
	public static Connection getConnection() {
		if (connection == null) {
			try {
				DriverManager.registerDriver(new com.mysql.jdbc.Driver());
				connection = DriverManager.getConnection(url, user, password);
			} catch (Exception e) {
				System.err.println("Connection failed : " + e.getMessage());
			}
		}
		return connection;
	}
}
