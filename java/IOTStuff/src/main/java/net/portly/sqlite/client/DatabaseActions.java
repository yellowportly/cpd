package net.portly.sqlite.client;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author sqlitetutorial.net
 */
public class DatabaseActions {

	private String db;
	private Object connection;

	public DatabaseActions(String db) {
		this.db = db;
	}

	/**
	 * Connect to the test.db database
	 *
	 * @return the Connection object
	 */
	private Connection connect() {
		String url = "jdbc:sqlite:" + db; // C://sqlite/db/test.db";
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return conn;
	}

	/**
	 * Insert a new row into the warehouses table
	 *
	 * @param action
	 * @param value
	 */
	public void insert(String action, String value) {
		String sql = "INSERT INTO actions(id,action,value,date) VALUES(?,?,?,?)";
		long timestamp = new Date().getTime();
		Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dt = formatter.format(new Date(timestamp));
		
		try (Connection conn = this.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setLong(1, timestamp);
			pstmt.setString(2, action);
			pstmt.setString(3, value);
			pstmt.setString(4, dt);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public void doWork(String action, String value) {
		if (connection == null) {
			connection = connect();
		}

		if (connection != null) {
			insert(action, value);
		}
	}
}
