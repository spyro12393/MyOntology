package SQWRL;

import java.io.File;
import java.io.FileReader;

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
import org.apache.jena.rdf.model.ResourceFactory;

public class calTVI {
	
	
	
	public static void getVal() {
		
		String fileName = "G:/MyOntology/OntoReSec/src/SQWRL/Security_output.owl";
		String baseURI = "http://isq.im.mgt.ncu.edu.tw/Security.owl#";
		
		System.out.println("====================================================");
		System.out.println("Reading results.");
		System.out.println("====================================================");

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
		
		
		String queryString = "PREFIX oo: <http://isq.im.mgt.ncu.edu.tw/Security.owl#>"
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + "SELECT * {"
				+ "?class a oo:Class ; oo:has_ID ?class_ID; oo:has_encrypt ?class_encrypt; oo:is_NameSimilar ?class_namesimilar; oo:has_Log ?class_Log" + "}";
		
		System.out.println(queryString);
		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.create(query, model);
				
		try {
			System.out.println("-----------\nQuerying Attribute info...");
			ResultSet results = qexec.execSelect();
			while(results.hasNext()) {
						
				QuerySolution soln = results.nextSolution();
				//System.out.println("--------------------query---------------------");
				Literal class_ID = soln.getLiteral("class_ID");
				Literal class_name = soln.getLiteral("class_name");
				Literal class_encrypt = soln.getLiteral("class_encrypt");
				Literal class_getset = soln.getLiteral("class_getset");
				
				System.out.println("Class Name: " + class_name + "\nClass ID:" + class_ID + "\nClass encrypt: " + class_encrypt + "\nClass Get/Set" + class_getset);
				
				//Target_ClassName.add(class_name);
				//Target_ClassID.add(class_ID);
				
				//calSim.printSimilarity("password", "Passwd");
			}
		} finally {
			System.out.println("end");
		}
		
		 /*/ 1 because you don't need to compare with yourself.
		for(int i=0; i<Target_ClassName.size(); i++) {
			for(int j=i+1; j<Target_ClassName.size(); j++) {
				
				//System.out.println(i + ": " + Target_ClassName.get(i) + " compare w/ " + j + ": " + Target_ClassName.get(j));
				//calSimilarity.printSimilarity(String.valueOf(Target_ClassName.get(i)), String.valueOf(Target_ClassName.get(j)));
				double sim = calSimilarity.similarity(String.valueOf(Target_ClassName.get(i)), String.valueOf(Target_ClassName.get(j)));
				if(sim > 0.7) {
					System.out.println(sim + " is too similar." + "ClassID: " + Target_ClassID.get(i) + " and " + Target_ClassID.get(j));
					
					Individual temp_individual = model.getIndividual(baseURI + Target_ClassID.get(i));
					System.out.println(temp_individual);
					temp_individual.setPropertyValue(is_NameSimilar , ResourceFactory.createTypedLiteral("true"));
					
					Individual temp_individual2 = model.getIndividual(baseURI + Target_ClassID.get(j));
					System.out.println(temp_individual2);
					temp_individual2.setPropertyValue(is_NameSimilar , ResourceFactory.createTypedLiteral("true"));
				}
			}
		}*/
	}
}
