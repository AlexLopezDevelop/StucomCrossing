/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package extra;

/**
 *
 * @author alexlopez
 */
public class TopTen {
    String name;
    int friends;

    public TopTen(String name, int friends) {
        this.name = name;
        this.friends = friends;
    }

    public TopTen() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFriends() {
        return friends;
    }

    public void setFriends(int friends) {
        this.friends = friends;
    }
    
    
}
