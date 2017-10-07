package com.creeps.hkthn;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Servlet implementation class Test
 */
@WebServlet("/Test")
public class Test extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Test() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		RSSParser rssParser1 = new RSSParser("https://timesofindia.indiatimes.com/rssfeeds/-2128838597.cms");
		
		RSS rss1 = rssParser1.readFeed();
		ArrayList<EntityLocation> el = new RelevanceTester(rss1.items).relevantItems();
	
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

		for(int k=0;k<el.size();k++)
		{
			try {
				
				Statement s = c.createStatement();
			
				ResultSet rs;
				rs = s.executeQuery("SELECT locale_id ,locale.name , city.city_id , city.name , state.state_id , state.name,ST_Distance(ST_SetSRID(ST_Point("+el.get(k).getLongitude()+","+el.get(k).getLat()+"),4326),(locale.location::geometry))*100 as distance FROM locale,city,state where  locale.city_id = city.city_id and city.state_id = state.state_id and ST_Distance( ST_SetSRID(ST_Point("+el.get(k).getLongitude()+","+el.get(k).getLat()+"),4326),(locale.location::geometry))*100 < 2 order by distance ;");

				synchronized(rs) {
				while(rs.next())
				{
						System.out.println(rs.getLong("locale_id")+" "+rs.getString("name"));

				}
				}
			
			
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//System.out.println(rss1.toString());
		
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
