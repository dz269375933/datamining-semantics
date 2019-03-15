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
    JButton ok = new JButton("OK");
    JRadioButton actor_included = new JRadioButton("included", true);
    JRadioButton actor_excluded = new JRadioButton("excluded", false);
    ButtonGroup actorButtons = new ButtonGroup();

    JRadioButton director_included = new JRadioButton("included", true);
    JRadioButton director_excluded = new JRadioButton("excluded", false);
    ButtonGroup directorButtons = new ButtonGroup();

    JRadioButton genre_included = new JRadioButton("included", true);
    JRadioButton genre_excluded = new JRadioButton("excluded", false);
    ButtonGroup genreButtons = new ButtonGroup();



    JTextArea ta = new JTextArea(8, 20);
    JTextField actor_input = new JTextField(40);
    JTextField director_input = new JTextField(40);
    JTextField genre_input = new JTextField(40);

    Map<String,Movie> map=new HashMap<>();

    public void init() {
        initMovieMap();
        f.add(ok,BorderLayout.SOUTH);

        JPanel actorCheck=new JPanel();
        actorButtons.add(actor_included);
        actorButtons.add(actor_excluded);
//        actorCheck.add(actorButtons);
        actorCheck.add(actor_included);
        actorCheck.add(actor_excluded);

        JLabel actorLabel=new JLabel();
        actorLabel.setText("Actor:");
        actorCheck.add(actorLabel);
        actorCheck.add(actor_input);

        JPanel directorCheck=new JPanel();
        directorButtons.add(director_included);
        directorButtons.add(director_excluded);
        directorCheck.add(director_included);
        directorCheck.add(director_excluded);

        JLabel directorLabel=new JLabel();
        directorLabel.setText("Director:");
        directorCheck.add(directorLabel);
        directorCheck.add(director_input);


        JPanel genreCheck=new JPanel();
        genreButtons.add(genre_included);
        genreButtons.add(genre_excluded);
        genreCheck.add(genre_included);
        genreCheck.add(genre_excluded);

        JLabel genreLabel=new JLabel();
        genreLabel.setText("Genre:");
        genreCheck.add(genreLabel);
        genreCheck.add(genre_input);




        Box topLeft = Box.createVerticalBox();
        JScrollPane taJsp = new JScrollPane(ta);
        topLeft.add(taJsp);
        topLeft.add(actorCheck);
        topLeft.add(directorCheck);
        topLeft.add(genreCheck);
        Box top = Box.createHorizontalBox();
        top.add(topLeft);
        f.add(top);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.pack();
        f.setVisible(true);
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String result="result:";
                String actor_text=actor_input.getText();
                String director_text=director_input.getText();
                String genre_text=genre_input.getText();
                for(String movieName:map.keySet()){
                    Movie movie=map.get(movieName);
                    if(!(actor_included.isSelected() ^ movie.hasActor(actor_text))
                            &
                            !(director_included.isSelected() ^ movie.hasActor(director_text))
                            & !(genre_included.isSelected() ^ movie.belongGenre(genre_text))){
                        result+="\n"+movieName;
                    }
                }
                ta.setText(result);
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
