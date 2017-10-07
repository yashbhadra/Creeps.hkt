package com.creeps.hkthn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class FetchGroup {

	private String email;
	private double latitude;
	private double longitude;
	
	ArrayList<String> groups;
	
	public FetchGroup(double latitude, double longitude) {
		super();
		
		this.latitude = latitude;
		this.longitude = longitude;
		groups = new ArrayList<String>();
		
	}
	
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String fetchGroups()
	{
		String JSON="";
		
		Connection c = null;
	      try {
	         Class.forName("org.postgresql.Driver");
	         c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/mydb","postgres", "root");
	      } catch (Exception e) {
	         e.printStackTrace();
	         System.err.println(e.getClass().getName()+": "+e.getMessage());
	         System.exit(0);
	      }
			System.out.println("Opened database successfully");
			String listString="";
		
			try {
				
				Statement s = c.createStatement();
			
				ResultSet rs;
				rs = s.executeQuery("SELECT locale_id ,locale.name , city.city_id , city.name , state.state_id , state.name,ST_Distance(ST_SetSRID(ST_Point("+getLongitude()+","+getLatitude()+"),4326),(locale.location::geometry))*100 as distance FROM locale,city,state where  locale.city_id = city.city_id and city.state_id = state.state_id and ST_Distance( ST_SetSRID(ST_Point("+getLongitude()+","+getLatitude()+"),4326),(locale.location::geometry))*100 < 2 order by distance ;");

				synchronized(rs) {
				while(rs.next())
				{
					if(groups.contains(Long.toString(rs.getLong("locale_id"))))
							continue;
					
					groups.add(Long.toString(rs.getLong("locale_id")));
					

				}
				}
			
				 listString = String.join(", ", groups);

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		
		
		return "{\"groups\":["+listString+"]}";
	}
	
	
	
	
	
	
	
}
