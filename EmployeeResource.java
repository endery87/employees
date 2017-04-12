package org.ender.webservices.employeeManagement;

import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.ender.webservices.employeeManagement.models.Employee;

import org.ender.webservices.employeeManagement.services.EmployeeService;


@Path("/employees")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class EmployeeResource {
	
	EmployeeService eS=new EmployeeService();
    
	@GET		// handles employee get request by filtering options
	@Produces(MediaType.APPLICATION_JSON)
    public List<Employee> getEmployees(@QueryParam("firstname") String firstname, 
    								@QueryParam("lastname") String lastname,
    								@QueryParam("position") String position,
    								@QueryParam("sort") String sort,
    								@QueryParam("sortdir") String sortdir)	
    								{		

			return eS.getAllEmployees(firstname, lastname, position,sort,sortdir);
			
    }
	
	@GET	//handles single employee get request by id
	@Path("/{Id}")
	@Produces(MediaType.APPLICATION_JSON)
    public List<Employee> getSingleEmployees(@PathParam("Id") String id) 	
    {		

		return eS.getEmployeesByPosition(id);
			
    }
	
	
	@POST	//handles employee post request
	@Produces(MediaType.APPLICATION_JSON)
	public Employee addEmployee(Employee employee)
	{
		eS.addEmployee(employee);
		return employee;
	}
	
	@DELETE
	@Path("/{index}")	//handles delete request
	public void deleteEmployee(@PathParam("index") String id)
	{
		eS.deleteEmployee(id);
	}

	@PUT
	@Path("/{index}")	//handles update request
	public void updateEmployee(@PathParam("index") String id, Employee employee)
	{
		eS.updateEmployee(id,employee);
	}
		
	
}
