package derbydb;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Db {
    final String JDBC_DRIVER="org.apache.derby.jdbc.EmbeddedDriver";
    final String URL="jdbc:derby:sampleDB;create=true";
    //Külső adatbázisszerverhez kella username és a password is:
   // final String USERNAME="";
   // final String PASSWORD="";
    
    //Létrehozzuk a kapcsolatot
    Connection conn= null;
    //Szállító beállítása nuu-ra
    Statement createStatement = null;
    //Adatbázis létezés vizsgálat null-ra
    DatabaseMetaData dbmd=null;
    
    
    public Db(){
      //Kapcsolat életre keltése  
        try {
        //Külső adatbázisszerverhez kella username és a password is:
        // Connection conn= DriverManager.getConnection(URL,USERNAME,PASSWORD);
            conn= DriverManager.getConnection(URL);
            System.out.println("Sikeres");
        } catch (SQLException ex) {
            System.out.println("Baj van a kapcsolattal");
            System.out.println(""+ex);           
        }
        
       //Ha van kapcsolat, akkor itt létrehozunk egy szállítót
        if (conn!=null){
            try {
                createStatement= conn.createStatement();
            } catch (SQLException ex) {
                System.out.println("Baj van a kapcsolattal");
                System.out.println(""+ex);   
            }
        }
        
        //Üres-e az adatbázis?
        try {
            dbmd = conn.getMetaData();
        } catch (SQLException ex) {
            Logger.getLogger(Db.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Baj van a kapcsolattal");
        }
        //Resultset: visszaérkeznek az eredmények listaszerűen
        //létezik-e USERS tábla az adatbázisban?
        try{
        ResultSet rs1=dbmd.getTables(null, "APP", "USERS", null);
        //Ha nincs első rekordja sem, akkor létrehozzuk a createStatement. execute-val
        if (!rs1.next())
        {
            createStatement.execute("create table users(name varchar(20), age varchar(20))");
            System.out.println("Tábla létrehozva");
        }
        } catch(SQLException ex){
            System.out.println("Baj van a create-al");
            System.out.println(""+ex);
        }  
    }
    
    public void addUser(String name , String age){
        try {
            String feltolt="insert into USERS values ('"+name +"','"+age+"')";
            createStatement.execute(feltolt);
            System.out.println("Első típusú adatbázis felvitel");
        } catch (SQLException ex) {
            System.out.println("Baj van");
            System.out.println(""+ex);
        }
    }
    
    public void masikAddUser(String name,String age){
        try {
            String feltoltps="insert into USERS values (?,?)";
            PreparedStatement preparedStatement;
            preparedStatement = conn.prepareStatement(feltoltps);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, age);
            preparedStatement.execute();
            System.out.println("Másik típusú adatbázis felvitel");
        }
        catch (SQLException ex) {
            System.out.println("Baj van az insert-el");
            System.out.println(""+ex);
        }              
    }  
    
    public void lekerdezes(){
        String lekerdez="select * from USERS";
        try {
            ResultSet rs2= createStatement.executeQuery(lekerdez);
            while(rs2.next()){
                String name=rs2.getString("name");
                String age= rs2.getString("age");
                System.out.println("Név: " + name + "\n"+ "Kor: " + age);
            }
        } catch (SQLException ex) {
           System.out.println("Baj van a lekérdezéssel");
            System.out.println(""+ex);
        }
    }
    
    //Attributumok kiíratása
    public void attributumMetaAdat(){
        String lekerdez = "select * from USERS";
        ResultSet rs3= null;
        ResultSetMetaData rsmd=null;
        try {
            rs3=createStatement.executeQuery(lekerdez);
            rsmd=rs3.getMetaData();
            int columnCount = rsmd.getColumnCount();
            for(int x=1; x<=columnCount; x++){
                System.out.println("|" + rsmd.getColumnName(x) + "|");
            }
        } catch (SQLException ex) {
            System.out.println("Baj van a lekérdezéssel");
            System.out.println(""+ex);
        }
    }
    
    public ArrayList<User> osszesUser(){
          String lekerdez="select * from USERS";
          ArrayList<User> users =null;
        try {
            ResultSet rs2= createStatement.executeQuery(lekerdez);
            users = new ArrayList<>();
            while(rs2.next()){
                User actualUser=new User(rs2.getString("name"),rs2.getString("age"));
                users.add(actualUser);
            }
        } catch (SQLException ex) {
           System.out.println("Baj van a lekérdezéssel");
            System.out.println(""+ex);
        }
        return users;
    }
    
    public void hozzaAd (User users){
        try {
            String feltoltps="insert into USERS values (?,?)";
            PreparedStatement preparedStatement;
            preparedStatement = conn.prepareStatement(feltoltps);
            preparedStatement.setString(1, users.getName());
            preparedStatement.setString(2, users.getAge());
            preparedStatement.execute();
        }
        catch (SQLException ex) {
            System.out.println("Baj van az insert-el");
            System.out.println(""+ex);
        }
    } 
}
