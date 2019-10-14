import org.sqlite.SQLiteConfig;
//import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Scanner;
public class Main {
    public static Connection db = null;

    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        openDatabase("Coursework database.db");
        //code to get data from, write to the database etc goes here

//StudentsDB.insertStudent();
//StudentsDB.selectStudent();
//StudentsDB.deleteStudent("J3ss13sh1");
        System.out.println("What is the username of the record you would like to change?");
        String username = sc.nextLine();
        System.out.println("What username would you like to change it to?");
        String newusername = sc.nextLine();
       System.out.println("What password would you like to change to?");
       String password =  sc.nextLine();
       System.out.println("What adult username would you like to connect to?");
       String adultusername = sc.nextLine();
       System.out.println("What full name would you like to change to?");
       String fullname = sc.nextLine();
StudentsDB.updateStudent(fullname, newusername, password, adultusername, username);
        //System.out.println("Enter the corresponding number:");
        //System.out.println("1. Register");
        //System.out.println("2. Log in");
       // int number = sc.nextInt();
       // if (number == 1){
          //  register();
        //}else if (number == 2){
           // logon();
       // }


        CoursesDB.selectCourse();
        QuestionsDB.selectQuestion();
        StudentCoursesDB.selectStudentCourse();

        closeDatabase();
    }
    private static void openDatabase(String dbFile) {


        try {
            Class.forName("org.sqlite.JDBC");
            SQLiteConfig config = new SQLiteConfig();
            config.enforceForeignKeys(true);
            db = DriverManager.getConnection("jdbc:sqlite:resources/" + dbFile, config.toProperties());
            System.out.println("Database connection successfully established.");

        } catch (Exception exception) {
            System.out.println("Database connection error: " + exception.getMessage());

        }
    }

private static void closeDatabase() {
    try {
        db.close();
        System.out.println("Disconnected from database.");
    } catch (Exception exception) {
        System.out.println("Database disconnection error: " + exception.getMessage());
    }

}

    public static void updateAccount(){
        Scanner sc = new Scanner(System.in);
        System.out.println("What is the username of the record you would like to change?");
        String username = sc.nextLine();
        System.out.println("What username would you like to change it to?");
        String newusername = sc.nextLine();
        System.out.println("What password would you like to change to?");
        String password =  sc.nextLine();
        System.out.println("What full name would you like to change to?");
        String fullname = sc.nextLine();
        System.out.println("Is your account a student or adult account");
        String accountType = sc.nextLine();
        if(accountType == "adult") {
            AdultsDB.updateAdults(newusername, fullname, password, '0', username);
        }else if(accountType == "student"){
            System.out.println("What adult username would you like to connect to?");
            String adultusername = sc.nextLine();
            StudentsDB.updateStudent(fullname, newusername, password, adultusername, username);
        }else{
            System.out.println("Something has gone wrong!");
         }
    }

    public static void deleteAccount(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter the username of the account you would like to delete");
        String username = sc.nextLine();
        System.out.println("Is your account a student or adult account");
        String accountType = sc.nextLine();
        if(accountType == "adult") {
            AdultsDB.deleteAdult(username);
        }else if(accountType == "student"){
            StudentsDB.deleteStudent(username);
        }else{
            System.out.println("Something has gone wrong!");
        }
    }

    public static void logon(){
        Scanner sc = new Scanner(System.in);

        System.out.println("Please enter your username");
        String username = sc.nextLine();
        System.out.println("Please enter you password");
        String password = sc.nextLine();
        System.out.println("Adult or student account?"); //will be controlled with tick boxes on the GUI
        String decision = sc.nextLine();
        String verification = "";
        if (decision.equals("adult")){
             verification = AdultsDB.selectAdult(username);
        }else if (decision.equals("student")){
             //verification = StudentsDB.selectStudent(username);
        }else{
            System.out.println("Error");

        }
        if (password.equals(verification)){
            System.out.println("Access granted");
        }else{
             System.out.println("Access denied");
        }
    }

    public static void register(){
        Scanner sc = new Scanner(System.in);

        System.out.println("Please enter what username you would like");
        String username = sc.nextLine();
        System.out.println("Please enter your password");
        String password = sc.nextLine();
        System.out.println("Please enter your full name");
        String name = sc.nextLine();
        System.out.println("Do you want to make an adult or student account?");
        String decision = sc.nextLine();
        if (decision.equals("adult")){
            AdultsDB.insertAdult(username,name,password);
            //AdultsDB.selectAdult();
        }else if (decision.equals("student")){
            System.out.println("Please enter your teachers/ parents username");
            String adultUsername = sc.nextLine();
            StudentsDB.insertStudent(name,username,password,adultUsername);
            //StudentsDB.selectStudent();
        }else{
            System.out.println("Error with entering in student/adult");
        }


    }
}