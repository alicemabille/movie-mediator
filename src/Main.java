import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

import csv.MovieGenreCSV;
import queries.JdbcConnection;
import queries.SqlQuery;

public class Main {

	public static void main(String[] args) {
		/*setting up connection to local DB and OMDB API key*/
		Properties props = new Properties();
		String omdbApiKey = "";
		FileInputStream in;
		try {
			in = new FileInputStream("./.env");
			props.load(in);
			in.close();

			String driver = props.getProperty("jdbc.driver");
			if (driver != null) {
			    Class.forName(driver) ;
			}

			omdbApiKey = props.getProperty("OMDB_API_key");
			String url = props.getProperty("jdbc.url");
			String username = props.getProperty("jdbc.username");
			String password = props.getProperty("jdbc.password");
			JdbcConnection.setDbConnection(url,"ied_project",username,password);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		/*asking user for type of query*/
		Scanner scanner = new Scanner(System.in);
		System.out.println("Choose an option below :\n"
				+ "1 - Get informations about a movie\n"
				+ "2 - Get informations about an actor\n"
				+ "3 - Create CSV file for every movie genre in www.the-numbers.com/market \n");
		String option = scanner.nextLine();
		
		/*3 types of queries*/
		switch (option) {
			case "1" :
				System.out.println("Enter a movie title\n");
				Mediator mediator = new Mediator(omdbApiKey);
				String movieTitle = scanner.nextLine().trim();
				if (movieTitle.isEmpty()) {
	                System.out.println("No film title entered. Exiting...");
	                scanner.close();
	                return;
	            }
				SqlQuery sqlQuery = new SqlQuery();
				ResultSet sqlResult = sqlQuery.getAllMovieDetails(movieTitle);
				Map<String,Object> movieInfo = new HashMap<>();
				try {
					if (sqlResult==null) {
						System.out.println("No result from local database. Please check that the connection properties were correctly set in the .env file and that the database is up.");
						scanner.close();
						break;
					}
					while (sqlResult.next()) {
						if (scanner.nextLine().equals("")) {
							movieInfo = mediator.allAboutMovie(sqlResult);
							for (String column : movieInfo.keySet()) {
								  System.out.println(column + " : " + movieInfo.get(column));
							}
						}
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				scanner.close();
				break;
				
			case "2":
			    System.out.println("Enter an actor name :");
			    String actorName = scanner.nextLine();
			    Mediator mediator2 = new Mediator(omdbApiKey);

			    if (actorName.isEmpty()) {
			        System.out.println("No actor name entered. Exiting...");
			        scanner.close();
			        return;
			    }

			    try {
			        List<Map<String, Object>> movies = mediator2.moviesByActor(actorName);
			        if (!movies.isEmpty()) {
			            System.out.println("Movies found for " + actorName + ":\n");
			            for (Map<String, Object> movieDetails : movies) {
			            	for (String column : movieDetails.keySet()) {
								  System.out.println(column + " : " + movieDetails.get(column));
							}
			                System.out.println("-------------------------");
			            }
			        }
			    } catch (SQLException e) {
			        System.err.println("SQL Error: " + e.getMessage());
			        e.printStackTrace();
			    }
			    scanner.close();
			    break;

				
			case "3" :
				System.out.println("Enter file path\n");
				String filePath = scanner.nextLine();
				System.out.println("Creating CSV files using data from www.the-numbers.com...\n");
				MovieGenreCSV csvWriter = new MovieGenreCSV(filePath);
				try {
					csvWriter.createAllCSV();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
		}
		scanner.close();	}
}
