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

@Path("students/")
    public class StudentsDB {
    @POST
    @Path("type")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public static String type(@CookieParam("username") String username){
        try{
            PreparedStatement check = Main.db.prepareStatement("SELECT UserType FROM AllUsers WHERE Username = ?");
            check.setString(1, username);
            ResultSet Type = check.executeQuery();
            if (Type.next()){
                JSONObject accountType = new JSONObject();
                accountType.put("accountType", Type.getString(1));
                return accountType.toString();
            }else{
                return "{\"error\": \"Unable to find account type\"}";

            }
        } catch (Exception exception) {
            System.out.println("Database error: " + (exception.getMessage()));
            return "{\"error\": \"Unable to find account type, failed at check, please see server console for more info.\"}";

        }
    }
    @POST
    @Path("checkLogon")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public static String checkLogon(@FormDataParam("username") String Username, @FormDataParam("password") String Password, @FormDataParam("fullname") String StudentName, @FormDataParam("adultUsername")  String AdultUsername, @FormDataParam("month") Integer month, @FormDataParam("day") Integer day, @FormDataParam("year") Integer year) {
        System.out.println("students/check");
        try {
            PreparedStatement check = Main.db.prepareStatement("SELECT UserType FROM AllUsers WHERE Username = ?");
            System.out.println("username: " + Username);
            check.setString(1, Username);
            System.out.println("here");
            ResultSet Type = check.executeQuery();
            System.out.println("after executeQuery()");
            String userType = Type.getString(1);
            if (userType.equals("Adult")) {
                return AdultsDB.logon(Username, Password);
            } else {
                return StudentsDB.logon(Username, Password, day, month, year);
            }
        } catch (Exception exception) {
            System.out.println("Database error: " + (exception.getMessage()));
            return "{\"error\": \"Unable to logon, failed at check, please see server console for more info.\"}";

        }
    }

    @POST
    @Path("checkLogout")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public static String checkLogout(@CookieParam("token") String token){
        try {
            PreparedStatement check = Main.db.prepareStatement("SELECT UserType FROM AllUsers WHERE Token = ?");
            check.setString(1, token);
            ResultSet Type = check.executeQuery();
            String userType = Type.getString(1);
            if (userType.equals("Adult")){
                return AdultsDB.logout(token);
            }else {
                return StudentsDB.logout(token);
            }
    }catch (Exception exception) {
            System.out.println("Database error: " + (exception.getMessage()));
            return "{\"error\": \"Unable to logout, failed at check, please see server console for more info.\"}";

        }
    }
    @POST
    @Path("logon")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public static String logon(@FormDataParam("StudentUsername") String StudentUsername, @FormDataParam("Password") String Password, @FormDataParam("month") Integer month, @FormDataParam("day") Integer day, @FormDataParam("year") Integer year){
        System.out.println("students/logon");
        try{

                // still need to add the recalculation of avatar stats based on last played!
                PreparedStatement ps = Main.db.prepareStatement("SELECT Password FROM Students WHERE StudentUsername = ?");
                ps.setString(1, StudentUsername);
                ResultSet results = ps.executeQuery();
                if (results.next()) {

                    String correctPassword = results.getString(1);
                    if (Password.equals(correctPassword)) {
                        String token = UUID.randomUUID().toString();
                        if(StudentsDB.updateDate(day, month, year, StudentUsername)==0){
                            return "{\"error\": \"Error adding dates\"}";
                        }

                        PreparedStatement ps2 = Main.db.prepareStatement("UPDATE Students SET Token = ? WHERE StudentUsername = ?");
                        ps2.setString(1, token);
                        ps2.setString(2, StudentUsername);
                        ps2.executeUpdate();

                        JSONObject userDetails = new JSONObject();
                        userDetails.put("username", StudentUsername);
                        userDetails.put("token", token);
                        userDetails.put("accountType", "student");
                        return userDetails.toString();

                        //return "{\"logon successful! Welcome\": \"" + StudentUsername + "\"}";
                    } else {
                        return "{\"error\": \"Incorrect password\"}";
                    }
                } else {
                    AdultsDB.logon(StudentUsername, Password);
                    return "{\"error\": \"Unknown user\"}";
                }

        }catch (Exception exception){
            System.out.println("Database error: " + (exception.getMessage()));
            return "{\"error\": \"Unable to logon, please see server console for more info.\"}";
        }
    }
    public static Integer updateDate(Integer day, Integer month, Integer year, String username){
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT LastDay, LastMonth, LastYear FROM Students WHERE StudentUsername =?");
            ps.setString(1,username);
            ResultSet results = ps.executeQuery();

            int oldDay = results.getInt(1);
            int oldMonth = results.getInt(2);
            int oldYear = results.getInt(3);

            int score = 0;
            int hunger = 0;
            int clever = 0;
            int dirty = 0;

            if (oldYear != year){
                score = score + ((year - oldYear - 1)*356);
            }
            if (oldMonth!= month){
                if (oldMonth > month){
                    score = score + ((12- oldMonth + month - 1)*30);
                }else{
                    score = score + ((month - oldMonth - 1)*30);
                }
            }
            if (oldDay != day){
                if (oldDay > day){
                    score = score + (30- oldDay + day);
                }else{
                    score = score + (day - oldDay);
                }
            }
            if (score != 0){
                hunger = 5 * score;
                clever = 5 * score;
                dirty = 5 * score;
            }


            PreparedStatement select = Main.db.prepareStatement("SELECT Hunger, Cleanliness, Intelligence FROM Students WHERE StudentUsername = ?");
            select.setString(1,username);
            ResultSet stats = select.executeQuery();

            System.out.println(stats.getInt(1));
            System.out.println(stats.getInt(2));
            System.out.println(stats.getInt(3));


            hunger = stats.getInt(1) - hunger;
            clever = stats.getInt(2) - clever;
            dirty = stats.getInt(3) - dirty;
            if (hunger < 0){
                hunger = 0;
            }
            if (clever < 0){
                clever = 0;
            }
            if (dirty < 0){
                dirty = 0;
            }
            PreparedStatement update = Main.db.prepareStatement("UPDATE Students SET Hunger = ?, Cleanliness = ?, Intelligence = ?, LastDay = ?, LastMonth = ?, LastYear = ? WHERE StudentUsername = ?");
            update.setInt(1, hunger);
            update.setInt(2,clever);
            update.setInt(3,dirty);
            update.setInt(4,day);
            update.setInt(5,month);
            update.setInt(6,year);
            update.setString(7, username);
            update.executeUpdate();


            return 1;

        }catch (Exception exception) {
            System.out.println("Database error: " + (exception.getMessage()));
            return 0;

        }

    }
    @POST
    @Path("logout")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public static String logout(@CookieParam("token") String token) {

        try {

                System.out.println("students/logout");

                PreparedStatement ps1 = Main.db.prepareStatement("SELECT StudentUsername FROM Students WHERE Token = ?");
                ps1.setString(1, token);
                ResultSet logoutResults = ps1.executeQuery();
                if (logoutResults.next()) {

                    int id = logoutResults.getInt(1);

                    PreparedStatement ps2 = Main.db.prepareStatement("UPDATE Students SET Token = NULL WHERE StudentUsername = ?");
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
            PreparedStatement ps = Main.db.prepareStatement("SELECT StudentUsername FROM Students WHERE Token = ?");
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
    public String registerStudent(@FormDataParam("username") String StudentUsername,@FormDataParam("fullname") String StudentName,@FormDataParam("password")  String Password,@FormDataParam("adultUsername")  String AdultUsername,@FormDataParam("day")  Integer day,@FormDataParam("month")  Integer month,@FormDataParam("year")  Integer year) {

        try {
            if (StudentName == null || StudentUsername == null || Password == null || AdultUsername == null) {
                throw new Exception("One or more form data parameters are missing in the HTTP request");
            }
            PreparedStatement uniqueCheck = Main.db.prepareStatement("SELECT StudentUsername FROM Students");
            ResultSet results = uniqueCheck.executeQuery();
            if (results.next()){
                while(results.next()){
                    String username = results.getString(1);
                    if (username.equals(StudentUsername)){
                        return "{\"error\": \"Sorry. This username is already taken by an adult\"}";

                    }
                }
            }
            //System.out.println("students/insert StudentName=" + StudentName + "students/insert StudentUsername=" + StudentUsername + "students/insert Password=" + Password + "students/insert AdultUsername=" + AdultUsername);
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Students (StudentName, StudentUsername, Password, AdultUsername) VALUES (?,?,?,?)");
            ps.setString(1, StudentName);
            ps.setString(2, StudentUsername);
            ps.setString(3, Password);
            ps.setString(4, AdultUsername);
            ps.executeUpdate();

            if (StudentsDB.setDate(day,month,year,AdultUsername)==0){
                return "{\"error\": \"Error with setting date\"}";
            }

            JSONObject userDetails = new JSONObject();
            userDetails.put("username", StudentUsername);
            userDetails.put("password", Password);
            return userDetails.toString();
            //return "{\"status\": \"OK\"}";
            //System.out.println("Course added successfully");
        } catch (Exception exception) {
            System.out.println("Database error" + exception.getMessage());
            return "{\"error\": \"Unable to create new item, please see server console for more info\"}";
        }

    }
    public static int setDate(Integer day, Integer month, Integer year, String username){
        try {
            PreparedStatement ps = Main.db.prepareStatement("UPDATE Students SET Hunger = ?, Cleanliness = ?, Intelligence = ?, LastDay = ?, LastMonth = ?, LastYear = ? WHERE StudentUsername = ?");
            ps.setInt(1,100);
            ps.setInt(2,100);
            ps.setInt(3,100);
            ps.setInt(4, day);
            ps.setInt(5,month);
            ps.setInt(6,year);
            ps.setString(7,username);
            ps.executeUpdate();
            return 1;

        }catch (Exception exception) {
            System.out.println("Database error: " + (exception.getMessage()));
            return 0;

        }


    }
    @POST
    @Path("get")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public static String get(@FormDataParam("StudentUsername") String StudentUsername, @CookieParam("token") String token) {
        if (!StudentsDB.validToken(token)) {
            return "{\"error\": \"You don't appear to be logged in.\"}";
        }
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
    @POST
    @Path("choosecourse")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public static String chooseCourse(@CookieParam("username") String StudentUsername, @FormDataParam("CourseID") Integer CourseID, @CookieParam("token") String token) {
        if (!StudentsDB.validToken(token)) {
            return "{\"error\": \"You don't appear to be logged in.\"}";
        }
        try{
            if (CourseID == null){
                throw new Exception("One or more form data parameters are missing in the HTTP request");
            }

                    //If the CourseID can't be found within the table, the course is added as a students course
                    PreparedStatement ps2 = Main.db.prepareStatement("INSERT INTO  StudentCourses(StudentUsername, CourseID) VALUES (?,?)");
                    ps2.setString(1, StudentUsername);
                    ps2.setInt(2, CourseID);
                    ps2.executeUpdate();
                    return "{\"Success\": \"You are now taking this course!\"}";

        }catch (Exception exception){
        System.out.println("Database error: " + (exception.getMessage()));
        return "{\"error\": \"You're already taking this course!\"}";
        }
    }
    @POST
    @Path("courses")
    @Produces(MediaType.APPLICATION_JSON)
    public static String viewCourses(@CookieParam("username") String StudentUsername, @CookieParam("token") String token) {
        if (!StudentsDB.validToken(token)) {
            return "{\"error\": \"You don't appear to be logged in.\"}";
        }
        JSONArray list = new JSONArray();
        System.out.println("students/courses");
        try {
            if (StudentUsername == null){
                throw new Exception("One or more form data parameters are missing in the HTTP request");

            }
            PreparedStatement ps = Main.db.prepareStatement("SELECT Courses.CourseName, StudentCourses.Score FROM Courses INNER JOIN StudentCourses ON Courses.CourseID = StudentCourses.CourseID WHERE StudentCourses.StudentUsername = ?");
            ps.setString(1, StudentUsername);

            ResultSet results = ps.executeQuery();

                while (results.next()) {
                    JSONObject item = new JSONObject();
                    item.put("CourseName", results.getString(1));
                    System.out.println(results.getString(1));
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
            PreparedStatement ps = Main.db.prepareStatement("SELECT StudentUsername, StudentName, Password, AdultUsername, LastDate, AvatarID FROM Students ");
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
                item.put("LastDate", results.getString(5));
                item.put("AvatarID", results.getInt(6));
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
    @Path("update")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
        public static String updateStudent(@FormDataParam("StudentName") String StudentName,@FormDataParam("StudentUsername")  String StudentUsername,@FormDataParam("Password")  String Password,@FormDataParam("AdultUsername")  String AdultUsername , @FormDataParam("StudentUsernameUpdated") String StudentUsernameUpdated, @CookieParam("token") String token) {
        if (!StudentsDB.validToken(token)) {
            return "{\"error\": \"You don't appear to be logged in.\"}";
        }
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
                return "{\"status\": \"OK\"}";
                //System.out.println("Course added successfully");
            } catch (Exception exception) {
                System.out.println("Database error" + exception.getMessage());
                return "{\"error\": \"Unable to update item, please see server console for more info\"}";
            }
        }
    @POST
    @Path("delete")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
        public static String deleteStudent(@FormDataParam("StudentUsername") String StudentUsername, @CookieParam("token") String token) {
        if (!StudentsDB.validToken(token)) {
            return "{\"error\": \"You don't appear to be logged in.\"}";
        }
            try{
                if (StudentUsername == null){
                    throw new Exception("One or more form data parameters are missing in the HTTP request");
                }
                System.out.println("students/delete StudentUsername=" + StudentUsername);
                PreparedStatement ps = Main.db.prepareStatement("DELETE FROM Students WHERE StudentUsername = ?");
                ps.setString(1, StudentUsername);
                ps.executeUpdate();
                return "{\"status\": \"OK\"}";
                //System.out.println("Course added successfully");
            } catch (Exception exception) {
                System.out.println("Database error" + exception.getMessage());
                return "{\"error\": \"Unable to delete item, please see server console for more info\"}";
            }
        }

    }