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
        //register();
        StudentsDB.selectStudent();
        AdultsDB.selectAdult();
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
    public static void logon(){

    }
    public static void register(){
        Scanner sc = new Scanner(System.in);

        System.out.println("Please enter what username you would like");
        String username = sc.nextLine();
        System.out.println("Please enter your password");
        String password = sc.nextLine();
        System.out.println("Please enter your full name");
        String name = sc.nextLine();
        System.out.println("Do you want to make and adult or student account?");
        String decision = sc.nextLine();
        if (decision.equals("adult")){
            AdultsDB.insertAdult(username,name,password);
            AdultsDB.selectAdult();
        }else if (decision.equals("student")){
            System.out.println("Please enter your teachers/ parents username");
            String adultUsername = sc.nextLine();
            StudentsDB.insertStudent(name,username,password,adultUsername);
            StudentsDB.selectStudent();
        }else{
            System.out.println("Error");
        }


    }
}