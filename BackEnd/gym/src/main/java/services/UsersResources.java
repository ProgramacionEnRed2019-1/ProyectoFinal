package services;

import java.io.IOException;

import javax.ejb.Stateless;
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
	@Path("open")
	public String openDoor(@FormParam("id") String id) {
		SQLConnection conexion = new SQLConnection();
		int state = conexion.inside(id);
		conexion.close();
		if (state == 1)
			return "true";
		else
			return "false";
	}

	@POST
	@Path("user")
	@Consumes("application/json")
	@Produces("application/json")
	public String findUser(String user) {
		SQLConnection conexion = new SQLConnection();
		User userR = conexion.getUser(gson.fromJson(user, User.class).getId());
		conexion.close();
		return gson.toJson(userR);

	}

}
