/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package animalcrossing;

import dao.AnimalCrossingDAO;
import exceptions.AnimalCrossingExceptions;
import extra.TopTen;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Contact;
import model.Inventory;
import model.Item;
import model.User;

/**
 *
 * @author alexlopez
 */
public class AnimalCrossing {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        AnimalCrossingDAO animalCrossingDAO = new AnimalCrossingDAO();
        
        //Data test
        
        Item itemData = new Item("Java", 12.2, "Libro", "DAM");
        model.Character characterData = new model.Character("surfing", "DAW", "stucom", "Libro");
        User userData = new User("Alex", "admin", "Mallorca");
        User user = new User("Alex", "admin", 1000, 0,"Mallorca",0);
        
        System.out.println("");
        System.out.println("Conexión con la base de datos");
        try{
            animalCrossingDAO.conect();
            System.out.println("");
            System.out.println("Conection Succefull.");
            
   
            //------------------------- Insert User ------------------------------->
            System.out.println("");
            System.out.println("Insert user " + userData.getUsername());
            insertUser(animalCrossingDAO, userData);
            
            //------------------------- Print User -------------------------------->
            System.out.println("");
            System.out.println("Print user data");
            getUserByUsername(animalCrossingDAO, userData);
            
            //------------------------ Insert character --------------------------->
            System.out.println("");
            System.out.println("Insert character");
            insertCharacter(animalCrossingDAO, characterData);
            
            //------------------------ Insert item -------------------------------->
            System.out.println("");
            System.out.println("Insert item");
            insertItem(animalCrossingDAO, itemData);
            
            //------------------------ Validate User ------------------------------>
            System.out.println("");
            System.out.println("Validate user");
            validateUser(animalCrossingDAO, userData);
            
            //------------------------ Modificate user ---------------------------->
            System.out.println("");
            System.out.println("Modificate User");//Modify password
            String password = "root";//New password
            modificateUser(animalCrossingDAO, userData, password);
            
            //------------------------ Modificate place --------------------------->
            System.out.println("");
            System.out.println("Modificate place");
            String place = "StucomLab";//New place
            modificatePlace(animalCrossingDAO, characterData, place);
            
            //------------------------ Modificate price --------------------------->
            System.out.println("");
            System.out.println("Modificate price");
            double price = 23.0;//New price
            modificatePrice(animalCrossingDAO, itemData, price);
            
            //---------------------- Characters from user ------------------------->
            System.out.println("");
            System.out.println("Characters from user place");
            charactersFromUserPlace(animalCrossingDAO, userData);
            
            //-------------------------- Buy object ------------------------------->
            System.out.println("");
            System.out.println("Buy item");
            buyItem(animalCrossingDAO, user, itemData);
            
            //-------------------------- Sell object ------------------------------>
            System.out.println("");
            System.out.println("Sell item");
            sellItem(animalCrossingDAO, user, itemData);
            
            //-------------------------- Give object ------------------------------>FALTA
            System.out.println("");
            System.out.println("Give item");
            giveItem(animalCrossingDAO,user, itemData, characterData);
            
            //-------------------------- User inventory --------------------------->
            System.out.println("");
            System.out.println("User inventory");
            userInventory(animalCrossingDAO, userData);
            
            //-------------------------- User friends ----------------------------->
            System.out.println("");
            System.out.println("User friends from "+userData.getUsername());
            userFriends(animalCrossingDAO, userData);
            
            //-------------------------- Items no used ---------------------------->
            System.out.println("");
            System.out.println("Items no used");
            itemsNoUsed(animalCrossingDAO);
            
            //------------------------------ TOP 10 ------------------------------->
            System.out.println("");
            System.out.println("TOP 10");
            topTen(animalCrossingDAO);
            
            //------------------------- Characters no meet user ------------------->
            System.out.println("");
            System.out.println("Characters no meet user");
            charactersNoMeetUser(animalCrossingDAO, userData);
            
        }catch(SQLException ex){
            System.out.println("Error SQL: " + ex.getMessage());
        }
    }
    
    private static void giveItem(AnimalCrossingDAO animalCrossingDAO, User userData, Item itemData, model.Character characterData) throws SQLException{
        try{
            boolean correct = animalCrossingDAO.giveItem(userData, itemData, characterData);
            if(correct){
                System.out.println("Succefull present transaction");
            }
        }catch(AnimalCrossingExceptions ex){
            System.out.println(ex.getMessage());
        }
    }
    
    private static void charactersNoMeetUser(AnimalCrossingDAO animalCrossingDAO, User userData) throws SQLException{
        List<model.Character> characters;
        characters = animalCrossingDAO.charactersNoMeetUser(userData);
        if(characters.isEmpty()){
            System.out.println(userData.getUsername() + " know all characters");
        }else{
            System.out.println("Found " + characters.size() + " characters to know");
            for(model.Character c : characters){
                System.out.println(c.getName());
            }
        }
    }
    
    private static void sellItem(AnimalCrossingDAO animalCrossingDAO, User userData, Item itemData) throws SQLException{
        try{
            boolean correct = animalCrossingDAO.sellItem(userData, itemData);
            if(correct){
                System.out.println("Thanks for sell");
            }else{
                System.out.println("Sell faild process");
            }
        }catch(AnimalCrossingExceptions ex){
            System.out.println(ex.getMessage());
        }
    }
    
    private static void buyItem(AnimalCrossingDAO animalCrossingDAO, User userData, Item itemData) throws SQLException{
        try{
            boolean correct = animalCrossingDAO.buyItem(userData, itemData);
            if(correct){
                System.out.println("Thanks for buy");
            }else{
                System.out.println("Buy faild process");
            }
        }catch(AnimalCrossingExceptions ex){
            System.out.println(ex.getMessage());
        }
    }
    
    private static void topTen(AnimalCrossingDAO animalCrossingDAO) throws SQLException{
        List<TopTen> list;
        list = animalCrossingDAO.topTen();
        if(list.isEmpty()){
           System.out.println("No users");
       }else{
           for(TopTen t : list){
               System.out.println("[ Name: " + t.getName() + " | Friends: " + t.getFriends() + " ]");
           }
       }
    }
   
   private static void itemsNoUsed(AnimalCrossingDAO animalCrossingDAO) throws SQLException{
       List<Item> items;
       items = animalCrossingDAO.itemsNoUsed();
       if(items.isEmpty()){
           System.out.println("All items have owner");
       }else{
           System.out.println("Found " + items.size() + " Items without owner");
           for(Item i : items){
               System.out.println("[ Name: " + i.getName() + " | Style: " + i.getStyle());
           }
       }
   }
    
   private static void userFriends(AnimalCrossingDAO animalCrossingDAO, User userData) throws SQLException{
       try{
           List<Contact> contact;
           contact = animalCrossingDAO.userFriends(userData);
           if(contact.isEmpty()){
               System.out.println(userData.getUsername()+" don't have friends, it's time to stop programming");
           }else{
               System.out.println("Found " + contact.size() + " friends of " + userData.getUsername());
               for(Contact c : contact){
                   System.out.println("[ Name: " + c.getCharacter().getName() + " | LevelFriend: " + c.getLevelFrind() + " | PointsFriend: " + c.getPoints()+ " | Date: "+ c.getDate() +" ]");
               }
           }
       }catch(AnimalCrossingExceptions ex){
           System.out.println(ex.getMessage());
       }
   }
    
    private static void userInventory(AnimalCrossingDAO animalCrossingDAO, User userData) throws SQLException{
        try {
            List<Inventory> inventory;
            inventory = animalCrossingDAO.userInventory(userData);
            if(inventory.isEmpty()){
                System.out.println("0 items in the inventory of " + userData.getUsername());
            }else{
                System.out.println("Found " + inventory.size() + " items in " + userData.getUsername() + " inventory");
                for(Inventory i : inventory){
                    System.out.println("[ Item: " + i.getItem().getName() + " | " + i.getItem().getPrice() + " | " + i.getItem().getSalePrice() + " | " + i.getItem().getStyle() + " | " + i.getItem().getType() + " ] Quantity: " + i.getQuantity() + " ]");
                }
            }
        } catch (AnimalCrossingExceptions ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    private static void charactersFromUserPlace(AnimalCrossingDAO animalCrossingDAO, User userData) throws SQLException{
        List<model.Character> characters;
        characters = animalCrossingDAO.charactersFromUserPlace(userData);
        if(characters.isEmpty()){
            System.out.println("0 characters in " + userData.getPlace());
        }else{
            System.out.println("Found " + characters.size() + " characters in " + userData.getPlace());
            for(model.Character c : characters){
                System.out.println(c.getName());
            }
        }
    }
    
    private static void modificatePrice(AnimalCrossingDAO animalCrossingDAO, Item itemData, Double price) throws SQLException{
        try{
            animalCrossingDAO.modificatePrice(itemData, price);
            System.out.println("Price changed succefull");
        }catch(AnimalCrossingExceptions ex){
            System.out.println(ex.getMessage());
        }
    }
    
    private static void modificatePlace(AnimalCrossingDAO animalCrossingDAO, model.Character characterData, String place) throws SQLException{
        try{
            animalCrossingDAO.modificatePlace(characterData, place);
            System.out.println("Place changed succefull");
        }catch(AnimalCrossingExceptions ex){
            System.out.println(ex.getMessage());
        }
    }
    
    private static void modificateUser(AnimalCrossingDAO animalCrossingDAO, User userData, String password) throws SQLException{
        try{
            animalCrossingDAO.modificateUser(userData, password);
            System.out.println("Password changed succefull");
        }catch(AnimalCrossingExceptions ex){
            System.out.println(ex.getMessage());
        }
        
    }
    
    private static void validateUser(AnimalCrossingDAO animalCrossingDAO, User userData) throws SQLException{
        boolean logIn = animalCrossingDAO.validateUser(userData);
        if(logIn){
            System.out.println("User login correct");
        }else{
            System.out.println("User/password incorrect");
        }
    }
    
    private static void insertItem(AnimalCrossingDAO animalCrossingDAO, Item itemData) throws SQLException{
        try{
            animalCrossingDAO.insertItem(itemData);
        }catch(AnimalCrossingExceptions ex){
            System.out.println(ex.getMessage());
        }
    }
    
    private static void insertCharacter(AnimalCrossingDAO animalCrossingDAO, model.Character characterData) throws SQLException{
        try{
            animalCrossingDAO.insertCharacter(characterData);
        }catch(AnimalCrossingExceptions ex){
            System.out.println(ex.getMessage());
        }
    }
    
    private static void getUserByUsername(AnimalCrossingDAO animalCrossingDAO, User userData) throws SQLException{
        User aux;
        try{
            String username = userData.getUsername();//Get username of user
            aux = animalCrossingDAO.getUserByUsername(username);
            System.out.println(aux);
        }catch(AnimalCrossingExceptions ex){
            System.out.println(ex.getMessage());
        }
    }
    
    private static void insertUser(AnimalCrossingDAO animalCrossingDAO, User userData) throws SQLException{
        try{
            animalCrossingDAO.insertUser(userData);
        }catch(AnimalCrossingExceptions ex){
            System.out.println(ex.getMessage());
        }
    }
    
}
