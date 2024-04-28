import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.xpath.XPathConstants;

import queries.DBpediaQuery;
import queries.OMDbXPath;
import queries.SqlQuery;

public class Mediator {
	
	/*Open Movie Database API*/
	private String URI;
	
	private DecimalFormat df;
	
	public Mediator(String OmdbApiKey) {
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setGroupingSeparator(' ');
		this.df = new DecimalFormat("###,###", symbols);
		this.URI = "http://www.omdbapi.com/?apikey="+OmdbApiKey+"&r=xml&plot=full";
	}
	
	public Map<String, Object> allAboutMovie(ResultSet sqlResult) {
		Map<String, Object> map = new HashMap<>();
		try {
			/*fetching data from local database*/
			String title = sqlResult.getString("title");
			int year = sqlResult.getInt("date");
			String genre = sqlResult.getString("genre");
			String distributor = sqlResult.getString("distributor");
			float budget = sqlResult.getFloat("budget");
			float domesticGross = sqlResult.getFloat("us_income");
			float worldwideGross = sqlResult.getFloat("global_income");
			map.put("title", title);
			map.put("release year", year);
			map.put("genre",genre);
			map.put("distributor",distributor);
			map.put("budget", df.format(budget));
			map.put("domestic gross", df.format(domesticGross));
			map.put("worldwide gross", df.format(worldwideGross));
			
			/*fetching movie plot from remote platform : Open Movie Database*/
			String movieURI = URI + "&t=" + title.replace(" ", "+");
			Object plot = OMDbXPath.XPath(movieURI, "//movie/@plot", XPathConstants.STRING);
			map.put("plot",plot);
			
			/*fetching producers, director and actors from remote platform : DBpedia*/
			DBpediaQuery dbpediaQuery = new DBpediaQuery();
			/*director*/
			List<String> directors = dbpediaQuery.getMovieDirectors(title);
			map.put("directors", directors);
			/*actors*/
			List<String> actors = dbpediaQuery.getMovieActors(title);
			map.put("actors", actors);
			/*producer*/
			List<String> producer = dbpediaQuery.getMovieProducers(title);
			map.put("producer", producer);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	return map;
	}

	
	public List<Map<String, Object>> moviesByActor(String actorName) throws SQLException {
	    DBpediaQuery dbpediaQuery = new DBpediaQuery();
	    SqlQuery sqlQuery = new SqlQuery();
	    List<Map<String, Object>> moviesList = new ArrayList<>();

	    System.out.println("Fetching movies for actor: " + actorName + "...");
	    /*asking DBpedia for the title of movies in which the actor performed*/
	    List<String> movieTitles = dbpediaQuery.getMoviesByActor(actorName);
	    /*handling empty result*/
	    if (movieTitles == null || movieTitles.isEmpty()) {
	        System.out.println("No movies found for actor: " + actorName);
	        return moviesList;
	    }
	    /*handling not empty result*/
        for (String title : movieTitles) {
        	Map<String, Object> movieDetails = new HashMap<>();
        	/*adding the title wether we found more info in the database, or we did not*/
            movieDetails.put("title", title);
            try {
            	/*fetching some movie details from RDB*/
            	ResultSet rs = sqlQuery.getSomeMovieDetails(title);
            	/*only looking at first result from the database since titles are unique*/
            	if (rs!=null && rs.next()) {
                    movieDetails.put("release year", rs.getString("date") != null ? rs.getString("date") : "Unknown");
                    movieDetails.put("genre", rs.getString("genre") != null ? rs.getString("genre") : "Unknown");
                    movieDetails.put("distributor", rs.getString("distributor") != null ? rs.getString("distributor") : "Unknown");

                    List<String> directors = dbpediaQuery.getMovieDirectors(title);
                    List<String> producers = dbpediaQuery.getMovieProducers(title);
                    movieDetails.put("directors", directors != null ? directors : "Unknown");
                    movieDetails.put("producers", producers != null ? producers : "Unknown");
                }
            } catch (SQLException e) {
            	e.printStackTrace();
    		}
            moviesList.add(movieDetails);
        }
	    System.out.println("Total movies processed: " + moviesList.size());
	    return moviesList;
	}
}
