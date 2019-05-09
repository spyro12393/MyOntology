package SQWRL;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.text.NumberFormat;
import java.util.Locale;

import org.apache.jena.ontology.DatatypeProperty;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.ObjectProperty;
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
import org.apache.jena.rdf.model.ResourceFactory;
import org.json.JSONException;

public class jena_query {
		
	public static void WriteModel(OntModel model, String baseURI) {

		model.setNsPrefix("Security_output", baseURI);
		// model.write(System.out, "RDF/XML-ABBREV");
		try {
			File file = new File("G:/MyOntology/OntoReSec/src/SQWRL/Security_output.owl/");
			System.out.println("\nWriting Model to " + file);
			FileWriter writer = new FileWriter(file);
			model.write(writer, "RDF/XML-ABBREV");
			System.out.println("Done.");
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
	// Tampering, Elevation of Privilege
	public static void set_hasServiceMethod(OntModel model, String baseURI) {
		
		System.out.println("-------------------------------");
		System.out.println("Start setting has_ServiceMethod.");
		ArrayList<RDFNode> Target_hasServiceMethod = new ArrayList<RDFNode>();
		ArrayList<RDFNode> Class_hasServiceMethod = new ArrayList<RDFNode>();
		DatatypeProperty has_GetSet = model.getDatatypeProperty(baseURI + "has_GetSet");
		ObjectProperty Method_Class = model.getObjectProperty(baseURI + "Method_Class");
				
		//System.out.println("----------Class----------");
				
		String queryString = "PREFIX oo: <http://isq.im.mgt.ncu.edu.tw/Security.owl#>"
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + "SELECT * {"
				+ "?method a oo:Method ; oo:has_ID ?method_ID ; oo:has_Name ?method_name ; oo:has_Visibility ?method_visibility; oo:has_Type ?method_type" + "}";
		
		System.out.println(queryString);
		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.create(query, model);
				
		try {
			
			ResultSet results = qexec.execSelect();
			while(results.hasNext()) {
						
				QuerySolution soln = results.nextSolution();
				//System.out.println("--------------------query---------------------");
				Literal method_ID = soln.getLiteral("method_ID");
				Literal method_name = soln.getLiteral("method_name");
				Literal method_visibility = soln.getLiteral("method_visibility");
				//System.out.println("Method ID: " + method_ID + "\n" + "Method Name: " + method_name + "\nMethod Visibility: " + method_visibility);
				
				if(String.valueOf(method_name).contains("get") || String.valueOf(method_name).contains("set")) {
					Target_hasServiceMethod.add(method_ID);
					System.out.println("Get/Set: " + method_name);
				}
			}
		} finally {
			System.out.println("end");
		}
		
		for(int i=0; i<Target_hasServiceMethod.size(); i++) {
			Individual temp_individual = model.getIndividual(baseURI + Target_hasServiceMethod.get(i));
			temp_individual.setPropertyValue(has_GetSet, ResourceFactory.createTypedLiteral("true"));
		}
		
		System.out.println("has Get/Set Done.");
		
		// If Get/Set = true, find which class it belongs.
		String queryString2 = "PREFIX oo: <http://isq.im.mgt.ncu.edu.tw/Security.owl#>"
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + "SELECT * {"
				+ "?method a oo:Method; oo:has_ID ?method_ID ; oo:has_Name ?method_name; oo:has_GetSet \"true\"; oo:Method_Class ?class."
				+ "?class a oo:Class ; oo:has_ID ?class_ID" + "}";
		
		System.out.println(queryString2);
		Query query2 = QueryFactory.create(queryString2);
		QueryExecution qexec2 = QueryExecutionFactory.create(query2, model);
		
		try {
			System.out.println("start");
			ResultSet results = qexec2.execSelect();
			while(results.hasNext()) {
						
				QuerySolution soln = results.nextSolution();
				//System.out.println("--------------------query---------------------");
				Literal method_ID = soln.getLiteral("method_ID");
				Literal method_name = soln.getLiteral("method_name");
				Literal method_getset = soln.getLiteral("method_getset");
				Literal class_ID = soln.getLiteral("class_ID");
				//Literal method_visibility = soln.getLiteral("method_visibility");
				//System.out.println("Method ID: " + method_ID + "\n" + "Method Name: " + method_name + "\nMethod Get/Set: " + method_getset + "\nwhat Class? " + class_ID);
				
				Class_hasServiceMethod.add(class_ID);
			}
		} finally {
			System.out.println("end");
		}
		
		for(int i=0; i<Class_hasServiceMethod.size(); i++) {
			Individual temp_individual = model.getIndividual(baseURI + Class_hasServiceMethod.get(i));
			temp_individual.setPropertyValue(has_GetSet, ResourceFactory.createTypedLiteral("true"));
		}
		
		System.out.println("has_ServiceMethod Done.");
		System.out.println("-------------------------------");
		
	}
	
	// Tampering, Information disclosure
	public static void set_isEncrypt(OntModel model, String baseURI) {
		System.out.println("Start setting isEncrypt.");
		ArrayList<RDFNode> Target_isPasswd = new ArrayList<RDFNode>();
		ArrayList<RDFNode> Target_has_encrypt = new ArrayList<RDFNode>();
		ArrayList<RDFNode> Class_has_encrypt = new ArrayList<RDFNode>();
		ArrayList<RDFNode> Class_has_passwd = new ArrayList<RDFNode>();
		ArrayList<RDFNode> tempArray = new ArrayList<RDFNode>();

		DatatypeProperty is_passwd = model.getDatatypeProperty(baseURI + "is_passwd");
		DatatypeProperty has_passwd = model.getDatatypeProperty(baseURI + "has_passwd");
		DatatypeProperty has_encrypt = model.getDatatypeProperty(baseURI + "has_encrypt");
		ObjectProperty Attribute_Class = model.getObjectProperty(baseURI + "Attribute_Class");
		
		//System.out.println("Output: " + Attribute_Class);
				
		//System.out.println("----------Class----------");
		String queryString = "PREFIX oo: <http://isq.im.mgt.ncu.edu.tw/Security.owl#>"
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + "SELECT * {"
				+ "?attribute a oo:Attribute ; oo:has_ID ?attribute_ID; oo:has_Name ?attribute_name; oo:has_Visibility ?attribute_Visibility; oo:has_Type ?attribute_Type" + "}";
				
		//System.out.println(queryString);
		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.create(query, model);
				
		try {
			//System.out.println("-----------\nQuerying Attribute info...");
			ResultSet results = qexec.execSelect();
			while(results.hasNext()) {
						
				QuerySolution soln = results.nextSolution();
				//System.out.println("--------------------query---------------------");
				Literal attribute_ID = soln.getLiteral("attribute_ID");
				Literal attribute_name = soln.getLiteral("attribute_name");
				// Literal attribute_Visibility = soln.getLiteral("attribute_Visibility");
				Literal attribute_type = soln.getLiteral("attribute_Type");
				
				//System.out.println("Attribute Name: " + attribute_name + "\nAttribute ID:" + attribute_ID + "\nAttribute Visibility:" + attribute_Visibility + "\nAttribute Type:" + attribute_type );
				
				
				// TODO: Input a list of words that is similar to password
				if(String.valueOf(attribute_name).compareTo("password")==0 || String.valueOf(attribute_name).compareTo("pwd")==0 || String.valueOf(attribute_name).compareTo("Password")==0) {
					//System.out.println("This is a password, setting up is_passwd in owl file.");
					Target_isPasswd.add(attribute_ID);
				}
				if(String.valueOf(attribute_type).compareTo("SecretKey")==0 || String.valueOf(attribute_type).compareTo("Hash")==0 || String.valueOf(attribute_type).compareTo("keygen")==0) {
					//System.out.println("These attributes has encryption, setting up has_encrypt in owl file.");
					Target_has_encrypt.add(attribute_ID);
				}
					
				
			}
		} finally {
			qexec.close();
		}
		
		// Add info to output.owl
		System.out.println("Attribute id that has Password: " + Target_isPasswd);
		
		for(int i=0; i<Target_isPasswd.size(); i++) {
			//System.out.println("T: Set \"" + Target_isPasswd.get(i) + "\" is_passwd.");
			Individual temp_individual = model.getIndividual(baseURI + Target_isPasswd.get(i));
			temp_individual.setPropertyValue(is_passwd, ResourceFactory.createTypedLiteral("true"));
		}
		
		System.out.println("Attribute id that has encrypt: " + Target_has_encrypt);
		for(int i=0; i<Target_has_encrypt.size(); i++) {
			//System.out.println("T: Set \"" + Target_has_encrypt.get(i) + "\" has_encrypt.");
			Individual temp_individual = model.getIndividual(baseURI + Target_has_encrypt.get(i));
			temp_individual.setPropertyValue(has_encrypt, ResourceFactory.createTypedLiteral("true"));
		}
		
		// Add false to all Class "has_encrypt" to false.
		String queryAllClassString = "PREFIX oo: <http://isq.im.mgt.ncu.edu.tw/Security.owl#>"
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + "SELECT * {"
				+ "?class a oo:Class; oo:has_ID ?class_ID "
				+ "}";
		Query queryAllClass = QueryFactory.create(queryAllClassString);
		QueryExecution qexecAllClass = QueryExecutionFactory.create(queryAllClass, model);
		// System.out.println("--------------------Initializing All Class---------------------");
		try {
			ResultSet resultsAllClass = qexecAllClass.execSelect();
			while (resultsAllClass.hasNext()) {
				QuerySolution soln2 = resultsAllClass.nextSolution();
				Literal class_ID = soln2.getLiteral("class_ID");
				tempArray.add(class_ID);
			}
		}
		finally {
			qexecAllClass.close();
		}
		for(int i = 0; i < tempArray.size(); i++) {
			//System.out.println("F: Init all class \"" + tempArray.get(i) + "\" has_encrypt to false.");
			Individual temp_individual = model.getIndividual(baseURI + tempArray.get(i));
			temp_individual.setPropertyValue(has_encrypt, ResourceFactory.createTypedLiteral("false"));
		}
		
		// Find Attribute's Class
		String queryString2 = "PREFIX oo: <http://isq.im.mgt.ncu.edu.tw/Security.owl#>"
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + "SELECT * {"
				+ "?attribute a oo:Attribute; oo:has_ID ?attribute_ID ; oo:has_Name ?attribute_name ; oo:is_passwd \"true\" ; oo:has_Type ?attribute_type ; oo:Attribute_Class ?class." 
				+ "?class a oo:Class ; oo:has_ID ?class_ID"
				+ "}";
			
		Query query2 = QueryFactory.create(queryString2);
		QueryExecution qexec2 = QueryExecutionFactory.create(query2, model);
		//System.out.println("--------------------class---------------------");
		try {
			ResultSet results2 = qexec2.execSelect();
			while (results2.hasNext()) {
				QuerySolution soln2 = results2.nextSolution();
				Literal class_ID = soln2.getLiteral("class_ID");
				//System.out.println("Classes that has password: " + class_ID);
				Class_has_passwd.add(class_ID);
			}
		}
		finally {
			qexec2.close();
		}
		
		// Input info into class ontology owl.
		for(int i=0; i<Class_has_passwd.size();i++) {
			//System.out.println("T: Set \"" + Class_has_passwd.get(i) + "\" has_passwd to true.");
			Individual temp_individual = model.getIndividual(baseURI + Class_has_passwd.get(i));
			temp_individual.setPropertyValue(has_passwd, ResourceFactory.createTypedLiteral("true"));
		}
		
		// Find which class hasn't encrypt
		String queryString3 = "PREFIX oo: <http://isq.im.mgt.ncu.edu.tw/Security.owl#>"
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + "SELECT * {"
				+ "?attribute a oo:Attribute; oo:has_ID ?attribute_ID ; oo:has_Name ?attribute_name ; oo:has_encrypt \"true\" ; oo:has_Type ?attribute_type ; oo:Attribute_Class ?class." 
				+ "?class a oo:Class ; oo:has_ID ?class_ID"
				+ "}";
			
		Query query3 = QueryFactory.create(queryString3);
		QueryExecution qexec3 = QueryExecutionFactory.create(query3, model);
		//System.out.println("--------------------class---------------------");
		try {
			ResultSet results3 = qexec3.execSelect();
			while (results3.hasNext()) {
				QuerySolution soln3 = results3.nextSolution();
				Literal class_ID = soln3.getLiteral("class_ID");
				//System.out.println("Classes that has encrypt func.: " + class_ID);
				Class_has_encrypt.add(class_ID);
			}
		}
		finally {
			qexec3.close();
		}
		
		for(int i=0; i<Class_has_encrypt.size();i++) {
			//System.out.println("T: Set \"" + Class_has_encrypt.get(i) + "\" has_encrypt to true.");
			Individual temp_individual = model.getIndividual(baseURI + Class_has_encrypt.get(i));
			temp_individual.setPropertyValue(has_encrypt, ResourceFactory.createTypedLiteral("true"));
		}
	
		// If has_password = true && has_encrypt = false then "Vulnerable"
		String queryString4 = "PREFIX oo: <http://isq.im.mgt.ncu.edu.tw/Security.owl#>"
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + "SELECT * {"
				+ "?class a oo:Class; oo:has_ID ?class_ID; oo:has_passwd \"true\"; oo:has_encrypt \"false\" "
				+ "}";
			
		Query query4 = QueryFactory.create(queryString4);
		QueryExecution qexec4 = QueryExecutionFactory.create(query4, model);
		//System.out.println("--------------------class---------------------");
		try {
			ResultSet results4 = qexec4.execSelect();
			while (results4.hasNext()) {
				QuerySolution soln4 = results4.nextSolution();
				Literal class_ID = soln4.getLiteral("class_ID");
				//Literal class_name = soln4.getLiteral("class_name");
				System.out.println("Classes that has password but no encrypt method: " + class_ID);
				//Target_has_encrypt.add(class_ID);
			}
		}
		finally {
			qexec2.close();
		}
		
		System.out.println("isEncrypt Done");
		System.out.println("-------------------------------");
	}
	
	public static void set_Decendant(OntModel model, String baseURI) {
		
		HashMap Method_Map = new HashMap();
		HashMap Attribute_Map = new HashMap();
		ArrayList<RDFNode> Target = new ArrayList<RDFNode>();
		// ArrayList<RDFNode> TargetA = new ArrayList<RDFNode>();
		
		DatatypeProperty is_Descendant = model.getDatatypeProperty(baseURI + "is_Descendant");
				
		// ----------------class---------------------
		String queryString = "PREFIX oo: <http://isq.im.mgt.ncu.edu.tw/Security.owl#>"
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + "SELECT * {"
				+ "?class a oo:Class ; oo:has_ID ?class_ID ;oo:Inheritance ?Inheritance_ID; oo:has_Name ?class_name." 
				+ "?Inheritance_ID oo:has_ID ?super_ID;"
				+ "}";
		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.create(query, model);
		try {
			ResultSet results = qexec.execSelect();
			while (results.hasNext()) {
				QuerySolution soln = results.nextSolution();
				// RDFNode x = soln.get("class");
				//System.out.println("--------------------query---------------------");
				Literal class_ID = soln.getLiteral("class_ID");
				Literal class_name = soln.getLiteral("class_name");
				Literal super_ID = soln.getLiteral("super_ID");
				//Literal Inheritance_ID = soln.getLiteral("Inheritance_ID");
				//System.out.println(class_ID + "\t" + class_name);
				Resource Inheritance_ID = (Resource) soln.get("Inheritance_ID");
				String Inheritance = Inheritance_ID.toString();
				//without replace the output had http://isq.im.mgt.ncu.edu.tw/Cohesion.owl#
				Inheritance = Inheritance.replace("http://isq.im.mgt.ncu.edu.tw/Security.owl#", "");
				System.out.println("super class "+super_ID  + "\t" +"sub class "+ class_ID);
				
				//query super class
				String queryString2 = "PREFIX oo: <http://isq.im.mgt.ncu.edu.tw/Security.owl#>"
						+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + "SELECT * {"
						+ "?class a oo:Class ; oo:has_ID \"" + super_ID + "\"; oo:Class_Method ?method."
						+ "?method oo:has_ID ?method_ID; oo:has_Name ?method_name; oo:has_Visibility ?Visibility;  "
						+  "}";
				Query query2 = QueryFactory.create(queryString2);
				QueryExecution qexec2 = QueryExecutionFactory.create(query2, model);
				try {
					ResultSet results3 = qexec2.execSelect();
					while (results3.hasNext()) {
						System.out.println("--------------------Method---------------------");
						QuerySolution soln2 = results3.nextSolution();
						Literal method_ID = soln2.getLiteral("method_ID");
						Literal method_name = soln2.getLiteral("method_name");
						Literal member_visibility = soln2.getLiteral("Visibility");
						
						//System.out.println(method_ID + "\t" + method_name + "\t" );
						
						//descendant
						if (!member_visibility.toString().equals("private")) {
							System.out.println(method_ID + "\t" + method_name + "\t" );
							Method_Map.put(method_ID,method_name);
							//temp_individual.setPropertyValue(is_Descendant , ResourceFactory.createTypedLiteral("true"));
						}
					}
				}
				catch (Exception ignored) {
					  System.out.println(ignored);
				}
				finally {
					qexec2.close();
				}
				//end of super method
				
				//query super class
				String queryString3 = "PREFIX oo: <http://isq.im.mgt.ncu.edu.tw/Security.owl#>"
						+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + "SELECT * {"
						+ "?class a oo:Class ; oo:has_ID \"" + super_ID + "\"; oo:Class_Attribute ?attribute."
						+ "?attribute oo:has_ID ?attribute_ID; oo:has_Name ?attribute_name; oo:has_Visibility ?Visibility;  "
						+  "}";
				Query query3 = QueryFactory.create(queryString3);
				QueryExecution qexec3 = QueryExecutionFactory.create(query3, model);
				try {
					ResultSet results3 = qexec3.execSelect();
					while (results3.hasNext()) {
						System.out.println("--------------------Attribute--------------------");
						QuerySolution soln2 = results3.nextSolution();
						Literal attribute_ID = soln2.getLiteral("attribute_ID");
						Literal attribute_name = soln2.getLiteral("attribute_name");
						Literal attribute_visibility = soln2.getLiteral("Visibility");
						
						if (!attribute_visibility.toString().equals("private")) {
							System.out.println(attribute_ID + "\t" + attribute_name + "\t" );
							Attribute_Map.put(attribute_ID,attribute_name);
						}
					}
				}
				catch (Exception ignored) {
					  System.out.println(ignored);
				}
				finally {
					qexec2.close();
				}
				// end of query super attribute
				
				//set sub class method descendant
				String queryString4 = "PREFIX oo: <http://isq.im.mgt.ncu.edu.tw/Security.owl#>"
						+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + "SELECT * {"
						+ "?class a oo:Class ; oo:has_ID \"" + class_ID + "\"; oo:Class_Method ?method."
						+ "?method oo:has_ID ?method_ID; oo:has_Name ?method_name; "
						+  "}";
				Query query4 = QueryFactory.create(queryString4);
				QueryExecution qexec4 = QueryExecutionFactory.create(query4, model);
				try {
					ResultSet results5 = qexec4.execSelect();
					while (results5.hasNext()) {
						System.out.println("--------------------Descendant Method---------------------");
						QuerySolution soln2 = results5.nextSolution();
						Literal method_ID = soln2.getLiteral("method_ID");
						Literal method_name = soln2.getLiteral("method_name");
						
						System.out.println(method_ID + "\t" + method_name + "\t" );
						//descendant
						
						if(Method_Map.containsValue(method_name)) {
							System.out.println(method_ID + "\t" + method_name + "\t" );
							Target.add(method_ID);
						}
							
							//temp_individual.setPropertyValue(is_Descendant , ResourceFactory.createTypedLiteral("true"));
						
					}
				}
				catch (Exception ignored) {
					  System.out.println(ignored);
				}
				finally {
					qexec2.close();
				}
				//end of set sub class method
				//set sub class Attribute descendant
				String queryString5 = "PREFIX oo: <http://isq.im.mgt.ncu.edu.tw/Security.owl#>"
						+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + "SELECT * {"
						+ "?class a oo:Class ; oo:has_ID \"" + class_ID + "\"; oo:Class_Attribute ?attribute."
						+ "?attribute oo:has_ID ?attribute_ID; oo:has_Name ?attribute_name;  "
						+  "}";
				Query query5 = QueryFactory.create(queryString5);
				QueryExecution qexec5 = QueryExecutionFactory.create(query5, model);
				try {
					ResultSet results5 = qexec5.execSelect();
					while (results5.hasNext()) {
						System.out.println("--------------------descandant Attribute--------------------");
						QuerySolution soln2 = results5.nextSolution();
						Literal attribute_ID = soln2.getLiteral("attribute_ID");
						Literal attribute_name = soln2.getLiteral("attribute_name");
						System.out.println(attribute_ID + "\t" + attribute_name + "\t" );
						if(Attribute_Map.containsValue(attribute_name)) {
							System.out.println(attribute_ID + "\t" + attribute_name + "\t" );
							Target.add(attribute_ID);
						}
						
					}
				}
				catch (Exception ignored) {
					  System.out.println(ignored);
				}
				finally {
					qexec2.close();
				}
			}
		}
		finally {
				qexec.close();
		}
		
		for(int i=0 ; i<Target.size() ; i++) {
			System.out.println("--------------Target----------------");
			System.out.println(Target.get(i));
			Individual temp_individual = model.getIndividual(baseURI + Target.get(i));
			System.out.println(temp_individual);
			temp_individual.setPropertyValue(is_Descendant , ResourceFactory.createTypedLiteral("true"));
			
		}
		
		System.out.println("set Decendant Done.");
	}
	
	// Spoofing
	public static void set_NameSimular(OntModel model, String baseURI) {
		
		System.out.println("Start setting has_NameSimular.");
		ArrayList<RDFNode> Target_ClassName = new ArrayList<RDFNode>();
		ArrayList<RDFNode> Target_ClassID = new ArrayList<RDFNode>();
		
		DatatypeProperty is_NameSimilar = model.getDatatypeProperty(baseURI + "is_NameSimilar");
				
		String queryString = "PREFIX oo: <http://isq.im.mgt.ncu.edu.tw/Security.owl#>"
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + "SELECT * {"
				+ "?class a oo:Class ; oo:has_ID ?class_ID; oo:has_Name ?class_name" + "}";
		
		//System.out.println(queryString);
		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.create(query, model);
				
		try {
			System.out.println("Querying Attribute info...");
			ResultSet results = qexec.execSelect();
			while(results.hasNext()) {
						
				QuerySolution soln = results.nextSolution();
				//System.out.println("--------------------query---------------------");
				Literal class_ID = soln.getLiteral("class_ID");
				Literal class_name = soln.getLiteral("class_name");
				// Literal is_passwd = soln.getLiteral("is_passwd");
				
				// System.out.println("Class Name: " + class_name + "\nClass ID:" + class_ID);
				
				Target_ClassName.add(class_name);
				Target_ClassID.add(class_ID);
				
				//calSim.printSimilarity("password", "Passwd");
			}
		} finally {
			System.out.println("end");
		}
		
		 // 1 because you don't need to compare with yourself.
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
		}
		
		System.out.println("-------------------------------");
	}
	
	public static void testClass(OntModel model) {
		System.out.println("------------------TESTING----------------------");
		System.out.println("Start setting has_ServiceMethod.");
		ArrayList<RDFNode> Target_hasServiceMethod = new ArrayList<RDFNode>();
				
		System.out.println("----------Class----------");
				
		String queryString = "PREFIX oo: <http://isq.im.mgt.ncu.edu.tw/Security.owl#>"
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + "SELECT * {"
				+ "?attribute a oo:Attribute ; oo:has_ID ?attribute_ID; oo:has_Name ?attribute_name; oo:has_Visibility ?attribute_Visibility; oo:has_Type ?attribute_Type" + "}";
		
		System.out.println(queryString);
		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.create(query, model);
				
		try {
			System.out.println("-----------\nQuerying Attribute info...");
			ResultSet results = qexec.execSelect();
			while(results.hasNext()) {
						
				QuerySolution soln = results.nextSolution();
				System.out.println("--------------------query---------------------");
				Literal attribute_ID = soln.getLiteral("attribute_ID");
				Literal attribute_name = soln.getLiteral("attribute_name");
				Literal attribute_Visibility = soln.getLiteral("attribute_Visibility");
				Literal attribute_Type = soln.getLiteral("attribute_Type");
				// Literal is_passwd = soln.getLiteral("is_passwd");
				
				System.out.println("Attribute Name: " + attribute_name + "\nAttribute ID:" + attribute_ID + "\nAttribute Visibility:" + attribute_Visibility + "\nAttribute Type:" + attribute_Type );
				
				
				if(String.valueOf(attribute_name).compareTo("password")==0) {
					System.out.println("This is a password, setting up is_passwd in owl file.");
				}
				
			}
		} finally {
			System.out.println("end");
		}
		
	}
	
	// Repudiation
	public static void set_hasLog(OntModel model, String baseURI) {
		
		System.out.println("Starting to set hasLog");
		ArrayList<RDFNode> Target_isLog = new ArrayList<RDFNode>();
		
		DatatypeProperty is_Log = model.getDatatypeProperty(baseURI + "is_Log");
				
		String queryString = "PREFIX oo: <http://isq.im.mgt.ncu.edu.tw/Security.owl#>"
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + "SELECT * {"
				+ "?class a oo:Class ; oo:has_ID ?class_ID; oo:has_Name ?class_name" + "}";
		
		//System.out.println(queryString);
		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.create(query, model);
				
		try {
			System.out.println("Querying Class info...");
			ResultSet results = qexec.execSelect();
			while(results.hasNext()) {
						
				QuerySolution soln = results.nextSolution();
				//System.out.println("--------------------query---------------------");
				Literal class_ID = soln.getLiteral("class_ID");
				Literal class_name = soln.getLiteral("class_name");
				// Literal is_passwd = soln.getLiteral("is_passwd");
				
				// System.out.println("Class Name: " + class_name + "\nClass ID:" + class_ID);
				
				if(String.valueOf(class_name).contains("Log") || String.valueOf(class_name).contains("log")) {
					Target_isLog.add(class_ID);
				}
				
			}
		} finally {
			System.out.println("end");
		}
		
		for(int i=0; i<Target_isLog.size(); i++) {
			Individual temp_individual = model.getIndividual(baseURI + Target_isLog.get(i));
			// System.out.println(temp_individual);
			temp_individual.setPropertyValue(is_Log , ResourceFactory.createTypedLiteral("true"));
		}
		
	}
	
	// Denial of Service
	public static void set_isRepeative(OntModel model, String baseURI) {
		// [TODO]
		System.out.println("Start setting is_Repeative.");
		ArrayList<RDFNode> Target_ClassName = new ArrayList<RDFNode>();
		ArrayList<RDFNode> Target_ClassID = new ArrayList<RDFNode>();
		
		DatatypeProperty is_NameSimilar = model.getDatatypeProperty(baseURI + "is_Repeative");
				
		String queryString = "PREFIX oo: <http://isq.im.mgt.ncu.edu.tw/Security.owl#>"
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + "SELECT * {"
				+ "?class a oo:Class ; oo:has_ID ?class_ID; oo:has_Name ?class_name" + "}";
		
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
				// Literal is_passwd = soln.getLiteral("is_passwd");
				
				// System.out.println("Class Name: " + class_name + "\nClass ID:" + class_ID);
				
				Target_ClassName.add(class_name);
				Target_ClassID.add(class_ID);
				
				//calSim.printSimilarity("password", "Passwd");
			}
		} finally {
			System.out.println("end");
		}
		
		 // 1 because you don't need to compare with yourself.
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
		}
		
	}
	
	
	public static void main(String[] args) throws JSONException { 
		
		// Def basic info.
		String recentPath = System.getProperty("user.dir");
		String fileName = "G:/MyOntology/OntoReSec/src/SQWRL/Security_Ver1.owl";
		String baseURI = "http://isq.im.mgt.ncu.edu.tw/Security.owl#";
				
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
		
		set_hasServiceMethod(model, baseURI);
		set_isEncrypt(model, baseURI);
		//set_Decendant(model, baseURI);
		set_NameSimular(model, baseURI);
		set_hasLog(model, baseURI);
		// TODO: set_isRepeative(model, baseURI);
		//testClass();
		
		
		//get_DynamicData();
		//get_Rerefence_Attribute();
		
		
		
		/*
		getInheritance();
		Set_Computable();
		WriteModel();
		Set_Refactoring();
		set_AttributeReference();
		WriteModel();
		
		System.out.println("end");
		WriteModel();
		*/
		
		WriteModel(model, baseURI);
		
		calTVI.getVal();
	}
		
			
		
}


