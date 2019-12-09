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
@Path("studentcourses/")
public class StudentCoursesDB {
    @GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)
    public static String selectStudentCourse() {
        System.out.println("studentcourses/list/");
        JSONArray list = new JSONArray();
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT StudentUsername, CourseID, LastDate FROM StudentCourses");
            // test pull works
            ResultSet results = ps.executeQuery();
            while (results.next()) {
                JSONObject item = new JSONObject();
                item.put("StudentUsername", results.getString(1));
                item.put("CourseID", results.getInt(2));
                item.put("LastDate", results.getString(3));
                list.add(item);
                /*String StudentUsername = results.getString(1);
                int CourseID = results.getInt(2);
                String LastDate = results.getString(3);
System.out.println("Student username: " + StudentUsername + ", Course ID: " + CourseID + ", Last date: " + LastDate );*/
            }
            return list.toString();
        } catch (SQLException exception) {
            System.out.println("Database error: " + (exception.getMessage()));
            return "{\"error\": \"Unable to list items, please see server console for more info.\"}";
        }
    }
    @POST
    @Path("insert")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public static String insertStudentCourse(@FormDataParam("StudentUsername") String StudentUsername, @FormDataParam("CourseID") Integer CourseID) {
        try {
            if (StudentUsername == null || CourseID == null) {
                throw new Exception("One or more form data parameters are missing in the HTTP request");
            }
            System.out.println("studentcourses/insert StudentUsername=" + StudentUsername + "studentcourses/insert CourseID=" + CourseID);
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO StudentCourses (StudentUsername, CourseID) VALUES (?,?)");
            ps.setString(1, StudentUsername);
            ps.setInt(2, CourseID);
            ps.executeUpdate();

            return "{\"status\": \"OK\"}";
            //System.out.println("Course added successfully");
        } catch (Exception exception) {
            System.out.println("Database error" + exception.getMessage());
            return "{\"error\": \"Unable to create new item, please see server console for more info\"}";
        }

    }
    @POST
    @Path("update")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public static String updateStudentCourse(@FormDataParam("StudentUsernameUpdated") String StudentUsernameUpdated,@FormDataParam("StudentUsername") String StudentUsername, @FormDataParam("CourseID") Integer CourseID){
        try{
            if (StudentUsername == null || CourseID == null || StudentUsernameUpdated == null) {
                throw new Exception("One or more form data parameters are missing in the HTTP request");
            }

            PreparedStatement ps = Main.db.prepareStatement("UPDATE StudentCourses SET StudentUsername = ?, CourseID = ? WHERE StudentUsername = ?");
            ps.setString(1, StudentUsernameUpdated);
            ps.setInt(2, CourseID);
            ps.setString(3, StudentUsername);
            ps.executeUpdate();
            return "{\"status\": \"OK\"}";
            //System.out.println("Course added successfully");
        } catch (Exception exception) {
            System.out.println("Database error " + exception.getMessage());
            return "{\"error\": \"Unable to create new item, please see server console for more info\"}";
        }
    }
    @POST
    @Path("delete")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public static String deleteStudentCourse(@FormDataParam("StudentUsername")String StudentUsername){
        try{
            if (StudentUsername == null){
                throw new Exception("One or more form data parameters are missing in the HTTP request");
            }
            System.out.println("avatar/delete StudentUsername=" + StudentUsername);
            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM StudentCourses WHERE StudentUsername = ?");
            ps.setString(1, StudentUsername);
            ps.executeUpdate();
            return "{\"status\": \"OK\"}";
        } catch (Exception exception) {
            System.out.println("Database error" + exception.getMessage());
            return "{\"error\": \"Unable to create new item, please see server console for more info\"}";
        }
    }
}
