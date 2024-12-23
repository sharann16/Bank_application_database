import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class ExistingUser {
    Scanner sc=new Scanner(System.in);
    //getting the bankAccount object from main
    public void existinguser( ){
        String userid;
        while(true){
            System.out.println("Enter your user id");
            userid = sc.nextLine();
            BankRepo check=new BankRepo();
            Boolean alr=check.userCheck(userid);
            if(alr){
                    break;
                }else {
                    System.out.println("invalid UserId ReEnter your userid");
                    userid="";
                }
        }
        while(true){
            System.out.println("Enter your password");
            String pass2 = sc.nextLine();
            BankRepo passw=new BankRepo();
            String depas=passw.passCheck(userid);
            if(depas.equals(pass2)){
                    Transaction trans = new Transaction();
                    trans.main(userid);
                    break;
                }else{
                    System.out.println("Invalid password");
                    pass2="";
                }



        }
    }
}
