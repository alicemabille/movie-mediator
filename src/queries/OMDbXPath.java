package queries;
import javax.xml.xpath.*;
import javax.xml.namespace.QName;
import javax.xml.parsers.*;
import org.w3c.dom.*;


public class OMDbXPath {

    public static Object XPath(String uri, String requete, QName typeRetour){
		//Le dernier parametre indique le type de resultat souhaité
		//XPathConstants.STRING: chaine de caracteres (String)
		//XPathConstants.NODESET: ensemble de noeuds DOM (NodeList)
		//XPathConstants.NODE: noeud DOM (Node) - le premier de la liste
		//XPathConstants.BOOLEAN: bool�en (Boolean) - vrai si la liste n'est pas vide
		//XPathConstants.NUMBER: num�rique (Double) - le contenu du noeud s�lectionn� transform� en Double

        try{
		//Transformation en document DOM du contenu XML
		DocumentBuilderFactory fabrique = DocumentBuilderFactory.newInstance();
		DocumentBuilder parseur = fabrique.newDocumentBuilder();
		Document document = parseur.parse(uri);

		//création de l'objet XPath 
        	XPathFactory xfabrique = XPathFactory.newInstance();
        	XPath xpath = xfabrique.newXPath();
        
        	//évaluation de l'expression XPath
        	XPathExpression exp = xpath.compile(requete);
        	return exp.evaluate(document, typeRetour);
        	
        } catch(Exception e){
        	System.out.println(e.getMessage());
        }
        return null;
    }
        	
}