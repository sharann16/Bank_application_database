
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.*;

public class NewUser extends  SQLException{




//getting the bank account object from main
    public void generateNewUser(){
        Scanner in = new Scanner(System.in);
        String userName=new String();
        String phoneNumber=new String();
        String dOB=new String();
        String accountType=new String();
        String userId=new String();
        long accNumber;
        String passWord=new String();
        double anualIncome=0;
        double initialAmount;
        String transcation = new String();
        String np = "^[A-Za-z]+$";
        Pattern namePattern = Pattern.compile(np);
        String pp = "^[0-9]{10}$" ;
        Pattern phonepatter = Pattern.compile(pp);
        System.out.println("\n============================================");
        System.out.println("-----------New User Regestritation----------");
        System.out.println("============================================");
        while(true) {
            System.out.println("Enter Your Full Name:");
            String name = in.next();
            Matcher namematch = namePattern.matcher(name);
            if(namematch.matches()) {
                userName=name;
                break;
            }
            else
                System.out.println("Invalid name. Please enter letters only.");
        }

        while(true) {
            System.out.println("Enter Your Mobile number:");
            String phonenumber = in.next();
            Matcher phonematch = phonepatter.matcher(String.valueOf(phonenumber));
            if(phonematch.matches()) {
                phoneNumber=phonenumber;
                break;
            }
            else {
                System.out.println("Invalid Number. Please enter numbers only.");
            }
        }

        while (true) {
            System.out.println("Enter Your Date of Birth (dd/MM/yyyy): ");
            dOB = in.next();

            if (dOB.length() == 10 && dOB.charAt(2) == '/' && dOB.charAt(5) == '/') {
                try {
                    int day = Integer.parseInt(dOB.substring(0, 2));
                    int month = Integer.parseInt(dOB.substring(3, 5));
                    int year = Integer.parseInt(dOB.substring(6, 10));

                    if (day >= 1 && day <= 31 && month >= 1 && month <= 12 && year >= 1930) {
                        // Format the date to yyyy/MM/dd format
                        String formattedDOB = year + "/" + (month < 10 ? "0" + month : month) + "/" + (day < 10 ? "0" + day : day);
                        dOB = formattedDOB;  // Assign the formatted DOB back
                        System.out.println("Formatted Date of Birth: " + dOB);
                        break;
                    } else {
                        System.out.println("The date is not valid. Please enter a correct day and month.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("The date contains invalid numbers.");
                }
            } else {
                System.out.println("Please enter the date in DD/MM/YYYY format.");
            }
        }
        while (true){
            System.out.println("annual Ammount : ");
            anualIncome=in.nextDouble();
            try{
                if(anualIncome>0){
                    break;
                }else {
                    System.out.println("enter a valid amount");
                }
            }catch (Exception e){
                System.out.println("enter a valid amount");
                anualIncome=0;
            }
        }

        while(true) {
            System.out.println("------Choice your Account Type------");
            System.out.println("1. Current Account\n2.Savings Account");
            System.out.println("Enter your option (1/2): ");
            int op = in.nextInt();
            if(op==1) {
                System.out.println("You selected ----> Current Account");
                accountType="Current Account";
                break;
            }else if(op==2) {
                System.out.println("You selected ----> Savings Account");
                accountType="Savings Account";
                break;
            }else {
                System.out.println("Invalid Choice. Please enter any one (1 or 2).");
            }
        }
        userId = userName.substring(0,4)+phoneNumber.substring(6);
        Random ram=new Random();
        accNumber = ram.nextLong(1000000,9999999);
        System.out.println("\n------------------------------------------------------");
        System.out.println("Your Account Number and UserId for Login is created.");
        System.out.println("------------------------------------------------------\n");
        System.out.println(userId);
        String passp = "^(?=.*[0-9])[a-zA-Z0-9]{8}$";
        Pattern passwordPattern = Pattern.compile(passp);
        while(true) {
            System.out.println("You Have to Set Password for furture Login Purpose");
            System.out.println("\nPassword Should\n-Must Contain 8 Characters\n-Must Contain atleast one Number\n-Should not Contain Characters");
            System.out.println("\nEnter new password: ");
            String password = in.next();
            Matcher passmatch = passwordPattern.matcher(password);
            if(passmatch.matches()) {
                passWord=password;
                System.out.println("Your password has been successfully set!");
                break;
            }else {
                System.out.println("Invalid Password. (Password Must Contain 8 Characters includes Number not Symbols)");
            }
        }
        System.out.println("Your Account Creation is going to Finish!");
        while(true) {
            System.out.println("Enter Your Inital Deposite Amount (Above 500): ");
            int initalamount = in.nextInt();
            in.nextLine();
            if(initalamount >= 500) {
                System.out.println("Amount Added to your Account");
                initialAmount=initalamount;
                break;
            }else {
                System.out.println("Invalid amount");
            }
        }
        Date dt = new Date();
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = format.format(dt);
        //using the object we call the method and pass the initial details
        BankRepo repo=new BankRepo();
        repo.newUserLogin(formattedDate,accNumber,userId,passWord,accountType,userName,phoneNumber,anualIncome,initialAmount);

    }
}
