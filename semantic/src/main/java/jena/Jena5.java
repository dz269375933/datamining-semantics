package jena;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Jena5 {
    public static void main(String[] arg){
        OntModel ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        try {
            ontModel.read(new FileInputStream("data/Movie.owl"),"");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Model ruleModel=JenaEngine.readInferencedModelFromRuleFile(ontModel,"data/rule.txt");
        System.out.println(JenaEngine.executeQueryFile(ruleModel,
                "data/Jena5ARQ.txt"));
    }
}
