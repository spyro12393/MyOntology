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

public class SQWRLDemo2_3 {
	public static void main(String[] args) {
		OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
		String baseURI = "http://isq.im.mgt.ncu.edu.tw/OOOntology.owl#";
		String fileName = "C:/Users/QZ_CHUNG/Desktop/alice/OOOntology3.owl";
		List<String> node_method = new ArrayList<String>();
		List<String> node_method_name = new ArrayList<String>();
		List<String> node_method_class = new ArrayList<String>();
		
		List<String> node_class = new ArrayList<String>();
		List<String> node_class_name = new ArrayList<String>();

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
				+ "?generalization oo:Generalization_Source ?sub_class; oo:Generalization_Target ?super_class."
				+ "?sub_class oo:Class_Method ?method; oo:has_ID ?sub_class_id; oo:has_Name ?sub_class_name."
				+ "?super_class oo:has_ID ?super_class_id; oo:has_Name ?super_class_name."
				+ "?method oo:has_ID ?method_id; oo:has_Name ?method_name; oo:is_Descendant ?descend; oo:is_Invoked ?invoked"
				+ "}";
		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.create(query, model);
		int count_edge = 0;
		try {
			ResultSet results = qexec.execSelect();
			while (results.hasNext()) {
				QuerySolution soln = results.nextSolution();
				JSONObject json_edge = new JSONObject();
				JSONObject json_edge_data = new JSONObject();
				JSONObject json_node = new JSONObject();
				JSONObject json_node_data = new JSONObject();
				Literal method_id = soln.getLiteral("method_id");
				Literal method_name = soln.getLiteral("method_name");
				Literal descend = soln.getLiteral("descend");
				Literal invoked = soln.getLiteral("invoked");
				
				Literal sub_class_id = soln.getLiteral("sub_class_id");
				Literal sub_class_name = soln.getLiteral("sub_class_name");
				
				Literal super_class_id = soln.getLiteral("super_class_id");
				Literal super_class_name = soln.getLiteral("super_class_name");
				
				
				json_node_data.put("id", "" + method_id);
				json_node_data.put("name", "" + method_name);
				
				if((boolean)descend.getValue()){
					json_node_data.put("group", "descendant");
					json_edge_data.put("id", "e" + count_edge);
					json_edge_data.put("source", "" + method_id);
					json_edge_data.put("target", super_class_id + "." + method_name + "()" );
					if(!node_method.contains(super_class_id + "." + method_name + "()")){
						node_method.add(super_class_id + "." + method_name+ "()");
						node_method_name.add("" + method_name);
						node_method_class.add("" + super_class_id);
					}
					if((boolean)invoked.getValue()){
						json_edge_data.put("group", "beused");
					}else{
						json_edge_data.put("group", "unused");
					}
					json_edge.put("data", json_edge_data);
					json_edge.put("group", "edges");
					json_element_arr.add(json_edge);
					
					count_edge++;
				}
				json_node_data.put("parent", "" + sub_class_id);
				json_node.put("data", json_node_data);
				json_node.put("group", "nodes");
				json_element_arr.add(json_node);
				System.out.println(method_id +"\t"+ method_name+"\t" + descend.getValue()+"\t" + invoked.getValue());
				
				if(!node_class.contains(sub_class_id.toString())){
					node_class.add(sub_class_id.toString());
					node_class_name.add(sub_class_name.toString());
				}
				if(!node_class.contains(super_class_id.toString())){
					node_class.add(super_class_id.toString());
					node_class_name.add(super_class_name.toString());
				}
			}
		} finally {
			qexec.close();
		}
		
		for (int i = 0; i < node_class.size(); i++) {
			JSONObject json_class_node = new JSONObject();
			JSONObject json_class_data = new JSONObject();
			json_class_data.put("id", "" + node_class.get(i));
			json_class_data.put("name", "" + node_class_name.get(i));
			json_class_node.put("data", json_class_data);
			json_class_node.put("group", "nodes");
			json_element_arr.add(json_class_node);
		}
		
		for (int i = 0; i < node_method.size(); i++) {
			JSONObject json_method_node = new JSONObject();
			JSONObject json_method_data = new JSONObject();
			json_method_data.put("id", "" + node_method.get(i));
			json_method_data.put("name", "" + node_method_name.get(i));
			json_method_data.put("parent", "" + node_method_class.get(i));
			json_method_node.put("data", json_method_data);
			json_method_node.put("group", "nodes");
			json_element_arr.add(json_method_node);
		}
		System.out.println(json_element_arr);
		
		try {
			File file = new File("C:/Users/QZ_CHUNG/Desktop/alice/0608н╫зялс/inheritance_data.json");
			//File file = new File("C:/Users/QZ_CHUNG/Documents/SQOnt Project/SQOntWeb/WebContent/json/inheritance_data.json");
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
