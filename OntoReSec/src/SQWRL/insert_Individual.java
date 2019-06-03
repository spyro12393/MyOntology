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
		
		DatatypeProperty has_Name = model.getDatatypeProperty(baseURI + "has_Name");
		DatatypeProperty has_ID = model.getDatatypeProperty(baseURI + "has_ID");
		
		ObjectProperty Project_Package = model.getObjectProperty(baseURI + "Project_Package");
		
		
		OntClass Project_Class = model.getOntClass(baseURI + "Project");
		OntClass Package_Class = model.getOntClass(baseURI + "Package");
		Individual Project = Project_Class.createIndividual(baseURI + split_line[0]);
		Individual Package = Package_Class.createIndividual(baseURI + split_line[2]);
		
		Project.addProperty(Project_Package,Package);
		Project.setPropertyValue(has_Name, ResourceFactory.createTypedLiteral(split_line[0]));
		Package.setPropertyValue(has_Name, ResourceFactory.createTypedLiteral(split_line[2]));
		Project.setPropertyValue(has_ID , ResourceFactory.createTypedLiteral(split_line[0]));
		Package.setPropertyValue(has_ID , ResourceFactory.createTypedLiteral(Package_name));
		
		
		
		return Package;
	}
	
	public static Individual[] Add_Class(OntModel model, String[] split_line, Individual Package) {
		
		ObjectProperty Class_Package = model.getObjectProperty(baseURI + "Class_Package");
		ObjectProperty Package_Classes = model.getObjectProperty(baseURI + "Package_Class");
		DatatypeProperty has_Name = model.getDatatypeProperty(baseURI + "has_Name");
		DatatypeProperty has_ID = model.getDatatypeProperty(baseURI + "has_ID");
		OntClass Class_Class = model.getOntClass(baseURI + "Class");
		
		// Add Class Name
		split_line[3] = split_line[3].replace('[',']');
		split_line[3] = split_line[3].replaceAll("]","");
		Class_name = split_line[3].split(", ");
		
		Individual[] Class = new Individual[Class_name.length];
		for(int i = 0 ; i < Class_name.length ; i++) {
			System.out.println(Class_name[i]);
			Class[i] = Class_Class.createIndividual(baseURI + Package_name + "." + Class_name[i]);
			Package.addProperty(Package_Classes, Class[i]);
			Class[i].setPropertyValue(has_Name , ResourceFactory.createTypedLiteral(Class_name[i]));
			Class[i].setPropertyValue(has_ID , ResourceFactory.createTypedLiteral(Package_name + "." + Class_name[i]));
			Class[i].addProperty(Class_Package, Package);
		}
		
		return Class;
	}
	
	public static Individual[][] Add_AttributeInfo(OntModel model,String[] split_line, Individual[] Class){
		OntClass Attribute_Class = model.getOntClass(baseURI + "Attribute");
		DatatypeProperty has_Name = model.getDatatypeProperty(baseURI + "has_Name");
		DatatypeProperty has_ID = model.getDatatypeProperty(baseURI + "has_ID");
		DatatypeProperty has_Visibility = model.getDatatypeProperty(baseURI + "has_Visibility");
		DatatypeProperty has_Type = model.getDatatypeProperty(baseURI + "has_Type");
		
		ObjectProperty Class_Attribute = model.getObjectProperty(baseURI + "Class_Attribute");
		ObjectProperty DataProperty_Attribute_Class = model.getObjectProperty(baseURI + "Attribute_Class");
		
		String[] First_Split_AttrbuteName = split_line[6].split("], ");
		String[] Sec_Split_AttrbuteName;
		
		String[] First_Split_Visibility = split_line[9].split("], ");
		
		String First_Split_AttributeType = split_line[7].replace("byte[]", "byte{}");
		System.out.println(First_Split_AttributeType);
		String[] Sec_Split_AttrbuteType = First_Split_AttributeType.split("], ");
		String[] Third_Split_AttrbuteType;
		
		String Attr_ID;
		
		Individual[][] Attribute = new Individual[First_Split_AttrbuteName.length][];
		
		for(int i = 0 ; i < First_Split_AttrbuteName.length ; i++) {
			First_Split_AttrbuteName[i] = First_Split_AttrbuteName[i].replace('[',']');
			First_Split_AttrbuteName[i] = First_Split_AttrbuteName[i].replaceAll("]","");
			
			
			First_Split_Visibility[i] = First_Split_Visibility[i].replace('[',']');
			First_Split_Visibility[i] = First_Split_Visibility[i].replaceAll("]","");
			
			Sec_Split_AttrbuteType[i] = Sec_Split_AttrbuteType[i].replace('[',']');
			Sec_Split_AttrbuteType[i] = Sec_Split_AttrbuteType[i].replaceAll("]","");
			Sec_Split_AttrbuteType[i] = Sec_Split_AttrbuteType[i].replace("byte{}","byte[]");
			
			Sec_Split_AttrbuteName = First_Split_AttrbuteName[i].split(", ");
			Sec_Split_Visibility = First_Split_Visibility[i].split(", ");
			Third_Split_AttrbuteType = Sec_Split_AttrbuteType[i].split(", ");
			Attribute[i] = new Individual[Sec_Split_AttrbuteName.length];
			
			for(int j = 0 ; j < Sec_Split_AttrbuteName.length ; j++) {
				if(Sec_Split_AttrbuteName[j].length()>0) {
					Attr_ID =  Package_name + "." + Class_name[i] + "." + Sec_Split_AttrbuteName[j];
					Attribute[i][j] = Attribute_Class.createIndividual(baseURI + Attr_ID);
					Attribute[i][j].setPropertyValue(has_Name , ResourceFactory.createTypedLiteral(Sec_Split_AttrbuteName[j]));
					Attribute[i][j].setPropertyValue(has_ID , ResourceFactory.createTypedLiteral(Attr_ID));
					Attribute[i][j].setPropertyValue(has_Visibility , ResourceFactory.createTypedLiteral(Sec_Split_Visibility[j]));
					Attribute[i][j].setPropertyValue(has_Type , ResourceFactory.createTypedLiteral(Third_Split_AttrbuteType[j]));
					
					Attribute[i][j].addProperty(DataProperty_Attribute_Class, Class[i]);
					Class[i].addProperty(Class_Attribute,Attribute[i][j]);
					
				}
			}
		}
		
		return Attribute;
	}
	
	public static Individual[][] Add_MethodInfo(OntModel model,String[] split_line, Individual[] Class, Individual[][] Attribute) {
		
		OntClass Method_Class = model.getOntClass(baseURI + "Method");
		
		DatatypeProperty has_Name = model.getDatatypeProperty(baseURI + "has_Name");
		DatatypeProperty has_ID = model.getDatatypeProperty(baseURI + "has_ID");
		DatatypeProperty has_Visibility = model.getDatatypeProperty(baseURI + "has_Visibility");
		DatatypeProperty has_Type = model.getDatatypeProperty(baseURI + "has_Type");
		
		ObjectProperty ObjectProperty_Method_Class = model.getObjectProperty(baseURI + "Method_Class");
		ObjectProperty Class_Method = model.getObjectProperty(baseURI + "Class_Method");
		String[] First_Split_MethodName = split_line[10].split("], ");
		String[] Sec_Split_MethodName;
		String[] First_Split_Visibility = split_line[13].split("], ");
		String[] Sec_Split_Visibility;
		
		String First_Split_Type = split_line[11].replace("byte[]","byte{}");
		String[] Sec_Split_Type = First_Split_Type.split("], ");
		String[] Third_Split_Type;
		
		
		
		String ID;
		
		Individual[][] Method = new Individual[First_Split_MethodName.length][];
		
		System.out.println("----- Add_MethodInfo ------");
		
		for(int i = 0 ; i < First_Split_MethodName.length ; i++) {
			First_Split_MethodName[i] = First_Split_MethodName[i].replace('[',']');
			First_Split_MethodName[i] = First_Split_MethodName[i].replaceAll("]","");
			
			First_Split_Visibility[i] = First_Split_Visibility[i].replace('[',']');
			First_Split_Visibility[i] = First_Split_Visibility[i].replaceAll("]","");
			
			Sec_Split_Type[i] = Sec_Split_Type[i].replace('[',']');
			Sec_Split_Type[i] = Sec_Split_Type[i].replaceAll("]","");
			Sec_Split_Type[i] = Sec_Split_Type[i].replace("byte{}","byte[]");
			
			System.out.println(First_Split_MethodName[i]);
			
			Sec_Split_MethodName = First_Split_MethodName[i].split(", ");
			Sec_Split_Visibility = First_Split_Visibility[i].split(", ");
			Third_Split_Type = Sec_Split_Type[i].split(", ");
			//System.out.println(Sec_Split_Type[i]);
			Method[i] = new Individual[Sec_Split_MethodName.length];
			System.out.println("----- Second AttributeInfo ------");
			
			for(int j = 0 ; j < Sec_Split_MethodName.length ; j++) {
				System.out.println(Sec_Split_MethodName[j]);
				if(Sec_Split_MethodName[j].length()>0) {
					
					ID =  Package_name + "." + Class_name[i] + "." + Sec_Split_MethodName[j]+"()";
					Method[i][j] = Method_Class.createIndividual(baseURI + ID);
					Method[i][j].setPropertyValue(has_Name , ResourceFactory.createTypedLiteral(Sec_Split_MethodName[j]+"()"));
					Method[i][j].setPropertyValue(has_ID , ResourceFactory.createTypedLiteral(ID));
					Method[i][j].setPropertyValue(has_Visibility , ResourceFactory.createTypedLiteral(Sec_Split_Visibility[j]));
					Method[i][j].setPropertyValue(has_Type, ResourceFactory.createTypedLiteral(Third_Split_Type[j]));
					
					Class[i].addProperty(Class_Method,Method[i][j]);
					Method[i][j].addProperty(ObjectProperty_Method_Class, Class[i]);
				}
				
			}
			System.out.println("----- End ------");
			
			
		}
		return Method;
		
		
		
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
		Individual[][] Attribute = Add_AttributeInfo(model,split_line,Class);
		Individual[][] Method = Add_MethodInfo(model,split_line,Class,Attribute);
		
		WriteModel();
		
		
		
		/*
		String Package_names = split_line[3];
		System.out.println(Package_names);
		*/
		
	}
	
}
