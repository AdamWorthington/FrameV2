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
	 * Posts a comment to the database for a specific post
	 */
	public static boolean postComment(Connection conn, int postID, String comment, String user) {
		/*
		 * table Comments: int CommentID, int PostID, varchar Comment, varchar User
		 */
		PreparedStatement stmt = null;
		String query = "INSERT INTO FrameV2.Comments (CommentID, PostID, Comment, User) Values (NULL, ?, ?, ?);";

		try {
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, postID);
			stmt.setString(2, comment);
			stmt.setString(3, user);
			int stmtReturn = stmt.executeUpdate();
		}
		catch (SQLException e) {
			System.err.println("Error posting comment to table Comments");
			System.err.println(e.getMessage());

			return false;
		}

		return true;
	}

	/*
	 * Post an image and its attributes to the database
	 */
	public static boolean postImage(Connection conn, String picture, String user, double lat, double lon, String caption) {
		
		/*
		 * table Media: int ID, varchar Image, double Latitude, double Longitude, varchar User, timestamp Date
		 */
		System.err.println("Picture: " + picture + " user: " + user + " lat: " + lat + " lng: " + lon);
		PreparedStatement stmt = null;
		String query = "INSERT INTO FrameV2.Media (ID, Image, IsVideo, Latitude, Longitude, User, Votes, Blobkey, Date, Caption) VALUES (NULL, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, ?);";


		try {
			stmt = conn.prepareStatement(query);
			stmt.setString(1, picture);
			stmt.setInt(2, 0);
			stmt.setDouble(3, lat);
			stmt.setDouble(4, lon);
			stmt.setString(5, user);
			stmt.setInt(6, 0);
			stmt.setString(7, "");
			stmt.setString(8, caption);

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
	 * Post a video and its attributes to the database
	 */
	public static boolean postVideo(Connection conn, String user, double lat, double lon, String blobkey, String Caption) {

		/*
		 * table Media: int ID, varchar Image, double Latitude, double Longitude, varchar User, timestamp Date
		 */
		System.err.println("Blobkey: " + blobkey + " user: " + user + " lat: " + lat + " lng: " + lon);
		PreparedStatement stmt = null;
		String query = "INSERT INTO FrameV2.Media (ID, Image, IsVideo, Latitude, Longitude, User, Votes, Blobkey, Date) VALUES (NULL, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, ?);";

		try {
			stmt = conn.prepareStatement(query);
			stmt.setString(1, "");
			stmt.setInt(2, 1);
			stmt.setDouble(3, lat);
			stmt.setDouble(4, lon);
			stmt.setString(5, user);
			stmt.setInt(6, 0);
			stmt.setString(7, blobkey);
			stmt.setString(8, Caption);

			int stmtReturn = stmt.executeUpdate();
		}
		catch(SQLException e) {
			System.err.println("Error posting image to table Media");
			System.err.println(e.getMessage());

			return false;
		}

		return true;
	}

	/*
	 * Update the number of likes on a post
	 */
	public static boolean updateLikes(Connection conn, int postID, int likes) {
		PreparedStatement stmt = null;
		String query = "UPDATE FrameV2.Media SET Votes = ? WHERE ID = ?";

		try {
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, likes);
			stmt.setInt(2, postID);

			int stmtReturn = stmt.executeUpdate();
		}
		catch(SQLException e) {
			System.err.println("Error updating votes in table Media");
			System.err.println(e.getMessage());

			return false;
		}

		return true;
	}

	/*
	 *  Retrieves all comments for a specific post
	 */
	public static ArrayList<Comment> getComments(Connection conn, int postID) {
		ArrayList<Comment> ret = new ArrayList<Comment>();

		PreparedStatement stmt = null;
		String query = "SELECT Comment, User FROM FrameV2.Comments WHERE PostID=?;";

		try {
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, postID);

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				String comment = rs.getString("Comment");
				String user = rs.getString("User");

				Comment c = new Comment(postID, comment, user);
				ret.add(c);
			}
		}
		catch (SQLException e) {
			System.err.println("Error retrieving comments from table Comments (PostID " + postID + ")");
			System.err.println(e.getMessage());

			return null;
		}

		return ret;
	}

	/*
	 * Retrieve all information about an image except for the image itself.
	 * Accessed by passing in the ID of the image
	 *
	 * Can also specify a user to filter by, returning only posts by that user
	 */
	public static ArrayList<ImageAttributeHolder> getAttributes(Connection conn, String userIdentifier) {
		ArrayList<ImageAttributeHolder> ret = new ArrayList<ImageAttributeHolder>();

		PreparedStatement stmt = null;

		String query = "SELECT ID, IsVideo, Latitude, Longitude, User, Votes, Blobkey, Date, Caption FROM FrameV2.Media";

		if (userIdentifier != null) {
			query += " WHERE User = ?";
		}


		try {
			stmt = conn.prepareStatement(query);

			if (userIdentifier != null) {
				stmt.setString(1, userIdentifier);
			}

			ResultSet rs = stmt.executeQuery();

			int ID;
			int isVideo;
			double latitude;
			double longitude;
			String user;
			int votes;
			String blobkey;
			String date;
			String caption;
			SQLStatements sql = new SQLStatements();

			while (rs.next()) {
				ID = rs.getInt("ID");
				isVideo = rs.getInt("isVideo");
				latitude = rs.getDouble("Latitude");
				longitude = rs.getDouble("Longitude");
				user = rs.getString("User");
				votes = rs.getInt("Votes");
				blobkey = rs.getString("Blobkey");
				date = rs.getString("Date");
				caption = rs.getString("Caption");
				
				ImageAttributeHolder iah = new ImageAttributeHolder(ID, isVideo, latitude, longitude, user, votes, blobkey, date, caption);

				ret.add(iah);
			}

			return ret;
		}
		catch(SQLException e) {
			System.err.println("Error retrieving attributes from table Media");
			System.err.println(e.getMessage());

			return null;
		}
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

}
