package SQWRL;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.jena.ontology.DatatypeProperty;
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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class SQWRLDemo {
	public static void main(String[] args) {
		OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
		String baseURI = "http://isq.im.mgt.ncu.edu.tw/OOOntology.owl#";
		String fileName = "C:/Users/QZ_CHUNG/Desktop/alice/OOOntology3.owl";

		try {
			File file = new File(fileName);
			FileReader reader = new FileReader(file);

			model.read(reader, null);

			// model.write(System.out, "RDF/XML-ABBREV");
		} catch (Exception e) {
			e.printStackTrace();
		}


		JSONArray json_element_arr = new JSONArray();

		System.out.println("--------------------class---------------------");
		// ----------------class---------------------
		String queryString = "PREFIX oo: <http://isq.im.mgt.ncu.edu.tw/OOOntology.owl#>"
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + "SELECT * {"
				+ "?class a oo:Class ; oo:has_ID ?class_ID ; oo:has_Name ?class_name." + "}";
		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.create(query, model);
		try {
			ResultSet results = qexec.execSelect();
			while (results.hasNext()) {
				JSONObject json_node = new JSONObject();
				JSONObject json_data = new JSONObject();
				JSONArray json_attribute_list = new JSONArray();
				JSONArray json_method_list = new JSONArray();

				QuerySolution soln = results.nextSolution();
				// RDFNode x = soln.get("class");
				System.out.println("--------------------query---------------------");
				Literal class_ID = soln.getLiteral("class_ID");
				Literal class_name = soln.getLiteral("class_name");
				System.out.println(class_ID + "\t" + class_name);
				json_data.put("id", "" + class_ID);
				json_data.put("name", "" + class_name);
				// ---------------Attribute--------------
				System.out.println("--------------------Attribute---------------------");
				String queryString2 = "PREFIX oo: <http://isq.im.mgt.ncu.edu.tw/OOOntology.owl#>"
						+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + "SELECT * {"
						+ "?class a oo:Class ; oo:has_ID \"" + class_ID + "\"; oo:Class_Attribute ?attribute."
						+ "?attribute oo:has_ID ?attribute_ID; oo:has_Name ?attribute_name; oo:has_Visibility ?attribute_visibility; oo:has_Type ?attribute_type."
						+ "}";
				Query query2 = QueryFactory.create(queryString2);
				QueryExecution qexec2 = QueryExecutionFactory.create(query2, model);
				try {
					ResultSet results2 = qexec2.execSelect();
					while (results2.hasNext()) {
						JSONObject json_attribute = new JSONObject();

						QuerySolution soln2 = results2.nextSolution();
						// RDFNode x = soln2.get("attributename");
						Literal attribute_ID = soln2.getLiteral("attribute_ID");
						Literal attribute_name = soln2.getLiteral("attribute_name");
						Literal attribute_visibility = soln2.getLiteral("attribute_visibility");
						Literal attribute_type = soln2.getLiteral("attribute_type");
						System.out.println(attribute_ID + "\t" + attribute_name + "\t" + attribute_visibility + "\t"
								+ attribute_type);

						json_attribute.put("attribute_ID", "" + attribute_ID);
						json_attribute.put("attribute_name", "" + attribute_name);
						json_attribute.put("attribute_visibility", "" + attribute_visibility);
						json_attribute.put("attribute_type", "" + attribute_type);
						json_attribute_list.add(json_attribute);
						json_data.put("attribut_list", json_attribute_list);
					}
				} finally {
					qexec2.close();
				}
				// ---------------Method--------------
				System.out.println("--------------------Method---------------------");
				queryString2 = "PREFIX oo: <http://isq.im.mgt.ncu.edu.tw/OOOntology.owl#>"
						+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + "SELECT * {"
						+ "?class a oo:Class ; oo:has_ID \"" + class_ID + "\"; oo:Class_Method ?method."
						+ "?method oo:has_ID ?method_ID; oo:has_Name ?method_name; oo:has_Visibility ?method_visibility."
						+ "OPTIONAL {?method oo:has_Type ?method_type}." + "}";
				query2 = QueryFactory.create(queryString2);
				qexec2 = QueryExecutionFactory.create(query2, model);
				try {
					ResultSet results3 = qexec2.execSelect();
					while (results3.hasNext()) {
						JSONObject json_method = new JSONObject();

						QuerySolution soln2 = results3.nextSolution();
						Literal method_ID = soln2.getLiteral("method_ID");
						Literal method_name = soln2.getLiteral("method_name");
						Literal method_visibility = soln2.getLiteral("method_visibility");
						Literal method_type = soln2.getLiteral("method_type");
						System.out.println(
								method_ID + "\t" + method_name + "\t" + method_visibility + "\t" + method_type);
						json_method.put("method_ID", "" + method_ID);
						json_method.put("method_name", "" + method_name);
						json_method.put("method_visibility", "" + method_visibility);
						if (method_type == null) {
							json_method.put("method_type", "");
						} else {
							json_method.put("method_type", "" + method_type);
						}
						json_method_list.add(json_method);
						json_data.put("method_list", json_method_list);
					}
				} finally {
					qexec2.close();
				}

				json_node.put("data", json_data);
				json_node.put("group", "nodes");
				json_node.put("removed", false);
				json_node.put("selected", false);
				json_node.put("selectable", true);
				json_node.put("locked", false);
				json_node.put("grabbable", true);
				json_node.put("class", "");
				json_element_arr.add(json_node);
			}
		} finally {
			qexec.close();
		}

		queryString = "PREFIX oo: <http://isq.im.mgt.ncu.edu.tw/OOOntology.owl#>"
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + "SELECT * {"
				+ "?coupling a oo:CallingReference; oo:CallingReference_Source ?source_class; "
				+ " oo:CallingReference_Target ?target_class;" + " oo:has_Number ?coupling_number."
				+ " ?source_class oo:has_ID ?source_class_ID." + " ?target_class oo:has_ID ?target_class_ID." + "}";
		query = QueryFactory.create(queryString);
		qexec = QueryExecutionFactory.create(query, model);
		try {
			ResultSet results = qexec.execSelect();
			int i = 0;
			while (results.hasNext()) {
				JSONObject json_edge = new JSONObject();
				JSONObject json_data = new JSONObject();
				JSONArray json_coupling_list = new JSONArray();

				QuerySolution soln = results.nextSolution();
				// RDFNode x = soln.get("class");
				System.out.println("--------------------Coupling---------------------");
				Resource coupling = (Resource) soln.get("coupling");
				Literal source_class_ID = soln.getLiteral("source_class_ID");
				Literal target_class_ID = soln.getLiteral("target_class_ID");
				Literal coupling_number = soln.getLiteral("coupling_number");
				json_data.put("id", "e" + i);
				json_data.put("name", coupling_number.getInt());
				json_data.put("source", "" + source_class_ID);
				json_data.put("target", "" + target_class_ID);
				System.out.println(
						source_class_ID + "->" + target_class_ID + "\t" + coupling_number.getInt() + "\t" + coupling);

				String queryString2 = "PREFIX oo: <http://isq.im.mgt.ncu.edu.tw/OOOntology.owl#>"
						+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + "SELECT * {"
						+ "?source_class a oo:Class; oo:has_ID \"" + source_class_ID + "\"; oo:Class_Method ?source."
						+ "?target_class a oo:Class; oo:has_ID \"" + target_class_ID + "\"."
						+ "{?target_class oo:Class_Method ?target}UNION{?target_class oo:Class_Attribute ?target}"
						+ "{?calling a oo:Call_Attribute ; oo:Calling_Source ?source; oo:Calling_Attribute_Target ?target.}UNION"
						+ "{?calling a oo:Call_Method ; oo:Calling_Source ?source; oo:Calling_Method_Target ?target.}"
						+ "?source oo:has_ID ?sourceID." + "?target oo:has_ID ?targetID." + "}";
				Query query2 = QueryFactory.create(queryString2);
				QueryExecution qexec2 = QueryExecutionFactory.create(query2, model);
				try {

					ResultSet results2 = qexec2.execSelect();
					System.out.println("--------------Couping Detail--------------");
					while (results2.hasNext()) {
						JSONObject json_coupling_content = new JSONObject();
						QuerySolution soln2 = results2.nextSolution();
						Literal source_ID = soln2.getLiteral("sourceID");
						Literal target_ID = soln2.getLiteral("targetID");
						System.out.println(source_ID + "->" + target_ID);
						json_coupling_content.put("type", "Call");
						json_coupling_content.put("source", "" + source_ID);
						json_coupling_content.put("target", "" + target_ID);
						json_coupling_list.add(json_coupling_content);

					}
				} finally {
					qexec2.close();
				}

				queryString2 = "PREFIX oo: <http://isq.im.mgt.ncu.edu.tw/OOOntology.owl#>"
						+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + "SELECT * {"
						+ "?source_class a oo:Class; oo:has_ID \"" + source_class_ID + "\"."
						+ "?target_class a oo:Class; oo:has_ID \"" + target_class_ID + "\"."
						+ "?reference oo:Reference_Target ?target_class."
						+ "{?reference oo:Reference_Attribute_Source ?source}UNION{?reference oo:Reference_Method_Source ?source}UNION{?reference oo:Reference_Parameter_Source ?source}"
						+ "{?source_class oo:Class_Attribute ?source}UNION{?source_class oo:Class_Method ?source}UNION{?source_class oo:Class_Method ?method. ?method oo:Method_Parameter ?source}"
						+ "?source oo:has_ID ?sourceID." + "}";
				query2 = QueryFactory.create(queryString2);
				qexec2 = QueryExecutionFactory.create(query2, model);
				try {
					ResultSet results2 = qexec2.execSelect();
					while (results2.hasNext()) {
						JSONObject json_coupling_content = new JSONObject();
						QuerySolution soln2 = results2.nextSolution();
						Literal source_ID = soln2.getLiteral("sourceID");
						System.out.println(source_ID + "->" + target_class_ID);

						json_coupling_content.put("type", "Refer");
						json_coupling_content.put("source", "" + source_ID);
						json_coupling_content.put("target", "" + target_class_ID);
						json_coupling_list.add(json_coupling_content);
					}
				} finally {
					qexec2.close();
				}
				
				queryString2 = "PREFIX oo: <http://isq.im.mgt.ncu.edu.tw/OOOntology.owl#>"
						+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + "SELECT * {"
						+ "?source_class a oo:Class; oo:has_ID \"" + source_class_ID + "\"."
						+ "?target_class a oo:Class; oo:has_ID \"" + target_class_ID + "\"."
						+ "{?inherit a oo:Inherit; oo:Generalization_Source ?source_class; oo:Generalization_Target ?target_class.} UNION "
						+ "{?realize a oo:Realize; oo:Generalization_Source ?source_class; oo:Generalization_Target ?target_class.}"
						+ "}";
				query2 = QueryFactory.create(queryString2);
				qexec2 = QueryExecutionFactory.create(query2, model);
				try {
					ResultSet results2 = qexec2.execSelect();
					while (results2.hasNext()) {
						JSONObject json_coupling_content = new JSONObject();
						QuerySolution soln2 = results2.nextSolution();
						System.out.println(source_class_ID + "->" + target_class_ID);

						
						if(soln2.get("inherit") != null){
							json_coupling_content.put("type", "Inherit");
						}else{
							json_coupling_content.put("type", "Realize");
						}
						json_coupling_content.put("source", "" + source_class_ID);
						json_coupling_content.put("target", "" + target_class_ID);
						json_coupling_list.add(json_coupling_content);
					}
				} finally {
					qexec2.close();
				}

				json_data.put("coupling_list", json_coupling_list);

				json_edge.put("data", json_data);
				json_edge.put("group", "edges");
				json_edge.put("removed", false);
				json_edge.put("selected", false);
				json_edge.put("selectable", true);
				json_edge.put("locked", false);
				json_edge.put("grabbable", true);
				json_edge.put("class", "");
				json_element_arr.add(json_edge);

				i++;
			}
		} finally {
			qexec.close();
		}
		System.out.println(json_element_arr);

		try {
			File file = new File("C:/Users/QZ_CHUNG/Desktop/alice/0608н╫зялс/class_data.json");
//			File file = new File(
//					"D:/system/RECDQ/REMQ_Web/REMQ/WebContent/CouplingDemo/Example9_cose_bilkent/class_data.json");
			// File file = new File("src/class_data.json");
			FileWriter writer = new FileWriter(file);
			writer.write(json_element_arr.toString());
			writer.flush();
			writer.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
