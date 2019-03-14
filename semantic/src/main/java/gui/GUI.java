package gui;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import jena.JenaEngine;
import org.apache.jena.rdf.model.Statement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class GUI {
    JFrame f = new JFrame("Movie");
    JButton ok = new JButton("确认");
    JRadioButton included = new JRadioButton("included", true);
    JRadioButton excluded = new JRadioButton("excluded", false);
    ButtonGroup bg = new ButtonGroup();

    JRadioButton actor=new JRadioButton("actor",true);
    JRadioButton director=new JRadioButton("director",true);
    JRadioButton genre=new JRadioButton("genre",true);
    ButtonGroup filter=new ButtonGroup();


    JTextArea ta = new JTextArea(8, 20);
    JTextField name = new JTextField(40);

    Map<String,Movie> map=new HashMap<>();

    public void init() {
        initMovieMap();
        JPanel bottom = new JPanel();
        bottom.add(name);
        bottom.add(ok);
        f.add(bottom, BorderLayout.SOUTH);
        JPanel checkPanel = new JPanel();
        bg.add(included);
        bg.add(excluded);
        checkPanel.add(included);
        checkPanel.add(excluded);

        filter.add(actor);
        filter.add(director);
        filter.add(genre);

        JPanel filterPanel = new JPanel();
        filterPanel.add(actor);
        filterPanel.add(director);
        filterPanel.add(genre);



        Box topLeft = Box.createVerticalBox();
        JScrollPane taJsp = new JScrollPane(ta);
        topLeft.add(taJsp);
        topLeft.add(checkPanel);
        topLeft.add(filterPanel);
        Box top = Box.createHorizontalBox();
        top.add(topLeft);
        f.add(top);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.pack();
        f.setVisible(true);
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input=name.getText();
                boolean flag=included.isSelected();
                String text="";
                if(flag){
                    if(actor.isSelected()){
                        text+=input+" is actor of:\n";
                        for(Movie m:map.values()){
                            if(m.hasActor(input)){
                                text+=m.getMovieName()+"\n";
                            }
                        }
                    }else if(director.isSelected()){
                        text+=input +" is director of:\n";
                        for(Movie m:map.values()){
                            if(m.hasDirector(input)){
                                text+=m.getMovieName()+"\n";
                            }
                        }
                    }else if(genre.isSelected()){
                        text+="These movies are belong "+input+"\n";
                        for(Movie m:map.values()){
                            if(m.belongGenre(input)){
                                text+=m.getMovieName()+"\n";
                            }
                        }
                    }
                }else{
                    if(actor.isSelected()){
                        text+=input+" is not actor of:\n";
                        for(Movie m:map.values()){
                            if(!m.hasActor(input)){
                                text+=m.getMovieName()+"\n";
                            }
                        }
                    }else if(director.isSelected()){
                        text+=input +"is not director of:\n";
                        for(Movie m:map.values()){
                            if(!m.hasDirector(input)){
                                text+=m.getMovieName()+"\n";
                            }
                        }
                    }else if(genre.isSelected()){
                        text+="These movies are not belong "+input+"\n";
                        for(Movie m:map.values()){
                            if(!m.belongGenre(input)){
                                text+=m.getMovieName()+"\n";
                            }
                        }
                    }
                }
                ta.setText(text);
            }
        });
    }

    private void initMovieMap() {
        OntModel ontModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        try {
            ontModel.read(new FileInputStream("data/Movie.owl"),"");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Model ruleModel = JenaEngine.readInferencedModelFromRuleFile(ontModel, "data/Jena4.txt");
        OntModel newModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM, ruleModel);

        for (Iterator<?> i = newModel.listIndividuals(); i.hasNext(); ) {
            Individual individual = (Individual) i.next();
            if (individual.getOntClass().getLocalName().equals("Movie")) {
                Movie movie=new Movie();
                movie.setMovieName(individual.getLocalName());

                for (Iterator<?> j = individual.listProperties(); j.hasNext(); ) {
                    Statement statement = (Statement) j.next();
                    String predicate = statement.getPredicate().getLocalName();
                    switch (predicate) {
                        case "hasActor": {
                            String thisActor = statement.getObject().toString();
                            thisActor = thisActor.substring(thisActor.indexOf("#") + 1);
                            movie.addActor(thisActor);
                        }
                        break;
                        case "belong": {
                            String thisGenre = statement.getObject().toString();
                            thisGenre = thisGenre.substring(thisGenre.indexOf("#") + 1);
                            movie.addGenre(thisGenre);
                        }
                        break;
                        case "hasDirector": {
                            String thisDirector = statement.getObject().toString();
                            thisDirector = thisDirector.substring(thisDirector.indexOf("#") + 1);
                            movie.addDirector(thisDirector);
                        }
                        break;
                    }
                }

                map.put(movie.getMovieName(),movie);
            }
        }
    }

    public static void main(String[] args) {

        new GUI().init();
    }

}
