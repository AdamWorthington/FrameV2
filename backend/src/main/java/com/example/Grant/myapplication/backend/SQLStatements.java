package com.example.Grant.myapplication.backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SQLStatements {
	
	/*
	 * Used to initialize an ImageAttributeHolder
	 */
	public SQLStatements() {
		
	}


    /*
     * Create a connection to access the database
     */
    public static Connection createConnection() {

        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.GoogleDriver");
            conn = DriverManager.getConnection(DatabaseInformation.url3);
			//conn = DriverManager.getConnection(DatabaseInformation.url2, DatabaseInformation.username, DatabaseInformation.password);
        }
        catch (ClassNotFoundException e) {
            System.err.println("Error generating connection in createConnection (ClassNotFound)");
            System.err.println(e.getMessage());
            return null;
        }
        catch (SQLException e) {
            System.err.println("Error generating connection in createConnection (SQLException)");
            System.err.println(e.getMessage());
			System.err.println(e.getStackTrace().toString());
            return null;
        }
        return conn;
    }
	
	/*
	 * Post an image and its attributes to the database
	 */
	public static boolean postImage(Connection conn, String picture, String user, double lat, double lon) {
		
		/*
		 * table Media: int ID, varchar Image, double Latitude, double Longitude, varchar User, timestamp Date
		 */
		System.err.println("Picture: " + picture + " user: " + user + " lat: " + lat + " lng: " + lon);
		PreparedStatement stmt = null;
		String query = "INSERT INTO FrameV2.Media (ID, Image, Latitude, Longitude, User, Date) VALUES (NULL, ?, ?, ?, ?, CURRENT_TIMESTAMP);";
		
		try {
			stmt = conn.prepareStatement(query);
			stmt.setString(1, picture);
			stmt.setDouble(2, lat);
			stmt.setDouble(3, lon);
			stmt.setString(4, user);
			
			int stmtReturn = stmt.executeUpdate();
		}
		catch(SQLException e) {
			System.err.println("Error posting picture to table Media");
			System.err.println(e.getMessage());
			
			return false;
		}
		
		return true;
	}
	
	/*
	 * Retrieve all information about an image except for the image itself.
	 * Accessed by passing in the ID of the image
	 */
	public static ArrayList<ImageAttributeHolder> getAttributes(Connection conn) {
		ArrayList<ImageAttributeHolder> ret = new ArrayList<ImageAttributeHolder>();
		
		PreparedStatement stmt = null;
		String query = "SELECT ID, Latitude, Longitude, User, Date FROM FrameV2.Media";
		
		try {
			stmt = conn.prepareStatement(query);
			
			ResultSet rs = stmt.executeQuery();
			
			int ID;
			double latitude;
			double longitude;
			String user;
			String date;
			SQLStatements sql = new SQLStatements();
			while (rs.next()) {
				ID = rs.getInt("ID");
				latitude = rs.getDouble("Latitude");
				longitude = rs.getDouble("Longitude");
				user = rs.getString("User");
				date = rs.getString("Date");
				
				ImageAttributeHolder iah = new ImageAttributeHolder(ID, latitude, longitude, user, date);
				ret.add(iah);
			}
		}
		catch(SQLException e) {
			System.err.println("Error retrieving attributes from table Media");
			System.err.println(e.getMessage());
			
			return null;
		}
		
		return ret;
	}
	
	/*
	 * Retrieve solely the image itself
	 * Accessed by passing in the image's ID
	 */
	public static String getImage(Connection conn, int id) {
		
		String ret = null;
		PreparedStatement stmt = null;
		String query = "SELECT Image FROM FrameV2.Media WHERE ID = ?;";
		
		try {
			stmt = conn.prepareStatement(query);
			
			stmt.setInt(1, id);
			
			ResultSet rs = stmt.executeQuery();
			
			rs.next();
			ret = rs.getString("Image");
		}
		catch(SQLException e) {
			System.err.println("Error getting picture from table Media");
			System.err.println(e.getMessage());
			
			return null;
		}
		
		return ret;
	}

	/*
    public static void main(String[] argv) {

        Connection conn = createConnection();

        if (conn == null) {
            System.out.println("Connection is Null");
            System.exit(1);
        }

        postImage(conn, "testing", "testing", 1.5, 4.5);
    }
    */
}
