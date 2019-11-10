import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QuestionsDB {
    public static void selectQuestion() {
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT QuestionID, CourseID, Question, CorrectAnswer, IncorrectAnswer1, IncorrectAnswer2, IncorrectAnswer3 FROM Questions");
            // test pull works
            ResultSet results = ps.executeQuery();
            while (results.next()) {
                int QuestionID = results.getInt(1);
                int CourseID = results.getInt(2);
                String Question = results.getString(3);
                String CorrectAnswer = results.getString(4);
                String IncorrectAnswer1 = results.getString(5);
                String IncorrectAnswer2 = results.getString(6);
                String IncorrectAnswer3 = results.getString(7);
                System.out.println("Question ID: " + QuestionID + ", Course ID: " + CourseID + ", Question: " + Question + ", Correct Answer: " + CorrectAnswer + ", Incorrect Answer 1: " + IncorrectAnswer1 + ", Incorrect Answer 2: " + IncorrectAnswer2 + ", Incorrect answer 3: " + IncorrectAnswer3);
            }
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
            System.out.println("Error. Something has gone wrong");
        }
    }

    public static void insertQuestion(int QuestionID, int CourseID, String Question, String CorrectAnswer, String IncorrectAnswer1, String IncorrectAnswer2, String IncorrectAnswer3) {
        try {
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Questions (QuestionID, CourseID, Question, CorrectAnswer, IncorrectAnswer1, IncorrectAnswer2, IncorrectAnswer3) VALUES (?,?,?,?,?,?,?)");
            ps.setInt(1, QuestionID);
            ps.setInt(2, CourseID);
            ps.setString(3, Question);
            ps.setString(4, CorrectAnswer);
            ps.setString(5, IncorrectAnswer1);
            ps.setString(6, IncorrectAnswer2);
            ps.setString(7, IncorrectAnswer3);
            ps.executeUpdate();
            System.out.println("Question added successfully");
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
            System.out.println("Error. Something has gone wrong");
        }

    }
    public static void updateQuestion(int QuestionIDUpdated, int CourseID, String Question, String CorrectAnswer, String IncorrectAnswer1, String IncorrectAnswer2, String IncorrectAnswer3, int QuestionID){
        try{
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
        }catch (Exception exception){
            System.out.println(exception.getMessage());
            System.out.println("Error. Something has gone wrong");
        }
    }
    public static void deleteQuestion(int QuestionID){
        try{
            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM Questions WHERE QuestionID = ?");
            ps.setInt(1, QuestionID);
            ps.executeUpdate();
        }catch (SQLException exception) {
            System.out.println(exception.getMessage());
            System.out.println("Error. Something has gone wrong");
        }
    }
}
