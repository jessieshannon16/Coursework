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
@Path("avatartype/")
public class AvatarTypeDB {

    @POST
    @Path("choose")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public static String choose(@CookieParam("username") String StudentUsername,@FormDataParam("colour") String AvatarColour, @CookieParam("token") String token){
        System.out.println("avatartype/choose");
        /*if (!StudentsDB.validToken(token)) {
            return "{\"error\": \"You don't appear to be logged in.\"}";
        }*/
        try {
            System.out.println("This is the avatar colour: " + AvatarColour);
            if (AvatarColour == null){
                throw new Exception("One or more form data parameters are missing in the HTTP request");
            }
            System.out.println("This is the username: " + StudentUsername);
            PreparedStatement getID = Main.db.prepareStatement("SELECT AvatarID FROM AvatarType WHERE AvatarColour = ?");
            getID.setString(1, AvatarColour);
            ResultSet results = getID.executeQuery();
            int AvatarID = results.getInt(1);
            System.out.println("This is the avatar ID: " + AvatarID);

            PreparedStatement ps = Main.db.prepareStatement("UPDATE Students SET AvatarID = ? WHERE StudentUsername = ?");
            ps.setInt(1, AvatarID);
            ps.setString(2, StudentUsername);
            ps.executeUpdate();

            JSONObject response = new JSONObject();
            response.put("status", "Avatar updated sucessfully!");
            return response.toString();


        }catch (Exception exception) {
            System.out.println("Database error: " + (exception.getMessage()));
            return "{\"error\": \"Unable to update avatar, please see server console for more info.\"}";
        }
    }
    @POST
    @Path("images")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public static String images(@FormDataParam("StudentUsername") String username, @CookieParam("token") String token) {
        if (!StudentsDB.validToken(token)) {
            return "{\"error\": \"You don't appear to be logged in.\"}";
        }
        System.out.println("avatartype/images");
        try{
            PreparedStatement ps = Main.db.prepareStatement("SELECT AvatarImage, HungryImage, ConfusedImage, DirtyImage, HCImage, HDImage, DCImage, HCDImage FROM AvatarType WHERE AvatarID = (SELECT AvatarID FROM Students WHERE StudentUsername = ?) ");
            ps.setString(1, username);
            ResultSet results = ps.executeQuery();
            if (results.next()){
                JSONObject item = new JSONObject();
                item.put("AvatarImage", results.getString(1));
                item.put("HungryImage", results.getString(2));
                item.put("ConfusedImage", results.getString(3));
                item.put("DirtyImage", results.getString(4));
                item.put("HCImage", results.getString(5));
                item.put("HDImage", results.getString(6));
                item.put("DCImage", results.getString(7));
                item.put("HDCImage", results.getString(8));
                return item.toString();
            }else{
                return "{\"error\": \"No images for this AvatarID\"}";
            }

        }catch (Exception exception) {
            System.out.println("Database error: " + (exception.getMessage()));
            return "{\"error\": \"Unable to display avatar images, please see server console for more info.\"}";


        }
    }
    @GET
    @Path("view")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public static String view(){
        System.out.println("avatartype/view");
        JSONArray list = new JSONArray();
        try{
            PreparedStatement ps = Main.db.prepareStatement("SELECT AvatarImage FROM AvatarType");
            ResultSet results = ps.executeQuery();
            while (results.next()){
                JSONObject item = new JSONObject();
                item.put("AvatarImage", results.getString(1));
                list.add(item);
            }
            return list.toString();
        }catch (Exception exception) {
            System.out.println("Database error" + exception.getMessage());
            return "{\"error\": \"Unable to display avatars, please see server console for more info\"}";
        }
    }

    @GET
    @Path("list")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public static String selectAvatar(){
        System.out.println("avatartype/list");
        JSONArray list = new JSONArray();
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT AvatarID, AvatarColour, AvatarImage, HungryImage, DirtyImage, ConfusedImage, HDImage, HCImage, DCImage, HCDImage FROM AvatarType");
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
                item.put("AvatarID", results.getInt(1));
                item.put("AvatarColour", results.getString(2));
                item.put("AvatarImage", results.getString(3));
                //item.put("Hungry", results.getInt(4));
                item.put("HungryImage", results.getString(4));
                //item.put("Dirty", results.getInt(6));
                item.put("DirtyImage", results.getString(5));
               // item.put("Confused", results.getString(8));
                item.put("ConfusedImage", results.getString(6));
                item.put("HDImage", results.getString(7));
                item.put("HCImage", results.getString(8));
                item.put("DCImage", results.getString(9));
                item.put("HCDImage", results.getString(10));
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
    public static String insertAvatar(@FormDataParam("AvatarID") Integer AvatarID,@FormDataParam("AvatarColour") String AvatarColour,@FormDataParam("AvatarImage") String AvatarImage, @FormDataParam("HungryImage")String HungryImage,@FormDataParam("DirtyImage") String DirtyImage, @FormDataParam("ConfusedImage") String ConfusedImage, @FormDataParam("HDImage") String HDImage, @FormDataParam("HCImage") String HCImage, @FormDataParam("DCImage") String DCImage,@FormDataParam("HCDImage") String HCDImage) {
        try {
            if (AvatarID == null || AvatarColour == null || AvatarImage == null|| HungryImage == null|| ConfusedImage == null|| DirtyImage == null|| HDImage == null|| HCImage == null|| DCImage == null|| HCDImage == null) {
                throw new Exception("One or more form data parameters are missing in the HTTP request");
            }
            System.out.println("avatartype/insert AvatarID=" + AvatarID + "avatartype/insert AvatarColour=" + AvatarColour + "avatartype/insert AvatarImage=" + AvatarImage + "avatartype/insert HungryImage=" + HungryImage + "avatartype/insert DirtyImage=" + DirtyImage + "avatartype/insert ConfusedImage=" + ConfusedImage + "avatartype/insert HDImage=" + HDImage + "avatartype/insert HCImage=" + HCImage + "avatartype/insert DCImage=" + DCImage + "avatartype/insert HCDImage=" + HCDImage);
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO AvatarType (AvatarID, AvatarColour, AvatarImage, HungryImage, DirtyImage, ConfusedImage, HDImage, HCImage, DCImage, HCDImage) VALUES (?,?,?,?,?,?,?,?,?,?)");
            ps.setInt(1, AvatarID);
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
    public static String updateAvatar(@FormDataParam("AvatarIDUpdated")Integer AvatarIDUpdated, @FormDataParam("AvatarID")Integer AvatarID,@FormDataParam("AvatarColour") String AvatarColour,@FormDataParam("AvatarImage") String AvatarImage, @FormDataParam("HungryImage")String HungryImage,@FormDataParam("DirtyImage") String DirtyImage, @FormDataParam("ConfusedImage") String ConfusedImage, @FormDataParam("HDImage") String HDImage, @FormDataParam("HCImage") String HCImage, @FormDataParam("DCImage") String DCImage,@FormDataParam("HCDImage") String HCDImage){
        try{
            if (AvatarIDUpdated == null || AvatarID == null || AvatarColour == null || AvatarImage == null|| HungryImage == null|| ConfusedImage == null|| DirtyImage == null|| HDImage == null|| HCImage == null|| DCImage == null|| HCDImage == null) {
                throw new Exception("One or more form data parameters are missing in the HTTP request");
            }
            System.out.println("avatartype/update AvatarID=" + AvatarID + "avatartype/update AvatarColour=" + AvatarColour + "avatartype/update AvatarImage=" + AvatarImage + "avatartype/update HungryImage=" + HungryImage + "avatartype/update DirtyImage=" + DirtyImage + "avatartype/update ConfusedImage=" + ConfusedImage + "avatartype/update HDImage=" + HDImage + "avatartype/update HCImage=" + HCImage + "avatartype/update DCImage=" + DCImage + "avatartype/update HCDImage=" + HCDImage + "avatartype/update AvatarIDUpdated=" + AvatarIDUpdated);
            PreparedStatement ps = Main.db.prepareStatement("UPDATE AvatarType SET AvatarID = ?, AvatarColour = ?, AvatarImage = ?, HungryImage = ?, DirtyImage = ?, ConfusedImage = ?, HDImage = ?, HCImage = ?, DCImage = ?, HCDImage = ? WHERE AvatarID = ?");
            ps.setInt(1, AvatarIDUpdated);
            ps.setString(2, AvatarColour);
            ps.setString(3, AvatarImage);
            ps.setString(4, HungryImage);
            ps.setString(5, DirtyImage);
            ps.setString(6, ConfusedImage);
            ps.setString(7, HDImage);
            ps.setString(8, HCImage);
            ps.setString(9, DCImage );
            ps.setString(10, HCDImage);
            ps.setInt(11, AvatarID);
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
    public static String deleteAvatar(@FormDataParam("AvatarID")Integer AvatarID){
        try{
            if (AvatarID == null){
                throw new Exception("One or more form data parameters are missing in the HTTP request");
            }
            System.out.println("avatartype/delete AvatarID=" + AvatarID);
            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM AvatarType WHERE AvatarID = ?");
            ps.setInt(1, AvatarID);
            ps.executeUpdate();
            return "{\"status\": \"OK\"}";
            //System.out.println("Course added successfully");
        } catch (Exception exception) {
            System.out.println("Database error" + exception.getMessage());
            return "{\"error\": \"Unable to delete item, please see server console for more info\"}";
        }
    }
}
