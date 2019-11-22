package Controllers;

import Server.Main;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Path("students/")
    public class StudentsDB {
    @GET
    @Path("logon")
    @Produces(MediaType.APPLICATION_JSON)
    public static String logon(@FormDataParam("StudentUsername") String StudentUsername, @FormDataParam("Password") String Password){
        System.out.println("students/logon");
        try{
            // still need to add the recalculation of avatar stats based on last played!
            PreparedStatement ps = Main.db.prepareStatement("SELECT Password FROM Students WHERE StudentUsername = ?");
            ps.setString(1, StudentUsername);
            ResultSet results = ps.executeQuery();
            if (results.next()) {

                String correctPassword = results.getString(1);
                if (Password.equals(correctPassword)){
                    return "[\"logon successful! Welcome\": \"" + StudentUsername + "\"}";
                }else{
                    return "{\"error\": \"Incorrect password\"}";
                }
            }else{
                return "{\"error\": \"Unknown user\"}";
            }
        }catch (Exception exception){
            System.out.println("Database error: " + (exception.getMessage()));
            return "{\"error\": \"Unable to logon, please see server console for more info.\"}";
        }
    }
    @GET
    @Path("get")
    @Produces(MediaType.APPLICATION_JSON)
    public static String get(@FormDataParam("StudentUsername") String StudentUsername){
        System.out.println("students/get");
        JSONArray list = new JSONArray();
        try{
            if (StudentUsername == null){
                throw new Exception("One or more form data parameters are missing in the HTTP request");
            }
            PreparedStatement ps = Main.db.prepareStatement("SELECT StudentUsername, StudentName, Password, AdultUsername, LastDate FROM Students WHERE StudentUsername = ?");
            ps.setString(1, StudentUsername);
            ResultSet results = ps.executeQuery();
            if (results.next()) {
                JSONObject item = new JSONObject();
                item.put("StudentUsername", results.getString(1));
                item.put("StudentName", results.getString(2));
                item.put("Password", results.getString(3));
                item.put("AdultUsername", results.getString(4));
                item.put("LastDate", results.getString(5));
                list.add(item);
                return item.toString();
            }else{
                return "{\"error\": \"Unable to find details linking to this student username\"}";

            }


        }catch (Exception exception){
            System.out.println("Database error: " + (exception.getMessage()));
            return "{\"error\": \"Unable to view profile, please see server console for more info.\"}";
        }
    }
    @GET
    @Path("chooseCourse")
    @Produces(MediaType.APPLICATION_JSON)
    public static String chooseCourse(@FormDataParam("StudentUsername") String StudentUsername, @FormDataParam("CourseID") Integer CourseID){
        try{
            if (StudentUsername == null || CourseID == null){
                throw new Exception("One or more form data parameters are missing in the HTTP request");
            }
            PreparedStatement ps = Main.db.prepareStatement("SELECT CourseID FROM StudentCourses WHERE StudentUsername = ? ");
            ps.setString(1, StudentUsername);
            ResultSet results = ps.executeQuery();
            if (results.next()){
            while (results.next());
                {
                    // stores the outputs in a variable
                    Integer takenCourseID = results.getInt(1);
                    if (CourseID.equals(takenCourseID)) {
                        //Id the CourseID is already stored as a course the student takes, the program throws an error
                        return "{\"error\": \"You are already taking this course!\"}";
                    } else {
                        //If the CourseID can't be found within the table, the course is added as a students course
                        PreparedStatement ps2 = Main.db.prepareStatement("INSERT INTO  StudentCourses(StudentUsername, CourseID) VALUES (?,?)");
                        ps2.setString(1, StudentUsername);
                        ps2.setInt(2, CourseID);
                        ps2.executeUpdate();
                        return "{\"Success\": \"You are now taking this course!\"}";
                    }
                }
            }else {//if the student hasn't currently got any courses, the username needs to first be validated and then it can be added
                PreparedStatement check = Main.db.prepareStatement("SELECT * FROM Students WHERE StudentUsername = ?");
                check.setString(1, StudentUsername);
                if (results.next()) {
                PreparedStatement ps2 = Main.db.prepareStatement("INSERT INTO  StudentCourses(StudentUsername, CourseID) VALUES (?,?)");
                ps2.setString(1, StudentUsername);
                ps2.setInt(2, CourseID);
                ps2.executeUpdate();
                return "{\"Success\": \"You are now taking this course!\"}";
                } else {
                    return "{\"error\": \"StudentUsername is not recognised\"}";

                }
            }
        }catch (Exception exception){
        System.out.println("Database error: " + (exception.getMessage()));
        return "{\"error\": \"Unable to add the course, please see server console for more info.\"}";
    }
    }
    @GET
    @Path("courses")
    @Produces(MediaType.APPLICATION_JSON)
    public static String viewCourses(@FormDataParam("StudentUsername") String StudentUsername) {
        JSONArray list = new JSONArray();
        System.out.println("students/courses");
        try {
            if (StudentUsername == null){
                throw new Exception("One or more form data parameters are missing in the HTTP request");

            }
            PreparedStatement ps = Main.db.prepareStatement("SELECT Courses.CourseName, StudentCourses.Score FROM Courses INNER JOIN StudentCourses ON Courses.CourseID = StudentCourses.CourseID WHERE StudentCourses.StudentUsername = ?");
            ps.setString(1, StudentUsername);

            ResultSet results = ps.executeQuery();
            while (results.next()){
                JSONObject item = new JSONObject();
                item.put("Course Name", results.getString(1));
                item.put("Score", results.getInt(2));
                list.add(item);
            }
            return list.toString();
        } catch (Exception exception) {
            System.out.println("Database error: " + (exception.getMessage()));
            return "{\"error\": \"Unable to list courses, please see server console for more info.\"}";
        }
    }
    @GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)
        public static String selectStudent(//String Username
                                            ) {
        System.out.println("students/list");
        JSONArray list = new JSONArray();
        //String Password = null;
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT StudentUsername, StudentName, Password, AdultUsername, Level FROM Students ");
            //WHERE StudentUsername = ?
            //test pull works
            //ps.setString(1, Username);
            ResultSet results = ps.executeQuery();
            while (results.next()) {
                JSONObject item = new JSONObject();
                item.put("StudentUsername", results.getString(1));
                item.put("StudentName", results.getString(2));
                item.put("Password", results.getString(3));
                item.put("AdultsUsername", results.getString(4));
                item.put("Level", results.getString(5));
                list.add(item);
                /*String StudentUsername = results.getString(1);
                String StudentName = results.getString(2);
                String Password = results.getString(3);
                String AdultUsername = results.getString(4);
System.out.println("Username: " + StudentUsername + ", Name: " + StudentName + ", Password: " + Password + ", Parent/Teachers Username: " + AdultUsername);*/

            }

            return list.toString();
        } catch (SQLException exception) {
            System.out.println("Database error: " + (exception.getMessage()));
            return "{\"error\": \"Unable to list items, please see server console for more info.\"}";
            // return Password;
        }
    }

    @POST
    @Path("insert")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
        public static String insertStudent(@FormDataParam("StudentName") String StudentName,@FormDataParam("StudentUsername")  String StudentUsername,@FormDataParam("Password")  String Password,@FormDataParam("AdultUsername")  String AdultUsername) {
            try {
                if (StudentName == null || StudentUsername == null || Password == null || AdultUsername == null) {
                    throw new Exception("One or more form data parameters are missing in the HTTP request");
                }
                System.out.println("students/insert StudentName=" + StudentName + "students/insert StudentUsername=" + StudentUsername + "students/insert Password=" + Password + "students/insert AdultUsername=" + AdultUsername);
                PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Students (StudentName, StudentUsername, Password, AdultUsername, level) VALUES (?,?,?,?,0)");
                ps.setString(1, StudentName);
                ps.setString(2, StudentUsername);
                ps.setString(3, Password);
                ps.setString(4, AdultUsername);
                ps.executeUpdate();

                return "[\"status\": \"OK\"}";
                //System.out.println("Course added successfully");
            } catch (Exception exception) {
                System.out.println("Database error" + exception.getMessage());
                return "[\"error\": \"Unable to create new item, please see server console for more info\"}";
            }

        }
    @POST
    @Path("update")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
        public static String updateStudent(@FormDataParam("StudentName") String StudentName,@FormDataParam("StudentUsername")  String StudentUsername,@FormDataParam("Password")  String Password,@FormDataParam("AdultUsername")  String AdultUsername , @FormDataParam("StudentNameUpdated") String StudentUsernameUpdated){
            try{
                if (StudentUsernameUpdated == null ||StudentName == null || StudentUsername == null || Password == null || AdultUsername == null) {
                    throw new Exception("One or more form data parameters are missing in the HTTP request");
                }
                System.out.println("students/update StudentName=" + StudentName + "students/update StudentUsername=" + StudentUsername + "students/update Password=" + Password + "students/update AdultUsername=" + AdultUsername+ "students/update StudentUsernameUpdated=" + StudentUsernameUpdated);
                PreparedStatement ps = Main.db.prepareStatement("UPDATE Students SET StudentName = ?, StudentUsername = ?, Password = ?, AdultUsername = ? WHERE StudentUsername = ?");
                ps.setString(1, StudentName);
                ps.setString(2, StudentUsernameUpdated);
                ps.setString(3, Password);
                ps.setString(4, AdultUsername);
                ps.setString(5, StudentUsername);
                ps.executeUpdate();
                return "[\"status\": \"OK\"}";
                //System.out.println("Course added successfully");
            } catch (Exception exception) {
                System.out.println("Database error" + exception.getMessage());
                return "[\"error\": \"Unable to update item, please see server console for more info\"}";
            }
        }
    @POST
    @Path("delete")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
        public static String deleteStudent(@FormDataParam("StudentUsername") String StudentUsername){
            try{
                if (StudentUsername == null){
                    throw new Exception("One or more form data parameters are missing in the HTTP request");
                }
                System.out.println("students/delete StudentUsername=" + StudentUsername);
                PreparedStatement ps = Main.db.prepareStatement("DELETE FROM Students WHERE StudentUsername = ?");
                ps.setString(1, StudentUsername);
                ps.executeUpdate();
                return "[\"status\": \"OK\"}";
                //System.out.println("Course added successfully");
            } catch (Exception exception) {
                System.out.println("Database error" + exception.getMessage());
                return "[\"error\": \"Unable to delete item, please see server console for more info\"}";
            }
        }

    }