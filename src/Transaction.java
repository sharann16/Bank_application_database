
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Date;

public class Transaction {
    public Connection con=null;

    //creating construstor  getting the bank account object from the existing user
    Transaction(Connection con){
        this.con=con;
    }

    Date dt = new Date();
    SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
    String farmatted=df.format(dt);

    LocalTime time = LocalTime.now();
    DateTimeFormatter formatter24Hour = DateTimeFormatter.ofPattern("HH:mm:ss");
    String formattedTime24Hour = time.format(formatter24Hour);
    int bal=0;

    boolean t=true;
    Scanner sc=new Scanner(System.in);
    String deposite="insert into usertransaction (created_date,user_id,deposite,balance,timee,loan_balance) values(?,?,?,?,?,?)";
    String withdraw="insert into usertransaction (created_date,user_id,withdraw,balance,timee,loan_balance) values(?,?,?,?,?,?)";

    String ba="SELECT balance FROM usertransaction WHERE user_id = ? ORDER BY created_date DESC, timee DESC LIMIT 1\n";
    String loanbalcnce="SELECT loan_balance FROM usertransaction WHERE user_id = ? ORDER BY created_date DESC, timee DESC LIMIT 1";
    public double loancheck(String userId){
        String anualinc="select annual_amount from userdetail where user_id=?";
        String depsum="select sum(deposite) from usertransaction where user_id=?";
        try{
            PreparedStatement st=con.prepareStatement(ba);
            st.setString(1,userId);
            ResultSet re=st.executeQuery();
            re.next();
            bal=re.getInt("balance");
        } catch (SQLException e) {
            System.out.println(e);
        }




        //foe income
        long annual_I=0;
        long totalDepts=0;
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

        double mi = annual_I/12;
        double check = (totalDepts/mi)*100;
        return check;
    }



    //getting the userId from the existinguser
    public void main(String userId){
        while (t){
            System.out.println("1)Deposite\n2)Withdraw\n3)Balance\n4)transaction\n5)Apply for loan\n6)Exit");
            int opt=sc.nextByte();
            switch (opt){
                case 1:
                    deposite(userId);
                    break;
                case 2:
                    withdraw(userId);
                    break;
                case 3:
                    balance(userId);
                    break;
                case 4:
                    transaction(userId);
                    break;
                case 5:
                    //passing the bank account object and user id to the loan class

                    double percentage = loancheck(userId);
//                    System.out.println(percentage); used  for debugging
                    if (percentage>35){
                        Loan l = new Loan(userId,con,bal);
                        l.loan(userId);
                    }
                    else {
                        System.out.println("\t\t\t\t\t\t\t low CREDIT SCORE /  you already avail the LOAN SERVICE");
                    }
                    break;
                case 6:
                    profile(userId);
                case 7:
                    t=false;
            }
        }
    }
    public void deposite(String userId){
        long loanamt = 0;
        try{
            PreparedStatement st=con.prepareStatement(loanbalcnce);
            st.setString(1,userId);
            ResultSet re=st.executeQuery();
            re.next();
            loanamt=re.getInt("loan_balance");
        }catch (SQLException e){
            System.out.println(e);
        }
        double amt=0;

        while(true){
            try{

                //getting the related userid values from hashmap and store it in the userDetail .
                System.out.println("Enter amount to deposite");
                amt=sc.nextDouble();
                if(amt>0){
                    try{
                        PreparedStatement st=con.prepareStatement(ba);
                        st.setString(1,userId);
                        ResultSet re=st.executeQuery();
                        re.next();
                        bal=re.getInt("balance");
                        int newbal= (int) (bal+amt);
                        PreparedStatement stForIns=con.prepareStatement(deposite);
                        stForIns.setDate(1, java.sql.Date.valueOf(farmatted));
                        stForIns.setString(2,userId);
                        stForIns.setInt(3, (int) amt);
                        stForIns.setInt(4,newbal);
                        stForIns.setTime(5, Time.valueOf(formattedTime24Hour));
                        stForIns.setLong(6,loanamt);
                        stForIns.executeUpdate();


                    } catch (SQLException e) {
                        System.out.println(e);
                    }

                    break;
                }else{
                    amt=0;
                    System.out.println("*Enter a valid amount to deposite*");
                }

            }catch (InputMismatchException e){
                System.out.println("Enter a valid Amount");
                sc.next();
            }
        }
    }
    public void withdraw(String userId){
        long loanamt = 0;
        try{
            PreparedStatement st=con.prepareStatement(loanbalcnce);
            st.setString(1,userId);
            ResultSet re=st.executeQuery();
            re.next();
            loanamt=re.getInt("loan_balance");
        }catch (SQLException e){
            System.out.println(e);
        }

        //getting the related userid values from hashmap and store it in the userDetail .
        double amt;
        while (true){
            System.out.println("enter amount multiple of 100");
            amt= sc.nextDouble();

                if(amt>0){
                    try{
                        PreparedStatement st=con.prepareStatement(ba);
                        st.setString(1,userId);
                        ResultSet re=st.executeQuery();
                        re.next();
                        int nbal=re.getInt("balance");
                        int newbal= (int)(nbal-amt);
                        PreparedStatement stForIns=con.prepareStatement(withdraw);
                        stForIns.setDate(1, java.sql.Date.valueOf(farmatted));
                        stForIns.setString(2,userId);
                        stForIns.setInt(3, (int) amt);
                        stForIns.setInt(4,newbal);
                        stForIns.setTime(5, Time.valueOf(formattedTime24Hour));
                        stForIns.setLong(6,loanamt);
                        stForIns.executeUpdate();
                        break;

                    } catch (SQLException e) {
                        System.out.println(e);
                    }
                }else {
                    System.out.println("Enter a valid amount to withdraw");
                }
        }


    }
    public void balance(String userId){
        try{
            PreparedStatement st=con.prepareStatement(ba);
            st.setString(1,userId);
            ResultSet res=st.executeQuery();
            res.next();
            long bal=res.getInt("balance");
            System.out.println("Balance : " +bal);
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
    public void transaction(String usseId){
        String trans="select created_date,deposite,withdraw,balance from usertransaction where user_id=?";
        try{
            PreparedStatement st=con.prepareStatement(trans);
            st.setString(1,usseId);
            ResultSet res=st.executeQuery();
            while (res.next()){
                double bal=res.getInt("balance");
                Date dt=res.getDate("created_date");
                double dep=res.getDouble("deposite");
                double withd=res.getDouble("withdraw");


                System.out.println("DATE : "+dt+"   DEPOSITE : "+dep+"    WIDTHDRAW : "+withd+"    BALANCE : "+bal);
            }
        }catch (SQLException e){
            System.out.println(e);
        }

    }

    public void profile(String userId){

        System.out.println("====================================================================================================================================");
        System.out.println("************************************************************* PROFILE **************************************************************");
//        System.out.println("\t\t\t\t\t\t\t AccountNumber: " + bankAccount.userDetail.get(userId).getaccountNumber()+ "\t\t\t\t\t\t\t AccountType: " +bankAccount.userDetail.get(userId).getaccountType()+"\n");
//        System.out.println("\t\t\t\t\t\t\t User Name:     " + bankAccount.userDetail.get(userId).getuserName()+ "\t\t\t\t\t\t\t  DOB :" + bankAccount.userDetail.get(userId).getdob()+"\n");
//        System.out.println("\t\t\t\t\t\t\t User ID:     " + bankAccount.userDetail.get(userId).getuserId()+ "\t\t\t\t\t\t\t  Phone No :" + bankAccount.userDetail.get(userId).getPhno()+"\n");
//        System.out.println("\n\n\t\t\t\t\t\t\t Balance: " + bankAccount.userDetail.get(userId).getbalance() + "\t\t\t\t\t\t\t\t Borrowings: " + bankAccount.userDetail.get(userId).getLoanamount() +"\n");
        System.out.println("====================================================================================================================================");
        System.out.println("\n1)Loan EMI\t 2)dashboard");
        int check = sc.nextInt();
        switch (check){
            case 1:
                String totloan="SELECT loan_balance FROM usertransaction WHERE user_id = ? ORDER BY created_date DESC, timee DESC LIMIT 1";
                long lbal=0;
                try{
                    PreparedStatement st= con.prepareCall(totloan);
                    st.setString(1,userId);
                    ResultSet re= st.executeQuery();
                    re.next();
                    lbal=re.getInt("loan_balance");
                }catch(SQLException e){
                    System.out.println(e);
                }





                double monthlyemi = monthlyEMI(userId,con);
                String ba="SELECT balance FROM usertransaction WHERE user_id = ? ORDER BY created_date DESC, timee DESC LIMIT 1\n";
                try{
                    PreparedStatement st=con.prepareStatement(ba);
                    st.setString(1,userId);
                    ResultSet re=st.executeQuery();
                    re.next();
                    bal=re.getInt("balance");
                } catch (SQLException e) {
                    System.out.println(e);
                }
                if(lbal>0){
                    System.out.println("============================your monthly EMI for 12 month============================");
                    System.out.println(monthlyemi);
                    System.out.println("pay your EMI");
                    double amt = sc.nextDouble();
                    if(amt>=monthlyemi){

                        double extraamount = amt - monthlyemi;// if customer has extra amount
                        amt=amt-extraamount;
                        long newbal= (long) (lbal-amt);
                        String insertloan="insert into usertransaction (created_date,user_id,balance,timee,emi,loan_balance) values(?,?,?,?,?,?)";


                        try{
                            PreparedStatement st=con.prepareStatement(insertloan);
                            st.setDate(1, java.sql.Date.valueOf(farmatted));
                            st.setString(2,userId);
                            st.setInt(3,bal);
                            st.setTime(4, Time.valueOf(formattedTime24Hour));
                            st.setInt(5, (int) amt);
                            st.setInt(6,(int)newbal);
                            st.executeUpdate();

                        }catch (SQLException e){
                            System.out.println(e);
                        }

                        String totloanafter="SELECT loan_balance FROM usertransaction WHERE user_id = ? ORDER BY created_date DESC, timee DESC LIMIT 1";
                        long lbalafter=0;
                        try{
                            PreparedStatement st= con.prepareCall(totloanafter);
                            st.setString(1,userId);
                            ResultSet re= st.executeQuery();
                            re.next();
                            lbalafter=re.getInt("loan_balance");
                        }catch(SQLException e){
                            System.out.println(e);
                        }
                        if(lbalafter==0){
                            String loanAmountReset="update userdetail set loanamount=0 where user_id=?";
                            try{
                                PreparedStatement st=con.prepareStatement(loanAmountReset);
                                st.setString(1,userId);
                                st.executeUpdate();
                            }catch (SQLException e){
                                System.out.println(e);
                            }
                        }




                        System.out.println("your change  :" + extraamount);
                        System.out.println("============================you successfully Paid your EMI ============================\n");
                        System.out.println("============================your loan amount============================\n");

                    }

                    else {
                        System.out.println("Insufficient amount");
                        profile(userId);
                    }
                }
                else {
                    System.out.println("NO loan on your account\n\n");
                }
            case 2:
                main(userId);

        }



    }
    static double monthlyEMI(String userId,Connection con){
        long loanamt=0;
        String la="select loanamount from userdetail where user_id=?";
        try{
            PreparedStatement st=con.prepareStatement(la);
            st.setString(1,userId);
            ResultSet re=st.executeQuery();
            re.next();
            loanamt=re.getInt("loanamount");
        }catch(Exception e){
            System.out.println(e);
        }

        System.out.println(loanamt);

        double totalemi =loanamt+(loanamt*0.08) ;
        double monthlyemi = totalemi/12;

        return monthlyemi;
    }

}
