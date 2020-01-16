package Controllers;

import Server.Main;
//import jdk.nashorn.internal.objects.annotations.Getter;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
@Path("courses/")
public class CoursesDB {
    @GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)

    public static String selectCourse(@CookieParam("token") String token){
        System.out.println("courses/list/");
        JSONArray list = new JSONArray();
        if (!StudentsDB.validToken(token) && !AdultsDB.validToken(token)) {
            return "{\"error\": \"You don't appear to be logged in.\"}";

        }
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT CourseID, CourseName FROM Courses");
            // test pull works
            ResultSet results = ps.executeQuery();
            while (results.next()) {
                JSONObject item = new JSONObject();
                item.put("CourseID", results.getInt(1));
                item.put("CourseName", results.getString(2));
                list.add(item);

               /* int CourseID = results.getInt(1);
                String CourseName = results.getString(2);*/
                //System.out.println("Course ID: " + CourseID + ", Course name: " + CourseName);
            }
            return list.toString();
        } catch (SQLException exception) {
            System.out.println("Database error: " + (exception.getMessage()));
            return "{\"error\": \"Unable to list items, please see server console for more info.\"}";
           // System.out.println("Error. Something has gone wrong");
        }
    }
    @POST
    @Path("insert")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public static String insertCourse(@FormDataParam("CourseID") Integer CourseID,@FormDataParam("CourseName") String CourseName) {
        try {
            if (CourseID == null || CourseName == null) {
            throw new Exception("One or more form data parameters are missing in the HTTP request");
            }
            System.out.println("courses/insert CourseID=" + CourseID + "courses/insert CourseName=" + CourseName);
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Courses (CourseID, CourseName) VALUES (?,?)");
            ps.setInt(1, CourseID);
            ps.setString(2, CourseName);

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
    public static String updateCourse(@FormDataParam("CourseIDUpdated") Integer CourseIDUpdated, @FormDataParam("CourseID") Integer CourseID,@FormDataParam("CourseName") String CourseName){
        try{
            if (CourseID == null || CourseName == null || CourseIDUpdated == null) {
                throw new Exception("One or more form data parameters are missing in the HTTP request");
            }
            System.out.println("courses/update CourseID=" + CourseID + "courses/update CourseName=" + CourseName + "courses/update CourseIDUpdated=" + CourseIDUpdated);
            PreparedStatement ps = Main.db.prepareStatement("UPDATE Courses SET CourseID = ?, CourseName = ? WHERE CourseID = ?");
            ps.setInt(1, CourseIDUpdated);
            ps.setString(2, CourseName);
            ps.setInt(3, CourseID);
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
    public static String deleteCourse(@FormDataParam("CourseID") Integer CourseID){
        try{
            if (CourseID == null){
                throw new Exception("One or more form data parameters are missing in the HTTP request");
            }
            System.out.println("courses/delete CourseID=" + CourseID);
            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM Courses WHERE CourseID = ?");
            ps.setInt(1, CourseID);
            ps.executeUpdate();
            return "{\"status\": \"OK\"}";
            //System.out.println("Course added successfully");
        } catch (Exception exception) {
            System.out.println("Database error" + exception.getMessage());
            return "{\"error\": \"Unable to create new item, please see server console for more info\"}";
        }
    }
}
