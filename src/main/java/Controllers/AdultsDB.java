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
import java.util.UUID;

@Path("adults/")
public class AdultsDB {
    @POST
    @Path("courses")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public static String courses(@CookieParam("username") String AdultUsername, @CookieParam("token") String token) {
        if (!AdultsDB.validToken(token)) {
            return "{\"error\": \"You don't appear to be logged in.\"}";
        }
        JSONArray list = new JSONArray();
        System.out.println("adults/courses");
        try{
            if (AdultUsername == null){
                throw new Exception("One or more form data parameters are missing in the HTTP request");
            }

            PreparedStatement ps = Main.db.prepareStatement("SELECT StudentUsername FROM Students WHERE AdultUsername = ?");
            ps.setString(1, AdultUsername);
            ResultSet results = ps.executeQuery();


                while (results.next()) {
                    String AdultsStudents = results.getString(1);
                    System.out.println(AdultsStudents);

                    PreparedStatement fetch = Main.db.prepareStatement("SELECT Courses.CourseName, StudentCourses.Score FROM Courses INNER JOIN StudentCourses ON Courses.CourseID = StudentCourses.CourseID WHERE StudentCourses.StudentUsername = ? ");
                    fetch.setString(1, AdultsStudents);
                    ResultSet courses = fetch.executeQuery();


                    while (courses.next()) {
                        JSONObject item = new JSONObject();
                        item.put("StudentUsername", AdultsStudents);
                        System.out.println(AdultsStudents);
                        item.put("CourseName", courses.getString(1));
                        System.out.println(courses.getString(1));
                        item.put("Score", courses.getInt(2));
                        System.out.println(courses.getInt(2));
                        list.add(item);
                    }

                }
                return list.toString();



        }catch (Exception exception){
            System.out.println("Database error: " + (exception.getMessage()));
            return "{\"error\": \"Unable to list courses, please see server console for more info.\"}";
        }
    }

    @POST
    @Path("get")
    @Produces(MediaType.APPLICATION_JSON)
    public static String get(@FormDataParam("AdultUsername") String AdultUsername, @CookieParam("token") String token) {
        if (!AdultsDB.validToken(token)) {
            return "{\"error\": \"You don't appear to be logged in.\"}";
        }
        System.out.println("adults/get");
        JSONArray list = new JSONArray();
        JSONArray studentList = new JSONArray();
        try{
            if(AdultUsername == null){
                throw new Exception("One or more form data parameters are missing in the HTTP request");
            }
            PreparedStatement ps = Main.db.prepareStatement("SELECT AdultUsername, AdultName, Password FROM Adults WHERE AdultUsername = ?");
            ps.setString(1, AdultUsername);
            ResultSet results = ps.executeQuery();
            PreparedStatement student = Main.db.prepareStatement("SELECT StudentUsername FROM Students WHERE AdultUsername = ?");
            student.setString(1, AdultUsername);
            ResultSet usernames = student.executeQuery();

            if (results.next() && usernames.next()) {
                JSONObject item = new JSONObject();
                item.put("AdultUsername", results.getString(1));
                item.put("AdultName", results.getString(2));
                item.put("Password", results.getString(3));
                list.add(item);
                while (usernames.next()) {
                    JSONObject names = new JSONObject();
                    names.put("StudentUsername", usernames.getString(1));
                    studentList.add(names);
                }
                return list.toString() + studentList.toString();
            }else if (results.next()) {
                JSONObject item = new JSONObject();
                item.put("AdultUsername", results.getString(1));
                item.put("AdultName", results.getString(2));
                item.put("Password", results.getString(3));
                list.add(item);
                return list.toString() + "{\"error\": \"This username has no students attached to it\"}";
            }else if(usernames.next()){
                while(usernames.next()){
                    JSONObject names = new JSONObject();
                    names.put("StudentUsername", usernames.getString(1));
                    studentList.add(names);
                }
                return studentList.toString() + "{\"error\": \"This username hasn't got any details\"}";
            }else{
                return "{\"error\": \"This username hasn't got any details\"}";
            }

        }catch (Exception exception){
            System.out.println("Database error: " + (exception.getMessage()));
            return "{\"error\": \"Unable to view profile, please see server console for more info.\"}";
        }

    }




    @POST
    @Path("logon")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public static String logon(@FormDataParam("AdultUsername") String AdultUsername, @FormDataParam("Password") String Password){
        System.out.println("adults/logon");
        try{
            if (AdultUsername == null|| Password == null){
                throw new Exception("One or more form data parameters are missing in the HTTP request");
            }
            PreparedStatement students = Main.db.prepareStatement("SELECT StudentUsername FROM Students WHERE AdultUsername = ?");
            students.setString(1, AdultUsername);
            ResultSet number = students.executeQuery();

            int counter = 0;
            while (number.next()) {
                counter = counter + 1;
            }
            PreparedStatement update = Main.db.prepareStatement("UPDATE Adults SET NoOfStudents = ? WHERE AdultUsername = ? ");
            update.setInt(1, counter);
            update.setString(2, AdultUsername);
            update.executeUpdate();

            PreparedStatement ps = Main.db.prepareStatement("SELECT Password FROM Adults WHERE AdultUsername = ?");
            ps.setString(1, AdultUsername);
            ResultSet results = ps.executeQuery();
            if (results.next()) {

                String correctPassword = results.getString(1);

                if (Password.equals(correctPassword)){
                    String token = UUID.randomUUID().toString();

                    PreparedStatement ps2 = Main.db.prepareStatement("UPDATE Adults SET Token = ? WHERE AdultUsername = ?");
                    ps2.setString(1, token);
                    ps2.setString(2, AdultUsername);
                    ps2.executeUpdate();

                    JSONObject userDetails = new JSONObject();
                    userDetails.put("username", AdultUsername);
                    userDetails.put("token", token);
                    userDetails.put("accountType", "adult");

                    return userDetails.toString();
                   //return "{\"logon successful! Welcome\": \"" + AdultUsername + "\"}";
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
    @POST
    @Path("logout")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public static String logout(@CookieParam("token") String token) {

        try {

            System.out.println("adults/logout");

            PreparedStatement ps1 = Main.db.prepareStatement("SELECT AdultUsername FROM Adults WHERE Token = ?");
            ps1.setString(1, token);
            ResultSet logoutResults = ps1.executeQuery();
            if (logoutResults.next()) {

                int id = logoutResults.getInt(1);

                PreparedStatement ps2 = Main.db.prepareStatement("UPDATE Adults SET Token = NULL WHERE AdultUsername = ?");
                ps2.setInt(1, id);
                ps2.executeUpdate();

                return "{\"status\": \"OK\"}";

            } else {

                return "{\"error\": \"Invalid token!\"}";

            }

        } catch (Exception exception){
            System.out.println("Database error during /user/logout: " + exception.getMessage());
            return "{\"error\": \"Server side error!\"}";
        }

    }

    public static boolean validToken(String token) {
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT AdultUsername FROM Adults WHERE Token = ?");
            ps.setString(1, token);
            ResultSet logoutResults = ps.executeQuery();
            return logoutResults.next();
        } catch (Exception exception) {
            System.out.println("Database error during /user/logout: " + exception.getMessage());
            return false;
        }
    }

    @POST
    @Path("register")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public static String registerAdult(@FormDataParam("username") String AdultUsername,@FormDataParam("fullname")  String AdultName,@FormDataParam("password")  String Password, @FormDataParam("adultUsername") String adultUsername) {
        /*if (!AdultsDB.validToken(token)) {
            return "{\"error\": \"You don't appear to be logged in.\"}";
        }*/
        try {
            if (AdultUsername == null || AdultName == null || Password == null) {
                throw new Exception("One or more form data parameters are missing in the HTTP request");
            }
            System.out.println("adults/insert AdultUsername=" + AdultUsername + "adults/insert AdultName=" + AdultName + "adults/insert Password=" + Password);

            PreparedStatement uniqueCheck = Main.db.prepareStatement("SELECT AdultUsername FROm Adults");
            ResultSet results = uniqueCheck.executeQuery();
            if (results.next()){
                while(results.next()){
                    String username = results.getString(1);
                    if (username.equals(AdultUsername)){
                        return "{\"Sorry. This username is already taken by a student\"}";
                    }
                }
            }

            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Adults (AdultUsername, AdultName, Password) VALUES (?,?,?)");
            ps.setString(1, AdultUsername);
            ps.setString(2, AdultName);
            ps.setString(3, Password);
            ps.executeUpdate();



            JSONObject userDetails = new JSONObject();
            userDetails.put("username", AdultUsername);
            userDetails.put("password", Password);
            return userDetails.toString();
            //return "{\"status\": \"OK\"}";
            //System.out.println("Course added successfully");
        } catch (Exception exception) {
            System.out.println("Database error" + exception.getMessage());
            return "{\"error\": \"Unable to create new item, please see server console for more info\"}";
        }

    }

    @POST
    @Path("choosecourse")
    @Produces(MediaType.APPLICATION_JSON)
    public static String chooseCourse(@CookieParam("username") String AdultUsername, @FormDataParam("CourseID") Integer CourseID, @FormDataParam("StudentUsername") String StudentUsername, @CookieParam("token") String token) {
        if (!AdultsDB.validToken(token)) {
            return "{\"error\": \"You don't appear to be logged in.\"}";
        }
        try {
            if (CourseID == null || AdultUsername == null || StudentUsername == null) {
                throw new Exception("One or more form data parameters are missing in the HTTP request");
            }

            PreparedStatement ps1 = Main.db.prepareStatement("SELECT AdultUsername FROM Students WHERE StudentUsername = ?");
            ps1.setString(1, StudentUsername);
            ResultSet results = ps1.executeQuery();
            if (results.next()) {
                String correctAdult = results.getString(1);
                if (AdultUsername.equals(correctAdult)) {
                    PreparedStatement ps2 = Main.db.prepareStatement("INSERT INTO  StudentCourses(StudentUsername, CourseID) VALUES (?,?)");
                    ps2.setString(1, StudentUsername);
                    ps2.setInt(2, CourseID);
                    ps2.executeUpdate();
                    return "{\"Success\": \"Your student is now taking this course!\"}";
                }else{
                    return "{\"error\": \"You don't have control over this students account\"}";

                }
            }else{
                return "{\"error\": \"AdultUsername is not recognised\"}";

            }

        } catch (Exception exception) {
            System.out.println("Database error: " + (exception.getMessage()));
            return "{\"error\": \"Unable to add the course, please see server console for more info.\"}";
        }
    }
    @GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)
    public static String selectAdult() {
        System.out.println("adults/list/");
        JSONArray list = new JSONArray();
       // String Password = null;
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT AdultUsername, AdultName, Password, NoOfStudents FROM Adults ");

           // ps.setString(1, Username);
            ResultSet results = ps.executeQuery();
           // Password = null;
            while (results.next()) {
                /*String AdultUsername = results.getString(1);
                String AdultName = results.getString(2);
                String Password = results.getString(3);
                int NoOfStudents = results.getInt(4);
                System.out.println("Username: " + AdultUsername + ", Name: " + AdultName + ", Password: " + Password + ", Number of Students: " + NoOfStudents);*/
                JSONObject item = new JSONObject();
                item.put("AdultUsername", results.getString(1));
                item.put("AdultName", results.getString(2));
                item.put("Password", results.getString(3));
                item.put("NoOfStudents", results.getString(4));
                list.add(item);
           }

            return list.toString();
        } catch (SQLException exception) {
            System.out.println("Database error: " + (exception.getMessage()));
            return "{\"error\": \"Unable to list items, please see server console for more info.\"}";
        }
       // return Password;
    }

    @POST
    @Path("update")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public static String updateAdults(@FormDataParam("AdultUsernameUpdated")String AdultUsernameUpdated, @FormDataParam("AdultUsername") String AdultUsername,@FormDataParam("AdultName") String AdultName,@FormDataParam("Password") String Password, @CookieParam("token") String token) {
        if (!AdultsDB.validToken(token)) {
            return "{\"error\": \"You don't appear to be logged in.\"}";
        }
        try{
            if (AdultUsernameUpdated == null ||AdultUsername == null || AdultName == null || Password == null) {
                throw new Exception("One or more form data parameters are missing in the HTTP request");
            }
            System.out.println("adults/update AdultUsernameUpdated=" + AdultUsernameUpdated +"adults/update AdultUsername=" + AdultUsername + "adults/update AdultName=" + AdultName + "adults/update Password=" + Password);
            PreparedStatement ps = Main.db.prepareStatement("UPDATE Adults SET AdultUsername = ?, AdultName = ?, Password = ? WHERE AdultUsername = ?");
            ps.setString(1, AdultUsernameUpdated);
            ps.setString(2, AdultName);
            ps.setString(3, Password);
            ps.setString(4, AdultUsername);
            ps.executeUpdate();
            return "{\"status\": \"OK\"}";
            //System.out.println("Course added successfully");
        } catch (Exception exception) {
            System.out.println("Database error" + exception.getMessage());
            return "{\"error\": \"Unable to create new item, please see server console for more info\"}";
        }
    }
    @POST
    @Path("delete")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public static String deleteAdult(@FormDataParam("AdultUsername")String AdultUsername, @CookieParam("token") String token) {
        if (!AdultsDB.validToken(token)) {
            return "{\"error\": \"You don't appear to be logged in.\"}";
        }
        try{
            if (AdultUsername == null){
                throw new Exception("One or more form data parameters are missing in the HTTP request");
            }
            System.out.println("adults/delete AdultUsername=" + AdultUsername);
            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM Adults WHERE AdultUsername = ?");
            ps.setString(1, AdultUsername);
            ps.executeUpdate();
            return "{\"status\": \"OK\"}";
            //System.out.println("Course added successfully");
        } catch (Exception exception) {
            System.out.println("Database error" + exception.getMessage());
            return "{\"error\": \"Unable to create new item, please see server console for more info\"}";
        }
    }
}
