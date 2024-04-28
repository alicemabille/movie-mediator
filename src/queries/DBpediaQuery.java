package queries;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.jena.query.*;
import org.apache.jena.rdfconnection.RDFConnection;

public class DBpediaQuery {
    
    private static final String DBPEDIA_SPARQL_ENDPOINT = "https://dbpedia.org/sparql";
    private static final String PREFIX = "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\r\n"
    		+ "PREFIX : <http://dbpedia.org/resource/>\r\n"
    		+ "PREFIX dbpedia2: <http://dbpedia.org/property/>\r\n"
    		+ "PREFIX dbpedia: <http://dbpedia.org/>\r\n"
    		+ "PREFIX dbo: <http://dbpedia.org/ontology/>\n"
    		+ "PREFIX dbr: <http://dbpedia.org/resource/>\n";
    
    public List<String> queryDBpedia(String queryString) {
    	RDFConnection conn = RDFConnection.connect(DBPEDIA_SPARQL_ENDPOINT);
    	List<String> list = new ArrayList<String>();
        try (QueryExecution qExec = conn.query(PREFIX + queryString)) {
	        ResultSet results = qExec.execSelect();
	        List<QuerySolution> solns = ResultSetFormatter.toList(results) ;
	        for (QuerySolution soln : solns ) {
	        	//only copying the valuable data and getting rid of ( ?title= and @en
	        	Pattern findTitle = Pattern.compile("\".*?\""); //valuable data is between double quotes
	 	        Matcher matcher = findTitle.matcher(soln.toString());
	 	        while (matcher.find()) {
	 	        	String title = matcher.group().replace("\"","");
	 	        	if (!list.contains(title)) { //avoid putting twice the same title
	 	        		list.add(title);
	 	        	}
	 	        }
	        }
	        qExec.close() ;
	        return list;
        } catch (Exception e) {
			// TODO: handle exception
        	//e.printStackTrace();
		}
    	conn.close() ;
        return null;
    }
    
    
    public List<String> getMovieDirectors(String movieTitle) {
    	String queryString = "SELECT ?directorName WHERE {"
    			+ "?film a dbo:Film ;"
    			+ "    foaf:name '" + movieTitle + "'@en ;"
    			+ "    dbo:director ?d ."
    			+ "?d foaf:name ?directorName ."
    			+ "}";
    	return queryDBpedia(queryString);
    }
    
    
    public List<String> getMovieActors(String movieTitle) {
    	String queryString = "SELECT ?actorName WHERE {"
    			+ "?film a dbo:Film ;"
    			+ "    foaf:name '" + movieTitle + "'@en ;"
    			+ "    dbo:starring ?a ."
    			+ "?a foaf:name ?actorName ."
    			+ "}";
    	return queryDBpedia(queryString);
    }
    
    public List<String> getMovieProducers(String movieTitle) {
    	String queryString = "SELECT ?producerName WHERE {"
    			+ "?film a dbo:Film ;"
    			+ "    foaf:name '"+ movieTitle + "'@en ;"
    			+ "    dbo:producer ?p ."
    			+ "?p foaf:name ?producerName ."
    			+ "}";
    	return queryDBpedia(queryString);
    }
    
    public List<String> getMoviesByActor(String actorName) {
        String escapedActorName = actorName.replace("'", "\\'");
        String queryString = "SELECT ?title WHERE {" +
                             "  ?film a dbo:Film ." +
                             "  ?film dbo:starring ?actor ." +
                             "  ?actor foaf:name '" + escapedActorName + "'@en ." +
                             "  ?film foaf:name ?title ." +
                             "}";
        return queryDBpedia(queryString);
    }
}