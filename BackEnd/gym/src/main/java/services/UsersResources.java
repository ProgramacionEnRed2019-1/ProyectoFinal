package services;

import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

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
	public String addUser(String user) {
		User u = new SQLConnection().addUser(gson.fromJson(user, User.class));
		return gson.toJson(u);	
	}

}
