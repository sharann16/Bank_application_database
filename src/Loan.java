import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.LinkedList;
import java.util.Scanner;

public class Loan {
    static Scanner sc=new Scanner(System.in);
    static BankRepo bank=new BankRepo();

    public Loan( String userId, Connection con) {
        this.userId = userId;
        this.con=con;


    }
//    static int bal=0;
    private  static Connection con=null;
   static String userId;
    private static double mi;
    public static long annual_I=0;//*
    private static double check;
    private static double checkper;
    private static double totalDepts;
    private static double lamount;//*
    private  static double totallaonamt;//*

    static long anilc=0;
        public static double loanAmount(){
            anilc=bank.anInc(userId);
            annual_I = 0;
            mi = anilc / 12;
            check = (totalDepts / mi) * 100;
            checkper = check / 100;
            lamount = Math.round(anilc*0.5+(anilc * checkper));
            return lamount;
        }
    public static void loan() {

lamount=loanAmount();
        System.out.println("====================================================================================================================================");
//        System.out.println("\t\t\t\t\t\t\t AccountNumber: " + Loan.bankAccount.userDetail.get(Loan.userId).getaccountNumber() + "\t\t\t\t\t\t\t AccountType: " + Loan.bankAccount.userDetail.get(Loan.userId).getaccountType());
        System.out.println("====================================================================================================================================");
        annual_I=bank.anInc(userId);
        totalDepts=bank.sumdepo(userId);
        double lamount = loanAmount();
        System.out.println("your loan eligible  amount    " + lamount);
        System.out.println("your INTEREST 8%\n\n");
        System.out.println("would   you like apply  for loan?\n");
        System.out.println("1) apply loan \t2) dashboard");


        while (true){
            if(!sc.hasNextInt()){
            System.out.println("enter the valid option");
            sc.next();
            continue;
            }
            int check  = sc.nextInt();
            switch (check){
                case 1:
                    passcheck(userId);
                    break;
                case 2:
                    Transaction  trans =  new Transaction();
                    trans.main(userId);
                    break;
                default:
                    loan();
            }
        }
    }

    //password checking method for applying loan
    private static void  passcheck(String userid) {
        sc.nextLine();
        while(true) {
            System.out.println("Enter your password");
            String pass2 = sc.nextLine();
            String depas=bank.passCheck(userid);
            if (depas.equals(pass2)) {
                System.out.println("Password match, proceeding to loan");
                loan_amount_add();
                break;
            } else {
                System.out.println("Invalid password");
                pass2 = "";
            }
        }
    }
    // set loan amount by getting amount from user method
    private static void loan_amount_add() {
           double existLoan=bank.loanamount(userId);
           if(existLoan!=0){
               System.out.println("you already hade a loan");
               Transaction tr=new Transaction();
               tr.main(userId);
           }

            while (true){
                System.out.println("enter an  amount");
                if(!sc.hasNextDouble()){
                    sc.next();
                    continue;
                }
                double l_amount_add = sc.nextDouble();

                if (l_amount_add>=0 && l_amount_add<=lamount ){
                    double interest=l_amount_add*0.08;
                    totallaonamt=interest+l_amount_add;
                    bank.loanIns(userId,l_amount_add);
                    Date dt = new Date();
                    SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
                    String farmatted=df.format(dt);
                    LocalTime time = LocalTime.now();
                    DateTimeFormatter formatter24Hour = DateTimeFormatter.ofPattern("HH:mm:ss");
                    String formattedTime24Hour = time.format(formatter24Hour);
                    int balance=bank.balanceamount(userId);
                    bank.inserttotalloan(farmatted,userId,balance,formattedTime24Hour,totallaonamt);
                    Transaction  trans =  new Transaction();
                    trans.main(userId);
                    break;
                }
                else {
                    System.out.println("Invalid  amount");
                    loan();
                }
            }

    }
}
