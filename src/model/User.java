/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.List;

/**
 *
 * @author alexlopez
 */
public class User {
    private String username;
    private String password;
    private int stucoins;
    private int level;
    private String place;
    private int points;
    private List<Inventory> inventory;
    private List<Contact> friends;
    
    public User() {
    }

    public User(String nombre) {
        this.username = username;
    }
    
    public User(String username, String password, String place){
        this.username = username;
        this.password = password;
        this.place = place;
    }
    
    public User(String username, String password, int stucoins, int level, String place, int points){
        this.username = username;
        this.password = password;
        this.stucoins = stucoins;
        this.level = level;
        this.place = place;
        this.points = points;
    }

    public User(String username, String password, String place, List<Inventory> inventory, List<Contact> friends) {
        this.username = username;
        this.password = password;
        this.place = place;
        this.inventory = inventory;
        this.friends = friends;
    }

    public User(String username, String password, int stucoins, int level, String place, int points, List<Inventory> inventory, List<Contact> friends) {
        this.username = username;
        this.password = password;
        this.stucoins = stucoins;
        this.level = level;
        this.place = place;
        this.points = points;
        this.inventory = inventory;
        this.friends = friends;
    }
    
    

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    

    public int getStucoins() {
        return stucoins;
    }

    public void setStucoins(int stucoins) {
        this.stucoins = stucoins;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public List<Inventory> getInventory() {
        return inventory;
    }

    public void setInventory(List<Inventory> inventory) {
        this.inventory = inventory;
    }

    public List<Contact> getFriends() {
        return friends;
    }

    public void setFriends(List<Contact> friends) {
        this.friends = friends;
    }
    
    
    
    @Override
    public String toString() {
        return "[ Username: " + username + " | Stucoins: " + stucoins + " | Level:  " + level + " | Place: " + place + " | Points: " + points + " ]";
    }
    
}
