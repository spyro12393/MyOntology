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

public class SQWRLDemo3 {
	public static void main(String[] args) {
		
		WTCoup wtcoup = new WTCoup();
		Map<String, Map<String, Double>> wcd = wtcoup.getWCD();
		
		
		wtcoup.findCall();
		Map<String, Map<String, Double>> wct = wtcoup.getWCT();
		
		for(String c : wcd.keySet()){
			System.out.println(c);
			for(String ca: wcd.get(c).keySet()){
				System.out.println("\t" + ca + "\t" + wcd.get(c).get(ca));
			}
		}
		double WTCoup = 0.0;
		double total = 0.0;
		for(String c : wct.keySet()){
			System.out.println(c);
			for(String ca: wct.get(c).keySet()){
				total = total + wct.get(c).get(ca);
				System.out.println("\t" + ca + "\t" + wct.get(c).get(ca));
			}
		}
		
		WTCoup = total/(wct.size()*wct.size()-wct.size());
		System.out.println("WTCoup = " + WTCoup);
		
	}

}
