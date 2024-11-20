import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class ExistingUser {
    Scanner sc=new Scanner(System.in);
    //getting the bankAccount object from main
    public void existinguser( Connection con){
        String userid;
        while(true){
            System.out.println("Enter your user id");
            userid = sc.nextLine();
            String userIdCheck="select * from userdetail where user_id=?";
            try{
                PreparedStatement st=con.prepareStatement(userIdCheck);
                st.setString(1,userid);
                ResultSet res=st.executeQuery();
                boolean alr=res.next();
                if(alr){
                    break;
                }else {
                    System.out.println("invalid UserId ReEnter your userid");
                    userid="";
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }
        while(true){
            System.out.println("Enter your password");
            String pass2 = sc.nextLine();

            String checkpass="select user_password from  userdetail where user_id=?";
            try{
                PreparedStatement st= con.prepareStatement(checkpass);
                st.setString(1,userid);
                ResultSet res=st.executeQuery();
                res.next();
                String depas=res.getNString("user_password");
                if(depas.equals(pass2)){
                    Transaction trans = new Transaction(con);
                    trans.main(userid);
                    break;
                }else{
                    System.out.println("Invalid password");
                    pass2="";
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }


        }
    }
}
