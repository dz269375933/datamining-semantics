package jena;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Iterator;


public class Jena3 {
    public static void main(String[] arg){
        OntModel ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        try {
            ontModel.read(new FileInputStream("data/Movie.owl"),"");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Model ruleModel=JenaEngine.readInferencedModelFromRuleFile(ontModel,"data/Jena3.txt");
        OntModel newModel=ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM,ruleModel);
        for(Iterator<?> i=newModel.listIndividuals();i.hasNext();){
            Individual individual=(Individual)i.next();
            if(individual.getOntClass().getLocalName().equals("Actor"))
                System.out.println(individual.getLocalName());
//            System.out.println(individual.getOntClass().getLocalName());
        }
    }
}
