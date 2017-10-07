package com.creeps.hkthn;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

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
						sendRequest(el.get(k).getItem(),"group_".concat(Long.toString(rs.getLong("locale_id"))));

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
	void sendRequest(Item item,String group) {
		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost("https://fcm.googleapis.com/fcm/send");
		post.setHeader("Content-type", "application/json");
		post.setHeader("Authorization", "key=AAAAkD5ggHw:APA91bF5qF5WB7W5xEUC-jdQP-FZzcU1EyXQgz46Fz6Bo439sALuBtYYqTfdLyawDitk1oL1EhUp-ttaNFvDfGogQgr-jYCc-3DJ1tBdGlugAGPWwq-XCh9lb3ZL_3ZoVSsDwyqxjOfn");   
		//post.setHeader("Authorization", "key=AAAALmzx16E:APA91bFQiyOVbZHhXtwAku0_GJ8JNhe2Qn9QbGjQa-yltL2zAPa-JFH10w-arQ6MQpgXo612V043_laYWTw8oe0-jWpURIVfeHwzb3kxuSiy2M51MlmeTfqZOv8w_qqsOzuzRMFZRbop");
		try {
			post.setEntity(new StringEntity(new TopicRequest(item,group).toString()));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		HttpResponse response1;
		try {
			response1 = client.execute(post);
			System.out.println(response1.getEntity());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
