package services;

import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Stateless
@Path("user")
public class UsersResources {
	
	@GET
	@Path("echo")
	public String echo() {
		return "echo";
	}

}
