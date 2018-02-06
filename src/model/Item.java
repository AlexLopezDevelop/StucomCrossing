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
public class Item {
    private String name;
    private double price;
    private String type;//preference
    private String style;//study
    private double salePrice;
    
    public Item(){
    }
    
    public Item(String name){
        this.name = name;
    }
    
    public Item(String name, double price, String type, String style){
        this.name = name;
        this.price = price;
        this.type = type;
        this.style = style;
    }
    
    public Item(String name, double price, String type, String style, double saleprice){
        this.name = name;
        this.price = price;
        this.type = type;
        this.style = style;
        this.salePrice = saleprice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getSalePrice() {
        return salePrice;
    }
    
    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }
    
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }
    
    
    
}
