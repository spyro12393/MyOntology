package MyJenaProject;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

public class MyOntology {
	private OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);

	private String packageName;
	private String className;

	private OntClass myPackage;
	private OntClass myClass;
	private Individual individual_class;
	private Individual individual_package;

	private OntClass myAttribute;
	private OntClass myMethod;
	private OntClass myParameter;
	private OntClass myCallMethod;
	private OntClass myCallAttribute;
	private OntClass myAttributeRefer;
	private OntClass myMethodRefer;
	private OntClass myParameterRefer;
	private OntClass myInherit;
	private OntClass myRealize;

	private DatatypeProperty hasID;
	private DatatypeProperty hasName;
	private DatatypeProperty hasType;
	private DatatypeProperty hasIsPrimitiveType;
	private DatatypeProperty has_Visibility;
	private DatatypeProperty is_GetSet;
	private DatatypeProperty is_Invoked;
	private DatatypeProperty is_Override;
	private DatatypeProperty is_Descendant;
	private DatatypeProperty is_Used;

	private ObjectProperty hasClass;
	private ObjectProperty hasAttribute;
	private ObjectProperty hasMethod;
	private ObjectProperty hasParameter; // for method
	private ObjectProperty callingSource;
	private ObjectProperty callingMethodTarget;
	private ObjectProperty callingAttributeTarget;
	private ObjectProperty referenceTarget;
	private ObjectProperty referenceAttributeSource;
	private ObjectProperty referenceMethodSource;
	private ObjectProperty referenceParameterSource;
	private ObjectProperty generalizationSource;
	private ObjectProperty generalizationTarget;

	private DatatypeProperty hasMessage;

	String baseURI = "http://isq.im.mgt.ncu.edu.tw/OOOntology.owl#";

	// String fileName = "src/OOontology.owl";
	String fileName = "C:/Users/QZ_CHUNG/Desktop/alice/OOOntology.owl";

	public MyOntology() {
		try {
			File file = new File(fileName);
			FileReader reader = new FileReader(file);

			model.read(reader, null);

			// model.write(System.out, "RDF/XML-ABBREV");
		} catch (Exception e) {
			e.printStackTrace();
		}

		myPackage = model.getOntClass(baseURI + "Package");
		myClass = model.getOntClass(baseURI + "Class");

		myAttribute = model.getOntClass(baseURI + "Attribute");
		myMethod = model.getOntClass(baseURI + "Method");
		myParameter = model.getOntClass(baseURI + "Parameter");
		myCallMethod = model.getOntClass(baseURI + "Call_Method");
		myCallAttribute = model.getOntClass(baseURI + "Call_Attribute");
		myAttributeRefer = model.getOntClass(baseURI + "Attribute_Refer");
		myMethodRefer = model.getOntClass(baseURI + "Method_Refer");
		myParameterRefer = model.getOntClass(baseURI + "Parameter_Refer");
		myInherit = model.getOntClass(baseURI + "Inherit");
		myRealize = model.getOntClass(baseURI + "Realize");

		hasID = model.getDatatypeProperty(baseURI + "has_ID");
		hasName = model.getDatatypeProperty(baseURI + "has_Name");
		hasType = model.getDatatypeProperty(baseURI + "has_Type");
		hasIsPrimitiveType = model.getDatatypeProperty(baseURI + "has_IsPrimitiveType");
		has_Visibility = model.getDatatypeProperty(baseURI + "has_Visibility");
		is_GetSet = model.getDatatypeProperty(baseURI + "is_GetSet");
		is_Invoked = model.getDatatypeProperty(baseURI + "is_Invoked");
		is_Override = model.getDatatypeProperty(baseURI + "is_Override");
		is_Descendant = model.getDatatypeProperty(baseURI + "is_Descendant");
		is_Used = model.getDatatypeProperty(baseURI + "is_Used");

		hasIsPrimitiveType.getRange();

		hasClass = model.getObjectProperty(baseURI + "Package_Class");
		hasAttribute = model.getObjectProperty(baseURI + "Class_Attribute");
		hasMethod = model.getObjectProperty(baseURI + "Class_Method");
		hasParameter = model.getObjectProperty(baseURI + "Method_Parameter");
		callingSource = model.getObjectProperty(baseURI + "Calling_Source");
		callingMethodTarget = model.getObjectProperty(baseURI + "Calling_Method_Target");
		callingAttributeTarget = model.getObjectProperty(baseURI + "Calling_Attribute_Target");
		referenceTarget = model.getObjectProperty(baseURI + "Reference_Target");
		referenceAttributeSource = model.getObjectProperty(baseURI + "Reference_Attribute_Source");
		referenceMethodSource = model.getObjectProperty(baseURI + "Reference_Method_Source");
		referenceParameterSource = model.getObjectProperty(baseURI + "Reference_Parameter_Source");
		generalizationSource = model.getObjectProperty(baseURI + "Generalization_Source");
		generalizationTarget = model.getObjectProperty(baseURI + "Generalization_Target");

		hasMessage = model.getDatatypeProperty(baseURI + "has_Message");
	}

	public void setPackage(String packageName) {
		this.packageName = packageName;
		individual_package = myPackage.createIndividual(baseURI + packageName);
	}

	public void setClass(String className, String superClassName, String interfaceClass) {
		String[] interfaceClassName;

		this.className = className;
		individual_class = myClass.createIndividual(baseURI + packageName + "." + className);
		individual_class.addProperty(hasID, packageName + "." + className);
		individual_class.addProperty(hasName, className);
		individual_class.addProperty(has_Visibility, "public");
		if (!superClassName.equals("null")) {
			System.out.println(className + " -----super class----> " + superClassName);
			myInherit.createIndividual(baseURI + packageName + "." + className + "_inherit")
					.addProperty(generalizationSource, individual_class).addProperty(hasMessage, superClassName);
		}
		if (interfaceClass.length() > 1)
			interfaceClass = interfaceClass.substring(1, interfaceClass.length());
		if (!interfaceClass.equals("[")) {
			interfaceClassName = interfaceClass.split(", ");
			for (int i = 0; i < interfaceClassName.length; i++) {
				System.out.println(className + " -----interface class----> " + interfaceClassName[i]);
				myRealize.createIndividual(baseURI + packageName + "." + className + "_realize" + i)
						.addProperty(generalizationSource, individual_class)
						.addProperty(hasMessage, interfaceClassName[i]);
			}
		}

		individual_package.addProperty(hasClass, individual_class);
		System.out.println(className);
	}

	public void setAttribute(String attributeName, String attributeType, String attributeIsPrimitiveType,
			String attributeVisibility) {
		Individual individual;
		String[] name;
		String[] type;
		String[] isPrimitive;
		String[] visibility;
		if (attributeName.length() > 1)
			attributeName = attributeName.substring(1, attributeName.length());
		if (attributeType.length() > 1)
			attributeType = attributeType.substring(1, attributeType.length());
		if (attributeIsPrimitiveType.length() > 1)
			attributeIsPrimitiveType = attributeIsPrimitiveType.substring(1, attributeIsPrimitiveType.length());
		if (attributeVisibility.length() > 1)
			attributeVisibility = attributeVisibility.substring(1, attributeVisibility.length());
		name = attributeName.split(", ");
		type = attributeType.split(", ");
		isPrimitive = attributeIsPrimitiveType.split(", ");
		visibility = attributeVisibility.split(", ");
		// attribute_name = new String[tmp.length][tmp2.length];
		for (int j = 0; j < name.length; j++) {
			if (name[j].length() > 1) {
				individual = myAttribute.createIndividual(baseURI + packageName + "." + className + "." + name[j]);
				individual.addLiteral(hasID, packageName + "." + className + "." + name[j]);
				individual.addLiteral(hasName, name[j]);
				individual.addLiteral(hasType, type[j]);
				individual.addLiteral(hasIsPrimitiveType, Boolean.parseBoolean(isPrimitive[j]));
				individual.addLiteral(has_Visibility, visibility[j]);
				individual.addLiteral(is_GetSet, false);

				individual_class.addProperty(hasAttribute, individual);
				System.out.println(individual);
				// attribute_name[i][j] = tmp2[j];

			} else {
				// attribute_name[i][j] = "null";
				System.out.println("[MyOntology.setAttribute()] this class no attribute");
			}
			// System.out.println(attribute_name[i][j]);

		}
	}

	public void setMethod(String methodName, String methodType, String methodIsPrimitiveType, String methodVisibility,
			String parameters, String parameterType, String parameterIsPrimitiveType, String invokeMethod,
			String invokeAttribute) {
		Individual individual_method;
		Individual individual_parameter;
		Individual individual_invokeAttribute;
		Individual individual_invokeMethod;

		String[] name;
		String[] type;
		String[] isPrimitive;
		String[] visibility;
		String[] para;
		String[] paraType;
		String[] paraIsPrimitive;
		String[] invoke_m;
		String[] invoke_a;
		if (methodName.length() > 1)
			methodName = methodName.substring(1, methodName.length());
		if (methodType.length() > 1)
			methodType = methodType.substring(1, methodType.length());
		if (methodIsPrimitiveType.length() > 1)
			methodIsPrimitiveType = methodIsPrimitiveType.substring(1, methodIsPrimitiveType.length());
		if (methodVisibility.length() > 1)
			methodVisibility = methodVisibility.substring(1, methodVisibility.length());
		if (parameters.length() > 1)
			parameters = parameters.substring(1, parameters.length());
		if (parameterType.length() > 1)
			parameterType = parameterType.substring(1, parameterType.length());
		if (parameterIsPrimitiveType.length() > 1)
			parameterIsPrimitiveType = parameterIsPrimitiveType.substring(1, parameterIsPrimitiveType.length());
		if (invokeMethod.length() > 1)
			invokeMethod = invokeMethod.substring(1, invokeMethod.length());
		if (invokeAttribute.length() > 1)
			invokeAttribute = invokeAttribute.substring(1, invokeAttribute.length());
		name = methodName.split(", ");
		type = methodType.split(", ");
		isPrimitive = methodIsPrimitiveType.split(", ");
		visibility = methodVisibility.split(", ");
		para = parameters.split("], ");
		paraType = parameterType.split("], ");
		paraIsPrimitive = parameterIsPrimitiveType.split("], ");
		invoke_m = invokeMethod.split("], ");
		invoke_a = invokeAttribute.split("], ");
		// attribute_name = new String[tmp.length][tmp2.length];
		for (int j = 0; j < name.length; j++) {
			if (name[j].length() > 1) {
				individual_method = myMethod
						.createIndividual(baseURI + packageName + "." + className + "." + name[j] + "()");
				individual_method.addLiteral(hasID, packageName + "." + className + "." + name[j] + "()");
				individual_method.addLiteral(hasName, name[j]);
				if (type[j].length() > 1) {
					individual_method.addLiteral(hasType, type[j]);
					individual_method.addLiteral(hasIsPrimitiveType, Boolean.parseBoolean(isPrimitive[j]));
				}
				individual_method.addLiteral(has_Visibility, visibility[j]);
				individual_method.addLiteral(is_Invoked, false);
				individual_method.addLiteral(is_Descendant, false);
				// individual_method.addLiteral(is_Override, false);

				if (para[j].length() > 1)
					para[j] = para[j].substring(1, para[j].length());
				if (paraType[j].length() > 1)
					paraType[j] = paraType[j].substring(1, paraType[j].length());
				if (paraIsPrimitive[j].length() > 1)
					paraIsPrimitive[j] = paraIsPrimitive[j].substring(1, paraIsPrimitive[j].length());
				// System.out.println("[alice test]" + para[j]);
				String[] p = para[j].split(", ");
				String[] pt = paraType[j].split(", ");
				String[] pipt = paraIsPrimitive[j].split(", ");
				for (int i = 0; i < p.length; i++) {
					if (p[i].length() > 1) {
						individual_parameter = myParameter.createIndividual(
								baseURI + packageName + "." + className + "." + name[j] + "()" + "." + p[i]);
						individual_parameter.addLiteral(hasID,
								packageName + "." + className + "." + name[j] + "()" + "." + p[i]);
						individual_parameter.addLiteral(hasName, p[i]);
						individual_parameter.addLiteral(hasType, pt[i]);
						individual_parameter.addLiteral(hasIsPrimitiveType, Boolean.parseBoolean(pipt[i]));

						individual_method.addProperty(hasParameter, individual_parameter);
					}
				}
				if (invoke_m[j].length() > 1)
					invoke_m[j] = invoke_m[j].substring(1, invoke_m[j].length());
				String[] im = invoke_m[j].split(", ");
				for (int i = 0; i < im.length; i++) {
					if (im[i].length() > 1) {
						individual_invokeMethod = myCallMethod.createIndividual(
								baseURI + packageName + "." + className + "." + name[j] + "()" + ".callmethod_" + i);
						individual_invokeMethod.addLiteral(hasMessage, im[i]);
						individual_invokeMethod.addProperty(callingSource, individual_method);
						individual_invokeMethod.addLiteral(is_Used, false);
					}
				}
				if (invoke_a[j].length() > 1)
					invoke_a[j] = invoke_a[j].substring(1, invoke_a[j].length());
				String[] ia = invoke_a[j].split(", ");
				for (int i = 0; i < ia.length; i++) {
					if (ia[i].length() > 1) {
						individual_invokeAttribute = myCallAttribute.createIndividual(
								baseURI + packageName + "." + className + "." + name[j] + "()" + ".callattribute_" + i);
						individual_invokeAttribute.addLiteral(hasMessage, ia[i]);
						individual_invokeAttribute.addProperty(callingSource, individual_method);
						individual_invokeAttribute.addLiteral(is_Used, false);
					}
				}

				individual_class.addProperty(hasMethod, individual_method);

				// System.out.println(individual_method);
				// attribute_name[i][j] = tmp2[j];

			} else {
				// attribute_name[i][j] = "null";
				System.out.println("[MyOntology.setMethod()] this class no method");
			}
			// System.out.println(attribute_name[i][j]);
		}
		if (model.getIndividual(baseURI + packageName + "." + className + "." + className + "()") == null) {
			individual_method = myMethod
					.createIndividual(baseURI + packageName + "." + className + "." + className + "()");
			individual_method.addLiteral(hasID, packageName + "." + className + "." + className + "()");
			individual_method.addLiteral(hasName, className);
			individual_method.addLiteral(has_Visibility, "public");
			individual_method.addLiteral(is_Invoked, false);
			individual_method.addLiteral(is_Descendant, false);
			// individual_method.addLiteral(is_Override, false);
			individual_class.addProperty(hasMethod, individual_method);
		}
	}

	public void setReference() {
		Individual individual;

		System.out.println("--------------setReference()---------------");
		String queryString = "PREFIX oo: <http://isq.im.mgt.ncu.edu.tw/OOOntology.owl#>"
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + "SELECT * {"
				+ "?source_class a oo:Class; oo:Class_Attribute ?attribute."
				+ "?attribute oo:has_IsPrimitiveType false; oo:has_Type ?class_id."
				+ "?target_class a oo:Class; oo:has_ID ?class_id." + "}" + " ORDER BY ?attribute ";
		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.create(query, model);
		System.out.println("--------------Attribute Reference---------------");
		try {
			ResultSet results = qexec.execSelect();
			int i = 0;
			while (results.hasNext()) {
				QuerySolution soln = results.nextSolution();
				Resource attribute = (Resource) soln.get("attribute");
				Resource target_class = (Resource) soln.get("target_class");
				individual = myAttributeRefer.createIndividual(baseURI + "attributerefer_" + i);
				individual.setPropertyValue(referenceAttributeSource, model.getIndividual(attribute.toString()));
				individual.setPropertyValue(referenceTarget, model.getIndividual(target_class.toString()));
				System.out.println(attribute + "\t" + target_class);
				i++;
			}
		} finally {
			qexec.close();
		}

		System.out.println("--------------Method Reference---------------");
		queryString = "PREFIX oo: <http://isq.im.mgt.ncu.edu.tw/OOOntology.owl#>"
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + "SELECT * {"
				+ "?source_class a oo:Class; oo:Class_Method ?method."
				+ "?method oo:has_IsPrimitiveType false; oo:has_Type ?class_id."
				+ "?target_class a oo:Class; oo:has_ID ?class_id." + "}" + " ORDER BY ?method ";
		query = QueryFactory.create(queryString);
		qexec = QueryExecutionFactory.create(query, model);

		try {
			ResultSet results = qexec.execSelect();
			int i = 0;
			while (results.hasNext()) {
				QuerySolution soln = results.nextSolution();
				Resource method = (Resource) soln.get("method");
				Resource target_class = (Resource) soln.get("target_class");
				individual = myMethodRefer.createIndividual(baseURI + "methodrefer_" + i);
				individual.setPropertyValue(referenceMethodSource, model.getIndividual(method.toString()));
				individual.setPropertyValue(referenceTarget, model.getIndividual(target_class.toString()));
				System.out.println(method + "\t" + target_class);
				i++;
			}
		} finally {
			qexec.close();
		}

		System.out.println("--------------Parameter Reference---------------");
		queryString = "PREFIX oo: <http://isq.im.mgt.ncu.edu.tw/OOOntology.owl#>"
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + "SELECT * {"
				+ "?method a oo:Method; oo:Method_Parameter ?parameter."
				+ "?parameter oo:has_IsPrimitiveType false; oo:has_Type ?class_id."
				+ "?target_class a oo:Class; oo:has_ID ?class_id." + "}" + " ORDER BY ?parameter ";
		query = QueryFactory.create(queryString);
		qexec = QueryExecutionFactory.create(query, model);

		try {
			ResultSet results = qexec.execSelect();
			int i = 0;
			while (results.hasNext()) {
				QuerySolution soln = results.nextSolution();
				Resource parameter = (Resource) soln.get("parameter");
				Resource target_class = (Resource) soln.get("target_class");
				individual = myParameterRefer.createIndividual(baseURI + "parameterrefer_" + i);
				individual.setPropertyValue(referenceParameterSource, model.getIndividual(parameter.toString()));
				individual.setPropertyValue(referenceTarget, model.getIndividual(target_class.toString()));
				System.out.println(parameter + "\t" + target_class);
				i++;
			}
		} finally {
			qexec.close();
		}
	}

	public void setCallMethod() {
		ArrayList<RDFNode> calling = new ArrayList<RDFNode>();
		ArrayList<RDFNode> method = new ArrayList<RDFNode>();

		String queryString = "PREFIX oo: <http://isq.im.mgt.ncu.edu.tw/OOOntology.owl#>"
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + "SELECT * {"
				+ "?calling a oo:Call_Method ; oo:has_Message ?message" + "."
				+ "?method a oo:Method; oo:has_ID ?message" + "}" + " ORDER BY ?calling ";
		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.create(query, model);
		// System.out.println("--------------setCallMethod()---------------");
		try {
			ResultSet results = qexec.execSelect();
			while (results.hasNext()) {
				QuerySolution soln = results.nextSolution();
				calling.add(soln.get("calling"));
				method.add(soln.get("method"));
				// RDFNode x = soln.get("class");
				// Literal mess = soln.getLiteral("message");
				// System.out.println(soln.get("invocation") + "\t"
				// +soln.get("method") );
			}
		} finally {
			qexec.close();
		}

		for (int j = 0; j < calling.size(); j++) {
			model.getIndividual(calling.get(j).toString()).addProperty(callingMethodTarget, method.get(j));
		}
	}

	public void setCallAttribute() {
		ArrayList<RDFNode> calling = new ArrayList<RDFNode>();
		ArrayList<RDFNode> attribute = new ArrayList<RDFNode>();

		String queryString = "PREFIX oo: <http://isq.im.mgt.ncu.edu.tw/OOOntology.owl#>"
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + "SELECT * {"
				+ "?calling a oo:Call_Attribute ; oo:has_Message ?message" + "."
				+ "?attribute a oo:Attribute; oo:has_ID ?message" + "}";
		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.create(query, model);
		try {
			ResultSet results = qexec.execSelect();
			while (results.hasNext()) {
				QuerySolution soln = results.nextSolution();
				calling.add(soln.get("calling"));
				attribute.add(soln.get("attribute"));
				// RDFNode x = soln.get("class");
				// Literal mess = soln.getLiteral("message");
				// System.out.println(soln.get("invocation") + "\t" +
				// soln.get("attribute"));
			}
		} finally {
			qexec.close();
		}

		for (int j = 0; j < calling.size(); j++) {
			model.getIndividual(calling.get(j).toString()).addProperty(callingAttributeTarget, attribute.get(j));
		}
	}

	public void setGeneralization() {
		ArrayList<RDFNode> inherit = new ArrayList<RDFNode>();
		ArrayList<RDFNode> realize = new ArrayList<RDFNode>();
		ArrayList<RDFNode> theClass = new ArrayList<RDFNode>();

		// -----------------Inherit-----------------
		String queryString = "PREFIX oo: <http://isq.im.mgt.ncu.edu.tw/OOOntology.owl#>"
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + "SELECT * {"
				+ "?inherit a oo:Inherit ; oo:has_Message ?message" + "." + "?class a oo:Class; oo:has_ID ?message"
				+ "}";
		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.create(query, model);
		try {
			ResultSet results = qexec.execSelect();
			System.out.println("--------------query--------------");
			while (results.hasNext()) {
				QuerySolution soln = results.nextSolution();
				inherit.add(soln.get("inherit"));
				theClass.add(soln.get("class"));
				// RDFNode x = soln.get("class");
				// Literal mess = soln.getLiteral("message");
				System.out.println(soln.get("inherit") + "\t" + soln.get("class"));
			}
		} finally {
			qexec.close();
		}

		for (int j = 0; j < inherit.size(); j++) {
			model.getIndividual(inherit.get(j).toString()).addProperty(generalizationTarget, theClass.get(j));
		}

		// -----------------Realize-----------------
		theClass.clear();
		queryString = "PREFIX oo: <http://isq.im.mgt.ncu.edu.tw/OOOntology.owl#>"
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + "SELECT * {"
				+ "?realize a oo:Realize ; oo:has_Message ?message" + "." + "?class a oo:Class; oo:has_ID ?message"
				+ "}";
		query = QueryFactory.create(queryString);
		qexec = QueryExecutionFactory.create(query, model);
		try {
			ResultSet results = qexec.execSelect();
			System.out.println("--------------query--------------");
			while (results.hasNext()) {
				QuerySolution soln = results.nextSolution();
				realize.add(soln.get("realize"));
				theClass.add(soln.get("class"));
				// RDFNode x = soln.get("class");
				// Literal mess = soln.getLiteral("message");
				System.out.println(soln.get("realize") + "\t" + soln.get("class"));
			}
		} finally {
			qexec.close();
		}

		for (int j = 0; j < realize.size(); j++) {
			model.getIndividual(realize.get(j).toString()).addProperty(generalizationTarget, theClass.get(j));
		}
	}

	public void setDescentMember() {
		ArrayList<Individual> mc = new ArrayList<Individual>();
		ArrayList<Individual> m = new ArrayList<Individual>();

		ArrayList<Individual> ac = new ArrayList<Individual>();
		ArrayList<Individual> a = new ArrayList<Individual>();
		String queryString = "PREFIX oo: <http://isq.im.mgt.ncu.edu.tw/OOOntology.owl#>"
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + "SELECT * {"
				+ "?generalization oo:Generalization_Source ?source; oo:Generalization_Target ?target."
				+ "?source oo:has_ID ?source_id." + "?target oo:has_ID ?target_id" + "}";
		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.create(query, model);
		try {
			ResultSet results = qexec.execSelect();
			System.out.println("--------------Generalization Relationship--------------");
			while (results.hasNext()) {
				QuerySolution soln = results.nextSolution();
				Resource source = (Resource) soln.get("source");
				Individual source_class = model.getIndividual(source.toString());
				Literal source_id = soln.getLiteral("source_id");
				Literal target_id = soln.getLiteral("target_id");
				System.out.println(source_id + "\t" + target_id);
				// ---------------------------------------------------------------
				Map<String, Individual> sm = new HashMap<>();
				Map<String, Individual> sa = new HashMap<>();
				String queryString2 = "PREFIX oo: <http://isq.im.mgt.ncu.edu.tw/OOOntology.owl#>"
						+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + "SELECT * {" + "?source oo:has_ID \""
						+ source_id + "\"."
						+ "{?source oo:Class_Method ?source_method. ?source_method oo:has_Name ?member_name.} UNION "
						+ "{?source oo:Class_Attribute ?source_attribute. ?source_attribute oo:has_Name ?member_name.}"
						+ "} ORDER BY ?member_name";
				Query query2 = QueryFactory.create(queryString2);
				QueryExecution qexec2 = QueryExecutionFactory.create(query2, model);
				try {
					ResultSet results2 = qexec2.execSelect();
					System.out.println("--------------Source member--------------");
					while (results2.hasNext()) {
						QuerySolution soln2 = results2.nextSolution();
						Literal member_name = soln2.getLiteral("member_name");
						if (soln2.get("source_method") == null) {
							Resource source_member = (Resource) soln2.get("source_attribute");
							Individual member = model.getIndividual(source_member.toString());
							sa.put(member_name.toString(), member);
							System.out.println("[Attribute]" + member_name);
						} else {
							Resource source_member = (Resource) soln2.get("source_method");
							Individual member = model.getIndividual(source_member.toString());
							sm.put(member_name.toString(), member);
							System.out.println("[Method]" + member_name);
						}
					}
				} finally {
					qexec2.close();
				}

				queryString2 = "PREFIX oo: <http://isq.im.mgt.ncu.edu.tw/OOOntology.owl#>"
						+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + "SELECT * {" + "?target oo:has_ID \""
						+ target_id + "\"."
						+ "{?target oo:Class_Method ?target_method. ?target_method oo:has_Name ?member_name; oo:has_Type ?type; oo:has_Visibility ?visibility;oo:has_IsPrimitiveType ?ipt.} UNION "
						+ "{?target oo:Class_Attribute ?target_attribute. ?target_attribute oo:has_Name ?member_name; oo:has_Type ?type; oo:has_Visibility ?visibility;oo:has_IsPrimitiveType ?ipt.}"
						+ "}";
				query2 = QueryFactory.create(queryString2);
				qexec2 = QueryExecutionFactory.create(query2, model);
				try {
					ResultSet results2 = qexec2.execSelect();
					System.out.println("--------------Target member--------------");
					while (results2.hasNext()) {
						QuerySolution soln2 = results2.nextSolution();
						Literal member_name = soln2.getLiteral("member_name");
						Literal member_type = soln2.getLiteral("type");
						Literal member_visibility = soln2.getLiteral("visibility");
						// Literal member_ipt = soln2.getLiteral("ipt");
						if (!member_visibility.toString().equals("private")) {
							if (soln2.get("target_method") == null) {
								if (!sa.containsKey(member_name.toString())) {
									Individual attribute = myAttribute.createIndividual(
											baseURI + source_class.getPropertyValue(hasID) + "." + member_name);
									attribute.addLiteral(hasID,
											"" + source_class.getPropertyValue(hasID) + "." + member_name);
									attribute.addLiteral(hasName, member_name);
									attribute.addLiteral(hasType, member_type);
									attribute.addLiteral(has_Visibility, member_visibility);
									attribute.addLiteral(is_Invoked, false);
									attribute.addLiteral(is_Descendant, true);
									ac.add(source_class);
									a.add(attribute);
								}
								System.out.println("[Attribute]" + member_name);
							} else {
								if (sm.containsKey(member_name.toString())) {
									sm.get(member_name.toString()).setPropertyValue(is_Override,
											ResourceFactory.createTypedLiteral(true));
								} else {
									Individual method = myMethod.createIndividual(
											baseURI + source_class.getPropertyValue(hasID) + "." + member_name + "()");
									method.addLiteral(hasID,
											source_class.getPropertyValue(hasID) + "." + member_name + "()");
									method.addLiteral(hasName, member_name);
									method.addLiteral(hasType, member_type);
									method.addLiteral(has_Visibility, member_visibility);
									method.addLiteral(is_Invoked, false);
									method.addLiteral(is_Descendant, true);
									mc.add(source_class);
									m.add(method);
								}
								System.out.println("[Method]" + member_name);
							}
						}
					}
				} finally {
					qexec2.close();
				}
			}
		} finally {
			qexec.close();
		}
		System.out.println("--------------Descendant member--------------");
		for (int i = 0; i < ac.size(); i++) {
			System.out.println(a.get(i).getPropertyValue(hasID));
			ac.get(i).addProperty(hasAttribute, a.get(i));
		}
		for (int i = 0; i < mc.size(); i++) {
			System.out.println(m.get(i).getPropertyValue(hasID));
			mc.get(i).addProperty(hasMethod, m.get(i));
		}

	}

	public void setClassCoupling() {
		Map<String, Integer> map = new HashMap<>();
		OntClass myCallRef = model.getOntClass(baseURI + "CallingReference");
		Individual individual;
		ObjectProperty CallRefSource = model.getObjectProperty(baseURI + "CallingReference_Source");
		ObjectProperty CallRefTarget = model.getObjectProperty(baseURI + "CallingReference_Target");
		DatatypeProperty hasNumber = model.getDatatypeProperty(baseURI + "has_Number");
		DatatypeProperty hasContent = model.getDatatypeProperty(baseURI + "has_Content");

		// -----------------Calling-----------------
		String queryString = "PREFIX oo: <http://isq.im.mgt.ncu.edu.tw/OOOntology.owl#>"
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + "SELECT * {"
				+ "{?calling a oo:Call_Attribute} UNION {?calling a oo:Call_Method}."
				+ "?calling oo:Calling_Source ?source."
				+ "{?calling oo:Calling_Attribute_Target ?target} UNION {?calling oo:Calling_Method_Target ?target}."
				+ "?source_class oo:Class_Method ?source."
				+ "{?target_class oo:Class_Method ?target} UNION {?target_class oo:Class_Attribute ?target}." + "}"
				+ " ORDER BY ?calling ";
		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.create(query, model);
		try {
			ResultSet results = qexec.execSelect();
			System.out.println("--------------query--------------");
			while (results.hasNext()) {
				QuerySolution soln = results.nextSolution();
				Resource source_class = (Resource) soln.get("source_class");
				Resource target_class = (Resource) soln.get("target_class");

				if (source_class != target_class) {
					Individual individual_source_class = model.getIndividual(source_class.toString());
					Individual individual_target_class = model.getIndividual(target_class.toString());
					String content = individual_source_class.getPropertyValue(hasID).toString() + "->"
							+ individual_target_class.getPropertyValue(hasID);
					if (model.getIndividual(baseURI + content) == null) {
						individual = myCallRef.createIndividual(baseURI + content);
						individual.addProperty(CallRefSource, individual_source_class);
						individual.addProperty(CallRefTarget, individual_target_class);
						individual.addProperty(hasContent, content);
						map.put(content, 1);

					} else {
						individual = model.getIndividual(baseURI + content);
						map.replace(content, map.get(content) + 1);

					}
					individual.setPropertyValue(hasNumber, ResourceFactory.createTypedLiteral(map.get(content)));
				}

			}
		} finally {
			qexec.close();
		}

		// -----------------Reference-----------------
		queryString = "PREFIX oo: <http://isq.im.mgt.ncu.edu.tw/OOOntology.owl#>"
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + "SELECT * {"
				+ "{?reference a oo:Attribute_Refer; oo:Reference_Target ?target_class ; oo:Reference_Attribute_Source ?source. ?source_class oo:Class_Attribute ?source} UNION"
				+ "{?reference a oo:Method_Refer; oo:Reference_Target ?target_class ; oo:Reference_Method_Source ?source. ?source_class oo:Class_Method ?source} UNION"
				+ "{?reference a oo:Parameter_Refer; oo:Reference_Target ?target_class; oo:Reference_Parameter_Source ?source. ?source_method a oo:Method; oo:Method_Parameter ?source. ?source_class oo:Class_Method ?source_method}."
				+ "}" + " ORDER BY ?source ";
		query = QueryFactory.create(queryString);
		qexec = QueryExecutionFactory.create(query, model);
		try {
			ResultSet results = qexec.execSelect();
			System.out.println("--------------Reference--------------");
			while (results.hasNext()) {
				QuerySolution soln = results.nextSolution();
				Resource source_class = (Resource) soln.get("source_class");
				Resource target_class = (Resource) soln.get("target_class");
				if (source_class != target_class) {
					Individual individual_source_class = model.getIndividual(source_class.toString());
					Individual individual_target_class = model.getIndividual(target_class.toString());
					String content = individual_source_class.getPropertyValue(hasID).toString() + "->"
							+ individual_target_class.getPropertyValue(hasID);
					if (model.getIndividual(baseURI + content) == null) {
						individual = myCallRef.createIndividual(baseURI + content);
						individual.addProperty(CallRefSource, individual_source_class);
						individual.addProperty(CallRefTarget, individual_target_class);
						individual.addProperty(hasContent, content);
						map.put(content, 1);

					} else {
						individual = model.getIndividual(baseURI + content);
						map.replace(content, map.get(content) + 1);

					}
					individual.setPropertyValue(hasNumber, ResourceFactory.createTypedLiteral(map.get(content)));
				}
			}
		} finally {
			qexec.close();
		}
		// -----------------Generalization-----------------
		queryString = "PREFIX oo: <http://isq.im.mgt.ncu.edu.tw/OOOntology.owl#>"
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + "SELECT * {"
				+ "?generalization oo:Generalization_Source ?source; oo:Generalization_Target ?target."
				+ "}";
		query = QueryFactory.create(queryString);
		qexec = QueryExecutionFactory.create(query, model);
		try {
			ResultSet results = qexec.execSelect();
			System.out.println("--------------Generalization--------------");
			while (results.hasNext()) {
				QuerySolution soln = results.nextSolution();
				Resource source_class = (Resource) soln.get("source");
				Resource target_class = (Resource) soln.get("target");
				Individual individual_source_class = model.getIndividual(source_class.toString());
				Individual individual_target_class = model.getIndividual(target_class.toString());
				String content = individual_source_class.getPropertyValue(hasID).toString() + "->"
						+ individual_target_class.getPropertyValue(hasID);
				if (model.getIndividual(baseURI + content) == null) {
					individual = myCallRef.createIndividual(baseURI + content);
					individual.addProperty(CallRefSource, individual_source_class);
					individual.addProperty(CallRefTarget, individual_target_class);
					individual.addProperty(hasContent, content);
					map.put(content, 1);

				} else {
					individual = model.getIndividual(baseURI + content);
					map.replace(content, map.get(content) + 1);

				}
				individual.setPropertyValue(hasNumber, ResourceFactory.createTypedLiteral(map.get(content)));
				
			}
		}finally {
			qexec.close();
		}
		

	}

	public void WriteModel() {
		model.setNsPrefix("OOOntology", baseURI);
		// model.write(System.out, "RDF/XML-ABBREV");
		try {
			File file = new File("C:/Users/QZ_CHUNG/Desktop/alice/OOOntology2.owl");
			// File file = new File("src/ATM_Ontology.owl");
			FileWriter writer = new FileWriter(file);
			model.write(writer, "RDF/XML-ABBREV");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
