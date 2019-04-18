package SQWRL;

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

public class WTCoup {
	private OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
	private String baseURI = "http://isq.im.mgt.ncu.edu.tw/OOOntology.owl#";
	private String fileName = "C:/Users/QZ_CHUNG/Desktop/alice/OOOntology3.owl";

	private Map<String, Map<String, Double>> wcd_all = new HashMap<>();/////// ANSER
	private Map<String, Map<String, Double>> wct_all = new HashMap<>();
	private Map<String, Double> wct;

	public WTCoup() {
		try {
			File file = new File(fileName);
			FileReader reader = new FileReader(file);

			model.read(reader, null);

			// model.write(System.out, "RDF/XML-ABBREV");
		} catch (Exception e) {
			e.printStackTrace();
		}

		// ----------------------------Class------------------------------
		Map<String, Map<String, Map<String, Integer>>> class_member = new HashMap<>();
		String queryString = "PREFIX oo: <http://isq.im.mgt.ncu.edu.tw/OOOntology.owl#>"
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + "SELECT * {"
				+ "?class a oo:Class; oo:has_ID ?class_id." + "} ORDER BY ?class_id";
		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.create(query, model);
		try {
			ResultSet results = qexec.execSelect();
			while (results.hasNext()) {
				QuerySolution soln = results.nextSolution();
				Literal class_id = soln.getLiteral("class_id");
				System.out.println("------------------" + class_id + "----------------");
				// ----------------------------Member------------------------------
				Map<String, Map<String, Integer>> member_call = new HashMap<>();
				String queryString2 = "PREFIX oo: <http://isq.im.mgt.ncu.edu.tw/OOOntology.owl#>"
						+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + "SELECT * {" + "?class oo:has_ID \""
						+ class_id + "\"." + "{?class oo:Class_Attribute ?member}UNION"
						+ "{?class oo:Class_Method ?member}" + "?member oo:has_ID ?member_id" + "} ORDER BY ?member_id";
				Query query2 = QueryFactory.create(queryString2);
				QueryExecution qexec2 = QueryExecutionFactory.create(query2, model);
				try {
					ResultSet results2 = qexec2.execSelect();
					while (results2.hasNext()) {
						QuerySolution soln2 = results2.nextSolution();
						Literal member_id = soln2.getLiteral("member_id");
						System.out.println(member_id);
						Map<String, Integer> other_class_number = new HashMap<>();
						// ----------------------------Other
						// Class------------------------------
						// ----------------------------Calling------------------------------
						System.out.println("------------------Calling----------------");
						String queryString3 = "PREFIX oo: <http://isq.im.mgt.ncu.edu.tw/OOOntology.owl#>"
								+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + "SELECT * {"
								+ "?source oo:has_ID \"" + member_id + "\"." + "?calling oo:Calling_Source ?source."
								+ "{?calling oo:Calling_Method_Target ?target} UNION {?calling oo:Calling_Attribute_Target ?target}"
								+ "?target oo:has_ID ?target_id"
								+ "{?class oo:Class_Method ?target} UNION {?class oo:Class_Attribute ?target}"
								+ "?class oo:has_ID ?class_id" + "}";
						Query query3 = QueryFactory.create(queryString3);
						QueryExecution qexec3 = QueryExecutionFactory.create(query3, model);
						try {
							ResultSet results3 = qexec3.execSelect();
							while (results3.hasNext()) {
								QuerySolution soln3 = results3.nextSolution();
								Literal target_id = soln3.getLiteral("target_id");
								Literal other_class_id = soln3.getLiteral("class_id");
								System.out.println("\t" + target_id);
								if (!other_class_id.equals(class_id)) {
									if (!other_class_number.containsKey(other_class_id.toString())) {
										other_class_number.put(other_class_id.toString(), 1);
									} else {
										other_class_number.put(other_class_id.toString(),
												other_class_number.get(other_class_id.toString()) + 1);
									}
								}
							}
						} finally {
							qexec3.close();
						}
						// ----------------------------Reference------------------------------
						System.out.println("------------------Reference----------------");
						queryString3 = "PREFIX oo: <http://isq.im.mgt.ncu.edu.tw/OOOntology.owl#>"
								+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + "SELECT * {"
								+ "?source oo:has_ID \"" + member_id + "\"."
								+ "{?reference oo:Reference_Attribute_Source ?source}UNION"
								+ "{?reference oo:Reference_Method_Source ?source}UNION"
								+ "{?reference oo:Reference_Parameter_Source ?para. ?source oo:Method_Parameter ?para. ?para oo:has_ID ?para_id}"
								+ "?reference oo:Reference_Target ?target. ?target oo:has_ID ?target_id" + "}";
						query3 = QueryFactory.create(queryString3);
						qexec3 = QueryExecutionFactory.create(query3, model);
						try {
							ResultSet results3 = qexec3.execSelect();
							while (results3.hasNext()) {
								QuerySolution soln3 = results3.nextSolution();
								// Literal para_id =
								// soln3.getLiteral("para_id");
								Literal other_class_id = soln3.getLiteral("target_id");
								System.out.println("\t" + other_class_id);
								if (!other_class_id.equals(class_id)) {
									if (!other_class_number.containsKey(other_class_id.toString())) {
										other_class_number.put(other_class_id.toString(), 1);
									} else {
										other_class_number.put(other_class_id.toString(),
												other_class_number.get(other_class_id.toString()) + 1);
									}
								}
							}
						} finally {
							qexec3.close();
						}

						member_call.put(member_id.toString(), other_class_number);
					}

				} finally {
					qexec2.close();
				}
				class_member.put(class_id.toString(), member_call);
			}
		} finally {
			qexec.close();
		}
		System.out.println("-------------------------------------------");
		Map<String, Map<String, Integer>> wcd = new HashMap<>();
		Map<String, Integer> class_member_number = new HashMap<>();
		for (String c : class_member.keySet()) {
			Map<String, Integer> tmp = new HashMap();
			for (String m : class_member.get(c).keySet()) {
				for (String ca : class_member.get(c).get(m).keySet()) {
					if (!tmp.containsKey(ca)) {
						tmp.put(ca, 1);
					} else {
						tmp.put(ca, tmp.get(ca) + 1);
					}
				}

			}
			wcd.put(c, tmp);
			class_member_number.put(c, class_member.get(c).keySet().size());
		}
		for (String c : wcd.keySet()) {
			Map<String, Double> tmp = new HashMap();
			// System.out.println(c);
			for (String ca : wcd.get(c).keySet()) {
				tmp.put(ca, (double) wcd.get(c).get(ca) / class_member_number.get(c));
				// System.out.println("\t"+ ca + "\t" + wcd.get(c).get(ca));
			}
			wcd_all.put(c, tmp);
		}

	}

	public Map<String, Map<String, Double>> getWCD() {
		return wcd_all;
	}
	
	public Map<String, Map<String, Double>> getWCT() {
		return wct_all;
	}

	public void findCall() {
		String queryString = "PREFIX oo: <http://isq.im.mgt.ncu.edu.tw/OOOntology.owl#>"
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + "SELECT * {"
				+ "?class a oo:Class; oo:has_ID ?class_id" + "}";
		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.create(query, model);
		try {
			ResultSet results = qexec.execSelect();
			while (results.hasNext()) {
				QuerySolution soln = results.nextSolution();
				String class_id = soln.getLiteral("class_id").toString();
				System.out.println(class_id + "----------------------------");
				wct = new HashMap<>();
				getCallingReference(class_id, class_id, 1);
				wct_all.put(class_id, wct);
			}
		} finally {
			qexec.close();
		}

	}

	public void getCallingReference(String start_class, String source_class_id, double pre_value) {
		String queryString = "PREFIX oo: <http://isq.im.mgt.ncu.edu.tw/OOOntology.owl#>"
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + "SELECT * {"
				+ "?coupling oo:CallingReference_Source ?source_class; oo:CallingReference_Target ?target_class."
				+ "?source_class oo:has_ID \"" + source_class_id + "\"." + "?target_class oo:has_ID ?target_class_ID."
				+ "}";
		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.create(query, model);
		
		try {
			ResultSet results = qexec.execSelect();
			
			while (results.hasNext()) {
				QuerySolution soln = results.nextSolution();
				String target_class_id = soln.getLiteral("target_class_ID").toString();
				if (!target_class_id.equals(start_class)) {
					if (wcd_all.get(source_class_id).get(target_class_id) != null) {
						double value = pre_value * wcd_all.get(source_class_id).get(target_class_id);
						if(wct.containsKey(target_class_id)){
							if(value > wct.get(target_class_id)){
								wct.put(target_class_id, value);
							}
						}else{
							wct.put(target_class_id, value);
						}
						System.out.print(source_class_id + " -> " + target_class_id + "\t");
						System.out.println(wcd_all.get(source_class_id).get(target_class_id) + "\tvalue = " + value);
						getCallingReference(start_class, target_class_id, value);
					}
				}
			}
		} finally {
			qexec.close();
		}
	}

}
