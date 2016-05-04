package database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AccessDatabase {
	
	public List<String> getAllMovieNames() {
		Connection conn = SQLiteJDBC.getConnection("jdbc:sqlite::resource:movie.sqlite");
		Statement stmt = null;
		ResultSet rs = null;

		List<String> movieNames = new ArrayList<String>();
		
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT name from Movie");
			while(rs.next()) {
				movieNames.add(rs.getString(1));
			}
			return movieNames;
		}
		catch (SQLException e) {
			return null;
		}
	}
	
	public List<String> getAllAlbumNames() {
		Connection conn = SQLiteJDBC.getConnection("jdbc:sqlite::resource:music.sqlite");
		Statement stmt = null;
		ResultSet rs = null;

		List<String> albumNames = new ArrayList<String>();
		
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT name from Album");
			while(rs.next()) {
				albumNames.add(rs.getString(1));
			}
			return albumNames;
		}
		catch (SQLException e) {
			return null;
		}
	}
	
	public List<String> getAllTrackNames() {
		Connection conn = SQLiteJDBC.getConnection("jdbc:sqlite::resource:music.sqlite");
		Statement stmt = null;
		ResultSet rs = null;

		List<String> trackNames = new ArrayList<String>();
		
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT name from Track");
			while(rs.next()) {
				trackNames.add(rs.getString(1));
			}
			return trackNames;
		}
		catch (SQLException e) {
			return null;
		}
	}
	
	public List<String> runGeographyWhQuery(String query) {
		Connection conn = SQLiteJDBC.getConnection("jdbc:sqlite::resource:geography.sqlite");
		Statement stmt = null;
		ResultSet rs = null;

		List<String> listOfResults = new ArrayList<String>();
		
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			while(rs.next()) {
				listOfResults.add(rs.getString(1));
			}
			return listOfResults;
		}
		catch (SQLException e) {
			return null;
		}
	}
	
	public String runGeographyYesNoQuery(String query) {
		Connection conn = SQLiteJDBC.getConnection("jdbc:sqlite::resource:geography.sqlite");
		Statement stmt = null;
		ResultSet rs = null;

		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			return rs.getString(1);
		} 

		catch (SQLException e) {
			return "I don't know!";
		}
	}
	
	public String runMovieYesNoQuery(String query){
		
		Connection conn = SQLiteJDBC.getConnection("jdbc:sqlite::resource:movie.sqlite");
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			return rs.getString(1);
		} 

		catch (SQLException e) {
			return "I don't know!";
		}
	}

	public List<String> runMovieWhQuery(String query) {
		Connection conn = SQLiteJDBC.getConnection("jdbc:sqlite::resource:movie.sqlite");
		Statement stmt = null;
		ResultSet rs = null;

		List<String> listOfResults = new ArrayList<String>();
		
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			while(rs.next()) {
				listOfResults.add(rs.getString(1));
			}
			return listOfResults;
		}
		catch (SQLException e) {
			return null;
		}
	}

	public String runMusicQuery(String query) {
		Connection conn = SQLiteJDBC.getConnection("jdbc:sqlite::resource:music.sqlite");
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(query);
			return rs.getString(1);
		} 	

		catch (SQLException e) {
			return "I don't know!";
		}
	}
}
