/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author alexlopez
 */
public class Character {
    private String name;
    private String study;
    private String place;
    private String preference;
    
    public Character(){
    }
    
    public Character(String name){
        this.name = name;
    }
    
    public Character(String name, String study, String place, String preference){
        this.name = name;
        this.study = study;
        this.place = place;
        this.preference = preference;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStudy() {
        return study;
    }

    public void setStudy(String study) {
        this.study = study;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getPreference() {
        return preference;
    }

    public void setPreference(String preference) {
        this.preference = preference;
    }
    
    
}

