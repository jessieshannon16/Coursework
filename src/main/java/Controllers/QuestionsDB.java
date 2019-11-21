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
@Path("questions/")
public class QuestionsDB {
    @GET
    @Path("list")
    @Produces(MediaType.APPLICATION_JSON)
    public static String selectQuestion() {
        System.out.println("questions/list/");
        JSONArray list = new JSONArray();
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT QuestionID, CourseID, Question, CorrectAnswer, IncorrectAnswer1, IncorrectAnswer2, IncorrectAnswer3 FROM Questions");
            // test pull works
            ResultSet results = ps.executeQuery();
            while (results.next()) {
                JSONObject item = new JSONObject();
                item.put("QuestionID", results.getInt(1));
                item.put("CourseID", results.getInt(2));
                item.put("Question",results.getString(3) );
                item.put( "CorrectAnswer", results.getString(4));
                item.put(" IncorrectAnswer1", results.getString(5));
                item.put ("IncorrectAnswer2", results.getString(6));
                item.put ("IncorrectAnswer3", results.getString(7));
                list.add(item);
                /*int QuestionID = results.getInt(1);
                int CourseID = results.getInt(2);
                String Question = results.getString(3);
                String CorrectAnswer = results.getString(4);
                String IncorrectAnswer1 = results.getString(5);
                String IncorrectAnswer2 = results.getString(6);
                String IncorrectAnswer3 = results.getString(7);
                System.out.println("Question ID: " + QuestionID + ", Course ID: " + CourseID + ", Question: " + Question + ", Correct Answer: " + CorrectAnswer + ", Incorrect Answer 1: " + IncorrectAnswer1 + ", Incorrect Answer 2: " + IncorrectAnswer2 + ", Incorrect answer 3: " + IncorrectAnswer3);*/
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
    public static String insertQuestion(@FormDataParam("QuestionID") Integer QuestionID, @FormDataParam("CourseID") Integer CourseID, @FormDataParam("Question") String Question, @FormDataParam("CorrectAnswer") String CorrectAnswer, @FormDataParam("IncorrectAnswer1") String IncorrectAnswer1, @FormDataParam("IncorrectAnswer2") String IncorrectAnswer2, @FormDataParam("IncorrectAnswer3") String IncorrectAnswer3) {
        try {
            if (QuestionID == null || CourseID == null || Question == null || CorrectAnswer == null || IncorrectAnswer1 == null || IncorrectAnswer2 == null || IncorrectAnswer3 == null) {
                throw new Exception("One or more form data parameters are missing in the HTTP request");
            }
            System.out.println("courses/insert QuestionID=" + QuestionID + "courses/insert CourseID=" + CourseID + "courses/insert Question=" + Question + "courses/insert CorrectAnswer=" + CorrectAnswer + "courses/insert IncorrectAnswer1=" + IncorrectAnswer1 + "courses/insert IncorrectAnswer2=" + IncorrectAnswer2 + "courses/insert IncorrectAnswer3=" + IncorrectAnswer3);
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Questions (QuestionID, CourseID, Question, CorrectAnswer, IncorrectAnswer1, IncorrectAnswer2, IncorrectAnswer3) VALUES (?,?,?,?,?,?,?)");
            ps.setInt(1, QuestionID);
            ps.setInt(2, CourseID);
            ps.setString(3, Question);
            ps.setString(4, CorrectAnswer);
            ps.setString(5, IncorrectAnswer1);
            ps.setString(6, IncorrectAnswer2);
            ps.setString(7, IncorrectAnswer3);
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
    public static String updateQuestion(@FormDataParam("QuestionIDUpdated") Integer QuestionIDUpdated, @FormDataParam("CourseID") Integer CourseID, @FormDataParam("Question") String Question, @FormDataParam("CorrectAnswer") String CorrectAnswer, @FormDataParam("IncorrectAnswer1") String IncorrectAnswer1, @FormDataParam("IncorrectAnswer2") String IncorrectAnswer2, @FormDataParam("IncorrectAnswer3") String IncorrectAnswer3, @FormDataParam("QuestionID") Integer QuestionID){
        try{
            if (QuestionIDUpdated == null ||QuestionID == null || CourseID == null || Question == null || CorrectAnswer == null || IncorrectAnswer1 == null || IncorrectAnswer2 == null || IncorrectAnswer3 == null) {
                throw new Exception("One or more form data parameters are missing in the HTTP request");
            }
            System.out.println("courses/update QuestionIDUpdated=" + QuestionIDUpdated +"courses/update QuestionID=" + QuestionID + "courses/update CourseID=" + CourseID + "courses/update Question=" + Question + "courses/update CorrectAnswer=" + CorrectAnswer + "courses/update IncorrectAnswer1=" + IncorrectAnswer1 + "courses/update IncorrectAnswer2=" + IncorrectAnswer2 + "courses/update IncorrectAnswer3=" + IncorrectAnswer3);
            PreparedStatement ps = Main.db.prepareStatement("UPDATE Questions SET QuestionID = ?, CourseId = ?, Question = ?, CorrectAnswer = ?, IncorrectAnswer1 = ?, IncorrectAnswer2 = ?, IncorrectAnswer3 = ?  WHERE QuestionID = ?");
            ps.setInt(1, QuestionIDUpdated);
            ps.setInt(2, CourseID);
            ps.setString(3, Question);
            ps.setString(4, CorrectAnswer);
            ps.setString(5, IncorrectAnswer1);
            ps.setString(6, IncorrectAnswer2);
            ps.setString(7, IncorrectAnswer3);
            ps.setInt(8, QuestionID);
            ps.executeUpdate();
            return "[\"status\": \"OK\"}";
        } catch (Exception exception) {
            System.out.println("Database error" + exception.getMessage());
            return "[\"error\": \"Unable to create new item, please see server console for more info\"}";
        }
    }
    @POST
    @Path("delete")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public static String deleteQuestion(@FormDataParam("QuestionID") Integer QuestionID){
        try{
            if (QuestionID == null){
                throw new Exception("One or more form data parameters are missing in the HTTP request");
            }
            System.out.println("questions/delete QuestionID=" + QuestionID);
            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM Questions WHERE QuestionID = ?");
            ps.setInt(1, QuestionID);
            ps.executeUpdate();
            return "[\"status\": \"OK\"}";
        } catch (Exception exception) {
            System.out.println("Database error" + exception.getMessage());
            return "[\"error\": \"Unable to create new item, please see server console for more info\"}";
        }
        }
    }

