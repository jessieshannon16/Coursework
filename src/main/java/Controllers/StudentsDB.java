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
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)
        public String selectStudent(//String Username
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
        public String insertStudent(@FormDataParam("StudentName") String StudentName,@FormDataParam("StudentUsername")  String StudentUsername,@FormDataParam("Password")  String Password,@FormDataParam("AdultUsername")  String AdultUsername) {
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
        public String updateStudent(@FormDataParam("StudentName") String StudentName,@FormDataParam("StudentUsername")  String StudentUsername,@FormDataParam("Password")  String Password,@FormDataParam("AdultUsername")  String AdultUsername , @FormDataParam("StudentNameUpdated") String StudentUsernameUpdated){
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
                return "[\"error\": \"Unable to create new item, please see server console for more info\"}";
            }
        }
    @POST
    @Path("delete")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
        public String deleteStudent(@FormDataParam("StudentUsername") String StudentUsername){
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
                return "[\"error\": \"Unable to create new item, please see server console for more info\"}";
            }
        }
        
    }