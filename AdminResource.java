package org.ender.webservices.employeeManagement;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.ender.webservices.employeeManagement.services.AdminService;


@Path("admin")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.TEXT_PLAIN)
public class AdminResource {	
	
    @GET			//handles get request for using checkAdmin service
    public boolean getIt(@QueryParam("username") String username,
    			@QueryParam("password") String password) {
    	
    	AdminService as= new AdminService();    	
    	return as.checkAdmin(username, password);	
    }
}
