package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import entity.Membership;
import entity.User;

public class SQLConnection {
	
	private Connection conexion;
	private Statement statement;
	
	public SQLConnection() {
		try {
			//SET GLOBAL time_zone = '-5:00';
			Class.forName("com.mysql.cj.jdbc.Driver");
			conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/gym", "root", "");
			statement = conexion.createStatement();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public User addUser(User user) {
		try {
			java.text.SimpleDateFormat f = new java.text.SimpleDateFormat("yyyy-MM-dd");
			statement.execute("INSERT INTO users(name, surname, id, email) "
					+ "VALUES ('"+user.getName()+"','"+user.getSurname()+"','"+user.getId()+"','"+ user.getEmail() +"')");
			Membership m = user.getMembership();
			statement.execute("INSERT INTO `membership`(init, end, value, userID)"
					+ " VALUES ('"+f.format(m.getInit())+"','"+f.format(m.getEnd())+"',"+m.getValue()+",'"+user.getId()+"')");
			return user;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void update() {
		try {
			statement.execute("");
			
		} catch (SQLException e) {
			e.printStackTrace();
		} 
	}
	
	

}