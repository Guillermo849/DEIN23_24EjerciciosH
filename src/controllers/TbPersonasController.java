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

public class TbPersonasController implements Initializable{

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
    private Button btnImportar;

    @FXML
    private Button btnExportar;

    @FXML
    private TextField tfFiltroNombre;
    
    private NuevaPersonaController newPersonaWindow;
    
    private static Image ICONO = new Image(Main.class.getResourceAsStream("/img/agenda.png"));
    
    private int personaIndex = -1;
    
    private ObservableList<Persona> originalLstPersona;
    
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
			newPersonaWindow.setParent(this, null);
			
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
				newPersonaWindow.setParent(this, tbViewPersonas.getItems().get(personaIndex));
				
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
    
    /* Elimina la persona seleccionada*/
    @FXML
    void eliminarPersona(ActionEvent event) {
    	if (personaIndex > -1) {
    		tbViewPersonas.getItems().remove(personaIndex);
    	}
    }

    /*
     * Añade la informacion de la ventana DatosPersonasAgregarController a la tabla
     * */
    public void devolverPersonaNueva(Persona person) {
    	originalLstPersona.add(person);
        tbViewPersonas.setItems(originalLstPersona);
    }
    
    /*
     * Añadirá la persona modificada a la tabla
     * */
    public void devolverPersonaMod(Persona person) {
    	tbViewPersonas.getItems().set(personaIndex, person);
    	originalLstPersona = tbViewPersonas.getItems();
    }
    
    /*
     * Filtrará por nombres de la tabla de personas
     * */
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
    
    /*
     * Importará el archivo csv que selecciones en el buscador de archivos
     *   y luego pondrá la información en la tabla
     * */
    @FXML
    void importarTabla(ActionEvent event) {
    	
    	
    	/* Habré el explorador de archivos */
    	FileChooser fc = new FileChooser();
    	/* Nos abrirá en explorador de archivos en el directorio donde se encuentra la aplicación */
    	String currentPath = Paths.get(".").toAbsolutePath().normalize().toString() + "/resources/csv";
    	fc.setInitialDirectory(new File(currentPath));
    	
    	fc.setTitle("Open CSV File");
    	fc.getExtensionFilters().add(new ExtensionFilter("CSV Files", "*.csv"));
    	
    	File ficheroElegido = fc.showOpenDialog(null);
    	
    	if (ficheroElegido != null) {
    		try {
    			
    			FileReader fr = new FileReader(ficheroElegido);
    			
    			try {
    				BufferedReader br = new BufferedReader(fr);
    				String linea = br.readLine();
    				linea = br.readLine();
    				
    				ObservableList<Persona> obLstImportado = FXCollections.observableArrayList();
    				
    				/* Leerá cada línea de información en el archivo csv que hemos seleccionado */
    				while (linea != null) {
    					String[] infoPersona = linea.split(",");
    					
    					obLstImportado.add(new Persona(infoPersona[0], infoPersona[1], Integer.parseInt(infoPersona[2].toString())));
    					
    					linea = br.readLine();
    				}
    				br.close();
    				
    				originalLstPersona = obLstImportado;
    				tbViewPersonas.setItems(originalLstPersona);
    				
    			} catch (IOException e) {
    				e.printStackTrace();
    			}
    		} catch (FileNotFoundException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    	}
    }
    
    /*
     * Exportará la información de la tabla a un fichero CSV
     * */
    @FXML
    void exportarTabla(ActionEvent event) {
    	
    	try {
    		FileChooser fc = new FileChooser();
    		/* Nos abrirá en explorador de archivos en el directorio donde se encuentra la aplicación */
        	String currentPath = Paths.get(".").toAbsolutePath().normalize().toString() + "/resources/csv";
        	fc.setInitialDirectory(new File(currentPath));
        	
        	fc.setTitle("Save CSV File");
        	fc.setInitialFileName("persona.csv");
        	fc.getExtensionFilters().add(new ExtensionFilter("CSV Files", "*.csv"));
        	
        	File ficheroElegido = fc.showSaveDialog(null);
        	fc.setInitialDirectory(ficheroElegido.getParentFile());
        	
        	/* Escribimos la información de la tabla en el archivo creado */
        	if (ficheroElegido != null) {
        		
        		try {
    				FileWriter fw = new FileWriter(ficheroElegido);
    				BufferedWriter bw = new BufferedWriter(fw);
    				
    				bw.write("Nombre,Apellidos,Edad \n");
    				
    				for (Persona pers : tbViewPersonas.getItems()) {
    					bw.write(pers.toString()+'\n');
    				}
    				bw.close();
    				
    				
    			} catch (IOException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
        	}
    	} catch (NullPointerException e) {
			// TODO: handle exception
		}
    }
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		originalLstPersona = tbViewPersonas.getItems();
		
		tbColNombre.setCellValueFactory(new PropertyValueFactory<Persona, String>("nombre"));
        
		tbColApellidos.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getApellido()));
        
		tbColEdad.setCellValueFactory(new PropertyValueFactory<Persona, Integer>("edad"));
	}

}

