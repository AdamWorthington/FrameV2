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
	 * Used to store image attributes in one easy place
	 */
	public class ImageAttributeHolder {
		int ID;
		double latitude;
		double longitude;
		String user;
		String date;
		
		public ImageAttributeHolder(int ID, double latitude, double longitude, String user, String date) {
			this.ID = ID;
			this.latitude = latitude;
			this.longitude = longitude;
			this.user = user;
			this.date = date;
		}
	}
	
	/*
	 * Post an image and its attributes to the database
	 */
	public static boolean postImage(Connection conn, String picture, String user, double lat, double lon) {
		
		/*
		 * Columns: int ID, varchar Image, double Latitude, double Longitude, varchar User, timestamp Date
		 */
		
		PreparedStatement stmt = null;
		String query = "INSERT INTO Media (ID, Image, Latitude, Longitude, User, Date) VALUES (NULL, ?, ?, ?, ?, NULL);";
		
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
	public static ArrayList<ImageAttributeHolder> getAttributes(Connection conn, int[] ids) {
		ArrayList<ImageAttributeHolder> ret = new ArrayList<ImageAttributeHolder>();
		
		PreparedStatement stmt = null;
		String query = "SELECT ID, Latitude, Longitude, User, Date FROM Media WHERE ";
		String idEquals = "ID = ? ";
		int numberOfRequestedIDs = ids.length;
		
		for (int i = 0; i < numberOfRequestedIDs; i++) {
			query += idEquals;
		}
		
		try {
			stmt = conn.prepareStatement(query);
			
			for (int i = 0; i < numberOfRequestedIDs; i++) {
				stmt.setInt(i + 1, ids[i]);
			}
			
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
				
				ImageAttributeHolder iah = sql.new ImageAttributeHolder(ID, latitude, longitude, user, date);
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
		String query = "SELECT Image FROM Media WHERE ID = ?;";
		
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
