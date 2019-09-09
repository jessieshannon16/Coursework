import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdultsDB {
    public static void selectAdult(){
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT AdultUsername, AdultName, Password, NoOfStudents FROM Adults");
            // test pull works
            ResultSet results = ps.executeQuery();
            while (results.next()) {
                String AdultUsername = results.getString(1);
                String AdultName = results.getString(2);
                String Password = results.getString(3);
                int NoOfStudents = results.getInt(4);
                System.out.println("Username: " + AdultUsername + ", Name: " + AdultName + ", Password: " + Password + ", Number of Students: " + NoOfStudents);
            }
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
            System.out.println("Error. Something has gone wrong");
        }
    }
    public static void insertAdult(String AdultUsername, String AdultName, String Password) {
        try {
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Adults (AdultUsername, AdultName, Password) VALUES (?,?,?)");
            ps.setString(1, AdultUsername);
            ps.setString(2, AdultName);
            ps.setString(3, Password);
            ps.executeUpdate();
            System.out.println("Adults account added successfully");
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
            System.out.println("Error. Something has gone wrong");
        }

    }
    public static void updateAdults(String AdultUsernameUpdated, String AdultName, String Password, int NoOfStudents, String AdultUsername){
        try{
            PreparedStatement ps = Main.db.prepareStatement("UPDATE Adults SET AdultUsername = ?, AdultName = ?, Password = ?, NoOfStudents = ? WHERE AdultUsername = ?");
            ps.setString(1, AdultUsernameUpdated);
            ps.setString(2, AdultName);
            ps.setString(3, Password);
            ps.setInt(4, NoOfStudents);
            ps.setString(5, AdultUsername);
            ps.executeUpdate();
        }catch (Exception exception){
            System.out.println(exception.getMessage());
            System.out.println("Error. Something has gone wrong");
        }
    }
    public static void deleteStudent(String AdultUsername){
        try{
            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM Adults WHERE AdultUsername = ?");
            ps.setString(1, AdultUsername);
            ps.executeUpdate();
        }catch (SQLException exception) {
            System.out.println(exception.getMessage());
            System.out.println("Error. Something has gone wrong");
        }
    }
}
