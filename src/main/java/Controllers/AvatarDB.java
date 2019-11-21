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
@Path("avatar/")
public class AvatarDB {
    @GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)
    public static String selectAvatar(){
        System.out.println("avatar/list/");
        JSONArray list = new JSONArray();
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT StudentUsername, AvatarColour, AvatarImage, Hungry, HungryImage, Dirty, DirtyImage, Confused, ConfusedImage, HDImage, HCImage, DCImage, HCDImage FROM Avatar");
            // test pull works
            ResultSet results = ps.executeQuery();
            while (results.next()) {
               /* String StudentUsername = results.getString(1);
                String AvatarColour = results.getString(2);
                String AvatarImage = results.getString(3);
                int Hungry = results.getInt(4);
                String HungryImage = results.getString(5);
                int Dirty = results.getInt(6);
                String DirtyImage = results.getString(7);
                int Confused = results.getInt(8);
                String ConfusedImage = results.getString(9);
                String HDImage = results.getString(10);
                String HCImage = results.getString(11);
                String DCImage = results.getString(12);
                String HCDImage = results.getString(13);

                System.out.println("Student Username: " + StudentUsername + ", Avatar colour: " + AvatarColour + ", Avatar image: " + AvatarImage + ", Hungry score: " + Hungry + ", Hungry image: " + HungryImage);
                System.out.println("Dirty score: " + Dirty + ", Dirty image: " + DirtyImage + ", Confused score: " + Confused + ", Confused image: " + ConfusedImage);
                System.out.println("Hungry and Dirty image: " + HDImage + ", Hungry and Confused image: " + HCImage);
                System.out.println("Dirty and Confused image: " + DCImage + ", Hungry, Confused and Dirty Image:" + HCDImage);*/
                JSONObject item = new JSONObject();
                item.put("StudentUsername", results.getString(1));
                item.put("AvatarColour", results.getString(2));
                item.put("AvatarImage", results.getString(3));
                item.put("Hungry", results.getInt(4));
                item.put("HungryImage", results.getString(5));
                item.put("Dirty", results.getInt(6));
                item.put("DirtyImage", results.getString(7));
                item.put("Confused", results.getString(8));
                item.put("ConfusedImage", results.getString(9));
                item.put("HDImage", results.getString(10));
                item.put("HCImage", results.getString(11));
                item.put("DCImage", results.getString(12));
                item.put("HCDImage", results.getString(13));
               list.add(item);
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
    public static String insertAvatar(@FormDataParam("StudentUsername")String StudentUsername,@FormDataParam("AvatarColour") String AvatarColour,@FormDataParam("AvatarImage") String AvatarImage, @FormDataParam("HungryImage")String HungryImage,@FormDataParam("DirtyImage") String DirtyImage, @FormDataParam("ConfusedImage") String ConfusedImage, @FormDataParam("HDImage") String HDImage, @FormDataParam("HCImage") String HCImage, @FormDataParam("DCImage") String DCImage,@FormDataParam("HCDImage") String HCDImage) {
        try {
            if (StudentUsername == null || AvatarColour == null || AvatarImage == null|| HungryImage == null|| ConfusedImage == null|| DirtyImage == null|| HDImage == null|| HCImage == null|| DCImage == null|| HCDImage == null) {
                throw new Exception("One or more form data parameters are missing in the HTTP request");
            }
            System.out.println("avatar/insert StudentUsername=" + StudentUsername + "avatar/insert AvatarColour=" + AvatarColour + "avatar/insert AvatarImage=" + AvatarImage + "avatar/insert HungryImage=" + HungryImage + "avatar/insert DirtyImage=" + DirtyImage + "avatar/insert ConfusedImage=" + ConfusedImage + "avatar/insert HDImage=" + HDImage + "avatar/insert HCImage=" + HCImage + "avatar/insert DCImage=" + DCImage + "avatar/insert HCDImage=" + HCDImage);
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Avatar (StudentUsername, AvatarColour, AvatarImage, HungryImage, DirtyImage, ConfusedImage, HDImage, HCImage, DCImage, HCDImage) VALUES (?,?,?,?,?,?,?,?,?,?)");
            ps.setString(1, StudentUsername);
            ps.setString(2, AvatarColour);
            ps.setString(3, AvatarImage);
            ps.setString(4, HungryImage);
            ps.setString(5, DirtyImage);
            ps.setString(6, ConfusedImage);
            ps.setString(7, HDImage);
            ps.setString(8, HCImage);
            ps.setString(9, DCImage );
            ps.setString(10, HCDImage);

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
    public static String updateAvatar(@FormDataParam("StudentUsernameUpdated")String StudentUsernameUpdated, @FormDataParam("StudentUsername")String StudentUsername,@FormDataParam("AvatarColour") String AvatarColour,@FormDataParam("AvatarImage") String AvatarImage, @FormDataParam("HungryImage")String HungryImage,@FormDataParam("DirtyImage") String DirtyImage, @FormDataParam("ConfusedImage") String ConfusedImage, @FormDataParam("HDImage") String HDImage, @FormDataParam("HCImage") String HCImage, @FormDataParam("DCImage") String DCImage,@FormDataParam("HCDImage") String HCDImage){
        try{
            if (StudentUsernameUpdated == null || StudentUsername == null || AvatarColour == null || AvatarImage == null|| HungryImage == null|| ConfusedImage == null|| DirtyImage == null|| HDImage == null|| HCImage == null|| DCImage == null|| HCDImage == null) {
                throw new Exception("One or more form data parameters are missing in the HTTP request");
            }
            System.out.println("avatar/update StudentUsername=" + StudentUsername + "avatar/update AvatarColour=" + AvatarColour + "avatar/update AvatarImage=" + AvatarImage + "avatar/update HungryImage=" + HungryImage + "avatar/update DirtyImage=" + DirtyImage + "avatar/update ConfusedImage=" + ConfusedImage + "avatar/update HDImage=" + HDImage + "avatar/update HCImage=" + HCImage + "avatar/update DCImage=" + DCImage + "avatar/update HCDImage=" + HCDImage + "avatar/update StudentUsernameUpdated=" + StudentUsernameUpdated);
            PreparedStatement ps = Main.db.prepareStatement("UPDATE Avatar SET StudentUsername = ?, AvatarColour = ?, AvatarImage = ?, HungryImage = ?, DirtyImage = ?, ConfusedImage = ?, HDImage = ?, HCImage = ?, DCImage = ?, HCDImage = ? WHERE StudentUsername = ?");
            ps.setString(1, StudentUsernameUpdated);
            ps.setString(2, AvatarColour);
            ps.setString(3, AvatarImage);
            ps.setString(4, HungryImage);
            ps.setString(5, DirtyImage);
            ps.setString(6, ConfusedImage);
            ps.setString(7, HDImage);
            ps.setString(8, HCImage);
            ps.setString(9, DCImage );
            ps.setString(10, HCDImage);
            ps.setString(11, StudentUsername);
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
    public static String deleteAvatar(@FormDataParam("StudentUsername")String StudentUsername){
        try{
            if (StudentUsername == null){
                throw new Exception("One or more form data parameters are missing in the HTTP request");
            }
            System.out.println("avatar/delete StudentUsername=" + StudentUsername);
            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM Avatar WHERE StudentUsername = ?");
            ps.setString(1, StudentUsername);
            ps.executeUpdate();
            return "[\"status\": \"OK\"}";
            //System.out.println("Course added successfully");
        } catch (Exception exception) {
            System.out.println("Database error" + exception.getMessage());
            return "[\"error\": \"Unable to create new item, please see server console for more info\"}";
        }
    }
}
