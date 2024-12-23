
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Date;

public class Transaction {
    public Connection con=null;


BankRepo bank=new BankRepo();
    Date dt = new Date();
    SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
    String farmatted=df.format(dt);

    LocalTime time = LocalTime.now();
    DateTimeFormatter formatter24Hour = DateTimeFormatter.ofPattern("HH:mm:ss");
    String formattedTime24Hour = time.format(formatter24Hour);
//    int bal=0;

    boolean t=true;
    Scanner sc=new Scanner(System.in);
//
    public double loancheck(String userId){
        long annual_I=bank.anInc(userId);
        long totalDepts=bank.totalDeb(userId);
        double mi = annual_I/12;
        double check = (totalDepts/mi)*100;
        return check;
    }



    //getting the userId from the existinguser
    public void main(String userId){
        int opt = 0;
        while (t) {
            System.out.println("1)Deposite\n2)Withdraw\n3)Balance\n4)transaction\n5)Apply for loan\n6)Exit");
            if (sc.hasNextInt()) {
                opt = sc.nextByte();
                if (opt > 0 && opt < 8) {
                    break;
                } else {
                    System.out.println("enter a valid number");
                    sc.next();
                }

            } else {
                System.out.println("enter a number");
                sc.next();
            }
        }
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
                    if (percentage>=35){
                        System.out.println("sucesssss");
                        Loan l = new Loan(userId,con);
                        l.loan();
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
    public void deposite(String userId){

        long loanamt=bank.loanBalance(userId);
        double amt=0;
        while(true){
            try{
                //getting the related userid values from hashmap and store it in the userDetail .
                System.out.println("Enter amount to deposite");
                amt=sc.nextDouble();
                if(amt>0){
                        int balance = bank.balanceamount(userId);
                        int newbal = (int) (balance + amt);
                        bank.deposite(farmatted, userId, amt, newbal, formattedTime24Hour, loanamt);
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
        long loanamt=bank.loanBalance(userId);
        //getting the related userid values from hashmap and store it in the userDetail .

        while (true){
            System.out.println("enter amount multiple of 100");
            if(!sc.hasNextDouble()){
                System.out.println("enter the valid amount");
                sc.next();
                continue;
            }
            double amt= sc.nextDouble();
                if(amt>0){
                        int balance= bank.balanceamount(userId);
                        int newbal= (int)(balance-amt);
                        bank.withdraw(farmatted,userId,amt,newbal,formattedTime24Hour,loanamt);
                        break;
                }else {
                    System.out.println("Enter a valid amount to withdraw");
                }
        }
    }
    public void balance(String userId){
        System.out.println(bank.balanceamount(userId));
    }
    public void transaction(String usseId){
        bank.transactio(usseId);
    }

    public void profile(String userId){

        System.out.println("====================================================================================================================================");
        System.out.println("************************************************************* PROFILE **************************************************************");
//        System.out.println("\t\t\t\t\t\t\t AccountNumber: " + bankAccount.userDetail.get(userId).getaccountNumber()+ "\t\t\t\t\t\t\t AccountType: " +bankAccount.userDetail.get(userId).getaccountType()+"\n");
//        System.out.println("\t\t\t\t\t\t\t User Name:     " + bankAccount.userDetail.get(userId).getuserName()+ "\t\t\t\t\t\t\t  DOB :" + bankAccount.userDetail.get(userId).getdob()+"\n");
//        System.out.println("\t\t\t\t\t\t\t User ID:     " + bankAccount.userDetail.get(userId).getuserId()+ "\t\t\t\t\t\t\t  Phone No :" + bankAccount.userDetail.get(userId).getPhno()+"\n");
//        System.out.println("\n\n\t\t\t\t\t\t\t Balance: " + bankAccount.userDetail.get(userId).getbalance() + "\t\t\t\t\t\t\t\t Borrowings: " + bankAccount.userDetail.get(userId).getLoanamount() +"\n");
        System.out.println("====================================================================================================================================");
        int check;
        while (true){
            System.out.println("\n1)Loan EMI\t 2)dashboard");
            if (sc.hasNextInt()){
                check = sc.nextInt();
                if(check==1||check==2){
                    break;
                }else {
                    System.out.println("Please enter a valid option (1 or 2).");
                }
            }else {
                System.out.println("enter a number");
            sc.next();            }
        }

        switch (check){
            case 1:
                long loanBalance=bank.loanBalance(userId);
                Transaction t=new Transaction();
                double monthlyemi = t.monthlyEMI(userId);
                int balance=bank.balanceamount(userId);
                if(loanBalance>0){
                    System.out.println("============================your monthly EMI for 12 month============================");
                    System.out.println(monthlyemi);
                    double amt;
                    while (true){
                        System.out.println("pay your EMI");
                        if(sc.hasNextInt()){
                            amt = sc.nextDouble();
                            break;
                        }else {
                            System.out.println("enter a valid amount");
                            sc.next();
                        }

                    }
                    if(amt>=monthlyemi){
                        double extraamount = amt - monthlyemi;// if customer has extra amount
                        amt=amt-extraamount;
                        long newbal= (long) (loanBalance-amt);
                        bank.loanamoins(farmatted,userId,balance,formattedTime24Hour,amt,newbal);
                        long loanbalafter=bank.loanBalance(userId);
                        if(loanbalafter==0){
                            bank.resetLoanamt(userId);
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
    public double monthlyEMI(String userId){
        long loanamt=bank.mainLoanAmt(userId);
        System.out.println(loanamt);
        double totalemi =loanamt+(loanamt*0.08) ;
        double monthlyemi = totalemi/12;
        return monthlyemi;
    }
}
