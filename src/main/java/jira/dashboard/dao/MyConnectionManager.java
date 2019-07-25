package jira.dashboard.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class MyConnectionManager {
	private static Connection rnd_ts_conn;
	
	public static Connection getRndTsJiraConnection() {
		try {
			InputStream inputStream = MyConnectionManager.class.getResourceAsStream("/db.properties");
			Properties properties = new Properties();
			properties.load(inputStream);
			
			String RND_TS_DRIVER = (String) properties.get("rnd_ts_driver");
			String RND_TS_URL = (String) properties.get("rnd_ts_url");
			String RND_TS_DBUSER = (String) properties.get("rnd_ts_dbuser");
			String RND_TS_DBPASS = (String) properties.get("rnd_ts_dbpass");
			
			Class.forName(RND_TS_DRIVER);
			rnd_ts_conn = DriverManager.getConnection(RND_TS_URL, RND_TS_DBUSER, RND_TS_DBPASS);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return rnd_ts_conn;
	}
}
