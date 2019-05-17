package services;

import java.io.IOException;

import javax.ejb.Stateless;
import javax.faces.event.ActionEvent;
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
	public void addUser(ActionEvent event) throws IOException {
        User user = new User(name.getText(),surname.getText(),email.getText(),id.getText(),getDate(init)
                ,getDate(end),Double.parseDouble(value.getText()));
        String response = request(new Gson().toJson(user),"add");
    }
	
	@POST
	@Path("update")
	@Consumes("application/json")
	public void update(ActionEvent event) throws IOException {
        User user = new User(name.getText(),surname.getText(),email.getText(),id.getText(),getDate(init)
                ,getDate(end),Double.parseDouble(value.getText()));
        String response = request(new Gson().toJson(user),"update");
    }
	

}
