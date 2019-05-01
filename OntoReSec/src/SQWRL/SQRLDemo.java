package SQWRL;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;  

import org.apache.jena.ontology.OntProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.DatatypeProperty;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;

public class SQRLDemo {
	public static void main(String[] args) {
		
		// Def basic info.
		String recentPath = System.getProperty("user.dir");
		String fileName = recentPath + "/src/SQWRL/Security_Ver1.owl";
		String baseURI = "http://isq.im.mgt.ncu.edu.tw/Security.owl#";
		
		// Create Ontology model
		OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
		
		try {
			
			File file = new File(fileName);
			FileReader reader = new FileReader(file);
			model.read(reader, null);
			System.out.println(model);
			
		} catch (Exception e) {	
			e.printStackTrace();
		}
		
		System.out.println("----------Class----------");
		
		String queryString = "PREFIX oo: <http://isq.im.mgt.ncu.edu.tw/Security.owl#>"
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + "SELECT * {"
				+ "?class a oo:Class ; oo:has_ID ?class_ID ; oo:has_Name ?class_name ; oo:has_Visibility ?class_visibility" + "}";
		
		System.out.println(queryString);
		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.create(query, model);
		
		try {
			System.out.println("start");
			ResultSet results = qexec.execSelect();
			while(results.hasNext()) {
				
				QuerySolution soln = results.nextSolution();
				System.out.println("--------------------query---------------------");
				Literal class_ID = soln.getLiteral("class_ID");
				Literal class_name = soln.getLiteral("class_name");
				Literal class_visibility = soln.getLiteral("class_visibility");
				System.out.println("Class ID: " + class_ID + "\n" + "Class Name: " + class_name + "\nClass Visibility: " + class_visibility);
				
			}
		} finally {
			System.out.println("end");
		}
		
		/*
		OntClass cls = model.createClass(":FoodClass");
		cls.addComment("the EquivalentClass of Food...", "EN");  
		OntClass oc = model.getOntClass(baseURI);
		*/
		
	}
}
