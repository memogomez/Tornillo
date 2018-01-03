package com.tikal.toledo.database.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class Conexion {
	private String url;
	public Connection c;
	private DataSource datasource;
	
	
	@Autowired
	@Qualifier("dataSource")
	DriverManagerDataSource driver;
	
	
	public Conexion() throws ServletException, SQLException{
		this.url= "jdbc:google:mysql://tornillosfact:us-central1:tornillos-db/tornillos_db?user=root&amp;password=TodoPoderoso1" ;
	    final String createTableSql = "CREATE TABLE IF NOT EXISTS visits ( visit_id INT NOT NULL "
	        + "AUTO_INCREMENT, user_ip VARCHAR(46) NOT NULL, timestamp DATETIME NOT NULL, "
	        + "PRIMARY KEY (visit_id) )";
	    final String createVisitSql = "INSERT INTO visits (user_ip, timestamp) VALUES (?, ?)";
	    final String selectSql = "SELECT user_ip, timestamp FROM visits ORDER BY timestamp DESC "
	        + "LIMIT 10";

//	    if (System
//	        .getProperty("com.google.appengine.runtime.version").startsWith("Google App Engine/")) {
//	      // Check the System properties to determine if we are running on appengine or not
//	      // Google App Engine sets a few system properties that will reliably be present on a remote
//	      // instance.
	      url = "jdbc:google:mysql://tornillosfact:us-central1:tornillos-db/tornillos_db?user=root&amp;password=TodoPoderoso1";
	      try {
//	        // Load the class that provides the new "jdbc:google:mysql://" prefix.
	        Class.forName("com.mysql.jdbc.GoogleDriver");
	      } catch (ClassNotFoundException e) {
	        throw new ServletException("Error loading Google JDBC Driver", e);
	      }
//	    } else {
	      // Set the url with the local MySQL database connection url when running locally
	    
//	      url = System.getProperty("ae-cloudsql.local-database-url");
	      
	      
//	    }
	     
	    this.url = System.getProperty("ae-cloudsql.cloudsql-database-url");
	    Connection conn = DriverManager.getConnection(this.url);
	    this.c= conn;
	    	// Retrieve the data source from the application context
	    
//	    
//	    driver= new DriverManagerDataSource();
//	    driver.setUrl("jdbc:mysql://11.62.0.20:3306/kunuldb");
//	    driver.setDriverClassName("com.mysql.jdbc.Driver");
//	    driver.setUsername("sitesUser");
//	    driver.setPassword("siteUser1");
	    
//	    	    DataSource ds = (DataSource) driver;
//	    Connection c = DataSourceUtils.getConnection(ds);
//	    this.c= this.datasource.getConnection();
	    
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public void reconnect() throws ServletException, SQLException{
		if(this.c.isClosed()){
//		  if (System.getProperty("com.google.appengine.runtime.version").startsWith("Google App Engine/")) {
//			      // Check the System properties to determine if we are running on appengine or not
//			      // Google App Engine sets a few system properties that will reliably be present on a remote
//			      // instance.
//			      url = "jdbc:google:mysql://tornillosfact:us-central1:tornillos-db/tornillos_db?user=root&amp;password=TodoPoderoso1";
//			      try {
//			        // Load the class that provides the new "jdbc:google:mysql://" prefix.
//			        Class.forName("com.mysql.jdbc.GoogleDriver");
//			      } catch (ClassNotFoundException e) {
//			        throw new ServletException("Error loading Google JDBC Driver", e);
//			      }
//			    } else {
//			      // Set the url with the local MySQL database connection url when running locally
//			      url = System.getProperty("ae-cloudsql.local-database-url");
//			    }
//			    this.url = System.getProperty("ae-cloudsql.cloudsql-database-url");
//			    Connection conn = DriverManager.getConnection(this.url);
//			    this.c= conn;
			Connection conn = DriverManager.getConnection(this.url);
		    this.c= conn;
//			this.c= this.datasource.getConnection();
		}
	}

	public DataSource getDatasource() {
		return datasource;
	}

	public void setDatasource(DataSource datasource) throws SQLException {
		this.datasource = datasource;
//		if(this.c==null){
//			this.c=this.datasource.getConnection();
//		}
//		Connection connection = DriverManager.getConnection("jdbc:mysql://23.229.178.197/tornillos_db?user=tornillosroot&password=TodoPoderoso1");
//		this.c=connection;
	}
	

}