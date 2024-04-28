package csv;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class MovieGenreCSV {
	private final String[] GENRES = {"Adventure", "Comedy", "Drama", "Action", "Thriller-or-Suspense", "Romantic-Comedy"};
	private String directory;
	
	public MovieGenreCSV(String directory) {
		this.directory = directory;
	}

	public void createCSV(String genre) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(directory + "/" + genre + ".csv"));
		writer.write("Rank	Movie	Release Date	Theatrical Distributor	MPAA Rating	Gross	Tickets Sold");
		for (int annee = 2000; annee < 2015; annee++) {
			/*generating url*/
			String url = "http://www.the-numbers.com/market/" + annee + "/genre/" + genre;
			//connecting and fetching data
			Document doc = Jsoup.connect(url).get();
			
			Elements contents = doc.getElementsByTag("tr");
			for (int l = 2; l < contents.size()-2; l++) { /*lines*/
				/*begin new line*/
				writer.write("\n");
				Element content = contents.get(l);
				Elements lineContents = content.children();
				
				for (Element lineContent : lineContents) { /*columns*/
					String tmpTxt = lineContent.text() + "\t";
					writer.write(tmpTxt);
				}
			}
		}
		writer.close();
	}
	
	public void createAllCSV() throws IOException {
		for (int i = 0; i<GENRES.length; i++) {
			createCSV(GENRES[i]);
		}
	}
}
