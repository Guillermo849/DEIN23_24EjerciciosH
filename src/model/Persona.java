package model;

/* Clase Persona */

public class Persona {
	
	int id;
	String nombre;
	String apellidos;
	int edad;
	
	public Persona(String nom, String ape, int eda) {
		nombre = nom;
		apellidos = ape;
		edad = eda;
	}
	
	public Persona(int ID, String nom, String ape, int eda) {
		this.id = ID;
		nombre = nom;
		apellidos = ape;
		edad = eda;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public int getEdad() {
		return edad;
	}

	public void setEdad(int edad) {
		this.edad = edad;
	}
	
	public boolean compararPersonas(Persona other) {
		
		if(this.getNombre().equals(other.getNombre()) && this.getApellido().equals(other.getApellido())) {
			return true;
		}
		return false;
		
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return nombre + "," + apellidos + "," + edad;
	}
	
}
