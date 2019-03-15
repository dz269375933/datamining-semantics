package jena;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Jena6 {
    public static void main(String[] arg){
        OntModel ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        try {
            ontModel.read(new FileInputStream("data/Movie.owl"),"");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Model ruleModel=JenaEngine.readInferencedModelFromRuleFile(ontModel,"data/Jena6.txt");
        String prefix="PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                "PREFIX dz:<http://www.semanticweb.org/dz/ontologies/2019/2/movie#>";
        System.out.println(JenaEngine.executeQuery(ruleModel,
                prefix+"SELECT ?writer_director where {?writer_director rdf:type dz:WriterDirector}"));
        System.out.println(JenaEngine.executeQuery(ruleModel,
                prefix+"SELECT ?actor_writer where {?actor_writer rdf:type dz:ActorWriter}"));
        System.out.println(JenaEngine.executeQuery(ruleModel,
                prefix+"SELECT ?thriller_movie where {?thriller_movie rdf:type dz:ThrillerMovie}"));
    }
}
