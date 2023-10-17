package dao;

/* Controlador de acceso a datos de Personas, para guardar, modificar y/o eliminar de la base de datos */

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import conexion.ConexionBDD;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Persona;

public class PersonasDao {
	private ConexionBDD conexion;
	
	/* Devuelve una lista de las personas almacenadas en la base de datos */
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
	
	/* Insertará una nueva persona en la base de datos */
	public void insertPersona(Persona persona) {
		
		String nom = persona.getNombre(); 
		String ape = persona.getApellido();
		int eda = persona.getEdad();
		
		try {
			conexion = new ConexionBDD();
			String consulta = "INSERT INTO persona(nombre,apellidos,edad) VALUES('" + nom + "','" + ape + "',"  + eda+ ");";
			PreparedStatement pstmt = conexion.getConexion().prepareStatement(consulta);
			pstmt.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/* Modificará una persona de la base de datos */
	public void modPersona(Persona persona) {
		
		String nom = persona.getNombre(); 
		String ape = persona.getApellido();
		int eda = persona.getEdad();
		
		try {
			conexion = new ConexionBDD();
			String consulta = "UPDATE persona SET nombre = '" + nom + "', apellidos = '" + ape + "', edad = '" + eda + "' WHERE id = " + persona.getId() + ";";
			PreparedStatement pstmt = conexion.getConexion().prepareStatement(consulta);
			pstmt.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/* Eliminará una persona en la base de datos*/
	public void eliminarPersona(Persona persona) {
		
		int idPersona = persona.getId();
		
		try {
			conexion = new ConexionBDD();
			String consulta = "DELETE FROM persona WHERE id = " + idPersona + ";";
			PreparedStatement pstmt = conexion.getConexion().prepareStatement(consulta);
			pstmt.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
