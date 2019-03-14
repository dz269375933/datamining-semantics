package jena;

import org.apache.jena.ontology.*;
import org.apache.jena.rdf.model.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Iterator;

public class Jena1 {
    public static void main(String[] arg){
//        final String personUrl="http://www.semanticweb.org/dz/ontologies/2019/2/movie#Person";
        OntModel ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        try {
            ontModel.read(new FileInputStream("data/Movie.owl"),"");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

//        OntClass oc = ontModel.
//                getOntClass(personUrl);
        for(Iterator<?> i=ontModel.listIndividuals();i.hasNext();){
            Individual individual=(Individual)i.next();
            if(individual.getOntClass().getLocalName().equals("Person"))
                System.out.println(individual.getLocalName());
        }


//        StmtIterator iter;
//        Property predicate;
//        Resource subject;
//        Statement stmt;
//        RDFNode obj;
//        iter = ontModel.listStatements();
//
//        while (iter.hasNext()) {
//            stmt = iter.next();
//            subject = stmt.getSubject();
//// 关键词
//            String key= subject.getURI().substring(
//                    subject.getURI().indexOf("#") + 1);
//            predicate = stmt.getPredicate();
//// 属性
//            String name = predicate.getLocalName();
//            obj = stmt.getObject();
//// 属性值
//            String value= obj.toString();
//            System.out.println(key+":"+name+":"+value);
//        }

//        for(Iterator<?> i=ontModel.list();i.hasNext();){
//            OntProperty ontProperty=(OntProperty) i.next();
//            System.out.println(ontProperty.getLocalName());
//        }

//        Iterator<?> i=ontModel.listSubjectsWithProperty();
//        OntClass person;
//        for(Iterator<?> i=ontModel.listClasses();i.hasNext();){
//            person=(OntClass) i.next();
//            //Not Anonymous class
//            if(person.getLocalName()=="Person")break;
//        }
//
//        for(Iterator<?> i=ontModel.listIndividuals();i.hasNext();){
//            Individual individual=(Individual) i.next();
//            if(individual.)
//            System.out.println(individual.getLocalName()+":"+individual.getURI());
//        }
//        ontModel.listIndividuals();
    }
}
