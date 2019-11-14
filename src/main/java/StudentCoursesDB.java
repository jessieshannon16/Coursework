import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentCoursesDB {
    public static void selectStudentCourse() {
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT StudentUsername, CourseID, LastDate FROM StudentCourses");
            // test pull works
            ResultSet results = ps.executeQuery();
            while (results.next()) {
                String StudentUsername = results.getString(1);
                int CourseID = results.getInt(2);
                String LastDate = results.getString(3);
System.out.println("Student username: " + StudentUsername + ", Course ID: " + CourseID + ", Last date: " + LastDate );
            }
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
            System.out.println("Error. Something has gone wrong");
        }
    }

    public static void insertStudentCourse(String StudentUsername, int CourseID) {
        try {
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO StudentCourses (StudentUsername, CourseID) VALUES (?,?)");
            ps.setString(1, StudentUsername);
            ps.setInt(2, CourseID);
            ps.executeUpdate();
            System.out.println("Student course added successfully");
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
            System.out.println("Error. Something has gone wrong");
        }

    }
    public static void updateStudentCourse(String StudentUsernameUpdated, int CourseID, String StudentUsername){
        try{
            PreparedStatement ps = Main.db.prepareStatement("UPDATE StudentCourses SET StudentUsername = ?, CourseID = ? WHERE StudentUsername = ?");
            ps.setString(1, StudentUsernameUpdated);
            ps.setInt(2, CourseID);
            ps.setString(3, StudentUsername);
            ps.executeUpdate();
        }catch (Exception exception){
            System.out.println(exception.getMessage());
            System.out.println("Error. Something has gone wrong");
        }
    }
    public static void deleteStudentCourse(String StudentUsername){
        try{
            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM StudentCourses WHERE StudentUsername = ?");
            ps.setString(1, StudentUsername);
            ps.executeUpdate();
        }catch (SQLException exception) {
            System.out.println(exception.getMessage());
            System.out.println("Error. Something has gone wrong");
        }
    }
}
