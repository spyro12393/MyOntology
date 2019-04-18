package SQWRL;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.jena.ontology.DatatypeProperty;
import org.apache.jena.ontology.Individual;
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

import MyJenaProject.MyOntology;
import MyJenaProject.MyOntology2;
import MyJenaProject.ProjectDataReader2;

public class SQWRLDemo4 {
	public static void main(String[] args) {// String fileName =
											// "src/ATM_Ontology.owl";
		String dynamicfile = "C:/Users/QZ_CHUNG/Desktop/alice/DynamicData.csv";
		//String dynamicfile = "C:/data/DynamicData.csv";

		MyOntology2 MyOnt = new MyOntology2();
		ProjectDataReader2 pdr2 = new ProjectDataReader2(dynamicfile);
		
		int step = 0;
		while (step < pdr2.getTotalStep()) {
			if (pdr2.getKind(step).contains("call")) {
				MyOnt.setRunTimeMethodCall(pdr2.getSource(step),pdr2.getTarget(step));
				if (step + 1 < pdr2.getTotalStep()) {
					if (pdr2.getKind(step + 1).contains("execution")) {
						if (!pdr2.getTarget(step).contains(pdr2.getTarget(step + 1))) {
							MyOnt.setRunTimeExec(pdr2.getTarget(step));
							MyOnt.setRunTimeRelationship(pdr2.getTarget(step),pdr2.getTarget(step + 1));
						}
					}
				}
			} else if (pdr2.getKind(step).contains("execution")) {
				MyOnt.setRunTimeExec(pdr2.getTarget(step));

			} else {
				MyOnt.setRunTimeAttributeCall(pdr2.getObject(step),pdr2.getSource(step),pdr2.getTarget(step));
				MyOnt.setRunTimeGetSet(pdr2.getTarget(step));
			}
			step++;
		}
		MyOnt.getCallingCoupling();
		MyOnt.getInheritCoupling();
		MyOnt.getReferenceCoupling();
		//MyOnt.getUnusedMember();
		
		MyOnt.WriteModel();

	}

}
