package jena;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Statement;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

public class Jena4 {
    public static void main(String[] arg) {
        Scanner sc = new Scanner(System.in);
        String movieName;
        OntModel ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        try {
            ontModel.read(new FileInputStream("data/Movie.owl"), "");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Model ruleModel = JenaEngine.readInferencedModelFromRuleFile(ontModel, "data/Jena4.txt");
        OntModel newModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM, ruleModel);

//        System.out.println(map.get("year"));
        System.out.println("Please input a movie name(input exit for exiting):");
        movieName = sc.next();
//            System.out.println(movieName);
//            findMovie(movieName,ontModel);
        Map map = findMovie(movieName, newModel);
        if (map.size() == 0) System.out.println("Sorry,wrong movie name");
        else {
            System.out.println("movieName:" + movieName);
            System.out.println("year:" + map.get("year"));
            System.out.println("country:" + map.get("country"));
            System.out.println("genre:" + map.get("genre"));
            System.out.println("actor:" + map.get("actor"));
        }
    }

    public static Map<String, String> findMovie(String movieName, OntModel ontModel) {
        Map<String, String> map = new HashMap<>();
        for (Iterator<?> i = ontModel.listIndividuals(); i.hasNext(); ) {
            Individual individual = (Individual) i.next();
//            System.out.println(individual.getLocalName());
            if (individual.getOntClass().getLocalName().equals("Movie")
                    && individual.getLocalName().equals(movieName)) {
                for (Iterator<?> j = individual.listProperties(); j.hasNext(); ) {
                    Statement statement = (Statement) j.next();
                    String predicate = statement.getPredicate().getLocalName();
//                    String object=statement.getObject();
//                    System.out.println(predicate);
                    switch (predicate) {
                        case "year": {
                            String object = statement.getObject().toString();
                            map.put("year", object.substring(0, object.indexOf("^^")));
                        }
                        break;
                        case "country": {
                            map.put("country", statement.getObject().toString());
                        }
                        break;
                        case "hasActor": {
                            String thisActor = statement.getObject().toString();
                            thisActor = thisActor.substring(thisActor.indexOf("#") + 1);
                            if (map.containsKey("actor")) {
                                String tempActor = map.get("actor") + "|" + thisActor;
                                map.put("actor", tempActor);
                            } else {
                                map.put("actor", thisActor);
                            }
                        }
                        break;
                        case "belong": {
                            String thisGenre = statement.getObject().toString();
                            thisGenre = thisGenre.substring(thisGenre.indexOf("#") + 1);
                            if (map.containsKey("genre")) {
                                String tempActor = map.get("genre") + "|" + thisGenre;
                                map.put("genre", tempActor);
                            } else {
                                map.put("genre", thisGenre);
                            }
                        }
                        break;
                    }
                }
            }
//                System.out.println(individual.getLocalName());
        }
        return map;
    }
}
