package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import conexion.ConexionBDD;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Persona;

public class PersonasDao {
	private ConexionBDD conexion;
	
	public ObservableList<Persona> cargarPersonas() {

		ObservableList<Persona> personas = FXCollections.observableArrayList();
		try {
			conexion = new ConexionBDD();
			String consulta = "select * from persona";
			PreparedStatement pstmt = conexion.getConexion().prepareStatement(consulta);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				int idPersona = rs.getInt("id");
				String nombre = rs.getString("nombre");
				String apellidos = rs.getString("apellidos");
				int edad = rs.getInt("edad");
		        Persona p = new Persona(idPersona, nombre, apellidos, edad);
				personas.add(p);
			}
			rs.close();
			conexion.CloseConexion();

		} catch (SQLException e) {e.printStackTrace();}
		return personas;
	}
	
	public void insertPersona(String nom, String ape, int eda) {
		
		try {
			conexion = new ConexionBDD();
			String consulta = "INSERT INTO persona(nombre,apellidos,edad) VALUES(" + nom + "," + ape + ","  + eda+ ");";
			PreparedStatement pstmt = conexion.getConexion().prepareStatement(consulta);
			pstmt.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
