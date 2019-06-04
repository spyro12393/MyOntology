package SQWRL;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
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
	
	// Create Refactor list.
	static List<String> Spoofing_refactor = new ArrayList<String>();
	static List<String> Tampering_refactor = new ArrayList<String>();
	static List<String> Repudiation_refactor = new ArrayList<String>();
	static List<String> Information_refactor = new ArrayList<String>();
	static List<String> Denial_refactor = new ArrayList<String>();
	static List<String> Elevation_refactor = new ArrayList<String>();
			
	// Create Vulnerability variables.
	static int total_class = 0;
	static int NV_PassNoEncrypt = 0;
	static int ServiceMethod = 0;
	static int NV_NameSimilar = 0;
	static int NV_Log = 0;
	
	static String refactorInfoFile = "G:\\MyOntology\\OntoReSec\\src\\SQWRL\\RefactorInfo.txt";
	
	public static String readInfo(String path) {
		String data = "";
		try {
			FileReader fr = new FileReader(path);
			BufferedReader br = new BufferedReader(fr);
			while (br.ready()) {
				data += br.readLine() + "\n";
			}
			fr.close();
			//deleteFile(path);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}
	
	public static void getVal() throws JSONException {
		
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
				
				// System.out.println("Class Name: " + class_name + "\nClass ID:" + class_ID);
				
				total_class += 1;
				System.out.println("Total amount of Classes: " + total_class);
			}
		} finally {
			qexec_totalClass.close();
		}
		
		System.out.print("Total Class number: " + total_class);
		System.out.println("\n--------");
		
		
		
		// has_encrypt calculate.
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
				
				// System.out.println("Has't encrypt: Class Name: " + class_name + "\nClass ID:" + class_ID);
				
				// Adding Refactor suggestions.
				String refactor_encrypt = "[Encrypt] Class: <" + class_name + "> contains Password but hasn't been Encrypted.";
				System.out.println(refactor_encrypt);
				Tampering_refactor.add(refactor_encrypt);
				
				NV_PassNoEncrypt += 1;
				
				/*if(Boolean.valueOf(class_encrypt.toString()) == true) {
					Encrypt += 1;
				}*/
				
			}
		} finally {
			qexec_encrypt.close();
		}
		
		System.out.print("PassNoEncrypt: " + NV_PassNoEncrypt);
		System.out.println("\n--------");
		
		// ====================================================================
		
		// has_ServiceMethod is False
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
				
				
				System.out.println("NoServiceMethod Class Name: " + class_name + "\nClass ID:" + class_ID + "\nClass Get/Set: " + class_getset);// + "\nClass encrypt: " + class_encrypt + "\nClass Get/Set" + class_getset);
				
				// Adding Refactor suggestions.
				String refactor_ServiceMethod = "[ServiceMethod] Class: <" + class_name + "> doesn't have any service method (eg. Get or Set) for var access.";
				System.out.println(refactor_ServiceMethod);
				Tampering_refactor.add(refactor_ServiceMethod);
				Elevation_refactor.add(refactor_ServiceMethod);
				
				if(Boolean.valueOf(class_getset.toString()) == false) {
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
						
				// [INFO] Refactor suggestions comes from jena_query.java.
				
				if(Boolean.valueOf(class_namesimilar.toString()) == true) {
					NV_NameSimilar += 1;
				}
						
			}
		} finally {
			qexec_NameSimilar.close();
		}
		
				String refactorInfo = "";
				refactorInfo = readInfo(refactorInfoFile);
				String[] refactorInfo_split_line = refactorInfo.split("\n");
				
				System.out.println(refactorInfo_split_line[0]);
				for(int i = 0; i < refactorInfo_split_line.length; i++) {
					System.out.println(refactorInfo_split_line[i]);
					Spoofing_refactor.add(refactorInfo_split_line[i]);
				}
				
		
		// ====================================================================
		
		// Has_Log
		String queryString_Log = "PREFIX oo: <http://isq.im.mgt.ncu.edu.tw/Security.owl#>"
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + "SELECT * {"
				+ "?package a oo:Package ; oo:has_ID ?package_ID; oo:has_Name ?package_name; oo:has_Log ?package_log}";// oo:has_encrypt ?class_encrypt; oo:has_Visiblity ?class_visibility; oo:is_NameSimilar ?class_namesimilar; oo:has_Log ?class_Log" + "}";
						
		System.out.println("\n" + queryString_Log);
		Query query_Log = QueryFactory.create(queryString_Log);
		QueryExecution qexec_Log = QueryExecutionFactory.create(query_Log, model);
								
		try {
			ResultSet results = qexec_Log.execSelect();
			while(results.hasNext()) {
								
				QuerySolution soln = results.nextSolution();
				Literal package_ID = soln.getLiteral("package_ID");
				Literal package_name = soln.getLiteral("package_name");
				Literal package_log = soln.getLiteral("package_log");
								
								
				System.out.println("Class Name: " + package_name + "\nClass ID:" + package_ID + "\nClass Name is Log: " + package_log);// + "\nClass encrypt: " + class_encrypt + "\nClass Get/Set" + class_getset);
					
				// Adding Refactor suggestions.
				String refactor_ServiceMethod = "[Log] Package <" + package_name + "> doesn't exist a Log related class.";
				System.out.println(refactor_ServiceMethod);
				Repudiation_refactor.add(refactor_ServiceMethod);
				
				if(Boolean.valueOf(package_log.toString()) == false) {
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
		
		int NV_ServiceMethod = ServiceMethod;
		System.out.println("NV_GetSet: " + NV_ServiceMethod);
		
		System.out.println("NV_PassNoEncrypt: " + NV_PassNoEncrypt);
		
		int VI_Spoofing = 0;
		int VI_Tampering = 0;
		int VI_Repudiation = 0;
		int VI_InformationDisclosure = 0;
		int VI_DenialOfService = 0;
		int VI_ElevationOfPrivilege = 0;
		
		VI_Spoofing = NV_NameSimilar;
		VI_Tampering = NV_ServiceMethod + NV_PassNoEncrypt;
		VI_Repudiation = NV_Log;
		VI_InformationDisclosure = NV_PassNoEncrypt;
		VI_DenialOfService = NV_Repeative;
		VI_ElevationOfPrivilege = NV_ServiceMethod;
		
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
		Map<String, Object> smap = new HashMap<String, Object>();
		smap.put("VI", VI_Spoofing);
		smap.put("Refactor", Spoofing_refactor);
		
		Map<String, Object> tmap = new HashMap<String, Object>();
		tmap.put("VI", VI_Tampering);
		tmap.put("Refactor", Tampering_refactor);
		
		Map<String, Object> rmap = new HashMap<String, Object>();
		rmap.put("VI", VI_Repudiation);
		rmap.put("Refactor", Repudiation_refactor);
		
		Map<String, Object> imap = new HashMap<String, Object>();
		imap.put("VI", VI_InformationDisclosure);
		imap.put("Refactor", Information_refactor);
		
		Map<String, Object> dmap = new HashMap<String, Object>();
		dmap.put("VI", VI_DenialOfService);
		dmap.put("Refactor", Denial_refactor);
		
		Map<String, Object> emap = new HashMap<String, Object>();
		emap.put("VI", VI_ElevationOfPrivilege);
		emap.put("Refactor", Elevation_refactor);
		
		JSONArray s_array = new JSONArray();
		s_array.put(smap);
		JSONArray t_array = new JSONArray();
		t_array.put(tmap);
		JSONArray r_array = new JSONArray();
		r_array.put(rmap);
		JSONArray i_array = new JSONArray();
		i_array.put(imap);
		JSONArray d_array = new JSONArray();
		d_array.put(dmap);
		JSONArray e_array = new JSONArray();
		e_array.put(emap);
		
		JSONObject obj = new JSONObject();
		obj.put("Spoofing", s_array);
		obj.put("Tampering", t_array);
		obj.put("Repudiation", r_array);
		obj.put("InformationDisclosure", i_array);
		obj.put("DenailOfService", d_array);
		obj.put("ElevationOfPrivilege", e_array);
		System.out.println(obj);
		
		
		
		
		// Save to .json file
		try (FileWriter file = new FileWriter("g:\\SecurityWeb\\my-app\\src\\assets\\results.json")) {
            file.write(String.valueOf(obj));
            System.out.println("Finish saving to json");
        } catch (IOException e) {
            e.printStackTrace();
        }
		
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
