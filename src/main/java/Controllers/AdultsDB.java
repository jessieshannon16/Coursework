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
@Path("adults/")
public class AdultsDB {
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
    @Path("insert")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public static String insertAdult(@FormDataParam("AdultUsername") String AdultUsername,@FormDataParam("AdultName")  String AdultName,@FormDataParam("Password")  String Password) {
        try {
            if (AdultUsername == null || AdultName == null || Password == null) {
                throw new Exception("One or more form data parameters are missing in the HTTP request");
            }
            System.out.println("adults/insert AdultUsername=" + AdultUsername + "adults/insert AdultName=" + AdultName + "adults/insert Password=" + Password);
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Adults (AdultUsername, AdultName, Password) VALUES (?,?,?)");
            ps.setString(1, AdultUsername);
            ps.setString(2, AdultName);
            ps.setString(3, Password);
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
    public static String updateAdults(@FormDataParam("AdultUsernameUpdated")String AdultUsernameUpdated, @FormDataParam("AdultUsername") String AdultUsername,@FormDataParam("AdultName")  String AdultName,@FormDataParam("Password")  String Password){
        try{
            if (AdultUsernameUpdated == null ||AdultUsername == null || AdultName == null || Password == null) {
                throw new Exception("One or more form data parameters are missing in the HTTP request");
            }
            System.out.println("adults/update AdultUsernameUpdated=" + AdultUsernameUpdated +"adults/update AdultUsername=" + AdultUsername + "adults/update AdultName=" + AdultName + "adults/update Password=" + Password);
            PreparedStatement ps = Main.db.prepareStatement("UPDATE Adults SET AdultUsername = ?, AdultName = ?, Password = ?, NoOfStudents = ? WHERE AdultUsername = ?");
            ps.setString(1, AdultUsernameUpdated);
            ps.setString(2, AdultName);
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
    @Path("delete")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public static String deleteAdult(@FormDataParam("AdultUsername")String AdultUsername){
        try{
            if (AdultUsername == null){
                throw new Exception("One or more form data parameters are missing in the HTTP request");
            }
            System.out.println("adults/delete AdultUsername=" + AdultUsername);
            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM Adults WHERE AdultUsername = ?");
            ps.setString(1, AdultUsername);
            ps.executeUpdate();
            return "[\"status\": \"OK\"}";
            //System.out.println("Course added successfully");
        } catch (Exception exception) {
            System.out.println("Database error" + exception.getMessage());
            return "[\"error\": \"Unable to create new item, please see server console for more info\"}";
        }
    }
}
