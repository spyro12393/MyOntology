package FirstOntology;

import java.io.InputStream;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.util.FileManager;

public class FirstOntology {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		OntModel model = ModelFactory.createOntologyModel();
		InputStream in = FileManager.get().open("D:/data/myfirstontology.owl");
		if (in == null) {
		    throw new IllegalArgumentException("File: not found");
		}
		model.read(in, null);
		
		model.write(System.out, "RDF/XML-ABBREV");
		
		String queryString=
				"PREFIX uni: <http://isq.im.mgt.ncu.edu.tw/myfirstontology.owl#>" + 
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>" + 
				"SELECT * {" + 
				"?student uni:first_name ?x" +
				"}";
		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.create(query, model);
		try{
			ResultSet results = qexec.execSelect();
			while(results.hasNext()){
				QuerySolution soln = results.nextSolution();
				Literal an = soln.getLiteral("x");
				System.out.println(an);
			}
		} finally {
			qexec.close();
		}
	}

}
