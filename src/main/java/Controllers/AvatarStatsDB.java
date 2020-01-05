package Controllers;

        import org.glassfish.jersey.media.multipart.FormDataParam;
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
        import javax.ws.rs.*;
        import javax.ws.rs.core.MediaType;
@Path("avatarstats/")
public class AvatarStatsDB {

    @POST
    @Path("setName")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public static String setName(@CookieParam("token") String token, @FormDataParam("avatarName") String AvatarName, @CookieParam("username") String StudentUsername){
        System.out.println("avatarstats/setName");
        if (!StudentsDB.validToken(token)) {
            return "{\"error\": \"You don't appear to be logged in.\"}";
        }
        try{
            PreparedStatement ps = Main.db.prepareStatement("UPDATE Students SET AvatarName = ? WHERE StudentUsername = ?");
            ps.setString(1, AvatarName);
            ps.setString(2, StudentUsername);
            ps.executeUpdate();
            return "{\"Your avatar name is now updated\"}";

        }catch (Exception exception) {
        System.out.println("Database error: " + (exception.getMessage()));
        return "{\"error\": \"Unable to set avatar name\"}";
        // System.out.println("Error. Something has gone wrong");
        }
    }

    @GET
    @Path("name")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public static String name(@CookieParam("username") String StudentUsername,  @CookieParam("token") String token){
        System.out.println("avatarstats/name");
        if (!StudentsDB.validToken(token)) {
            return "{\"error\": \"You don't appear to be logged in.\"}";
        }
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT AvatarName FROM Students WHERE StudentUsername = ?");
            ps.setString(1, StudentUsername);
            ResultSet results = ps.executeQuery();

            if (results.next()){
                JSONObject item = new JSONObject();
                item.put("AvatarName", results.getString(1));
                return item.toString();
            }else{
                return "{\"error\": \"No username!\"}";
            }

        }catch (Exception exception) {
            System.out.println("Database error: " + (exception.getMessage()));
            return "{\"error\": \"Unable to feed avatar, please see server console for more info.\"}";
            // System.out.println("Error. Something has gone wrong");
        }

    }
    @POST
    @Path("feed")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.MULTIPART_FORM_DATA)

    public static String feed(@FormDataParam("StudentUsername") String StudentUsername, @FormDataParam("Hunger") Integer Hunger){
        System.out.println("avatarstats/feed");

        try {
            if (StudentUsername == null || Hunger == null){
                throw new Exception("One or more form data parameters are missing in the HTTP request");
            }

            PreparedStatement ps = Main.db.prepareStatement("UPDATE Students SET Hunger = ? WHERE StudentUsername = ?");
            ps.setInt(1, Hunger);
            ps.setString(2, StudentUsername);
            ps.executeUpdate();

            return "{\"Your avatar is now less hungry!\"}";

        }catch (Exception exception) {
            System.out.println("Database error: " + (exception.getMessage()));
            return "{\"error\": \"Unable to feed avatar, please see server console for more info.\"}";
            // System.out.println("Error. Something has gone wrong");
        }

    }
    @POST
    @Path("clean")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.MULTIPART_FORM_DATA)

    public static String clean(@FormDataParam("StudentUsername") String StudentUsername, @FormDataParam("Cleanliness") Integer Cleanliness){
        System.out.println("avatarstats/clean");

        try {
            if (StudentUsername == null || Cleanliness == null){
                throw new Exception("One or more form data parameters are missing in the HTTP request");
            }

            PreparedStatement ps = Main.db.prepareStatement("UPDATE Students SET Cleanliness = ? WHERE StudentUsername = ?");
            ps.setInt(1, Cleanliness);
            ps.setString(2, StudentUsername);
            ps.executeUpdate();

            return "{\"Your avatar is now cleaner!\"}";

        }catch (Exception exception) {
            System.out.println("Database error: " + (exception.getMessage()));
            return "{\"error\": \"Unable to clean avatar, please see server console for more info.\"}";
            // System.out.println("Error. Something has gone wrong");
        }

    }
    @POST
    @Path("view")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.MULTIPART_FORM_DATA)

    public static String view(@FormDataParam("StudentUsername") String StudentUsername){
        System.out.println("avatarstats/view");
        JSONArray list = new JSONArray();
        try{
            if (StudentUsername == null){
                throw new Exception("One or more form data parameters are missing in the HTTP request");
            }
            PreparedStatement ps = Main.db.prepareStatement("SELECT Hunger, Cleanliness, Intelligence FROM Students WHERE StudentUsername = ?");
            ps.setString(1,StudentUsername);
            ResultSet results = ps.executeQuery();
            PreparedStatement ps2 = Main.db.prepareStatement("SELECT AvatarImage FROM AvatarType WHERE AvatarID = (SELECT AvatarID FROM Students WHERE StudentUsername = ?)");
            ps2.setString(1,StudentUsername);
            ResultSet image = ps2.executeQuery();

            if(results.next() && image.next()){
                JSONObject item = new JSONObject();
                item.put("Hunger", results.getInt(1));
                item.put("Cleanliness", results.getInt(2));
                item.put("Intelligence", results.getInt(3));
                item.put("Image", image.getString(1));
                list.add(item);

                return list.toString();
            }else{
                return "{\"error\": \"There are no stats for this username\"}";

            }


        }catch (Exception exception) {
            System.out.println("Database error: " + (exception.getMessage()));
            return "{\"error\": \"Unable to view avatar stats, please see server console for more info.\"}";
            // System.out.println("Error. Something has gone wrong");
        }
    }
    @GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)

    public static String selectAvatar(){
        System.out.println("avatarstats/list/");
        JSONArray list = new JSONArray();
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT StudentUsername, AvatarName, Hunger, Cleanliness, Intelligence FROM Students");
            ResultSet results = ps.executeQuery();
            while (results.next()) {
                JSONObject item = new JSONObject();
                item.put("StudentUsername", results.getString(1));
                item.put("AvatarName", results.getString(2));
                item.put("Hunger", results.getInt(3));
                item.put("Cleanliness", results.getInt(4));
                item.put("Intelligence", results.getInt(5));
                list.add(item);
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
    public static String insertAvatar(@FormDataParam("StudentUsername") String StudentUsername, @FormDataParam("AvatarName") String AvatarName, @FormDataParam("Hunger") Integer Hunger, @FormDataParam("Cleanliness") Integer Cleanliness ,@FormDataParam("Intelligence") Integer Intelligence) {
        try {
            if (StudentUsername == null || AvatarName == null || Hunger == null || Cleanliness == null || Intelligence == null) {
                throw new Exception("One or more form data parameters are missing in the HTTP request");
            }
            System.out.println("avatarstats/insert StudentUsername=" + StudentUsername + "avatarstats/insert AvatarName=" + AvatarName + "avatarstats/insert Hunger=" + Hunger + "avatarstats/insert Cleanliness=" + Cleanliness + "avatarstats/insert Intelligence=" + Intelligence);
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Students (StudentUsername, AvatarName, Hunger, Cleanliness, Intelligence) VALUES (?,?,?,?,?)");
            ps.setString(1, StudentUsername);
            ps.setString(2, AvatarName);
            ps.setInt(3, Hunger);
            ps.setInt(4, Cleanliness);
            ps.setInt(5, Intelligence);
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
    public static String updateCourse(@FormDataParam("StudentUsernameUpdated") String StudentUsernameUpdated, @FormDataParam("StudentUsername") String StudentUsername, @FormDataParam("AvatarName") String AvatarName, @FormDataParam("Hunger") Integer Hunger, @FormDataParam("Cleanliness") Integer Cleanliness ,@FormDataParam("Intelligence") Integer Intelligence){
        try{
            if (StudentUsernameUpdated == null ||StudentUsername == null || AvatarName == null || Hunger == null || Cleanliness == null || Intelligence == null) {
                throw new Exception("One or more form data parameters are missing in the HTTP request");
            }
            System.out.println("avatarstats/update StudentUsernameUpdated=" + StudentUsernameUpdated + "avatarstats/update StudentUsername=" + StudentUsername + "avatarstats/update AvatarName=" + AvatarName + "avatarstats/update Hunger=" + Hunger + "avatarstats/update Cleanliness=" + Cleanliness + "avatarstats/update Intelligence=" + Intelligence);
            PreparedStatement ps = Main.db.prepareStatement("UPDATE Students SET StudentUsername = ?, AvatarName = ?, Hunger = ?, Cleanliness = ?, Intelligence = ? WHERE StudentUsername = ?");
            ps.setString(1, StudentUsernameUpdated);
            ps.setString(2, AvatarName);
            ps.setInt(3, Hunger);
            ps.setInt(4, Cleanliness);
            ps.setInt(5, Intelligence);
            ps.setString(6, StudentUsername);
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
    public static String deleteCourse(@FormDataParam("StudentUsername") String StudentUsername){
        try{
            if (StudentUsername == null){
                throw new Exception("One or more form data parameters are missing in the HTTP request");
            }
            System.out.println("avatarstats/delete StudentUsername=" + StudentUsername);
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
