package Server;

import Controllers.AdultsDB;
import Controllers.AvatarTypeDB;
import Controllers.AvatarStatsDB;
import Controllers.StudentsDB;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.server.Server;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
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

//Controllers.StudentsDB.insertStudent();
//Controllers.StudentsDB.selectStudent();
//Controllers.StudentsDB.deleteStudent("J3ss13sh1");
       /* System.out.println("What is the username of the record you would like to change?");
        String username = sc.nextLine();
        System.out.println("What username would you like to change it to?");
        String newusername = sc.nextLine();
       System.out.println("What password would you like to change to?");
       String password =  sc.nextLine();
      // System.out.println("What adult username would you like to connect to?");
      // String adultusername = sc.nextLine();
       System.out.println("What full name would you like to change to?");
       String fullname = sc.nextLine();
       int no = 0;
Controllers.AdultsDB.updateAdults(newusername, fullname, password, no, username); */
        //System.out.println("Enter the corresponding number:");
        //System.out.println("1. Register");
        //System.out.println("2. Log in");
       // int number = sc.nextInt();
       // if (number == 1){
          // register();
        //}else if (number == 2){
           // logon();
       // }
        /*System.out.println("What is the username of the record you would like to change?");
        String username = sc.nextLine();
Controllers.AdultsDB.deleteAdult(username);
//Controllers.AdultsDB.selectAdult();*/
       /* System.out.println("Please enter the current username");
        String username = sc.nextLine();*/
       /* System.out.println("Please enter the new username");
        String newusername = sc.nextLine();
        System.out.println("What is the new course ID?");
        int courseID = sc.nextInt();
        Controllers.StudentCoursesDB.updateStudentCourse(newusername, courseID, username);*/
//Controllers.StudentCoursesDB.deleteStudentCourse(username);
        //System.out.println("What is the QuestionID of the record you want to change?");
        //int questionID = sc.nextInt();
       /* System.out.println("What is the new question ID?");
        int questionIDupdated = sc.nextInt();
        System.out.println("What is the new course ID?");
        int courseID = sc.nextInt();
        System.out.println("What is the new question?");
        String question = sc.next();
        System.out.println("What is the new correct answer?");
        String correctAnswer = sc.next();
        System.out.println("What are the new incorrect answers?");
        String incorrectAnswer1 = sc.next();
        String incorrectAnswer2 = sc.next();
        String incorrectAnswer3 = sc.next();
Controllers.QuestionsDB.updateQuestion(questionIDupdated, courseID, question, correctAnswer,incorrectAnswer1, incorrectAnswer2, incorrectAnswer3, questionID);*/
       /* System.out.println("What is the new course name?");
        String courseName = sc.next();
        System.out.println("What is the new course ID?");
        int newCourseID = sc.nextInt();*/
//Controllers.QuestionsDB.deleteQuestion(questionID);
        //Controllers.CoursesDB.deleteCourse(courseID);
       // Controllers.CoursesDB.selectCourse();
       // Controllers.QuestionsDB.selectQuestion();
        //Controllers.StudentCoursesDB.selectStudentCourse();

        /*System.out.println("What is the current username?");
        String username = sc.nextLine();
        System.out.println("What is the new username?");
        String newusername = sc.nextLine();
        System.out.println("What is the new avatar colour?");
        String colour = sc.nextLine();
        System.out.println("Whats is the new avatar image?");
        String image = sc.nextLine();
        System.out.println("What is the new hungry image?");
        String image1 = sc.nextLine();
        System.out.println("What is the new dirty image?");
        String image2 = sc.nextLine();
        System.out.println("What is the new confused image?");
        String image3 = sc.nextLine();
        System.out.println("What is the new hungry and dirty image?");
        String image4 = sc.nextLine();
        System.out.println("What is the new hungry and confused image?");
        String image5 = sc.nextLine();
        System.out.println("What is the new dirty and confused image?");
        String image6 = sc.nextLine();
        System.out.println("What is the new hungry, dirty and confused image?");
        String image7 = sc.nextLine();

        Controllers.AvatarDB.updateAvatar(newusername, colour, image, image1, image2, image3, image4, image5, image6, image7, username);*/
        //Controllers.AvatarDB.selectAvatar();

       /* System.out.println("What is the username?");
        String username = sc.nextLine();
        AvatarDB.deleteAvatar(username);*/
        ResourceConfig config = new ResourceConfig();
        config.packages("Controllers");
        config.register(MultiPartFeature.class);
        ServletHolder servlet = new ServletHolder(new ServletContainer(config));

        Server server = new Server(8081);
        ServletContextHandler context = new ServletContextHandler(server, "/");
        context.addServlet(servlet, "/*");

        try {
            server.start();
            System.out.println("Server successfully started.");
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
        }

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

   /* public static void updateAccount(){
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
            //AdultsDB.updateAdults(newusername, fullname, password, '0', username);
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
            // verification = Controllers.AdultsDB.selectAdult(username);
        }else if (decision.equals("student")){
             //verification = Controllers.StudentsDB.selectStudent(username);
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
            //Controllers.AdultsDB.selectAdult();
        }else if (decision.equals("student")){
            System.out.println("Please enter your teachers/ parents username");
            String adultUsername = sc.nextLine();
            //StudentsDB.insertStudent(name,username,password,adultUsername);
            //Controllers.StudentsDB.selectStudent();
        }else{
            System.out.println("Error with entering in student/adult");
        }


    }*/
}