import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentsController {
    public static void selectStudent (){
        try{
            PreparedStatement ps = Main.db.prepareStatement("SELECT StudentName, StudentUsername, Password, AdultUsername, Level FROM Students");
            // test pull works
            ResultSet results = ps.executeQuery();
            while (results.next()){
                String StudentName = results.getString(1);
                String StudentUsername = results.getString(2);
                String password = results.getString(3);
                String AdultUsername = results.getString(4);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void insertStudent(String StudentName, String StudentUsername, String password, String AdultUsername){
        try{
            PreparedStatement ps = Main.db.prepareStatement( "INSERT INTO StudentS (StudentName, StudentUsername, password, AdultUsername) VALUES (?,?,?,?)");
        }catch (SQLException e){
            e.printStackTrace();}
        }
    }
}