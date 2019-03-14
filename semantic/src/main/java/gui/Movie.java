package gui;

import java.util.ArrayList;
import java.util.List;

public class Movie {
    List<String> actors=new ArrayList<>();
    List<String> directors=new ArrayList<>();
    List<String> genres=new ArrayList<>();
    String movieName;

    public void setMovieName(String name){
        movieName=name;
    }
    public String getMovieName(){
        return movieName;
    }
    public void addActor(String actor){
        for(String actorName:actors){
            if(actor.equals(actorName))return;
        }
        actors.add(actor);
    }
    public void addDirector(String director){
        for(String name:directors){
            if(director.equals(name))return;
        }
        directors.add(director);
    }
    public void addGenre(String genre){
        for(String name:genres){
            if(genre.equals(name))return;
        }
        genres.add(genre);
    }

    public boolean hasActor(String actor){
        for(String name:actors){
            if(name.equals(actor))return true;
        }
        return false;
    }
    public boolean hasDirector(String director){
        for(String name:directors){
            if(name.equals(director))return true;
        }
        return false;
    }
    public boolean belongGenre(String genre){
        for(String name:genres){
            if(name.equals(genre))return true;
        }
        return false;
    }

}
