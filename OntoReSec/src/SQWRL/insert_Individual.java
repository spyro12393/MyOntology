package SQWRL;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.ModelFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import org.apache.jena.ontology.DatatypeProperty;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.ontology.OntProperty;
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

public class insert_Individual {
	
	static OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
	static String baseURI = "http://isq.im.mgt.ncu.edu.tw/Security.owl#";
	static String fileName = "G://MyOntology/OntoReSec/src/SQWRL/Security_input.owl";
	static String staticDataFile = "G://MyOntology/OntoReSec/src/SQWRL/StaticData.txt";
	

	static String Package_name;
	static String[] Class_name;
	static String[] Class_Visibility;
	static String[] Inheritance_Class;
	static String[] Sec_Split_Visibility;
	
	public static OntModel ReadModel(String fileName) {
		try {
			File file = new File(fileName);
			FileReader reader = new FileReader(file);

			model.read(reader, null);
			System.out.println(model);

			// model.write(System.out, "RDF/XML-ABBREV");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return model;
	}
	
	public static void WriteModel() {
		model.setNsPrefix("Security", baseURI);
		// model.write(System.out, "RDF/XML-ABBREV");
		try {
			File file = new File(fileName);
			
			FileWriter writer = new FileWriter(file);
			model.write(writer, "RDF/XML-ABBREV");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

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
	
	public static void deleteFile(String path) {
		File file = new File(path);
		file.delete();
	}
	
	public static Individual Add_ProjectPackage(OntModel model, String[] split_line) {
		
		ObjectProperty Project_Package = model.getObjectProperty(baseURI + "Project_Package");
		OntClass Project_Class = model.getOntClass(baseURI + "Project");
		OntClass Package_Class = model.getOntClass(baseURI + "Package");
		Individual Project = Project_Class.createIndividual(baseURI + split_line[0]);
		Individual Package = Package_Class.createIndividual(baseURI + split_line[2]);
		Project.addProperty(Project_Package,Package);
		return Package;
	}
	
	public static Individual[] Add_Class(OntModel model, String[] split_line, Individual Package) {
		
		ObjectProperty Package_Classes = model.getObjectProperty(baseURI + "Package_Class");
		ObjectProperty has_Visibility = model.getObjectProperty(baseURI + "has_Visibility");
		DatatypeProperty has_Name = model.getDatatypeProperty(baseURI + "has_Name");
		DatatypeProperty has_ID = model.getDatatypeProperty(baseURI + "has_ID");
		OntClass Class_Class = model.getOntClass(baseURI + "Class");
		
		// Add Class Name
		split_line[3] = split_line[3].replace('[',']');
		split_line[3] = split_line[3].replaceAll("]","");
		Class_name = split_line[3].split(", ");
		
		split_line[13] = split_line[13].replace('[',']');
		split_line[13] = split_line[13].replaceAll("]","");
		Class_Visibility = split_line[13].split(", ");
		
		Individual[] Class = new Individual[Class_name.length];
		for(int i = 0 ; i < Class_name.length ; i++) {
			System.out.println(Class_name[i]);
			Class[i] = Class_Class.createIndividual(baseURI + Package_name + "." + Class_name[i]);
			Package.addProperty(Package_Classes, Class[i]);
			Class[i].setPropertyValue(has_Name , ResourceFactory.createTypedLiteral(Class_name[i]));
			Class[i].setPropertyValue(has_ID , ResourceFactory.createTypedLiteral(Package_name + "." + Class_name[i]));
			
			for(int j = 0; j < Class_Visibility.length; j++) {
				System.out.println(Class_Visibility[j]);
			}
		}
		
		
		/* Add Visibility
		split_line[13] = split_line[13].replace('[',']');
		split_line[13] = split_line[13].replaceAll("]","");
		Class_Visibility = split_line[13].split(", ");
		
		for(int i = 0 ; i < Class_Visibility.length ; i++) {
			System.out.println(Class_Visibility[i]);
			Class[i] = Class_Class.createIndividual(baseURI + Package_name + "." + Class_Visibility[i]);
			Package.addProperty(Package_Classes, Class[i]);
			Class[i].setPropertyValue(has_Visibility , ResourceFactory.createTypedLiteral(Class_Visibility[i]));
		}*/
		
		return Class;
	}
	
	public static void main(String[] args) {
		
		String staticData = "";
		staticData = readInfo(staticDataFile);
		// System.out.println(staticData);
		String[] split_line = staticData.split("\n");
		model = ReadModel(fileName);
		
		Package_name = split_line[2];
		
		Individual Package = Add_ProjectPackage(model, split_line);
		Individual[] Class = Add_Class(model, split_line, Package);
		
		WriteModel();
		
		
		
		/*
		String Package_names = split_line[3];
		System.out.println(Package_names);
		*/
		
	}
	
}
