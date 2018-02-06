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
public class Contact {
   private Character character;
   private int levelFrind;
   private int points;
   private String date;
   
   public Contact(){
   }

    public Contact(Character character, int levelFrind, int points, String date) {
        this.character = character;
        this.levelFrind = levelFrind;
        this.points = points;
        this.date = date;
    }

    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }

    public int getLevelFrind() {
        return levelFrind;
    }

    public void setLevelFrind(int levelFrind) {
        this.levelFrind = levelFrind;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
      
    
    
}
