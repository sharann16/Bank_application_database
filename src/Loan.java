import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.LinkedList;
import java.util.Scanner;

public class Loan {
    static Scanner sc=new Scanner(System.in);

    public Loan( String userId, Connection con,int bal) {
        this.userId = userId;
        this.con=con;


    }
    static int bal=0;
    private  static Connection con=null;
    private static String userId;
    private static double mi;
    public static long annual_I=0;//*
    private static double check;
    private static double checkper;
    private static double totalDepts;
    private static double lamount;//*
    private  static double totallaonamt;//*


    static long anilc=0;
        public static double loanAmount(){

            String anualinc="select annual_amount from userdetail where user_id=?";
            try{
                PreparedStatement st=con.prepareStatement(anualinc);
                st.setString(1,userId);
                ResultSet re=st.executeQuery();
                re.next();
                anilc =re.getInt("annual_amount");
            }catch(SQLException e){
                System.out.println(e);
            }
            annual_I = 0;
            mi = anilc / 12;
            check = (totalDepts / mi) * 100;
            checkper = check / 100;
            lamount = Math.round(anilc*0.5+(anilc * checkper));
            return lamount;

        }


    public static void loan( String userId) {



lamount=loanAmount();

        System.out.println("====================================================================================================================================");
//        System.out.println("\t\t\t\t\t\t\t AccountNumber: " + Loan.bankAccount.userDetail.get(Loan.userId).getaccountNumber() + "\t\t\t\t\t\t\t AccountType: " + Loan.bankAccount.userDetail.get(Loan.userId).getaccountType());
        System.out.println("====================================================================================================================================");
        String anualinc="select annual_amount from userdetail where user_id=?";
        String depsum="select sum(deposite) from usertransaction where user_id=?";
        //foe income
        try{
            PreparedStatement st=con.prepareStatement(anualinc);
            st.setString(1,userId);
            ResultSet re=st.executeQuery();
            re.next();
            annual_I =re.getInt("annual_amount");
        }catch(SQLException e){
            System.out.println(e);
        }
        //for dep sum
        try{
            PreparedStatement st=con.prepareStatement(depsum);
            st.setString(1,userId);
            ResultSet re=st.executeQuery();
            re.next();
            totalDepts=re.getInt("sum(deposite)");
        } catch (SQLException e) {
            System.out.println(e);
        }
        double lamount = loanAmount();

        System.out.println("your loan eligible  amount    " + lamount);
        System.out.println("your INTEREST 8%\n\n");
        System.out.println("would   you like apply  for loan?\n");
        System.out.println("1) apply loan \t2) dashboard");

        int check  = sc.nextInt();

        while (true){
            switch (check){
                case 1:
                    passcheck(userId);
                    break;
                case 2:
                    Transaction  trans =  new Transaction(con);
                    trans.main(userId);
                    break;
                default:
                    loan(userId);

            }
        }


    }


    //password checking method for applying loan
    private static void  passcheck(String userid) {
        sc.nextLine();
        while(true) {
            System.out.println("Enter your password");
            String pass2 = sc.nextLine();

            String checkpass = "select user_password from  userdetail where user_id=?";
            try {
                PreparedStatement st = con.prepareStatement(checkpass);
                st.setString(1, userid);
                ResultSet res = st.executeQuery();
                res.next();
                String depas = res.getNString("user_password");
                if (depas.equals(pass2)) {
                    System.out.println("Password match, proceeding to loan");
                    loan_amount_add();
                    break;
                } else {
                    System.out.println("Invalid password");
                    pass2 = "";
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }



    }
    // set loan amount by getting amount from user method
    private static void loan_amount_add() {
        System.out.println("enter an  amount");
        double l_amount_add = sc.nextDouble();

        if (l_amount_add>=0 && l_amount_add<=lamount ){
            double interest=l_amount_add*0.08;
            totallaonamt=interest+l_amount_add;





//
            String insLoanAmt="UPDATE userdetail SET loanamount = ? WHERE user_id = ?";
            String insertTotalLoanAmount="INSERT INTO usertransaction (created_date, user_id, balance, timee, loan_balance) VALUES (?, ?, ?, ?, ?);";
            try{
                PreparedStatement st=con.prepareStatement(insLoanAmt);
                st.setInt(1, (int) l_amount_add);
                st.setString(2,userId);
                st.executeUpdate();
            }catch(Exception e){
                System.out.println(e);
            }
            Date dt = new Date();
            SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
            String farmatted=df.format(dt);

            LocalTime time = LocalTime.now();
            DateTimeFormatter formatter24Hour = DateTimeFormatter.ofPattern("HH:mm:ss");
            String formattedTime24Hour = time.format(formatter24Hour);
            String ba="SELECT balance FROM usertransaction WHERE user_id = ? ORDER BY created_date DESC, timee DESC LIMIT 1\n";

            try{
                PreparedStatement st1=con.prepareStatement(ba);
                st1.setString(1,userId);
                ResultSet re=st1.executeQuery();
                re.next();
                int bal=re.getInt("balance");
                PreparedStatement st=con.prepareStatement(insertTotalLoanAmount);
                st.setDate(1, java.sql.Date.valueOf(farmatted));
                st.setString(2,userId);
                st.setInt(3,bal);
                st.setTime(4, Time.valueOf(formattedTime24Hour));
                st.setInt(5, (int) totallaonamt);
                st.executeUpdate();

            }catch(Exception e){
                System.out.println(e);
            }





            Transaction  trans =  new Transaction(con);
            trans.main(userId);


        }
        else {
            System.out.println("Invalid  amount");
            loan(userId);
        }


    }




}
