package SQWRL;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

public class SQWRLDemo2_2 {
	public static void main(String[] args) {
		OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
		String baseURI = "http://isq.im.mgt.ncu.edu.tw/OOOntology.owl#";
		String fileName = "C:/Users/QZ_CHUNG/Desktop/alice/OOOntology3.owl";

		List<String> node_member = new ArrayList<String>();
		List<String> node_member_name = new ArrayList<String>();

		List<String> node_class = new ArrayList<String>();
		List<String> node_class_name = new ArrayList<String>();
		List<String> is_parent = new ArrayList<String>();
		

		try {
			File file = new File(fileName);
			FileReader reader = new FileReader(file);

			model.read(reader, null);

			// model.write(System.out, "RDF/XML-ABBREV");
		} catch (Exception e) {
			e.printStackTrace();
		}
		JSONArray json_element_arr = new JSONArray();

		String queryString = "PREFIX oo: <http://isq.im.mgt.ncu.edu.tw/OOOntology.owl#>"
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + "SELECT * {"
				+ "{?reference oo:Reference_Target ?target; oo:Reference_Attribute_Source ?source.}UNION"
				+ "{?reference oo:Reference_Target ?target; oo:Reference_Method_Source ?source.}UNION"
				+ "{?reference oo:Reference_Target ?target; oo:Reference_Parameter_Source ?parameter. ?source oo:Method_Parameter ?parameter. ?parameter oo:has_ID ?parameter_id}"
				+ "?reference oo:is_Used ?use." 
				+ "?source oo:has_ID ?source_id; oo:has_Name ?source_name."
				+ "?target oo:has_ID ?target_id; oo:has_Name ?target_name." + "} ORDER BY ?source_id";
		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.create(query, model);
		int count_edge = 0;
		try {
			ResultSet results = qexec.execSelect();
			while (results.hasNext()) {
				QuerySolution soln = results.nextSolution();
				JSONObject json_edge = new JSONObject();
				JSONObject json_edge_data = new JSONObject();
				Literal use = soln.getLiteral("use");
				String source_id = soln.getLiteral("source_id").toString();
				if (!node_member.contains(source_id)) {
					node_member.add(source_id);
					node_member_name.add(soln.getLiteral("source_name").toString());
				}
				String target_id = soln.getLiteral("target_id").toString();
				if (!node_class.contains(target_id)) {
					node_class.add(target_id);
					node_class_name.add(soln.getLiteral("target_name").toString());
				}
				json_edge_data.put("id", "e" + count_edge);
				json_edge_data.put("source", "" + source_id);
				json_edge_data.put("target", "" + target_id);
				if ((boolean) use.getValue()) {
					json_edge_data.put("group", "beused");

				} else {
					json_edge_data.put("group", "unused");
				}
				if (soln.get("parameter") != null)
					source_id = soln.getLiteral("parameter_id").toString();
				json_edge_data.put("name", source_id + " -> " + target_id);
				System.out.println(source_id + " -> " + target_id);

				json_edge.put("data", json_edge_data);
				json_edge.put("group", "edges");
				json_element_arr.add(json_edge);
				count_edge++;
			}
		} finally {
			qexec.close();
		}
		for (int i = 0; i < node_member.size(); i++) {
			JSONObject json_member_node = new JSONObject();
			JSONObject json_member_data = new JSONObject();
			json_member_data.put("id", "" + node_member.get(i));
			json_member_data.put("name", "" + node_member_name.get(i));

			queryString = "PREFIX oo: <http://isq.im.mgt.ncu.edu.tw/OOOntology.owl#>"
					+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + "SELECT * {" 
					+ "?member oo:has_ID \"" + node_member.get(i) + "\"."
					+ "{?class oo:Class_Attribute ?member}UNION{?class oo:Class_Method ?member}"
					+ "?class oo:has_ID ?class_id; oo:has_Name ?class_name" + "}";
			query = QueryFactory.create(queryString);
			qexec = QueryExecutionFactory.create(query, model);
			try {
				ResultSet results = qexec.execSelect();
				while (results.hasNext()) {
					QuerySolution soln = results.nextSolution();
					Literal class_id = soln.getLiteral("class_id");
					json_member_data.put("parent", "" + class_id);
					if (!is_parent.contains(class_id.toString()))is_parent.add("" + class_id);
					if (!node_class.contains(class_id.toString())) {
						node_class.add(class_id.toString());
						node_class_name.add(soln.getLiteral("class_name").toString());
					}
				}
			} finally {
				qexec.close();
			}
			json_member_node.put("data", json_member_data);
			json_member_node.put("group", "nodes");
			json_element_arr.add(json_member_node);
		}

		for (int i = 0; i < node_class.size(); i++) {
			JSONObject json_class_node = new JSONObject();
			JSONObject json_class_data = new JSONObject();
			json_class_data.put("id", "" + node_class.get(i));
			json_class_data.put("name", "" + node_class_name.get(i));
			if(is_parent.contains(node_class.get(i))){
				json_class_data.put("group", "HasChildren");				
			}else{
				json_class_data.put("group", "NoChildren");
			}
			json_class_node.put("data", json_class_data);
			json_class_node.put("group", "nodes");
			json_element_arr.add(json_class_node);
		}
		System.out.println(json_element_arr);
		
		//reference_data.json
		try {
			File file = new File("C:/Users/QZ_CHUNG/Desktop/alice/0608н╫зялс/reference_data.json");
			//File file = new File("C:/Users/QZ_CHUNG/Documents/SQOnt Project/SQOntWeb/WebContent/json/reference_data.json");
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
