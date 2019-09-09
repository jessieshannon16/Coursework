import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


    public class StudentsDB {
        public static void selectStudent() {
            try {
                PreparedStatement ps = Main.db.prepareStatement("SELECT StudentUsername, StudentName, Password, AdultUsername, Level FROM Students ");
                // test pull works
                ResultSet results = ps.executeQuery();
                while (results.next()) {
                    String StudentUsername = results.getString(1);
                    String StudentName = results.getString(2);
                    String Password = results.getString(3);
                    String AdultUsername = results.getString(4);
System.out.println("Username: " + StudentUsername + ", Name: " + StudentName + ", Password: " + Password + ", Parent/ Teachers Username: " + AdultUsername);
                }
            } catch (SQLException exception) {
                System.out.println(exception.getMessage());
                System.out.println("Error. Something has gone wrong");
            }
        }

        public static void insertStudent(String StudentName, String StudentUsername, String Password, String AdultUsername) {
            try {
                PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Students (StudentName, StudentUsername, Password, AdultUsername, level) VALUES (?,?,?,?,0)");
                ps.setString(1, StudentName);
                ps.setString(2, StudentUsername);
                ps.setString(3, Password);
                ps.setString(4, AdultUsername);
                ps.executeUpdate();
                System.out.println("Student account added successfully");
            } catch (SQLException exception) {
                System.out.println(exception.getMessage());
                System.out.println("Error. Something has gone wrong");
            }

        }
        public static void updateStudent(String StudentName, String StudentUsernameUpdated, String Password, String AdultUsername, String StudentUsername){
            try{
                PreparedStatement ps = Main.db.prepareStatement("UPDATE Students SET StudentName = ?, StudentUsername = ?, Password = ?, AdultUsername = ? WHERE StudentUsername = ?");
                ps.setString(1, StudentName);
                ps.setString(2, StudentUsernameUpdated);
                ps.setString(3, Password);
                ps.setString(4, AdultUsername);
                ps.setString(5, StudentUsername);
                ps.executeUpdate();
            }catch (Exception exception){
                System.out.println(exception.getMessage());
                System.out.println("Error. Something has gone wrong");
            }
        }
        public static void deleteStudent(String StudentUsername){
            try{
                PreparedStatement ps = Main.db.prepareStatement("DELETE FROM Students WHERE StudentUsername = ?");
                ps.setString(1, StudentUsername);
                ps.executeUpdate();
            }catch (SQLException exception) {
                System.out.println(exception.getMessage());
                System.out.println("Error. Something has gone wrong");
            }
        }

    }