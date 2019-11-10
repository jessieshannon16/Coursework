import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CoursesDB {
    public static void selectCourse(){
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT CourseID, CourseName FROM Courses");
            // test pull works
            ResultSet results = ps.executeQuery();
            while (results.next()) {
                int CourseID = results.getInt(1);
                String CourseName = results.getString(2);
                System.out.println("Course ID: " + CourseID + ", Course name: " + CourseName);
            }
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
            System.out.println("Error. Something has gone wrong");
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
