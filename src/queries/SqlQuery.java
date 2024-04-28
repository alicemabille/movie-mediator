/**
 * File containing the class to execute the SQL query and get the results
 * 
 * @version 1.0.0
 * @since 30/01/2024
 * @author Mabille Alice, Volquardsen Alex
 */
package queries;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SqlQuery {
	
	public ResultSet getAllMovieDetails(String movieTitle)
	{
		String query = "SELECT title, date, genre, budget, us_income, global_income, distributor "
				+ "FROM movie "
				+ "WHERE title LIKE ? ;";
		try
		{
			if(JdbcConnection.getConnection()!=null) {
				PreparedStatement ps = JdbcConnection.getConnection().prepareStatement(query);
				ps.setString(1, movieTitle);
				return ps.executeQuery();
			}
		
		} catch (SQLException se) {
			se.printStackTrace();
		}
		return null;
	}
	
	public ResultSet getSomeMovieDetails(String movieTitle)
	{
		String query = "SELECT date, genre, distributor "
				+ "FROM movie "
				+ "WHERE title = ? ;";
		try
		{
			if(JdbcConnection.getConnection()!=null) {
				PreparedStatement ps = JdbcConnection.getConnection().prepareStatement(query);
				ps.setString(1, movieTitle);
				return ps.executeQuery();
			}
		} catch (SQLException se) {
			//se.printStackTrace();
		}
		return null;
	}
}
