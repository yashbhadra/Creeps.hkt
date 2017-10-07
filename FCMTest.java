package com.creeps.hkthn;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;
import java.util.Scanner;
/**
 * Servlet implementation class FCMTest
 */
@WebServlet("/FCMTest")
public class FCMTest extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FCMTest() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		 
		
		  URL url = new URL("https://gcm-http.googleapis.com/gcm/send");
		 
		  Scanner in;
		try {
			  
			  	
			  	Item item = new Item("Title","Link","DESC","AUTH","GUID");
				HttpClient client = HttpClientBuilder.create().build();
		    	HttpPost post = new HttpPost("https://fcm.googleapis.com/fcm/send");
		    	post.setHeader("Content-type", "application/json");
		    	post.setHeader("Authorization", "key=AAAAkD5ggHw:APA91bF5qF5WB7W5xEUC-jdQP-FZzcU1EyXQgz46Fz6Bo439sALuBtYYqTfdLyawDitk1oL1EhUp-ttaNFvDfGogQgr-jYCc-3DJ1tBdGlugAGPWwq-XCh9lb3ZL_3ZoVSsDwyqxjOfn");   
		    	//post.setHeader("Authorization", "key=AAAALmzx16E:APA91bFQiyOVbZHhXtwAku0_GJ8JNhe2Qn9QbGjQa-yltL2zAPa-JFH10w-arQ6MQpgXo612V043_laYWTw8oe0-jWpURIVfeHwzb3kxuSiy2M51MlmeTfqZOv8w_qqsOzuzRMFZRbop");
		    	post.setEntity(new StringEntity(new TopicRequest(item,"group_0").toString()));
		    	System.out.println(new TopicRequest(item,"group_0").toString());
		    	HttpResponse response1 = client.execute(post);
		    	System.out.println(response1.getEntity());

		    	

		}
			catch (Exception ex) {

			    //handle exception here

			} finally
	        {
	            
	            //((OutputStreamWriter) response).close();
	        }
			    

		}
	
	
	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
