package com.tikal.toledo.database.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.servlet.ServletException;

public class Conexion {
	private String url;
	public Connection c;
	
	public Conexion() throws ServletException, SQLException{
		this.url= "jdbc:google:mysql://tornillosfact:us-central1:tornillos-db/tornillos_db?user=root&amp;password=TodoPoderoso1" ;
	    final String createTableSql = "CREATE TABLE IF NOT EXISTS visits ( visit_id INT NOT NULL "
	        + "AUTO_INCREMENT, user_ip VARCHAR(46) NOT NULL, timestamp DATETIME NOT NULL, "
	        + "PRIMARY KEY (visit_id) )";
	    final String createVisitSql = "INSERT INTO visits (user_ip, timestamp) VALUES (?, ?)";
	    final String selectSql = "SELECT user_ip, timestamp FROM visits ORDER BY timestamp DESC "
	        + "LIMIT 10";

	    if (System
	        .getProperty("com.google.appengine.runtime.version").startsWith("Google App Engine/")) {
	      // Check the System properties to determine if we are running on appengine or not
	      // Google App Engine sets a few system properties that will reliably be present on a remote
	      // instance.
	      url = "jdbc:google:mysql://tornillosfact:us-central1:tornillos-db/tornillos_db?user=root&amp;password=TodoPoderoso1";
	      try {
	        // Load the class that provides the new "jdbc:google:mysql://" prefix.
	        Class.forName("com.mysql.jdbc.GoogleDriver");
	      } catch (ClassNotFoundException e) {
	        throw new ServletException("Error loading Google JDBC Driver", e);
	      }
	    } else {
	      // Set the url with the local MySQL database connection url when running locally
	      url = System.getProperty("ae-cloudsql.local-database-url");
	    }
	    this.url = System.getProperty("ae-cloudsql.cloudsql-database-url");
	    Connection conn = DriverManager.getConnection(this.url);
	    this.c= conn;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public void reconnect() throws ServletException, SQLException{
		if(this.c.isClosed()){
		  if (System.getProperty("com.google.appengine.runtime.version").startsWith("Google App Engine/")) {
			      // Check the System properties to determine if we are running on appengine or not
			      // Google App Engine sets a few system properties that will reliably be present on a remote
			      // instance.
			      url = "jdbc:google:mysql://tornillosfact:us-central1:tornillos-db/tornillos_db?user=root&amp;password=TodoPoderoso1";
			      try {
			        // Load the class that provides the new "jdbc:google:mysql://" prefix.
			        Class.forName("com.mysql.jdbc.GoogleDriver");
			      } catch (ClassNotFoundException e) {
			        throw new ServletException("Error loading Google JDBC Driver", e);
			      }
			    } else {
			      // Set the url with the local MySQL database connection url when running locally
			      url = System.getProperty("ae-cloudsql.local-database-url");
			    }
			    this.url = System.getProperty("ae-cloudsql.cloudsql-database-url");
			    Connection conn = DriverManager.getConnection(this.url);
			    this.c= conn;
		}
	}
	

}