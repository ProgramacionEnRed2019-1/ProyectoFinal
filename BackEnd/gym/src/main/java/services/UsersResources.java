package services;

import java.io.IOException;

import javax.ejb.Stateless;
import javax.faces.event.ActionEvent;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.google.gson.Gson;

import db.SQLConnection;
import entity.User;

@Stateless
@Path("user")
public class UsersResources {
	
	private Gson gson = new Gson();
	
	@GET
	@Path("echo")
	public String echo() {
		return "echo";
	}
	
	@POST
	@Path("add")
	@Consumes("application/json")
	@Produces("application/json")
	public String addUser(String user) throws IOException {
		User toResponse = new SQLConnection().addUser(gson.fromJson(user, User.class));
        return gson.toJson(toResponse);
    }
	
	@POST
	@Path("update")
	@Consumes("application/json")
	@Produces("application/json")
	public String update(String user) throws IOException {
		User toResponse = new SQLConnection().updateUser(gson.fromJson(user, User.class));
        return gson.toJson(toResponse);
    }

	@POST 
	@Path("")
	public String exist(@FormParam("id") String id) {
		return "true"; 
	}

}
