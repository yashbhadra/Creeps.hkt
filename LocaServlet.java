package com.creeps.hkthn;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class LocaServlet
 */
@WebServlet("/LocaServlet")
public class LocaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LocaServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
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




	try {


	Statement s = c.createStatement();

	ArrayList <String> list=new ArrayList <String>();

	ResultSet rs;

	rs = s.executeQuery("select * from item  where item_id IN(select item_id from domain where locale_id IN(select locale_id from subscription where username IN(select username where username='yash' ))order by item_id desc);");

	String str;

	int count = 0;



	while (rs.next()) {

	    ++count;

	    // Get data from the current row and use it

	}



	if (count == 0) {

	    System.out.println("No records found");

	}


	for(int i=1;i<=count;i++) {

		rs.beforeFirst();
		rs.next();


	str="{id:"+rs.getInt("item_id")+",message:"+rs.getString("message")+"}";

	list.add(str); 




	}


	synchronized(rs) {


	}



	} catch (SQLException e) {

	// TODO Auto-generated catch block

	e.printStackTrace();

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
