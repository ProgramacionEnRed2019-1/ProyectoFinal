package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import entity.Membership;
import entity.User;

public class SQLConnection {

	private Connection conexion;
	private Statement statement;

	public SQLConnection() {
		try {
			// SET GLOBAL time_zone = '-5:00';
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

	public void close() {
		try {
			conexion.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public User addUser(User user) {
		try {
			java.text.SimpleDateFormat f = new java.text.SimpleDateFormat("yyyy-MM-dd");
			statement.execute("INSERT INTO users(name, surname, id, email, inside) " + "VALUES ('" + user.getName()
					+ "','" + user.getSurname() + "','" + user.getId() + "','" + user.getEmail() + "',0 )");
			Membership m = user.getMembership();
			statement
					.execute("INSERT INTO `membership`(init, end, value, userID)" + " VALUES ('" + f.format(m.getInit())
							+ "','" + f.format(m.getEnd()) + "'," + m.getValue() + ",'" + user.getId() + "')");
			return user;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public User updateUser(User user) {
		try {
			java.text.SimpleDateFormat f = new java.text.SimpleDateFormat("yyyy-MM-dd");
			statement.execute(
					"UPDATE `users` SET " + "`name`='" + user.getName() + "'," + "`surname`='" + user.getSurname()
							+ "'," + "`email`='" + user.getEmail() + "'" + " WHERE id = '" + user.getId() + "'");
			Membership m = user.getMembership();
			statement.execute("UPDATE `membership` SET " + "`init`='" + f.format(m.getInit()) + "'," + "`end`='"
					+ f.format(m.getEnd()) + "'," + "`value`=" + m.getValue() + " " + "WHERE `userID`='" + user.getId()
					+ "'");
			return user;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public int inside(String id) {
		try {
			int state = -1;
			ResultSet resultados = statement.executeQuery("SELECT inside FROM users WHERE id = " + id + " ");
			if (resultados.next()) {
				state = resultados.getInt(1);
			}
			return state;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public User getUser(String id) {
		User user = null;
		try {
			ResultSet resultado = statement.executeQuery("SELECT * FROM users WHERE id= '" + id + "' ");
			if (resultado.next()) {
				user = new User(resultado.getString(2), resultado.getString(3), resultado.getString(4),
						resultado.getString(1), new Date(),new Date(),0);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			System.out.println(id);
		}

		return user;
	}
	
	public void updateState(String id,String gym) {
		try {
			statement.execute("UPDATE users SET inside = "+gym+" WHERE id ="+id);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}