package Controllers;

import Server.Main;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AvatarDB {
    public static void selectAvatar(){
        try {
            PreparedStatement ps = Main.db.prepareStatement("SELECT StudentUsername, AvatarColour, AvatarImage, Hungry, HungryImage, Dirty, DirtyImage, Confused, ConfusedImage, HDImage, HCImage, DCImage, HCDImage FROM Avatar");
            // test pull works
            ResultSet results = ps.executeQuery();
            while (results.next()) {
                String StudentUsername = results.getString(1);
                String AvatarColour = results.getString(2);
                String AvatarImage = results.getString(3);
                int Hungry = results.getInt(4);
                String HungryImage = results.getString(5);
                int Dirty = results.getInt(6);
                String DirtyImage = results.getString(7);
                int Confused = results.getInt(8);
                String ConfusedImage = results.getString(9);
                String HDImage = results.getString(10);
                String HCImage = results.getString(11);
                String DCImage = results.getString(12);
                String HCDImage = results.getString(13);

                System.out.println("Student Username: " + StudentUsername + ", Avatar colour: " + AvatarColour + ", Avatar image: " + AvatarImage + ", Hungry score: " + Hungry + ", Hungry image: " + HungryImage);
                System.out.println("Dirty score: " + Dirty + ", Dirty image: " + DirtyImage + ", Confused score: " + Confused + ", Confused image: " + ConfusedImage);
                System.out.println("Hungry and Dirty image: " + HDImage + ", Hungry and Confused image: " + HCImage);
                System.out.println("Dirty and Confused image: " + DCImage + ", Hungry, Confused and Dirty Image:" + HCDImage);
            }
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
            System.out.println("Error. Something has gone wrong");
        }
    }
    public static void insertAvatar(String StudentUsername,String AvatarColour,String AvatarImage,String HungryImage,String DirtyImage,String ConfusedImage,String HDImage,String HCImage,String DCImage,String HCDImage) {
        try {
            PreparedStatement ps = Main.db.prepareStatement("INSERT INTO Avatar (StudentUsername, AvatarColour, AvatarImage, HungryImage, DirtyImage, ConfusedImage, HDImage, HCImage, DCImage, HCDImage) VALUES (?,?,?,?,?,?,?,?,?,?)");
            ps.setString(1, StudentUsername);
            ps.setString(2, AvatarColour);
            ps.setString(3, AvatarImage);
            ps.setString(4, HungryImage);
            ps.setString(5, DirtyImage);
            ps.setString(6, ConfusedImage);
            ps.setString(7, HDImage);
            ps.setString(8, HCImage);
            ps.setString(9, DCImage );
            ps.setString(10, HCDImage);

            ps.executeUpdate();
            System.out.println("Course added successfully");
        } catch (SQLException exception) {
            System.out.println(exception.getMessage());
            System.out.println("Error. Something has gone wrong");
        }

    }
    public static void updateAvatar(String StudentUsernameUpdated, String AvatarColour,String AvatarImage,String HungryImage,String DirtyImage,String ConfusedImage,String HDImage,String HCImage,String DCImage,String HCDImage, String StudentUsername){
        try{
            PreparedStatement ps = Main.db.prepareStatement("UPDATE Avatar SET StudentUsername = ?, AvatarColour = ?, AvatarImage = ?, HungryImage = ?, DirtyImage = ?, ConfusedImage = ?, HDImage = ?, HCImage = ?, DCImage = ?, HCDImage = ? WHERE StudentUsername = ?");
            ps.setString(1, StudentUsernameUpdated);
            ps.setString(2, AvatarColour);
            ps.setString(3, AvatarImage);
            ps.setString(4, HungryImage);
            ps.setString(5, DirtyImage);
            ps.setString(6, ConfusedImage);
            ps.setString(7, HDImage);
            ps.setString(8, HCImage);
            ps.setString(9, DCImage );
            ps.setString(10, HCDImage);
            ps.setString(11, StudentUsername);
            ps.executeUpdate();
        }catch (Exception exception){
            System.out.println(exception.getMessage());
            System.out.println("Error. Something has gone wrong");
        }
    }
    public static void deleteAvatar(String StudentUsername){
        try{
            PreparedStatement ps = Main.db.prepareStatement("DELETE FROM Avatar WHERE StudentUsername = ?");
            ps.setString(1, StudentUsername);
            ps.executeUpdate();
        }catch (SQLException exception) {
            System.out.println(exception.getMessage());
            System.out.println("Error. Something has gone wrong");
        }
    }
}
