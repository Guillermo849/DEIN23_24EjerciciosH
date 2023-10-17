package controllers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;

import application.Main;
import dao.PersonasDao;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

import model.Persona;

import javafx.event.ActionEvent;

public class TbPersonasController implements Initializable {

	@FXML
	private Button btnAgregarPersona;

	@FXML
	private Button btnModificar;

	@FXML
	private Button btnEliminar;

	@FXML
	private TableView<Persona> tbViewPersonas;

	@FXML
	private TableColumn<Persona, String> tbColNombre;

	@FXML
	private TableColumn<Persona, String> tbColApellidos;

	@FXML
	private TableColumn<Persona, Integer> tbColEdad;

	@FXML
	private TextField tfFiltroNombre;
	
	private NuevaPersonaController newPersonaWindow;

	private static Image ICONO = new Image(Main.class.getResourceAsStream("/img/agenda.png"));

	private int personaIndex = -1;

	private ObservableList<Persona> originalLstPersona;

	// Vartiables de base de datos
	private PersonasDao personasD;

	@FXML
	void selectPersona(MouseEvent event) {
		if (tbViewPersonas.getSelectionModel().getSelectedItem() != null) {
			personaIndex = tbViewPersonas.getSelectionModel().getSelectedIndex();
		}
	}

	@FXML
	void aniadirPersona(ActionEvent event) {
		try {

			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/DatosPersonasAgregar.fxml"));
			Parent root = loader.load();
			/* Le dice a la nueva ventana cual es su ventana padre */
			newPersonaWindow = loader.getController();
			newPersonaWindow.setParent(this, null, personasD);

			Stage agregarStage = new Stage();
			agregarStage.setScene(new Scene(root));
			agregarStage.setResizable(false);
			agregarStage.getIcons().add(ICONO);
			agregarStage.setTitle("Nueva Persona");
			agregarStage.showAndWait();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	void modificarPersona(ActionEvent event) {
		if (personaIndex > -1) {
			try {

				FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/DatosPersonasAgregar.fxml"));
				Parent root = loader.load();
				newPersonaWindow = loader.getController();
				newPersonaWindow.setParent(this, tbViewPersonas.getItems().get(personaIndex), personasD);

				Stage agregarStage = new Stage();
				agregarStage.setScene(new Scene(root));
				agregarStage.getIcons().add(ICONO);
				agregarStage.setTitle("Modificar Persona");
				agregarStage.showAndWait();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/* Elimina la persona seleccionada */
	@FXML
	void eliminarPersona(ActionEvent event) {
		if (personaIndex > -1) {
			Persona perEliminar = tbViewPersonas.getItems().get(personaIndex);
			personasD.eliminarPersona(perEliminar);
			tbViewPersonas.setItems(personasD.cargarPersonas());
		}
	}

	/*
	 * Añade la informacion de la ventana DatosPersonasAgregarController a la tabla
	 */
	public void devolverPersonaNueva(Persona person) {
		
		personasD.insertPersona(person);
		
		originalLstPersona = personasD.cargarPersonas();
		tbViewPersonas.setItems(personasD.cargarPersonas());
	}

	/*
	 * Añadirá la persona modificada a la tabla
	 */
	public void devolverPersonaMod(Persona person) {
		personasD.modPersona(person);
		
		originalLstPersona = personasD.cargarPersonas();
		tbViewPersonas.setItems(personasD.cargarPersonas());
	}

	/*
	 * Filtrará por nombres de la tabla de personas
	 */
	@FXML
	void filtrarPorNombre(ActionEvent event) {

		String nom = tfFiltroNombre.getText().toString();

		if (nom != null) {
			ObservableList<Persona> obLstPersonasFiltrado = FXCollections.observableArrayList();

			for (Persona per : originalLstPersona) {

				if (per.getNombre().length() >= nom.length()) {

					boolean iguales = true;
					char[] nomFiltro = nom.toCharArray();
					char[] nomPersona = per.getNombre().toCharArray();

					for (int i = 0; i < nomFiltro.length; i++) {
						if (nomFiltro[i] != nomPersona[i]) {
							iguales = false;
							break;
						}
					}

					if (iguales == true) {
						obLstPersonasFiltrado.add(per);
					}
				}
			}

			if (!obLstPersonasFiltrado.isEmpty()) {
				tbViewPersonas.setItems(obLstPersonasFiltrado);
			}

		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		tbColNombre.setCellValueFactory(new PropertyValueFactory<Persona, String>("nombre"));

		tbColApellidos.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getApellido()));

		tbColEdad.setCellValueFactory(new PropertyValueFactory<Persona, Integer>("edad"));

		personasD = new PersonasDao();

		tbViewPersonas.setItems(personasD.cargarPersonas());

		originalLstPersona = personasD.cargarPersonas();
	}

}
