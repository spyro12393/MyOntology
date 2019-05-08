package SQWRL;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import org.apache.jena.ontology.Individual;
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
import org.apache.jena.rdf.model.ResourceFactory;

public class calTVI {
	
	
	
	public static void getVal() {
		
		int total_class = 0;
		int NV_Encrypt = 0;
		int NV_ServiceMethod = 0;
		int NV_NameSimilar = 0;
		int NV_Log = 0;
		
		String fileName = "G:/MyOntology/OntoReSec/src/SQWRL/Security_output.owl";
		String baseURI = "http://isq.im.mgt.ncu.edu.tw/Security.owl#";
		
		System.out.println("Reading results.");
		

		// Create Ontology model
		System.out.println(fileName);
		OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
			
		try {
			File file = new File(fileName);
			FileReader reader = new FileReader(file);
			model.read(reader, null);
			System.out.println(model);
			
			} catch (Exception e) {	
				e.printStackTrace();
			}
		
		
		
		// Find all class number
		String queryString_totalClass = "PREFIX oo: <http://isq.im.mgt.ncu.edu.tw/Security.owl#>"
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + "SELECT * {"
				+ "?class a oo:Class ; oo:has_ID ?class_ID; oo:has_Name ?class_name}";// oo:has_encrypt ?class_encrypt; oo:has_Visiblity ?class_visibility; oo:is_NameSimilar ?class_namesimilar; oo:has_Log ?class_Log" + "}";
		
		System.out.println(queryString_totalClass);
		Query query_totalClass = QueryFactory.create(queryString_totalClass);
		QueryExecution qexec_totalClass = QueryExecutionFactory.create(query_totalClass, model);
				
		try {
			System.out.println("-----------\nQuerying Attribute info...");
			ResultSet results = qexec_totalClass.execSelect();
			while(results.hasNext()) {
						
				QuerySolution soln = results.nextSolution();
				//System.out.println("--------------------query---------------------");
				Literal class_ID = soln.getLiteral("class_ID");
				Literal class_name = soln.getLiteral("class_name");
				
				System.out.println("Class Name: " + class_name + "\nClass ID:" + class_ID);
				
				total_class += 1;
			}
		} finally {
			qexec_totalClass.close();
		}
		
		System.out.print("Total Class number: " + total_class);
		System.out.println("\n--------");
		
		
		
		// has_encrypt cal
		String queryString_encrypt = "PREFIX oo: <http://isq.im.mgt.ncu.edu.tw/Security.owl#>"
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + "SELECT * {"
				+ "?class a oo:Class ; oo:has_ID ?class_ID; oo:has_Name ?class_name; oo:has_encrypt ?class_encrypt}";// oo:has_encrypt ?class_encrypt; oo:has_Visiblity ?class_visibility; oo:is_NameSimilar ?class_namesimilar; oo:has_Log ?class_Log" + "}";
		
		System.out.println(queryString_encrypt);
		Query query_encrypt = QueryFactory.create(queryString_encrypt);
		QueryExecution qexec_encrypt = QueryExecutionFactory.create(query_encrypt, model);
				
		try {
			System.out.println("-----------\nQuerying Attribute info...");
			ResultSet results = qexec_encrypt.execSelect();
			while(results.hasNext()) {
						
				QuerySolution soln = results.nextSolution();
				//System.out.println("--------------------query---------------------");
				Literal class_ID = soln.getLiteral("class_ID");
				Literal class_name = soln.getLiteral("class_name");
				Literal class_encrypt = soln.getLiteral("class_encrypt");
				
				
				System.out.println("Class Name: " + class_name + "\nClass ID:" + class_ID + "\nClass encrypt: " + class_encrypt);// + "\nClass encrypt: " + class_encrypt + "\nClass Get/Set" + class_getset);
				
				
				
				if(Boolean.valueOf(class_encrypt.toString()) == true) {
					NV_Encrypt += 1;
				}
				
			}
		} finally {
			qexec_encrypt.close();
		}
		
		System.out.print("NV_Encrypt: " + NV_Encrypt);
		System.out.println("\n--------");
		
		// ====================================================================
		
		// has_ServiceMethod
		String queryString_ServiceMethod = "PREFIX oo: <http://isq.im.mgt.ncu.edu.tw/Security.owl#>"
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + "SELECT * {"
				+ "?class a oo:Class ; oo:has_ID ?class_ID; oo:has_Name ?class_name; oo:has_GetSet ?class_getset}";// oo:has_encrypt ?class_encrypt; oo:has_Visiblity ?class_visibility; oo:is_NameSimilar ?class_namesimilar; oo:has_Log ?class_Log" + "}";
		
		System.out.println("\n" + queryString_ServiceMethod);
		Query query_ServiceMethod = QueryFactory.create(queryString_ServiceMethod);
		QueryExecution qexec_ServiceMethod = QueryExecutionFactory.create(query_ServiceMethod, model);
				
		try {
			ResultSet results = qexec_ServiceMethod.execSelect();
			while(results.hasNext()) {
						
				QuerySolution soln = results.nextSolution();
				Literal class_ID = soln.getLiteral("class_ID");
				Literal class_name = soln.getLiteral("class_name");
				Literal class_getset = soln.getLiteral("class_getset");
				
				
				System.out.println("Class Name: " + class_name + "\nClass ID:" + class_ID + "\nClass Get/Set: " + class_getset);// + "\nClass encrypt: " + class_encrypt + "\nClass Get/Set" + class_getset);
				
				if(Boolean.valueOf(class_getset.toString()) == true) {
					NV_ServiceMethod += 1;
				}
				
			}
		} finally {
			qexec_ServiceMethod.close();
		}
		
		// ====================================================================
		
		// has_NameSimilar
		String queryString_NameSimilar = "PREFIX oo: <http://isq.im.mgt.ncu.edu.tw/Security.owl#>"
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + "SELECT * {"
				+ "?class a oo:Class ; oo:has_ID ?class_ID; oo:has_Name ?class_name; oo:is_NameSimilar ?class_namesimilar}";// oo:has_encrypt ?class_encrypt; oo:has_Visiblity ?class_visibility; oo:is_NameSimilar ?class_namesimilar; oo:has_Log ?class_Log" + "}";
				
		System.out.println("\n" + queryString_NameSimilar);
		Query query_NameSimilar = QueryFactory.create(queryString_NameSimilar);
		QueryExecution qexec_NameSimilar = QueryExecutionFactory.create(query_NameSimilar, model);
						
		try {
			ResultSet results = qexec_NameSimilar.execSelect();
			while(results.hasNext()) {
								
				QuerySolution soln = results.nextSolution();
				Literal class_ID = soln.getLiteral("class_ID");
				Literal class_name = soln.getLiteral("class_name");
				Literal class_namesimilar = soln.getLiteral("class_namesimilar");
						
						
				System.out.println("Class Name: " + class_name + "\nClass ID:" + class_ID + "\nClass Name Similar: " + class_namesimilar);// + "\nClass encrypt: " + class_encrypt + "\nClass Get/Set" + class_getset);
						
				if(Boolean.valueOf(class_namesimilar.toString()) == true) {
					NV_NameSimilar += 1;
				}
						
			}
		} finally {
			qexec_NameSimilar.close();
		}
		
		
		
		
		// Show Results
		System.out.println("==================Results=================");
		System.out.println("Total Class number: " + total_class);
		System.out.println("NV_Encrypt: " + NV_Encrypt);
		System.out.println("NV_getset: " + NV_ServiceMethod);
		System.out.println("NV_NameSimilar: " + NV_NameSimilar);
	}
}
