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
		SQLConnection conexion = new SQLConnection();
		User toResponse = conexion.addUser(gson.fromJson(user, User.class));
		conexion.close();
        return gson.toJson(toResponse);
    }
	
	@POST
	@Path("update")
	@Consumes("application/json")
	@Produces("application/json")
	public String update(String user) throws IOException {
		SQLConnection conexion = new SQLConnection();
		User toResponse = new SQLConnection().updateUser(gson.fromJson(user, User.class));
		conexion.close();
        return gson.toJson(toResponse);
    }

	@POST 
	@Path("")
	public String exist(@FormParam("id") String id) {
		SQLConnection conexion = new SQLConnection();
		boolean exist = conexion.exist(id);
		conexion.close();
		if(exist)
			return "true";
		else 
			return "false"; 
	}

}
