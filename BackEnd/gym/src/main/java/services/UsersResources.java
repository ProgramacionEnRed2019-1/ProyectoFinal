package services;

import java.io.IOException;
import java.net.ConnectException;
import java.sql.Connection;
import java.util.Date;

import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

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
	public String openDoor(@FormParam("id") String id, @FormParam("gym") String idGym) {
		SQLConnection conexion = new SQLConnection();
		int state = conexion.inside(id);
		
		String response = "";
		
		//TAMBIEN PROBAR SI LA SUSCRIPCIÓN ESTÁ ACTIVA
//		System.out.println("ID DEL USER: "+ id);
//		System.out.println("ID: " + idGym);
		
		java.sql.Date endDate = conexion.getEndDate(id);
		java.util.Date fechaActual = new Date();
//		System.out.println("Usuario: " +conexion.getUser(id) +" Comparacion de fechas "+ fechaActual.compareTo(endDate) + " Endate != null "+ endDate!=null);
		if( fechaActual.compareTo(endDate) <= 0) {
			System.out.println("Endate " +endDate);
			
			if(state == 0) {
				response = "true";
				
				conexion.updateState(id, idGym);
				
			}else {
				
				if(idGym.equals(""+state)) {
					response = "true";
					conexion.updateState(id, "0");
				}
			}
			
			conexion.close();
			
			System.out.println(response);
			return response;
		}else {
			
			return "false";
		}
		
		
	}
	
	@GET
	@Path("end/{id}")
	public String endDate(@PathParam("id") String id) {
		SQLConnection connection = new SQLConnection();
		java.sql.Date d =connection.getEndDate(id);
		java.util.Date fechaActual = new Date();
		
		int compare = fechaActual.compareTo(d);
		
		connection.close();
		
		return d.toString() + " La fecha actual es " +fechaActual.toString() +  " Fecha actual.compareTo = " + compare;
		
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
