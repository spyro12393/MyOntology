package SQWRL;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.jena.atlas.json.JsonArray;
import org.apache.jena.atlas.json.JsonObject;
import org.apache.jena.atlas.json.io.parser.JSONParser;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

public class calTVI {
	
	public static void getVal() throws JSONException {
		
		int total_class = 0;
		int NV_PassNoEncrypt = 0;
		int ServiceMethod = 0;
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
		
		
		
		// has_encrypt calculate
		String queryString_encrypt = "PREFIX oo: <http://isq.im.mgt.ncu.edu.tw/Security.owl#>"
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + "SELECT * {"
				+ "?class a oo:Class ; oo:has_ID ?class_ID; oo:has_Name ?class_name; oo:has_EncryptPassword \"false\" " + "}";
		
		System.out.println(queryString_encrypt);
		Query query_encrypt = QueryFactory.create(queryString_encrypt);
		QueryExecution qexec_encrypt = QueryExecutionFactory.create(query_encrypt, model);
		
		System.out.println("Calculating encrypt value:");
		try {
			ResultSet results = qexec_encrypt.execSelect();
			while(results.hasNext()) {
						
				QuerySolution soln = results.nextSolution();
				//System.out.println("--------------------query---------------------");
				Literal class_ID = soln.getLiteral("class_ID");
				Literal class_name = soln.getLiteral("class_name");
				//Literal class_encrypt = soln.getLiteral("class_encrypt");
				
				System.out.println("Has't encrypt: Class Name: " + class_name + "\nClass ID:" + class_ID);
				
				
				
				NV_PassNoEncrypt += 1;
				
				/*if(Boolean.valueOf(class_encrypt.toString()) == true) {
					Encrypt += 1;
				}*/
				
			}
		} finally {
			qexec_encrypt.close();
		}
		
		// [TODO]: HERE
		String refactor_encrypt = "";
		
		System.out.print("PassNoEncrypt: " + NV_PassNoEncrypt);
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
					ServiceMethod += 1;
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
		
		// ====================================================================
		
				// has_NameSimilar
		String queryString_Log = "PREFIX oo: <http://isq.im.mgt.ncu.edu.tw/Security.owl#>"
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + "SELECT * {"
				+ "?class a oo:Class ; oo:has_ID ?class_ID; oo:has_Name ?class_name; oo:is_Log ?class_log}";// oo:has_encrypt ?class_encrypt; oo:has_Visiblity ?class_visibility; oo:is_NameSimilar ?class_namesimilar; oo:has_Log ?class_Log" + "}";
						
		System.out.println("\n" + queryString_Log);
		Query query_Log = QueryFactory.create(queryString_Log);
		QueryExecution qexec_Log = QueryExecutionFactory.create(query_Log, model);
								
		try {
			ResultSet results = qexec_Log.execSelect();
			while(results.hasNext()) {
								
				QuerySolution soln = results.nextSolution();
				Literal class_ID = soln.getLiteral("class_ID");
				Literal class_name = soln.getLiteral("class_name");
				Literal class_log = soln.getLiteral("class_log");
								
								
				System.out.println("Class Name: " + class_name + "\nClass ID:" + class_ID + "\nClass Name is Log: " + class_log);// + "\nClass encrypt: " + class_encrypt + "\nClass Get/Set" + class_getset);
						
				if(Boolean.valueOf(class_log.toString()) == true) {
					NV_Log += 1;
				}
								
			}
		} finally {
			qexec_Log.close();
		}
		
		
		// Show Results
		System.out.println("==================Results=================");
		
		System.out.println("Total Class number: " + total_class);
		
		// TODO:
		int NV_Repeative = 0;
		System.out.println("Repeative: " + NV_Repeative);
		
		System.out.println("PassNoEncrypt: " + NV_PassNoEncrypt);
		
		System.out.println("GetSet: " + ServiceMethod);
		
		System.out.println("NV_NameSimilar: " + NV_NameSimilar);
		
		System.out.println("NV_Log: " + NV_Log);
		
		int NV_ServiceMethod = total_class - ServiceMethod;
		System.out.println("NV_GetSet: " + NV_ServiceMethod);
		
		System.out.println("NV_PassNoEncrypt: " + NV_PassNoEncrypt);
		
		int Spoofing = 0;
		int Tampering = 0;
		int Repudiation = 0;
		int InformationDisclosure = 0;
		int DenialOfService = 0;
		int ElevationOfPrivilege = 0;
		
		Spoofing = NV_NameSimilar;
		Tampering = NV_ServiceMethod + NV_PassNoEncrypt;
		Repudiation = NV_Log;
		InformationDisclosure = NV_PassNoEncrypt;
		DenialOfService = NV_Repeative;
		ElevationOfPrivilege = NV_ServiceMethod;
		
		/*
		String arj = "{"
				+
				"\"InformationDisclosure\":{\"VI\":" + InformationDisclosure + ", \"Refactor\":{\"Encrypt\":\"problem is...\", \"Decode\":\"Hello\"}},\r\n" + 
				"\"Tampering\":{\"VI\":" + Tampering + ", \"Refactor\":\"info\"},\r\n" + 
				"\"Repudiation\":{\"VI\":" + Repudiation + ", \"Refactor\":\"info\"},\r\n" + 
				"\"ElevationOfPrivilege\":{\"VI\":" + ElevationOfPrivilege + ", \"Refactor\":\"info\"},\r\n" + 
				"\"Spoofing\":{\"VI\":" + Spoofing + ", \"Refactor\":\"info\"},\r\n" + 
				"\"DenialOfService\":{\"VI\":" + DenialOfService + ", \"Refactor\":\"info\"}"
				+"}";
		*/
		
		// Save results to json file
		List<String> list_refactor = new ArrayList<String>();
		list_refactor.add("Error: Fix a");
		list_refactor.add("Warning: Fix a and b");
		list_refactor.add("Error: Fix B");
		
		Map map = new HashMap();
		map.put("VI", 3);
		map.put("Refactor", list_refactor);
		
		JSONArray array = new JSONArray();
		array.put(map);
		
		JSONObject obj = new JSONObject();
		obj.put("Spoofing", array);
		obj.put("Tampering", array);
		obj.put("Repudiation", array);
		obj.put("InformationDisclosure", array);
		obj.put("DenailOfService", array);
		obj.put("ElevationOfPrivilege", array);
		System.out.println(obj);
		
		
		
		
		// Save to .json file
		try (FileWriter file = new FileWriter("g:\\SecurityWeb\\my-app\\src\\assets\\results.json")) {
            file.write(String.valueOf(obj));
            System.out.println("Finish saving to json");
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        
	}
	
	public static void save2JSON(String index, int value) {
		JsonObject jsonObj1 = new JsonObject();
	}

	public static void getSpoofingVal() {
		
	}
	
	public static void getRefactor() {
		
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
		
		
		
	}

}
