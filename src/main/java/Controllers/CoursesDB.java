package Controllers;

import Server.Main;
import jdk.nashorn.internal.objects.annotations.Getter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
@Path("courses/")
public class CoursesDB {
    @GET
    @Path("list/")
    @Produces(MediaType.APPLICATION_JSON)

    public static String selectCourse(){
        System.out.println("courses/list/");
        JSONArray list = new JSONArray();
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
    public static void insertCourse(int CourseID, String CourseName) {
        try {
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Courses (CourseID, CourseName) VALUES (?,?)");
            ps.setInt(1, CourseID);
            ps.setString(2, CourseName);

            ps.executeUpdate();
            System.out.println("Course added successfully");
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
            System.out.println("Error. Something has gone wrong");
        }

    }
    public static void updateCourse(int CourseIDUpdated, String CourseName, int CourseID){
        try{
            PreparedStatement ps = Main.db.prepareStatement("UPDATE Courses SET CourseID = ?, CourseName = ? WHERE CourseID = ?");
            ps.setInt(1, CourseIDUpdated);
            ps.setString(2, CourseName);
            ps.setInt(3, CourseID);
            ps.executeUpdate();
        }catch (Exception exception){
            System.out.println(exception.getMessage());
            System.out.println("Error. Something has gone wrong");
        }
    }
    public static void deleteCourse(int CourseID){
        try{
            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM Courses WHERE CourseID = ?");
            ps.setInt(1, CourseID);
            ps.executeUpdate();
        }catch (SQLException exception) {
            System.out.println(exception.getMessage());
            System.out.println("Error. Something has gone wrong");
        }
    }
}
