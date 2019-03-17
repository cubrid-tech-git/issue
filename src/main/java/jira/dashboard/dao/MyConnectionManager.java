package jira.dashboard.dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class MyConnectionManager {
	private static String RND_DRIVER;
	private static String RND_URL;
	private static String RND_DBUSER;
	private static String RND_DBPASS;

	private static String TECH_DRIVER;
	private static String TECH_URL;
	private static String TECH_DBUSER;
	private static String TECH_DBPASS;

	private static Connection rnd_conn;
	private static Connection tech_conn;

	public static Connection getRndJiraConnection() {
		try {
			InputStream inputStream = MyConnectionManager.class.getResourceAsStream("/db.properties");
			Properties properties = new Properties();
			properties.load(inputStream);

			RND_DRIVER = (String) properties.get("rnd_driver");
			RND_URL = (String) properties.get("rnd_url");
			RND_DBUSER = (String) properties.get("rnd_dbuser");
			RND_DBPASS = (String) properties.get("rnd_dbpass");
			
			Class.forName(RND_DRIVER);
			rnd_conn = DriverManager.getConnection(RND_URL, RND_DBUSER, RND_DBPASS);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return rnd_conn;
	}

	public static Connection getTechJiraConnection() {

		
		try {
			InputStream inputStream = MyConnectionManager.class.getResourceAsStream("/db.properties");
			Properties properties = new Properties();
			properties.load(inputStream);

			TECH_DRIVER = (String) properties.get("tech_driver");
			TECH_URL = (String) properties.get("tech_url");
			TECH_DBUSER = (String) properties.get("tech_dbuser");
			TECH_DBPASS = (String) properties.get("tech_dbpass");
			
			Class.forName(TECH_DRIVER);
			tech_conn = DriverManager.getConnection(TECH_URL, TECH_DBUSER, TECH_DBPASS);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return tech_conn;
	}
	
	/*
	public static Connection getCubridConnection() {
		try {
			Class.forName(CUB_CLASSNAME);
			conn = DriverManager.getConnection(CUB_URL, CUB_USER, CUB_PWD);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return conn;
	}
	*/
}
