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
        import java.util.ArrayList;
        import java.util.Random;
@Path("avatarstats/")
public class AvatarStatsDB {

    @POST
    @Path("setName")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public static String setName(@CookieParam("token") String token, @FormDataParam("avatarName") String AvatarName, @CookieParam("username") String StudentUsername) {
        System.out.println("avatarstats/setName");
        if (!StudentsDB.validToken(token)) {
            return "{\"error\": \"You don't appear to be logged in.\"}";
        }
        try {
            PreparedStatement ps = Main.db.prepareStatement("UPDATE Students SET AvatarName = ? WHERE StudentUsername = ?");
            ps.setString(1, AvatarName);
            ps.setString(2, StudentUsername);
            ps.executeUpdate();
            JSONObject response = new JSONObject();
            response.put("status", "Avatar name updated sucessfully!");
            return response.toString();

        } catch (Exception exception) {
            System.out.println("Database error: " + (exception.getMessage()));
            return "{\"error\": \"Unable to set avatar name\"}";
            // System.out.println("Error. Something has gone wrong");
        }
    }

    @GET
    @Path("name")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public static String name(@CookieParam("username") String StudentUsername, @CookieParam("token") String token) {
        System.out.println("avatarstats/name");
        if (!StudentsDB.validToken(token)) {
            return "{\"error\": \"You don't appear to be logged in.\"}";
        }
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT AvatarName FROM Students WHERE StudentUsername = ?");
            ps.setString(1, StudentUsername);
            ResultSet results = ps.executeQuery();

            if (results.next()) {
                JSONObject item = new JSONObject();
                item.put("AvatarName", results.getString(1));
                return item.toString();
            } else {
                return "{\"error\": \"No username!\"}";
            }

        } catch (Exception exception) {
            System.out.println("Database error: " + (exception.getMessage()));
            return "{\"error\": \"Unable to feed avatar, please see server console for more info.\"}";
            // System.out.println("Error. Something has gone wrong");
        }

    }

    public static Integer randomQuestion(String username) {
        try {
            //int[] quizArray = {0};
            ArrayList<Integer> quizArray = new ArrayList<Integer>();
            PreparedStatement ps = Main.db.prepareStatement("SELECT Questions.QuestionID FROM Questions INNER JOIN StudentCourses ON Questions.CourseID = StudentCourses.CourseID WHERE StudentCourses.StudentUsername = ?");
            ps.setString(1, username);
            ResultSet id = ps.executeQuery();

            while (id.next()) {
                //quizArray[i] = id.getInt(1);
                quizArray.add(id.getInt(1));
                //System.out.println(id.getInt(1));
            }
            Random rand = new Random();
            //System.out.println(rand.nextInt(quizArray.size() - 1));
            int value = rand.nextInt(quizArray.size());
            return quizArray.get(value);

        } catch (Exception exception) {
            System.out.println("Database error: " + (exception.getMessage()));
            return 0;
        }
    }

    @GET
    @Path("question")
    @Produces(MediaType.APPLICATION_JSON)
    public static String question(@CookieParam("token") String token, @CookieParam("username") String username, @FormDataParam("questionID")Integer QuestionID) {
        if (!StudentsDB.validToken(token)) {
            return "{\"error\": \"You don't appear to be logged in.\"}";
        }
        int questionId;

        try {
            do {
                questionId = randomQuestion(username);
                if (questionId == 0) {
                    return "{\"error\": \"You don't appear to have any courses.\"}";
                }
            }while (questionId == QuestionID);

            PreparedStatement question = Main.db.prepareStatement("SELECT * FROM Questions WHERE QuestionID = ?");
            question.setInt(1,questionId);
            ResultSet questions = question.executeQuery();

            int courseID = questions.getInt(2);

            PreparedStatement name = Main.db.prepareStatement("SELECT CourseName FROM Courses WHERE CourseID = ?");
            name.setInt(1,courseID);
            ResultSet courseId = name.executeQuery();

            if (questions.next() && courseId.next()) {

                JSONObject item = new JSONObject();
                item.put("CourseName", courseId.getString(1));
                item.put("QuestionID", questions.getInt(1));
                item.put("CourseID", questions.getInt(2));
                item.put("Question", questions.getString(3));
                item.put("CorrectAnswer", questions.getString(4));
                item.put("IncorrectAnswer1", questions.getString(5));
                item.put("IncorrectAnswer2", questions.getString(6));
                item.put("IncorrectAnswer3", questions.getString(7));

                return item.toString();
            }
            return "{\"error\": \"There is no questions attached to this id\"}";

        } catch (Exception exception) {
            System.out.println("Database error: " + (exception.getMessage()));
            return "{\"error\": \"Unable to feed avatar, please see server console for more info.\"}";
            // System.out.println("Error. Something has gone wrong");
        }
    }

    @POST
    @Path("score")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public static String score(@CookieParam("token")String token,@CookieParam("username") String StudentUsername, @FormDataParam("CourseID") Integer CourseID){
        if (!StudentsDB.validToken(token)) {
            return "{\"error\": \"You don't appear to be logged in.\"}";
        }
        try {
            if (CourseID == null){
                throw new Exception("One or more form data parameters are missing in the HTTP request");
            }

            PreparedStatement select = Main.db.prepareStatement("SELECT Score FROM StudentCourses WHERE StudentUsername = ? AND CourseID = ?");
            select.setString(1, StudentUsername);
            select.setInt(2, CourseID);
            ResultSet score = select.executeQuery();
            int newScore = score.getInt(1) + 1;


            PreparedStatement update = Main.db.prepareStatement("UPDATE StudentCourses SET Score = ? WHERE StudentUsername = ? AND CourseID = ?");
            update.setInt(1,newScore);
            update.setString(2, StudentUsername);
            update.setInt(3, CourseID);
            update.executeUpdate();


            return "{\"success!\":\"Score updated!\"}";

        }catch (Exception exception) {
            System.out.println("Database error: " + (exception.getMessage()));
            return "{\"error\": \"Unable to update score, please see server console for more info.\"}";
            // System.out.println("Error. Something has gone wrong");
        }

    }
    @POST
    @Path("learn")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.MULTIPART_FORM_DATA)

    public static String learn(@CookieParam("token")String token,@CookieParam("username") String StudentUsername, @FormDataParam("Score") Integer intelligence){
        System.out.println("avatarstats/learn");
        if (!StudentsDB.validToken(token)) {
            return "{\"error\": \"You don't appear to be logged in.\"}";
        }
        try {
            if (intelligence == null){
                throw new Exception("One or more form data parameters are missing in the HTTP request");
            }

            PreparedStatement select = Main.db.prepareStatement("SELECT Intelligence FROM Students WHERE StudentUsername = ?");
            select.setString(1, StudentUsername);
            ResultSet intelligent = select.executeQuery();
            int newScore = intelligent.getInt(1) + (10*intelligence);
            if(newScore >100){
                newScore = 100;
            }

            PreparedStatement update = Main.db.prepareStatement("UPDATE Students SET Intelligence = ? WHERE StudentUsername = ?");
            update.setInt(1,newScore);
            update.setString(2, StudentUsername);

            return "{\"success!\":\"Your avatar is now less dumb!\"}";

        }catch (Exception exception) {
            System.out.println("Database error: " + (exception.getMessage()));
            return "{\"error\": \"Unable to feed avatar, please see server console for more info.\"}";
            // System.out.println("Error. Something has gone wrong");
        }

    }
    @GET
    @Path("feed")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public static String feed(@CookieParam("token") String token, @CookieParam("username") String StudentUsername){
        System.out.println("avatarstats/feed");
        if (!StudentsDB.validToken(token)) {
            return "{\"error\": \"You don't appear to be logged in.\"}";
        }
        try {
            if (StudentUsername == null){
                throw new Exception("One or more form data parameters are missing in the HTTP request");
            }

            PreparedStatement select = Main.db.prepareStatement("SELECT Hunger FROM Students WHERE StudentUsername = ?");
            select.setString(1, StudentUsername);
            ResultSet hunger = select.executeQuery();
            int newScore = hunger.getInt(1) + 10;
            if(newScore >100){
                newScore = 100;
            }

            PreparedStatement update = Main.db.prepareStatement("UPDATE Students SET Hunger = ? WHERE StudentUsername = ?");
            update.setInt(1,newScore);
            update.setString(2, StudentUsername);
            update.executeUpdate();

            return "{\"success!\":\"Your avatar is now less hungry!\"}";

        }catch (Exception exception) {
            System.out.println("Database error: " + (exception.getMessage()));
            return "{\"error\": \"Unable to feed avatar, please see server console for more info.\"}";
            // System.out.println("Error. Something has gone wrong");
        }

    }
    @GET
    @Path("clean")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.MULTIPART_FORM_DATA)

    public static String clean(@CookieParam("username") String StudentUsername, @CookieParam("token")String token){
        System.out.println("avatarstats/clean");

        try {
            if (StudentUsername == null){
                throw new Exception("One or more form data parameters are missing in the HTTP request");
            }

            PreparedStatement select = Main.db.prepareStatement("SELECT Cleanliness FROM Students WHERE StudentUsername = ?");
            select.setString(1, StudentUsername);
            ResultSet cleanliness = select.executeQuery();
            int newScore = cleanliness.getInt(1) + 10;
            if(newScore >100){
                newScore = 100;
            }

            PreparedStatement update = Main.db.prepareStatement("UPDATE Students SET Cleanliness = ? WHERE StudentUsername = ?");
            update.setInt(1,newScore);
            update.setString(2, StudentUsername);
            update.executeUpdate();

            return "{\"success!\":\"Your avatar is now cleaner!\"}";

        }catch (Exception exception) {
            System.out.println("Database error: " + (exception.getMessage()));
            return "{\"error\": \"Unable to clean avatar, please see server console for more info.\"}";
            // System.out.println("Error. Something has gone wrong");
        }

    }
    @GET
    @Path("view")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.MULTIPART_FORM_DATA)

    public static String view(@CookieParam("username") String StudentUsername){
        System.out.println("avatarstats/view");

        try{
            if (StudentUsername == null){
                throw new Exception("One or more form data parameters are missing in the HTTP request");
            }
            PreparedStatement ps = Main.db.prepareStatement("SELECT Hunger, Cleanliness, Intelligence, AvatarName FROM Students WHERE StudentUsername = ?");
            ps.setString(1,StudentUsername);
            ResultSet results = ps.executeQuery();
            PreparedStatement ps2 = Main.db.prepareStatement("SELECT AvatarImage, HungryImage, DirtyImage, ConfusedImage, HDImage, HCImage, DCImage, HCDImage FROM AvatarType WHERE AvatarID = (SELECT AvatarID FROM Students WHERE StudentUsername = ?)");
            ps2.setString(1,StudentUsername);
            ResultSet image = ps2.executeQuery();

            if(results.next() && image.next()){
                JSONObject item = new JSONObject();

                int hunger = results.getInt(1);
                int cleanliness = results.getInt(2);
                int intelligence = results.getInt(3);

                if (hunger<80 && cleanliness<80 && intelligence<80) {
                    item.put("Image", image.getString(8));
                }else if (hunger<80 && cleanliness<80) {
                    item.put("Image", image.getString(5));
                }else if(hunger<80 && intelligence<80){
                    item.put("Image", image.getString(6));
                }else if(cleanliness<80 && intelligence<80){
                    item.put("Image", image.getString(7));
                }else if (cleanliness<80){
                    item.put("Image", image.getString(3));
                }else if(hunger<80){
                    item.put("Image", image.getString(2));
                }else if (intelligence<80){
                    item.put("Image", image.getString(4));
                }else{
                    item.put("Image", image.getString(1));
                }

                item.put("Hunger", results.getInt(1));
                item.put("Cleanliness", results.getInt(2));
                item.put("Intelligence", results.getInt(3));
                item.put("AvatarName", results.getString(4));

                return item.toString();

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
