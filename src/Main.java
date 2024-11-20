import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.*;
import java.time.format.DateTimeFormatter;
import java.sql.*;
public  class Main{
    public static void main(String[] args){
        Scanner sc=new Scanner(System.in);
        //creating object for BamkAccount

        String path="jdbc:mysql://localhost:3306/bank";
        String username="root";
        String passw="0000";
        Connection con = null;
        try{
            con=DriverManager.getConnection(path,username,passw);
            System.out.println("sucess");
        }catch (SQLException e){
            System.out.println(e);
        }




         boolean t=true;

        while (t) {
            System.out.println("------------------------------------------------------");
            System.out.println("----------- Console Based - Banking System -----------");
            System.out.println("------------------------------------------------------");
            System.out.println("\nWelcome to Banking");
            System.out.println("1. New User Registration");
            System.out.println("2. Login with User ID");
            System.out.println("3. Exit");
            System.out.println("Enter your option: ");
            int opt=sc.nextInt();
            switch (opt) {
                case 1:
                    //cretung object for New user and pass the bankAccount object to the generate new user method
                    NewUser newuser=new NewUser();
                    newuser.generateNewUser(con);
                    break;
                case 2:
                    //cretung object for Existinguser and pass the bankAccount object to the existinguser method
                    ExistingUser existing=new ExistingUser();
                    existing.existinguser(con);
                    break;
            }
        }
    }
}