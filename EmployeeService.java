package org.ender.webservices.employeeManagement.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.ender.webservices.employeeManagement.models.Employee;

public class EmployeeService {

//	private List<Employee> employees=new ArrayList<>();
	
	public EmployeeService(){
	}
	
	//gets all employees by restrictions(filtering and sorting)
	public List<Employee> getAllEmployees(String fN, String lN, String pos,String sort, String sortdir){
		List<Employee> resultList = new ArrayList<>();

		try{
			//database connection
			Class.forName("com.mysql.jdbc.Driver");
			Connection myConn=DriverManager.getConnection("jdbc:mysql://localhost:3306/employeemanagement?autoReconnect=true&useSSL=false","enro","sql123");
			Statement myStat=myConn.createStatement();
			
			//query production
			if(fN==null)
				fN="";
			if(lN==null)
				lN="";
			if(pos==null)
				pos="";

			String sqlStr="SELECT * FROM employeemanagement.employees WHERE firstname LIKE '%"
					+ fN +"%' AND lastname LIKE '%"+lN+"%' AND position LIKE '%"+pos+"%'";
			
			if(sort!=null && sortdir!=null)
				sqlStr+= " ORDER BY "+sort+" "+sortdir;	
			
			//create the result list from resultSet that is returned from the query
			ResultSet rS=myStat.executeQuery(sqlStr);
			while(rS.next())
			{

				Employee employee= new Employee();
				employee.setFirstName(rS.getString(1));
				employee.setLastName(rS.getString(2));				
				employee.setPosition(rS.getString(3));
				employee.setId(rS.getString(5));

				resultList.add(employee);
								
			}
						return resultList;
		}
		catch(Exception e)
		{
			System.out.print("dbexception");
		}
		//return resultlist
		return resultList;
	}
	
	//get single employee
	public List<Employee> getEmployeesByPosition(String id){
		List<Employee> resultList = new ArrayList<>();

		try{
			Class.forName("com.mysql.jdbc.Driver");
			Connection myConn=DriverManager.getConnection("jdbc:mysql://localhost:3306/employeemanagement?autoReconnect=true&useSSL=false","enro","sql123");
			Statement myStat=myConn.createStatement();

			String sqlStr="SELECT * FROM employeemanagement.employees WHERE Id='"+id+"'";	
			ResultSet rS=myStat.executeQuery(sqlStr);
			while(rS.next())
			{

				Employee employee= new Employee();
				employee.setFirstName(rS.getString(1));
				employee.setLastName(rS.getString(2));				
				employee.setPosition(rS.getString(3));
				employee.setId(rS.getString(5));
				resultList.add(employee);							
			}
			
			return resultList;
		}
		catch(Exception e)
		{
			System.out.print("dbexception");
		}
		return resultList;

	}
	
	//adds a new employee to database
	public Employee addEmployee(Employee employee)
	{
		try{
			//database connection
			Class.forName("com.mysql.jdbc.Driver");
			Connection myConn=DriverManager.getConnection("jdbc:mysql://localhost:3306/employeemanagement?autoReconnect=true&useSSL=false","enro","sql123");
			Statement myStat=myConn.createStatement();
			
			//create query
			String sqlStr="INSERT INTO employeemanagement.employees (firstname, lastname, position, Id) VALUES "
					+ "('"+employee.getFirstName()+"', '"+employee.getLastName()+"', '"+employee.getPosition()+"', '"+employee.getId()+"')";		
			//execute update
			myStat.executeUpdate(sqlStr);
		}
		catch(Exception e)
		{
			System.out.print("dbexception");
		}

		return employee;//if database insert is successful return the employee
	}
	
	//deletes employee by using its id
	public String deleteEmployee(String id)
	{		
		try{
			//db connection
			Class.forName("com.mysql.jdbc.Driver");
			Connection myConn=DriverManager.getConnection("jdbc:mysql://localhost:3306/employeemanagement?autoReconnect=true&useSSL=false","enro","sql123");
			Statement myStat=myConn.createStatement();
			
			//create query
			String sqlStr="DELETE FROM employeemanagement.employees  WHERE Id='"+id+"'";
			
			//execute query
			myStat.executeUpdate(sqlStr);
		}
		catch(Exception e)
		{
			System.out.print("dbexception");
		}
		return id;//if succesful returns the id

	}
	
	//update employee by its id and new employee instance
	public String updateEmployee(String id,Employee employee)
	{		
		try{
			//database connection
			Class.forName("com.mysql.jdbc.Driver");
			Connection myConn=DriverManager.getConnection("jdbc:mysql://localhost:3306/employeemanagement?autoReconnect=true&useSSL=false","enro","sql123");
			Statement myStat=myConn.createStatement();
			
			//query creation
			String sqlStr="UPDATE employeemanagement.employees  "
					+ "SET firstname= '"+employee.getFirstName()
					+ "', lastname= '"+employee.getLastName()
					+ "', position= '"+employee.getPosition()+"' WHERE Id= '"+id+"'";	

			//execute query
			myStat.executeUpdate(sqlStr);
		}
		catch(Exception e)
		{
			System.out.print("dbexception");
		}
		return id;//if successful returns the id back

	}
}
