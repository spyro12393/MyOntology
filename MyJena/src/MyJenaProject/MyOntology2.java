package MyJenaProject;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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

public class MyOntology2 {
	private OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);

	private OntClass myMessage; // run time
	// private OntClass myRunTimeInher; // run time
	// private OntClass myRunTimeDB; // run time

	private DatatypeProperty is_Invoked; // method
	private DatatypeProperty is_Used; // refer call
	private DatatypeProperty is_GetSet; // attribute

	private ObjectProperty callingSource;
	private ObjectProperty callingMethodTarget;

	private List RunTimeRelationship = new ArrayList();
	private int rtr = 0; // number of RunTimeRelationship
	// String fileName = "src/OOontology.owl";
	private String fileName = "C:/Users/QZ_CHUNG/Desktop/alice/OOOntology2.owl";
	private String baseURI = "http://isq.im.mgt.ncu.edu.tw/OOOntology.owl#";

	public MyOntology2() {
		try {
			File file = new File(fileName);
			FileReader reader = new FileReader(file);

			model.read(reader, null);

			// model.write(System.out, "RDF/XML-ABBREV");
		} catch (Exception e) {
			e.printStackTrace();
		}

		myMessage = model.getOntClass(baseURI + "Message");
		// myRunTimeInher = model.getOntClass(baseURI + "Inheritance");
		// myRunTimeDB = model.getOntClass(baseURI + "DynamicBinding");

		is_GetSet = model.getDatatypeProperty(baseURI + "is_GetSet");
		is_Invoked = model.getDatatypeProperty(baseURI + "is_Invoked");
		is_Used = model.getDatatypeProperty(baseURI + "is_Used");

		callingSource = model.getObjectProperty(baseURI + "Calling_Source");
		callingMethodTarget = model.getObjectProperty(baseURI + "Calling_Method_Target");
	}

	public void setMessage(int step) {
		Individual individual = myMessage.createIndividual(baseURI + "step_" + step);

	}

	public void setRunTimeMethodCall(String source, String target) {
		List call_is_Used = new ArrayList();
		String queryString = "PREFIX oo: <http://isq.im.mgt.ncu.edu.tw/OOOntology.owl#>"
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + "SELECT * {"
				+ "?calling oo:Calling_Source ?source; oo:Calling_Method_Target ?target." + "?target oo:has_ID \""
				+ target + "\"." + "?source oo:has_ID \"" + source + "\"." + "}";
		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.create(query, model);
		try {
			ResultSet results = qexec.execSelect();

			while (results.hasNext()) {
				QuerySolution soln = results.nextSolution();
				Resource runTimeCalling = (Resource) soln.get("calling");
				if (!call_is_Used.contains(model.getIndividual(runTimeCalling.toString()))) {
					call_is_Used.add(model.getIndividual(runTimeCalling.toString()));
					// System.out.println(runTimeCalling);
				}
				// Resource target = (Resource) soln.get("target_id");

			}
		} finally {
			qexec.close();
		}
		for (int i = 0; i < call_is_Used.size(); i++) {
			Individual calling = (Individual) call_is_Used.get(i);
			calling.setPropertyValue(is_Used, ResourceFactory.createTypedLiteral(true));
		}

	}

	public void setRunTimeAttributeCall(String object, String source, String target) {
		System.out.println("---------------------setRunTimeAttributeCall-----------------");
		System.out.println("[step]" + object + "\t" + source + "\t" + target);
		String new_target = "";
		List call_is_Used = new ArrayList();
		boolean is_inher = false;
		Resource source_attribute = model.createResource();
		Resource target_attribute = model.createResource();
		Literal source_class_id = null;
		Literal target_class_id = null;
		Literal target_name = null;
		String queryString = "PREFIX oo: <http://isq.im.mgt.ncu.edu.tw/OOOntology.owl#>"
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + "SELECT * {" + "?method oo:has_ID \""
				+ source + "\". " + "?source_class oo:Class_Method ?method; oo:has_ID ?source_class_id." + "}";
		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.create(query, model);
		try {
			ResultSet results = qexec.execSelect();
			while (results.hasNext()) {
				QuerySolution soln = results.nextSolution();
				source_class_id = soln.getLiteral("source_class_id");
			}
		} finally {
			qexec.close();
		}
		queryString = "PREFIX oo: <http://isq.im.mgt.ncu.edu.tw/OOOntology.owl#>"
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + "SELECT * {" + "?attribute oo:has_ID \""
				+ target + "\"; oo:has_Name ?target_name. "
				+ "?target_class oo:Class_Attribute ?attribute; oo:has_ID ?target_class_id." + "}";
		query = QueryFactory.create(queryString);
		qexec = QueryExecutionFactory.create(query, model);
		try {
			ResultSet results = qexec.execSelect();
			while (results.hasNext()) {
				QuerySolution soln = results.nextSolution();
				target_class_id = soln.getLiteral("target_class_id");
				target_name = soln.getLiteral("target_name");
				target_attribute = (Resource) soln.get("attribute");
			}
		} finally {
			qexec.close();
		}
		if (target_name != null)
			new_target = object + "." + target_name.toString();

		if (target_name != null && source_class_id != null && target_class_id != null) {
			if (object != "" && object.equals(target_class_id.toString())) {
				queryString = "PREFIX oo: <http://isq.im.mgt.ncu.edu.tw/OOOntology.owl#>"
						+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + "SELECT * {"
						+ "?calling oo:Calling_Source ?source; oo:Calling_Attribute_Target ?target."
						+ "?source oo:has_ID \"" + source + "\"." + "?target oo:has_ID \"" + target + "\"." + "}";
				query = QueryFactory.create(queryString);
				qexec = QueryExecutionFactory.create(query, model);
				try {
					ResultSet results = qexec.execSelect();
					while (results.hasNext()) {
						QuerySolution soln = results.nextSolution();
						Resource runTimeCalling = (Resource) soln.get("calling");
						// System.out.println("[1] " + source + " -> " +
						// target);
						if (!call_is_Used.contains(model.getIndividual(runTimeCalling.toString()))) {
							call_is_Used.add(model.getIndividual(runTimeCalling.toString()));
							// System.out.println(runTimeCalling);
						}
					}
				} finally {
					qexec.close();
				}

			} else if (source_class_id.toString().equals(target_class_id.toString())) {
				queryString = "PREFIX oo: <http://isq.im.mgt.ncu.edu.tw/OOOntology.owl#>"
						+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + "SELECT * {"
						+ "?calling oo:Calling_Source ?source; oo:Calling_Attribute_Target ?target."
						+ "?source oo:has_ID \"" + source + "\"." + "?target oo:has_ID \"" + target + "\"." + "}";
				query = QueryFactory.create(queryString);
				qexec = QueryExecutionFactory.create(query, model);
				try {
					ResultSet results = qexec.execSelect();
					while (results.hasNext()) {
						QuerySolution soln = results.nextSolution();
						Resource runTimeCalling = (Resource) soln.get("calling");
						// System.out.println("[2] " + source + " -> " +
						// target);
						if (!call_is_Used.contains(model.getIndividual(runTimeCalling.toString()))) {
							call_is_Used.add(model.getIndividual(runTimeCalling.toString()));
							// System.out.println(runTimeCalling);
						}
					}
				} finally {
					qexec.close();
				}
			} else if (new_target != "") {
				queryString = "PREFIX oo: <http://isq.im.mgt.ncu.edu.tw/OOOntology.owl#>"
						+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + "SELECT * {"
						+ "?calling oo:Calling_Source ?source; oo:Calling_Attribute_Target ?target."
						+ "?source oo:has_ID \"" + source + "\"." + "?target oo:has_ID \"" + new_target + "\"." + "}";
				query = QueryFactory.create(queryString);
				qexec = QueryExecutionFactory.create(query, model);
				try {
					ResultSet results = qexec.execSelect();
					while (results.hasNext()) {
						QuerySolution soln = results.nextSolution();
						Resource runTimeCalling = (Resource) soln.get("calling");
						// System.out.println("[3-1] " + source + " -> " +
						// target);
						// System.out.println("[3-2] " + source + " -> " +
						// new_target);
						is_inher = true;
						source_attribute = (Resource) soln.get("target"); // new_target
						if (!call_is_Used.contains(model.getIndividual(runTimeCalling.toString()))) {
							call_is_Used.add(model.getIndividual(runTimeCalling.toString()));
							// System.out.println(runTimeCalling);
						}
					}
				} finally {
					qexec.close();
				}
			}

		}
		if (is_inher) {
			if (!RunTimeRelationship.contains(new_target + " -> " + target)) {
				System.out.println(new_target + " -> " + target);
				RunTimeRelationship.add(new_target + " -> " + target);
				OntClass myRunTime = model.getOntClass(baseURI + "Inheritance");
				Individual i = myRunTime.createIndividual(baseURI + "RunTimeRelationship_" + "Inheritance_" + rtr);
				i.setPropertyValue(callingSource, model.getIndividual(source_attribute.toString()));
				i.setPropertyValue(callingMethodTarget, model.getIndividual(target_attribute.toString()));
				setRunTimeGetSet(new_target);
				rtr++;
			}
		}
		for (int i = 0; i < call_is_Used.size(); i++) {
			Individual calling = (Individual) call_is_Used.get(i);
			calling.setPropertyValue(is_Used, ResourceFactory.createTypedLiteral(true));
		}

		// for (int i = 0; i < call_is_Used.size(); i++) {
		// Individual calling = (Individual) call_is_Used.get(i);
		// calling.setPropertyValue(is_Used,
		// ResourceFactory.createTypedLiteral(true));
		// }

	}

	public void getCallingCoupling() {
		int staticNumber = 0;
		int dynamicNumber = 0;

		String queryString = "PREFIX oo: <http://isq.im.mgt.ncu.edu.tw/OOOntology.owl#>"
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + "SELECT * {"
				+ "?calling oo:Calling_Source ?source; oo:is_Used ?used."
				+ "{?calling oo:Calling_Attribute_Target ?target} UNION {?calling oo:Calling_Method_Target ?target}."
				+ "?target oo:has_ID ?target_id." + "?source oo:has_ID ?source_id." + "}";
		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.create(query, model);
		try {
			ResultSet results = qexec.execSelect();

			while (results.hasNext()) {
				QuerySolution soln = results.nextSolution();
				Literal source_id = soln.getLiteral("source_id");
				Literal target_id = soln.getLiteral("target_id");
				Literal used = soln.getLiteral("used");
				staticNumber++;
				if ((boolean) used.getValue()) {
					dynamicNumber++;
				} else {
					System.out.println(source_id + "\t" + target_id);
				}

				// Resource target = (Resource) soln.get("target_id");

			}
		} finally {
			qexec.close();
		}
		System.out.println("\n\n\nStatic Calling: " + staticNumber + "\t" + "Dynamic Callying: " + dynamicNumber + "\n"
				+ "Unused Coupling: " + (double) (staticNumber - dynamicNumber) / staticNumber);

	}

	public void getReferenceCoupling() {
		int staticNumber = 0;
		int dynamicNumber = 0;
		List refer_is_Used = new ArrayList();
		String queryString = "PREFIX oo: <http://isq.im.mgt.ncu.edu.tw/OOOntology.owl#>"
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + "SELECT * {"
				+ "{?reference oo:Reference_Attribute_Source ?source}UNION"
				+ "{?reference oo:Reference_Method_Source ?source}UNION"
				+ "{?reference oo:Reference_Parameter_Source ?parameter. ?source oo:Method_Parameter ?parameter.}"
				+ "?source oo:has_ID ?source_id." + "{?source oo:is_Invoked ?used}UNION{?source oo:is_GetSet ?used}."
				+ "}";
		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.create(query, model);
		try {
			ResultSet results = qexec.execSelect();

			while (results.hasNext()) {
				QuerySolution soln = results.nextSolution();
				Resource reference = (Resource) soln.get("reference");
				Literal source_id = soln.getLiteral("source_id");
				Literal used = soln.getLiteral("used");
				staticNumber++;
				if ((boolean) used.getValue()) {
					refer_is_Used.add(model.getIndividual(reference.toString()));
					dynamicNumber++;
				} else {
					System.out.println(source_id);
				}
			}
		} finally {
			qexec.close();
		}
		System.out.println("\n\n\nStatic Calling: " + staticNumber + "\t" + "Dynamic Callying: " + dynamicNumber + "\n"
				+ "Unused Coupling: " + (double) (staticNumber - dynamicNumber) / staticNumber);
		for (int i = 0; i < refer_is_Used.size(); i++) {
			Individual refer = (Individual) refer_is_Used.get(i);
			refer.setPropertyValue(is_Used, ResourceFactory.createTypedLiteral(true));
		}

	}

	public void getInheritCoupling() {
		int staticNumber = 0;
		int dynamicNumber = 0;
		System.out.println("method\tdescent\tinvoked");
		String queryString = "PREFIX oo: <http://isq.im.mgt.ncu.edu.tw/OOOntology.owl#>"
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
				+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>" + "SELECT * {"
				+ "?member oo:has_ID ?member_id; oo:is_Descendant \"true\"^^xsd:boolean."
				+ "{?member oo:is_Invoked ?invoked} UNION {?member oo:is_GetSet ?getset}" + "}";
		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.create(query, model);
		try {
			ResultSet results = qexec.execSelect();
			while (results.hasNext()) {
				QuerySolution soln = results.nextSolution();
				// Resource method = (Resource) soln.get("method");
				Literal member_id = soln.getLiteral("member_id");
				Literal invoked = soln.getLiteral("invoked");
				Literal getset = soln.getLiteral("getset");
				staticNumber++;
				if (invoked != null) {
					if ((boolean) invoked.getValue()) {
						dynamicNumber++;
					} else {
						System.out.println(member_id);
					}
				} else {
					if ((boolean) getset.getValue()) {
						dynamicNumber++;
					} else {
						System.out.println(member_id);
					}
				}
			}
		} finally {
			qexec.close();
		}
		System.out.println("\n\n\nStatic Descendant Used: " + staticNumber + "\t" + "Dynamic Descendant Used: "
				+ dynamicNumber + "\n" + "Unused Coupling: " + (double) (staticNumber - dynamicNumber) / staticNumber);

	}

	public void getUnusedMember() { // 自己看而以
		int staticNumber = 0;
		int dynamicNumber = 0;

		String queryString = "PREFIX oo: <http://isq.im.mgt.ncu.edu.tw/OOOntology.owl#>"
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + "SELECT * {"
				+ "{?member oo:is_Invoked ?used}UNION{?member oo:is_GetSet ?used}" + "?member oo:has_ID ?member_id"
				+ "}";
		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.create(query, model);
		try {
			ResultSet results = qexec.execSelect();

			while (results.hasNext()) {
				QuerySolution soln = results.nextSolution();
				Literal member_id = soln.getLiteral("member_id");
				Literal used = soln.getLiteral("used");
				staticNumber++;
				if ((boolean) used.getValue()) {
					dynamicNumber++;
				} else {
					System.out.println(member_id);
				}
			}
		} finally {
			qexec.close();
		}
		System.out.println("\n\n\nStatic Member: " + staticNumber + "\t" + "||be Used in Run Time Member: "
				+ dynamicNumber + "\n" + "Unused Coupling: " + (double) (staticNumber - dynamicNumber) / staticNumber);

	}

	public void setRunTimeExec(String target) {
		List method_exec = new ArrayList();
		String queryString = "PREFIX oo: <http://isq.im.mgt.ncu.edu.tw/OOOntology.owl#>"
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + "SELECT * {"
				+ "?member a oo:Method; oo:has_ID \"" + target + "\"." + "}";
		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.create(query, model);
		try {
			ResultSet results = qexec.execSelect();

			while (results.hasNext()) {
				QuerySolution soln = results.nextSolution();
				Resource member = (Resource) soln.get("member");
				// Literal target_id = soln.getLiteral("target_id");
				if (!method_exec.contains(model.getIndividual(member.toString()))) {
					method_exec.add(model.getIndividual(member.toString()));
				}
			}
		} finally {
			qexec.close();
		}

		for (int i = 0; i < method_exec.size(); i++) {
			Individual method = (Individual) method_exec.get(i);
			method.setPropertyValue(is_Invoked, ResourceFactory.createTypedLiteral(true));
		}

	}

	public void setRunTimeGetSet(String target) {
		List attribute_setget = new ArrayList();
		String queryString = "PREFIX oo: <http://isq.im.mgt.ncu.edu.tw/OOOntology.owl#>"
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + "SELECT * {"
				+ "?member a oo:Attribute; oo:has_ID \"" + target + "\"." + "}";
		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.create(query, model);
		try {
			ResultSet results = qexec.execSelect();

			while (results.hasNext()) {
				QuerySolution soln = results.nextSolution();
				Resource member = (Resource) soln.get("member");
				// Literal target_id = soln.getLiteral("target_id");
				if (!attribute_setget.contains(model.getIndividual(member.toString()))) {
					attribute_setget.add(model.getIndividual(member.toString()));
				}

			}
		} finally {
			qexec.close();
		}

		for (int i = 0; i < attribute_setget.size(); i++) {
			Individual attribute = (Individual) attribute_setget.get(i);
			attribute.setPropertyValue(is_GetSet, ResourceFactory.createTypedLiteral(true));
		}

	}

	// ResourceFactory.createTypedLiteral(Int or Boolean data);
	public void setRunTimeRelationship(String source, String target) {
		String kind = "";
		Resource source_method = model.createResource();
		Resource target_method = model.createResource();
		String source_class_id = "";
		String target_class_id = "";
		String queryString = "PREFIX oo: <http://isq.im.mgt.ncu.edu.tw/OOOntology.owl#>"
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + "SELECT * {"
				+ "?source_class oo:has_ID ?source_class_id; oo:Class_Method ?source_method."
				+ "?source_method oo:has_ID \"" + source + "\"." + "}";
		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.create(query, model);
		try {
			ResultSet results = qexec.execSelect();
			while (results.hasNext()) {
				QuerySolution soln = results.nextSolution();
				source_class_id = soln.getLiteral("source_class_id").toString();
				source_method = (Resource) soln.get("source_method");
			}
		} finally {
			qexec.close();
		}
		queryString = "PREFIX oo: <http://isq.im.mgt.ncu.edu.tw/OOOntology.owl#>"
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + "SELECT * {"
				+ "?target_class oo:has_ID ?target_class_id; oo:Class_Method ?target_method."
				+ "?target_method oo:has_ID \"" + target + "\"." + "}";
		query = QueryFactory.create(queryString);
		qexec = QueryExecutionFactory.create(query, model);
		try {
			ResultSet results = qexec.execSelect();
			while (results.hasNext()) {
				QuerySolution soln = results.nextSolution();
				target_class_id = soln.getLiteral("target_class_id").toString();
				target_method = (Resource) soln.get("target_method");
			}
		} finally {
			qexec.close();
		}
		queryString = "PREFIX oo: <http://isq.im.mgt.ncu.edu.tw/OOOntology.owl#>"
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + "SELECT * {"
				+ "?generalization oo:Generalization_Source ?source_class; oo:Generalization_Target ?target_class"
				+ "{?source_class oo:has_ID \"" + source_class_id + "\". ?target_class oo:has_ID \"" + target_class_id
				+ "\" ; oo:has_Name ?inher.}UNION" + "{?source_class oo:has_ID \"" + target_class_id
				+ "\". ?target_class oo:has_ID \"" + source_class_id + "\" ; oo:has_Name ?db.}" + "}";
		query = QueryFactory.create(queryString);
		qexec = QueryExecutionFactory.create(query, model);
		try {
			ResultSet results = qexec.execSelect();
			while (results.hasNext()) {
				QuerySolution soln = results.nextSolution();
				if (soln.getLiteral("inher") != null) {
					kind = "Inheritance";
				} else {
					kind = "DynamicBinding";
				}
			}
		} finally {
			qexec.close();
		}

		OntClass myRunTime;

		if (kind != "") {
			if (!RunTimeRelationship.contains(source + " -> " + target)) {
				System.out.println(source + " -> " + target);
				RunTimeRelationship.add(source + " -> " + target);
				myRunTime = model.getOntClass(baseURI + kind);
				Individual i = myRunTime.createIndividual(baseURI + "RunTimeRelationship_" + kind + "_" + rtr);
				i.setPropertyValue(callingSource, model.getIndividual(source_method.toString()));
				i.setPropertyValue(callingMethodTarget, model.getIndividual(target_method.toString()));

				setRunTimeExec(target);
				rtr++;
			}
		}
	}

	public void WriteModel() {
		model.setNsPrefix("OOOntology", baseURI);
		// model.write(System.out, "RDF/XML-ABBREV");
		try {
			File file = new File("C:/Users/QZ_CHUNG/Desktop/alice/OOOntology3.owl");
			// File file = new File("src/ATM_Ontology.owl");
			FileWriter writer = new FileWriter(file);
			model.write(writer, "RDF/XML-ABBREV");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
