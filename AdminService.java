package org.ender.webservices.employeeManagement.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.ender.webservices.employeeManagement.models.Admin;
import org.ender.webservices.employeeManagement.models.Employee;

public class AdminService {
	
	private List<Admin> admins= new ArrayList<>();
	
	public AdminService(){
	/*	System.out.println("fromadminservice");
		admins.add(new Admin("Ender","Yunz"));
		admins.add(new Admin("Nad", "Sel"));
		admins.add(new Admin("Kad","Dan"));*/
	}
	
	public boolean checkAdmin(String username, String password){
	//	Admin user=new Admin(username,password);
		try{
			Class.forName("com.mysql.jdbc.Driver");
			Connection myConn=DriverManager.getConnection("jdbc:mysql://localhost:3306/employeemanagement?autoReconnect=true&useSSL=false","enro","sql123");
			Statement myStat=myConn.createStatement();

			String sqlStr="SELECT * FROM employeemanagement.admin "
					+ "WHERE password = '"+password+"' AND username = '"+username+"'";	
			System.out.println("sqlStr+"+sqlStr);

			ResultSet rS=myStat.executeQuery(sqlStr);
			System.out.println("succesful retrieve");

			if(rS.next())
				return true;
			else 
				return false;
		}
		catch(Exception e)
		{
			System.out.print("dbexception");
		}
		return false;
		
		
		
	}
}
