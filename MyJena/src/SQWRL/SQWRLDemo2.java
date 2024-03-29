package SQWRL;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

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

public class SQWRLDemo2 {
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
				JSONObject json_class_node = new JSONObject();
				JSONObject json_class_data = new JSONObject();

				QuerySolution soln = results.nextSolution();
				System.out.println("--------------------query---------------------");
				Literal class_ID = soln.getLiteral("class_ID");
				Literal class_name = soln.getLiteral("class_name");
				System.out.println(class_ID + "\t" + class_name);
				json_class_data.put("id", "" + class_ID);
				json_class_data.put("name", "" + class_name);
				json_class_node.put("data", json_class_data);
				json_class_node.put("group", "nodes");
//				json_class_node.put("removed", false);
//				json_class_node.put("selected", false);
//				json_class_node.put("selectable", true);
//				json_class_node.put("locked", false);
//				json_class_node.put("grabbable", true);
//				json_class_node.put("class", "");
				json_element_arr.add(json_class_node);
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
						JSONObject json_attribute_node = new JSONObject();
						JSONObject json_attribute_data = new JSONObject();

						QuerySolution soln2 = results2.nextSolution();
						// RDFNode x = soln2.get("attributename");
						Literal attribute_ID = soln2.getLiteral("attribute_ID");
						Literal attribute_name = soln2.getLiteral("attribute_name");
						;
						System.out.println(attribute_ID + "\t" + attribute_name);

						json_attribute_data.put("id", "" + attribute_ID);
						json_attribute_data.put("name", "" + attribute_name);
						json_attribute_data.put("parent", "" + class_ID);
						json_attribute_node.put("data", json_attribute_data);
						json_attribute_node.put("group", "nodes");
//						json_attribute_node.put("removed", false);
//						json_attribute_node.put("selected", false);
//						json_attribute_node.put("selectable", true);
//						json_attribute_node.put("locked", false);
//						json_attribute_node.put("grabbable", true);
//						json_attribute_node.put("class", "");
						json_element_arr.add(json_attribute_node);
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
						JSONObject json_method_node = new JSONObject();
						JSONObject json_method_data = new JSONObject();

						QuerySolution soln2 = results3.nextSolution();
						Literal method_ID = soln2.getLiteral("method_ID");
						Literal method_name = soln2.getLiteral("method_name");
						System.out.println(method_ID + "\t" + method_name);
						json_method_data.put("id", "" + method_ID);
						json_method_data.put("name", "" + method_name);
						json_method_data.put("parent", "" + class_ID);
						json_method_node.put("data", json_method_data);
						json_method_node.put("group", "nodes");
//						json_method_node.put("removed", false);
//						json_method_node.put("selected", false);
//						json_method_node.put("selectable", true);
//						json_method_node.put("locked", false);
//						json_method_node.put("grabbable", true);
//						json_method_node.put("class", "");
						json_element_arr.add(json_method_node);

					}
				} finally {
					qexec2.close();
				}

			}
		} finally {
			qexec.close();
		}
		
		int i = 0;

		System.out.println("--------------------calling---------------------");
		//JSONArray json_element_arr2 = new JSONArray();
		queryString = "PREFIX oo: <http://isq.im.mgt.ncu.edu.tw/OOOntology.owl#>"
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + "SELECT * {"
				+ "?calling oo:is_Used ?use"
				+ "{?calling a oo:Call_Attribute ; oo:Calling_Source ?source; oo:Calling_Attribute_Target ?target.}UNION"
				+ "{?calling a oo:Call_Method ; oo:Calling_Source ?source; oo:Calling_Method_Target ?target.}"
				+ "?source oo:has_ID ?source_id. ?target oo:has_ID ?target_id."
				+ "}";
		query = QueryFactory.create(queryString);
		qexec = QueryExecutionFactory.create(query, model);
		try {
			ResultSet results = qexec.execSelect();
			
			while (results.hasNext()) {
				JSONObject json_edge = new JSONObject();
				JSONObject json_edge_data = new JSONObject();
				
				QuerySolution soln = results.nextSolution();
				// RDFNode x = soln.get("class");
				
				Literal source_id = soln.getLiteral("source_id");
				Literal target_id = soln.getLiteral("target_id");
				Literal use = soln.getLiteral("use");
				
				json_edge_data.put("id", "e" + i);
				json_edge_data.put("name", "");
				if(!(boolean)use.getValue()){
					json_edge_data.put("group", "unused");
				}else{
					json_edge_data.put("group", "beused");
				}
				json_edge_data.put("source", "" + source_id);
				json_edge_data.put("target", "" + target_id);
				json_edge.put("data", json_edge_data);
				json_edge.put("group", "edges");
//				json_edge.put("removed", false);
//				json_edge.put("selected", false);
//				json_edge.put("selectable", true);
//				json_edge.put("locked", false);
//				json_edge.put("grabbable", true);
//				json_edge.put("class", "");
				
				System.out.println(source_id +" -> "+target_id);
				
				json_element_arr.add(json_edge);

				i++;
			}
		} finally {
			qexec.close();
		}
		
		System.out.println("--------------------run time---------------------");
		queryString = "PREFIX oo: <http://isq.im.mgt.ncu.edu.tw/OOOntology.owl#>"
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + "SELECT * {"
				+ "{?db a oo:DynamicBinding ; oo:Calling_Source ?source; oo:Calling_Method_Target ?target.}UNION"
				+ "{?in a oo:Inheritance ; oo:Calling_Source ?source; oo:Calling_Method_Target ?target.}"
				+ "?source oo:has_ID ?source_id. ?target oo:has_ID ?target_id."
				+ "}";
		query = QueryFactory.create(queryString);
		qexec = QueryExecutionFactory.create(query, model);
		try {
			ResultSet results = qexec.execSelect();
			while (results.hasNext()) {
				JSONObject json_edge = new JSONObject();
				JSONObject json_edge_data = new JSONObject();
				
				QuerySolution soln = results.nextSolution();
				// RDFNode x = soln.get("class");
				
				Literal source_id = soln.getLiteral("source_id");
				Literal target_id = soln.getLiteral("target_id");
				
				json_edge_data.put("id", "e" + i);
				json_edge_data.put("name", "");
				if(soln.get("db")!=null){
					json_edge_data.put("group", "db");
				}else{
					json_edge_data.put("group", "inherit");
				}
				json_edge_data.put("source", "" + source_id);
				json_edge_data.put("target", "" + target_id);
				json_edge.put("data", json_edge_data);
				json_edge.put("group", "edges");
//				json_edge.put("removed", false);
//				json_edge.put("selected", false);
//				json_edge.put("selectable", true);
//				json_edge.put("locked", false);
//				json_edge.put("grabbable", true);
//				json_edge.put("class", "");
				
				System.out.println(source_id +" -> "+target_id);
				
				json_element_arr.add(json_edge);

				i++;
			}
		} finally {
			qexec.close();
		}
		
		System.out.println(json_element_arr);
//		System.out.println(json_element_arr2);

		try {
			File file = new File("C:/Users/QZ_CHUNG/Desktop/alice/0608�ק��/calling_data.json");
//			File file = new File("C:/Users/QZ_CHUNG/Documents/SQOnt Project/SQOntWeb/WebContent/json/calling_data_node.json");
//			File file2 = new File("C:/Users/QZ_CHUNG/Documents/SQOnt Project/SQOntWeb/WebContent/json/calling_data_edge.json");
			FileWriter writer = new FileWriter(file);
			//FileWriter writer2 = new FileWriter(file2);
			writer.write(json_element_arr.toString());
			//writer2.write(json_element_arr2.toString());
			writer.flush();
			//writer2.flush();
			writer.close();
			//writer2.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
