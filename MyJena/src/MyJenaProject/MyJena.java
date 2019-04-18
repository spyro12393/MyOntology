package MyJenaProject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Iterator;

import org.apache.jena.ontology.Individual;
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

public class MyJena {

	private String[] class_name;

	private String[][] attribute_name;
	private String[][] attribute_type;
	private String[][] attribute_isprimitivetype;
	private String[][] attribute_visibility;

	private String[][] method_name;
	private String[][] method_return_type;
	private String[][] method_return_isprimitivetype;
	private String[][] method_visibility;
	private String[][][] method_parameters;
	private String[][][] method_invocations;

	public void createOntology() {
		// TODO Auto-generated method stub
		OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
		OntClass myClass;
		OntClass myAttribute;
		OntClass myMethod;

		OntProperty hasName;
		OntProperty hasAttribute;

		String baseURI = "http://isq.im.mgt.ncu.edu.tw/OOOntology.owl#";

		// String fileName = "src/OOontology.owl";
		String fileName = "D:/中央資管研究所/研究室/論文/OOOntology3.owl";
		try {
			File file = new File(fileName);
			FileReader reader = new FileReader(file);

			model.read(reader, null);

			model.write(System.out, "RDF/XML-ABBREV");
		} catch (Exception e) {
			e.printStackTrace();
		}

		myClass = model.getOntClass("http://isq.im.mgt.ncu.edu.tw/OOOntology.owl#Class");
		myAttribute = model.getOntClass("http://isq.im.mgt.ncu.edu.tw/OOOntology.owl#Attribute");
		myMethod = model.getOntClass("http://isq.im.mgt.ncu.edu.tw/OOOntology.owl#Method");

		hasName = model.getOntProperty("http://isq.im.mgt.ncu.edu.tw/OOOntology.owl#has_Name");
		hasAttribute = model.getOntProperty("http://isq.im.mgt.ncu.edu.tw/OOOntology.owl#Class_Attribute");

		Individual a = myAttribute.createIndividual(baseURI + "B.a");
		Individual b = myAttribute.createIndividual(baseURI + "B.b");
		myClass.createIndividual(baseURI + "B").addProperty(hasName, "B").addProperty(hasAttribute, a).addProperty(hasAttribute, b);

		/*
		 * for (int i = 0; i < class_name.length; i++) {
		 * myClass.createIndividual(baseURI + class_name[i]);
		 * //System.out.println("class: " + class_name[i] );
		 * //System.out.print("attribute:　"); for (int j = 0; j <
		 * attribute_name[i].length; j++) { //System.out.print(" " +
		 * attribute_name[i][j]); myAttribute.createIndividual(baseURI +
		 * attribute_name[i][j]); }
		 * 
		 * for (int j = 0; j < method_name[i].length; j++) {
		 * myMethod.createIndividual(baseURI + method_name[i][j]); }
		 * 
		 * }
		 */

		model.setNsPrefix("OOOntology", baseURI);
		model.write(System.out, "RDF/XML-ABBREV");
		
		/*
		 * StringWriter sw = new StringWriter(); model.write(sw,"RDF/XML");
		 * String owlCode = sw.toString();
		 * 
		 * File file = new File("D:/中央資管研究所/研究室/論文/OOOntology2.owl"); try{
		 * FileWriter fw = new FileWriter(file); fw.write(owlCode); fw.close();
		 * }catch(FileNotFoundException fnfe){ fnfe.printStackTrace();
		 * }catch(IOException ioe){ ioe.printStackTrace(); }
		 */

		/*
		 * 
		 * // Retrieve Ontology Classes Iterator classIter =
		 * model.listClasses(); while (classIter.hasNext()) { OntClass ontclass
		 * = (OntClass) classIter.next(); String uri = ontclass.getURI(); String
		 * classname = ontclass.getLocalName(); if (uri != null) {
		 * System.out.println(uri); System.out.println(classname); OntClass c =
		 * model.getOntClass(uri); } }
		 * 
		 * // Retrieve a Specified Class String classURI =
		 * "http://isq.im.mgt.ncu.edu.tw/myfirstontology.owl#Student"; OntClass
		 * son = model.getOntClass(classURI); String classname =
		 * son.getLocalName(); System.out.println(classname);
		 * 
		 * Iterator propIter = son.listDeclaredProperties(); //
		 * listDatatypeProperties() // and // listObjectProperties() while
		 * (propIter.hasNext()) { OntProperty property = (OntProperty)
		 * propIter.next();
		 * 
		 * String propertyname = property.getLocalName();
		 * System.out.print(propertyname); String dom = ""; String rng = ""; if
		 * (property.getDomain() != null) dom =
		 * property.getDomain().getLocalName(); if (property.getRange() != null)
		 * rng = property.getRange().getLocalName(); System.out.println(":\t(" +
		 * dom + ")\t -> (" + rng + ")");
		 * 
		 * }
		 * 
		 */
		String queryString = "PREFIX uni: <http://isq.im.mgt.ncu.edu.tw/OOOntology.owl#>"
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + "SELECT * {"
				+ "?Class a uni:Attribute; uni:has_Name ?x" + "}";
		Query query = QueryFactory.create(queryString);

		try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
			ResultSet results = qexec.execSelect();
			for (; results.hasNext();) {
				QuerySolution soln = results.nextSolution();
				RDFNode x = soln.get("Class"); // Get a result variable by name.
				//Resource r = soln.getResource("x"); // Get a result variable
				// - must be a resource
			    Literal l = soln.getLiteral("x"); // Get a result variable -
				// must be a literal
				System.out.println(x + "\t" + l);
			}
		}

	}

	public void setClassName(String[] class_name) {
		this.class_name = class_name;
	}

	public void setAttributeName(String[][] s) {
		this.attribute_name = s;
	}

	public void setAttributeType(String[][] s) {
		this.attribute_type = s;
	}

	public void setAttributeVisibility(String[][] s) {
		this.attribute_visibility = s;
	}

	public void setAttributeTypeisPri(String[][] s) {
		this.attribute_isprimitivetype = s;
	}

	public void setMethodName(String[][] s) {
		this.method_name = s;
	}

	public void setMethodReturnType(String[][] s) {
		this.method_return_type = s;
	}

	public void setMethodReturnTypeisPri(String[][] s) {
		this.method_return_isprimitivetype = s;
	}

	public void setMethodVisibility(String[][] s) {
		this.method_visibility = s;
	}

	public void setParameters(String[][][] s) {
		this.method_parameters = s;
	}

	public void setInvocations(String[][][] s) {
		this.method_invocations = s;
	}

}
