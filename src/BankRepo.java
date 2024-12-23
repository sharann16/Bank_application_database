import java.sql.*;
import java.util.Date;

public class BankRepo {
    static Connection con = null;
    static void connection(){
        String path="jdbc:mysql://localhost:3306/bank";
        String username="root";
        String passw="0000";

        try{
            con= DriverManager.getConnection(path,username,passw);
            System.out.println("sucess");
        }catch (SQLException e){
            System.out.println(e);
        }
    }
    public void newUserLogin(String formattedDate, long accNumber, String userId, String passWord, String accountType, String userName, String phoneNumber, double anualIncome, double initialAmount){
        String insert="insert into userdetail (created_Date,account_number,user_id,user_password,account_type,user_name,phone_number,annual_amount) values(?,?,?,?,?,?,?,?)";
        try{
            PreparedStatement st= con.prepareStatement(insert);
            st.setDate(1, java.sql.Date.valueOf(formattedDate));
            st.setInt(2, (int)accNumber);
            st.setString(3,userId);
            st.setString(4,passWord);
            st.setString(5,accountType);
            st.setString(6,userName);
            st.setLong(7, Long.parseLong(phoneNumber));
            st.setInt(8, (int) anualIncome);
            st.executeUpdate();
        }catch (SQLException e){
            System.out.println(e);
        }

        String trans="insert into usertransaction (created_Date,user_id,deposite,balance) values (?,?,?,?)";
        try{
            PreparedStatement bal=con.prepareStatement(trans);
            bal.setDate(1, java.sql.Date.valueOf(formattedDate));
            bal.setString(2,userId);
            bal.setInt(3, (int) initialAmount);
            bal.setInt(4, (int) initialAmount);
            bal.executeUpdate();
        }catch (SQLException e){
            System.out.println(e);
        }
    }




    public  boolean userCheck(String userid){
        String userIdCheck="select * from userdetail where user_id=?";
        try{
            PreparedStatement st=con.prepareStatement(userIdCheck);
            st.setString(1,userid);
            ResultSet res=st.executeQuery();
            boolean alr=res.next();
            return alr;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }


    public  String passCheck(String userid){
        String checkpass="select user_password from  userdetail where user_id=?";
        try{
            PreparedStatement st= con.prepareStatement(checkpass);
            st.setString(1,userid);
            ResultSet res=st.executeQuery();
            res.next();
            String depas=res.getNString("user_password");
            return depas;
        } catch (SQLException e) {//
            throw new RuntimeException(e);
        }
    }

    public long loanBalance (String userId){

        String loanbalcnce="SELECT loan_balance FROM usertransaction WHERE user_id = ? ORDER BY created_date DESC, timee DESC LIMIT 1";
        try{
            PreparedStatement st=con.prepareStatement(loanbalcnce);
            st.setString(1,userId);
            ResultSet re=st.executeQuery();
            re.next();
            long loanamt=re.getInt("loan_balance");
            return loanamt;
        }catch (SQLException e){
            System.out.println(e);
        }
        return 0;
    }

    public int balanceamount(String userId){
        String ba="SELECT balance FROM usertransaction WHERE user_id = ? ORDER BY created_date DESC, timee DESC LIMIT 1\n";
        try{
            PreparedStatement st=con.prepareStatement(ba);
            st.setString(1,userId);
            ResultSet re=st.executeQuery();
            re.next();
            int bal=re.getInt("balance");
            return bal;
        }catch (SQLException e){
            System.out.println(e);
        }
        return 0;
    }


    public void deposite(String farmatted, String userId, double amt, int newbal, String formattedTime24Hour, long loanamt){
        String deposite="insert into usertransaction (created_date,user_id,deposite,balance,timee,loan_balance) values(?,?,?,?,?,?)";
        try{
            PreparedStatement stForIns=con.prepareStatement(deposite);
            stForIns.setDate(1, java.sql.Date.valueOf(farmatted));
            stForIns.setString(2,userId);
            stForIns.setInt(3, (int) amt);
            stForIns.setInt(4,newbal);
            stForIns.setTime(5, Time.valueOf(formattedTime24Hour));
            stForIns.setLong(6,loanamt);
            stForIns.executeUpdate();
        }catch (SQLException e){
            System.out.println(e);
        }
    }


    public void withdraw(String farmatted, String userId, double amt, int newbal, String formattedTime24Hour, long loanamt){
        String withdraw="insert into usertransaction (created_date,user_id,withdraw,balance,timee,loan_balance) values(?,?,?,?,?,?)";
        try {
            PreparedStatement stForIns=con.prepareStatement(withdraw);
            stForIns.setDate(1, java.sql.Date.valueOf(farmatted));
            stForIns.setString(2,userId);
            stForIns.setInt(3, (int) amt);
            stForIns.setInt(4,newbal);
            stForIns.setTime(5, Time.valueOf(formattedTime24Hour));
            stForIns.setLong(6,loanamt);
            stForIns.executeUpdate();
        }catch (SQLException e){
            System.out.println(e);
        }
    }


    public void transactio(String usseId){
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

                System.out.println( "DATE : "+dt+"   DEPOSITE : "+dep+"    WIDTHDRAW : "+withd+"    BALANCE : "+bal);
            }
        }catch (SQLException e){
            System.out.println(e);
        }

    }


    public void loanamoins(String farmatted, String userId, int bal, String formattedTime24Hour, double amt, long newbal){
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
            System.out.println(e);//ig
        }
    }

    public void resetLoanamt(String userId){
        String loanAmountReset="update userdetail set loanamount=0 where user_id=?";
        try{
            PreparedStatement st=con.prepareStatement(loanAmountReset);
            st.setString(1,userId);
            st.executeUpdate();
        }catch (SQLException e){
            System.out.println(e);
        }
    }

    public long mainLoanAmt(String userId){
        String la="select loanamount from userdetail where user_id=?";
        try{
            PreparedStatement st=con.prepareStatement(la);
            st.setString(1,userId);
            ResultSet re=st.executeQuery();
            re.next();
            long loanamt=re.getInt("loanamount");
            return loanamt;
        }catch(Exception e){
            System.out.println(e);
        }
        return 0;
    }

    public long anInc(String userId){
        String anualinc="select annual_amount from userdetail where user_id=?";
        try{
            PreparedStatement st=con.prepareStatement(anualinc);
            st.setString(1,userId);
            ResultSet re=st.executeQuery();
            re.next();
            long annual_I =re.getInt("annual_amount");
            return annual_I;
        }catch(SQLException e){
            System.out.println(e);
        }
        return 0;
    }

    public long totalDeb(String userId){
        String depsum="select sum(deposite) from usertransaction where user_id=?";
        try{
            PreparedStatement st=con.prepareStatement(depsum);
            st.setString(1,userId);
            ResultSet re=st.executeQuery();
            re.next();
            long totalDepts=re.getInt("sum(deposite)");
            return totalDepts;
        } catch (SQLException e) {
            System.out.println(e);
        }
        return 0;
    }
    public double sumdepo(String userId){
        String depsum="select sum(deposite) from usertransaction where user_id=?";
        try{
            PreparedStatement st=con.prepareStatement(depsum);
            st.setString(1,userId);
            ResultSet re=st.executeQuery();
            re.next();
            double totalDepts=re.getInt("sum(deposite)");
            return totalDepts;
        } catch (SQLException e) {
            System.out.println(e);
        }
        return 0;
    }
    public void loanIns(String userId, double l_amount_add){
        String insLoanAmt="UPDATE userdetail SET loanamount = ? WHERE user_id = ?";
        try{
            PreparedStatement st=con.prepareStatement(insLoanAmt);
            st.setInt(1, (int) l_amount_add);
            st.setString(2,userId);
            st.executeUpdate();
        }catch(Exception e){
            System.out.println(e);
        }
    }
    public void inserttotalloan(String farmatted, String userId, int bal, String formattedTime24Hour, double totallaonamt){
        String insertTotalLoanAmount="INSERT INTO usertransaction (created_date, user_id, balance, timee, loan_balance) VALUES (?, ?, ?, ?, ?);";

        try{
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
    }
    public double loanamount(String userId){
        String sql="select loanamount from userdetail where user_id=?";
        try{
            PreparedStatement st=con.prepareStatement(sql);
            st.setString(1,userId);
            ResultSet re=st.executeQuery();
            re.next();
            double loanex=re.getDouble("loanamount");
            return loanex;
        }catch (Exception e){
            System.out.println(e);
        }
        return 0;
    }
}
