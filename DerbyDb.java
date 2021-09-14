package derbydb;

import java.util.ArrayList;

public class DerbyDb {

    public static void main(String[] args) {
       Db db= new Db();
      // db.addUser("Tibi", "50");
      // db.masikAddUser("Zsuzsi", "51");
      // db.lekerdezes();
       //db.attributumMetaAdat();
       // System.out.println(db.osszesUser());
       ArrayList<User> users = db.osszesUser();
       for(User u : users){
           System.out.println(u.getName()+": "+u.getAge());
       }
       User user= new User();
       user.setName("Kicsi");
       user.setAge("46");
       
       db.hozzaAd(user);
    }
    
  
            
}
