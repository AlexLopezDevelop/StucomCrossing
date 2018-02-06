/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import exceptions.AnimalCrossingExceptions;
import extra.TopTen;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import model.Character;
import model.Contact;
import model.Inventory;
import model.Item;
import model.User;

/**
 *
 * @author alexlopez
 */
public class AnimalCrossingDAO {
    Connection conn;
    
    // ----------------------------------- Inserts -------------------------------------->
    public void updateCharacterPlace(Character c) throws AnimalCrossingExceptions, SQLException {//Actualizamos el lugar de un personaje
        if (!existsCharacter(c)) {
            throw new AnimalCrossingExceptions("ERROR: No exists character with these username");
        }
        String update = "update stucomcrossing.character set place = ? where name = ?";
        PreparedStatement ps = conn.prepareStatement(update);
        ps.setString(1, c.getPlace());
        ps.setString(2, c.getName());
        ps.executeUpdate();
        ps.close();
    }
    
    public void insertContact(User u, Character c) throws SQLException, AnimalCrossingExceptions {//Insertamos un contacto

        if (!friend(u, c)) {//miramos si no son amigos para en este caso tener que añadirlo a su listado de amigos
            System.out.println("se a comprobado q no son amigos");
            LocalDate hoy = LocalDate.of(Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH);//Recogemos la fecha actual para añadirla a la hora del update

            String insert = "insert into contact values(?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(insert);
            ps.setString(1, u.getUsername());
            ps.setString(2, c.getName());
            ps.setDate(3, (Date.valueOf(hoy)));
            ps.setInt(4, 1);
            ps.setInt(5, 0);
            ps.executeUpdate();
            ps.close();
        } else {//En este caso ya tiene el personaje en su listado de amigos
            int randow = (int) (Math.random() * 6 + 3);
            String update = "update contact set points = points + " + randow + " where user ='" + u.getUsername() + "' and contact.character ='" + c.getName() + "'";
            Statement st = conn.createStatement();
            st.executeUpdate(update);
            st.close();

            if (pointsOfContact(u, c) >= 10) {//Miramos que los puntos que tiene por si hay que subirlo de nivel
                randow = (int) (Math.random() * 8 + 4);
                Statement st1 = conn.createStatement();
                try {
                    conn.setAutoCommit(false);

                    System.out.println("subir nv de amistad");
                    String updateFriendLevel = "update contact set level=level+1, points = 0 where user ='" + u.getUsername() + "' and contact.character ='" + c.getName() + "'";
                    String updateUserLevel = "update user set level=level+" + randow + ", points=poins+1 where username='" + u.getUsername() + "'";
                    String updateUserPoints = "update user set points=poins+1 where username='" + u.getUsername() + "'";

                    st1.executeUpdate(updateFriendLevel);
                    st1.executeUpdate(updateUserLevel);
                    st1.executeUpdate(updateUserPoints);
                    conn.commit();
                } catch (SQLException ex) {
                    conn.rollback();
                    throw new AnimalCrossingExceptions("ERROR: To update data");
                } finally {
                    st.close();
                    conn.setAutoCommit(true);
                }
            }
        }
    }
    
    public void insertIventory(User u, Item i) throws SQLException{
        if (!itemExistInInventory(u, i)) {//Miramos si tiene el item en su inventario por si hay que añadirlo
            String insert = "insert into inventory values(?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(insert);
            ps.setString(1, u.getUsername());
            ps.setString(2, i.getName());
            ps.setInt(3, 1);
            ps.executeUpdate();
            ps.close();
        } else {//En el caso de tenerlo solo hay que subirle la cantidad
            String update = "update inventory set quantity=quantity+1 where user='" + u.getUsername() + "' and item='" + i.getName() + "'";
            Statement st = conn.createStatement();
            st.executeUpdate(update);
            st.close();
        }
    }
    
    public void insertItem(Item i) throws AnimalCrossingExceptions, SQLException {//Insert user
        if (existsItem(i)) {
            throw new AnimalCrossingExceptions("ERROR: Item exists with these name");
        }
        String insert = "insert into item values (?, ?, ?, ?, ?);";
        PreparedStatement ps = conn.prepareStatement(insert);
        ps.setString(1, i.getName());
        ps.setDouble(2, i.getPrice());
        ps.setDouble(3, i.getSalePrice());
        ps.setString(4, i.getType());
        ps.setString(5, i.getStyle());
        ps.executeUpdate();
        ps.close();
        System.out.println(i.getName() + ": inset succefull");
    }
    
    public void insertCharacter(model.Character c) throws AnimalCrossingExceptions, SQLException {//Insert user
        if (existsCharacter(c)) {
            throw new AnimalCrossingExceptions("ERROR: Character exists with these name");
        }
        String insert = "insert into stucomcrossing.character values (?, ?, ?, ?);";
        PreparedStatement ps = conn.prepareStatement(insert);
        ps.setString(1, c.getName());
        ps.setString(2, c.getStudy());
        ps.setString(3, c.getPlace());
        ps.setString(4, c.getPreference());
        ps.executeUpdate();
        ps.close();
        System.out.println(c.getName() + ": inset succefull");
    }
    
    public void insertUser(User u) throws AnimalCrossingExceptions, SQLException {//Insert user
        if (existsUser(u)) {
            throw new AnimalCrossingExceptions("ERROR: User exists with these name");
        }
        String insert = "insert into user values (?, ?, ?, ?, ?, ?);";
        PreparedStatement ps = conn.prepareStatement(insert);
        ps.setString(1, u.getUsername());
        ps.setString(2, u.getPassword());
        ps.setInt(3, 100);//Default stucomcoin 100
        ps.setInt(4, 0);//Default level 0
        ps.setString(5, u.getPlace());
        ps.setInt(6, 0);//Default points 0
        ps.executeUpdate();
        ps.close();
        System.out.println(u.getUsername() + ": inset succefull");
    }
    
    //-------------------------------------- Selects ------------------------------------>
    private int pointsOfContact(User u, Character c) throws SQLException {//Recogemos los puntos de amistad
        String select = "select points from contact where user='" + u.getUsername() + "' and contact.character ='" + c.getName() + "'";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(select);
        int exist = 0;
        while (rs.next()) {
            exist++;
        }
        rs.close();
        st.close();
        return exist;
    }
    
    public List<Character> charactersNoMeetUser(User userData) throws SQLException{//Miramos los persobajes que aun no conocen un usuario
        String select = "select * from stucomcrossing.character where name not in (select contact.character from contact where user='"+ userData.getUsername() +"')";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(select);
        List<model.Character> character = new ArrayList<>();
        while(rs.next()){
            Character c = new Character();
            c.setName(rs.getString("name"));
            c.setPlace(rs.getString("place"));
            c.setStudy(rs.getString("study"));
            c.setPreference(rs.getString("preference"));
            character.add(c);
        }
        rs.close();
        st.close();
        return character;
    }
    
    public List<TopTen> topTen() throws SQLException{//Listado de top 10
        String select = "select user, count(*) as friends from contact group by user order by count(*) desc limit 10";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(select);
        List<TopTen> list = new ArrayList<>();
        while(rs.next()){
            TopTen t = new TopTen();
            t.setName(rs.getString("user"));
            t.setFriends(rs.getInt("friends"));
            list.add(t);
        }
        rs.close();
        st.close();
        return list;
    }
    
    public List<Item> itemsNoUsed() throws SQLException{//Miramos los items que no estan en uso por ningun usuario
        String select = "select * from item left join inventory on name=item where user is null";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(select);
        List<Item> item = new ArrayList<>();
        while(rs.next()){
            Item i = new Item();
            i.setName(rs.getString("name"));
            i.setPrice(rs.getDouble("price"));
            i.setSalePrice(rs.getDouble("saleprice"));
            i.setStyle(rs.getString("style"));
            i.setType(rs.getString("type"));
            item.add(i);
        }
        rs.close();
        st.close();
        return item;
    }
    
    public model.Character getCharacterByName(String name) throws SQLException, AnimalCrossingExceptions{
        model.Character aux = new model.Character();
        aux.setName(name);
        if(!existsCharacter(aux)){
            throw new AnimalCrossingExceptions("ERROR: No exists any character with these name");
        }
        String select = "select * from stucomcrossing.character where name= '" +name+ "'";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(select);
        model.Character c = new model.Character();
        if(rs.next()){
            c.setName(rs.getString("name"));
            c.setStudy(rs.getString("study"));
            c.setPlace(rs.getString("place"));
            c.setPreference(rs.getString("preference"));
        }
        rs.close();
        st.close();
        return c;
    }
    
     public List<Contact> userFriends(User u) throws SQLException, AnimalCrossingExceptions{//Amigos de un usuario
        String user = u.getUsername();
        String query = "select * from contact where user='"+ user +"'";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(query);
        List<Contact> contact = new ArrayList<>();
        while(rs.next()){
            Contact c = new Contact();
            c.setCharacter(getCharacterByName(rs.getString("character")));
            c.setLevelFrind(rs.getInt("level"));
            c.setPoints(rs.getInt("points"));
            c.setDate(rs.getString("date"));
            contact.add(c);
        }
        rs.close();
        st.close();
        return contact;
    }
    
    public Item getItemByName(String name) throws SQLException, AnimalCrossingExceptions{
        Item aux = new Item();
        aux.setName(name);
        if(!existsItem(aux)){
            throw new AnimalCrossingExceptions("ERROR: No exists any item with these name");
        }
        String select = "select * from item where name = '" +name+ "'";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(select);
        Item i = new Item();
        if(rs.next()){
            i.setName(rs.getString("name"));
            i.setPrice(rs.getDouble("price"));
            i.setStyle(rs.getString("style"));
            i.setType(rs.getString("type"));
            i.setSalePrice(rs.getDouble("saleprice"));
        }
        rs.close();
        st.close();
        return i;
    }
    
     public List<Inventory> userInventory(User u) throws SQLException, AnimalCrossingExceptions{
        String user = u.getUsername();
        //String query = "select * from inventory inner join item on inventory.item = item.name where user='"+ user +"'";
        String query = "select * from inventory inner join item on inventory.item = item.name where user='Paco'";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(query);
        List<Inventory> inventory = new ArrayList<>();
        while(rs.next()){
            Inventory i = new Inventory();
            i.setItem(getItemByName(rs.getString("name")));
            i.setQuantity(rs.getInt("quantity"));
            inventory.add(i);
        }
        rs.close();
        st.close();
        return inventory;
    }
    
    public List<model.Character> charactersFromUserPlace(User u) throws SQLException{//Miramos los personajes que estan en el mismo sitio que un usuairo
        String place = u.getPlace();
        String query = "select * from stucomcrossing.character where place='"+ place +"'";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(query);
        List<model.Character> characters = new ArrayList<>();
        while(rs.next()){
            model.Character c = new model.Character();
            c.setName(rs.getString("name"));
            c.setStudy(rs.getString("study"));
            c.setPlace(rs.getString("place"));
            c.setPreference(rs.getString("preference"));
            characters.add(c);
        }
        rs.close();
        st.close();
        return characters;
    }
    
    public User getUserByUsername(String username) throws SQLException, AnimalCrossingExceptions{
        User aux = new User();
        aux.setUsername(username);
        if(!existsUser(aux)){
            throw new AnimalCrossingExceptions("ERROR: No exists any user with these username");
        }
        String select = "select * from user where username = '" +username+ "'";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(select);
        User u = new User();
        if(rs.next()){
            u.setUsername(rs.getString("username"));
            u.setPassword(rs.getString("password"));
            u.setStucoins(rs.getInt("stucoins"));
            u.setLevel(rs.getInt("level"));
            u.setPlace(rs.getString("place"));
            u.setPoints(rs.getInt("points"));
        }
        rs.close();
        st.close();
        return u;
    }
    
    //-------------------------------------- Update ------------------------------------->
    public boolean giveItem(User userData, Item itemData, model.Character characterData) throws SQLException, AnimalCrossingExceptions{//Regalamos un item
        if(!existsUser(userData)){
            throw new AnimalCrossingExceptions("ERROR: No exists user with these username");
        }
        if(!existsItem(itemData)){
            throw new AnimalCrossingExceptions("ERROR: No exists item with these username");
        }
        if(!existsCharacter(characterData)){
            throw new AnimalCrossingExceptions("ERROR: No exists character with these username");
        }
        
        if (characterData.getStudy() == itemData.getStyle() && characterData.getPreference() == itemData.getType()) {
            Statement st = conn.createStatement();
            try {
                conn.setAutoCommit(false);
                String update = "update user set stucoins=stucoins + 100 where username ='" + userData.getUsername() + "'";
                String updateInventori;
                if (quantityOfItem(userData, itemData) > 1) {
                    updateInventori = "update inventory set quantity = quantity - 1 where user = '" + userData.getUsername() + "'";
                } else {
                    updateInventori = "delete from inventory where user='" + userData.getUsername() + "' and item='" + itemData.getName() + "'";
                }
                insertContact(userData, characterData);
                characterData.setPlace("casa");
                updateCharacterPlace(characterData);
                st.executeUpdate(update);
                st.executeUpdate(updateInventori);

                conn.commit();
            } catch (SQLException ex) {
                conn.rollback();
                throw new AnimalCrossingExceptions("ERROR: To update " + ex.getMessage() + " data");
            } finally {
                st.close();
                conn.setAutoCommit(true);
            }
        } else {
            throw new AnimalCrossingExceptions("ERROR: Give item to character no match with preference");
        }
        
        return true;
    }
    
    public boolean sellItem(User userData, Item itemData) throws SQLException, AnimalCrossingExceptions{
        if(!existsUser(userData)){
            throw new AnimalCrossingExceptions("ERROR: No exists user with these username");
        }
        if(!existsItem(itemData)){
            throw new AnimalCrossingExceptions("ERROR: No exists item with these username");
        }
        String updateStucoints, updateInventory;

        if (quantityOfItem(userData, itemData) > 1) {
            updateStucoints = "update user set stucoins = stucoins + " + itemData.getSalePrice() + " where username = '" + userData.getUsername() + "'";
            updateInventory = "update inventory set quantity = quantity - 1 where user = '" + userData.getUsername() + "'";
        } else {
            updateStucoints = "update user set stucoins = stucoins + " + itemData.getSalePrice() + " where username = '" + userData.getUsername() + "'";
            updateInventory = "DELETE FROM inventory WHERE user='" + userData.getUsername() + "' and item='" + itemData.getName() + "'";
        }

        Statement st = conn.createStatement();

        try {
            conn.setAutoCommit(false);
            st.executeUpdate(updateStucoints);
            st.executeUpdate(updateInventory);
            conn.commit();
        } catch (SQLException ex) {
            conn.rollback();
            throw new AnimalCrossingExceptions("No se ha podido actualizar los datos " + ex.getMessage());
        } finally {
            st.close();
            conn.setAutoCommit(true);
        }
        return true;
    }
    
    public void updateInventory(User userData, Item itemData) throws SQLException{
        String update = "update inventory set  quantity=quantity+1 where user='"+ userData.getUsername() +"' and item ='"+ itemData.getName() +"'";
        PreparedStatement ps = conn.prepareStatement(update);
        ps.executeUpdate();
        ps.close();
    }
    
    public boolean buyItem(User userData, Item itemData) throws SQLException, AnimalCrossingExceptions{
        if(!existsUser(userData)){
            throw new AnimalCrossingExceptions("ERROR: No exists user with these username");
        }
        if(!existsItem(itemData)){
            throw new AnimalCrossingExceptions("ERROR: No exists item with these username");
        }
       if (userData.getStucoins() < itemData.getPrice()) {
            throw new AnimalCrossingExceptions("ERROR: Item it's too expensive");
        } else {
            Statement st = conn.createStatement();
            try {
                conn.setAutoCommit(false);
                String update = "update user set stucoins = stucoins - " + itemData.getPrice() + " where username = '" + userData.getUsername() + "'";
                st.executeUpdate(update);
                insertIventory(userData, itemData);
                conn.commit();
            } catch (SQLException ex) {
                conn.rollback();
                throw new AnimalCrossingExceptions("ERROR: To update");
            } finally {
                st.close();
                conn.setAutoCommit(true);
            }
        }
        return true;
    }
    
    public void modificatePrice(Item i, double price) throws SQLException, AnimalCrossingExceptions{
        String name = i.getName();
        if(!existsItem(i)){
            throw new AnimalCrossingExceptions("ERROR: No exists character with these username");
        }
        String update = "update item set price='"+ price +"', saleprice='"+ price/2 +"' where name = '" +name+ "'";
        PreparedStatement ps = conn.prepareStatement(update);
        ps.executeUpdate();
        ps.close();
    }
    
    public void modificatePlace(model.Character c, String place) throws SQLException, AnimalCrossingExceptions{
        String name = c.getName();
        if(!existsCharacter(c)){
            throw new AnimalCrossingExceptions("ERROR: No exists character with these username");
        }
        String update = "update stucomcrossing.character set place='"+ place +"' where name = '" +name+ "'";
        PreparedStatement ps = conn.prepareStatement(update);
        ps.executeUpdate();
        ps.close();
    }
    
    public void modificateUser(User u, String password) throws SQLException, AnimalCrossingExceptions{
        String username = u.getUsername();
        if(!existsUser(u)){
            throw new AnimalCrossingExceptions("ERROR: No exists user with these username");
        }
        String update = "update user set password='"+ password +"' where username = '" +username+ "'";
        PreparedStatement ps = conn.prepareStatement(update);
        ps.executeUpdate();
        ps.close();
    }
    
    //------------------------------------ Aux functions --------------------------------> 
    private boolean friend(User u, model.Character c) throws SQLException{
        String query = "select * from contact where user='" + u.getUsername() + "' and contact.character ='" + c.getName() + "'";
        return exist(query);
    }
    
    private int quantityOfItem(User userData, Item itemData) throws SQLException {//devolvemos la cantidad de items
        String query = "select quantity from inventory where user ='" + userData.getUsername() + "' and item ='" + itemData.getName() + "'";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(query);
        int exist = 0;
        while (rs.next()) {
            exist++;
        }
        rs.close();
        st.close();
        return exist;
    }
    
    private boolean exist(String query) throws SQLException {//Miramos si devuelve algo
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(query);
        boolean exist = false;
        if (rs.next()) {
            exist = true;
        }
        rs.close();
        st.close();
        return exist;
    }
    
    private boolean itemExistInInventory(User u, Item i) throws SQLException {
        String select = "select quantity from inventory where user ='" + u.getUsername() + "' and item ='" + i.getName() + "'";
        return exist(select);
    }
    
    public boolean validateUser(User u) throws SQLException{
        String username = u.getUsername();
        String password = u.getPassword();
        String select = "select * from user where username = '"+ u.getUsername() +"' and password = '"+ u.getPassword() +"'";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(select);
        boolean correct = false;
        if (rs.next()) {
            correct = true;
        }
        rs.close();
        st.close();
        return correct;
    }
    
     private boolean existsCharacter(model.Character c) throws SQLException {//Verificate item exists
        String select = "select * from stucomcrossing.character where name='" + c.getName()+ "'";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(select);
        boolean exists = false;
        if (rs.next()) {
            exists = true;
        }
        rs.close();
        st.close();
        return exists;
    }
    
    private boolean existsItem(Item i) throws SQLException {//Verificate item exists
        String select = "select * from item where name='" + i.getName()+ "'";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(select);
        boolean exists = false;
        if (rs.next()) {
            exists = true;
        }
        rs.close();
        st.close();
        return exists;
    }
    
    private boolean existsUser(User u) throws SQLException {//Verificate user exists
        String select = "select * from user where username='" + u.getUsername()+ "'";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(select);
        boolean exists = false;
        if (rs.next()) {
            exists = true;
        }
        rs.close();
        st.close();
        return exists;
    }
    
    // ----------------------------- Conectar / Desconectar ----------------------------->
    public void conect() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/stucomcrossing";
        String user = "root";
        String pass = "root";
        conn = DriverManager.getConnection(url, user, pass);
    }

    public void desconect() throws SQLException {
        if (conn != null) {
            conn.close();
        }
    }
    //---------------------------------------------------------------------------------->
}
