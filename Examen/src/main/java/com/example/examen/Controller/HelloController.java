package com.example.examen.Controller;

import com.example.examen.DAO.LogicaMysql;
import com.example.examen.Model.Alumno;
import com.example.examen.Model.Carrera;
import com.example.examen.Util.AlertUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


public class HelloController {
    @FXML
    private Button btBorrar;

    @FXML
    private Button btLimpiar;

    @FXML
    private Button btModificar;

    @FXML
    private Button btMostrarAlumno;

    @FXML
    private Button btnBuscar;

    @FXML
    private Button btnVer;

    @FXML
    private ComboBox<String> cbCarrera;

    @FXML
    private TableColumn col_DNI;

    @FXML
    private TableColumn col_Email;

    @FXML
    private TableColumn col_Nombre;

    @FXML
    private TableColumn col_Telefono;

    @FXML
    private TableView<Alumno> tablaExamen;

    @FXML
    private TextField txtDNI;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtTelefono;

    LogicaMysql conectarDAO = new LogicaMysql();


    public  HelloController() {
        try {
            conectarDAO.conectar();
            System.out.println("");
        } catch (SQLException sql) {
            System.out.println("Error al conectar con la base de datos");
        } catch (ClassNotFoundException cnfe) {
            System.out.println("Error al iniciar la aplicación");
        } catch (IOException ioe) {
            System.out.println("Error al cargar la configuración");
        }

    }
    //Mostrar datos en la tabla al iniciar el programa
    public void initialize() {
        try {
            List<Alumno> alumnos= conectarDAO.listadoAlumnos(); //Llamada al método
            ObservableList<Alumno> data= FXCollections.observableArrayList(alumnos);

            tablaExamen.setItems(data);

            this.col_DNI.setCellValueFactory(new PropertyValueFactory("dni"));
            this.col_Nombre.setCellValueFactory(new PropertyValueFactory("nombre"));
            this.col_Telefono.setCellValueFactory(new PropertyValueFactory("telefono"));
            this.col_Email.setCellValueFactory(new PropertyValueFactory("email"));

            // nombres de las carreras en el ComboBox
            List<String> nombresCarreras = conectarDAO.cargarNombresCarreras();
            ObservableList<String> items = FXCollections.observableArrayList(nombresCarreras);
            cbCarrera.setItems(items);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        desactivarBotones();

    }
    @FXML
    void limpiar(ActionEvent event) {
        txtDNI.setText("");
        txtNombre.setText("");
        txtTelefono.setText("");
        txtEmail.setText("");
        cbCarrera.setValue("");
        desactivarBotones();
    }
    //Método que sirve para cuando se pulsa sobre un campo de la tabla se pase a los textField
    @FXML
    void seleccionar(MouseEvent event) throws SQLException {
        Alumno alumno=tablaExamen.getSelectionModel().getSelectedItem();

        if(alumno !=null){
            txtDNI.setText(alumno.getDni());
            txtNombre.setText(alumno.getNombre());
            txtTelefono.setText(alumno.getTelefono());
            txtEmail.setText(alumno.getEmail());

            //A través de dos métodos distintos obtengo el nombre en el combobox
            //Primero filtro por alumno para obtener su codigo de carrera
            int codigo=conectarDAO.obtenerCodCarreraPorAlumno(alumno);

            try {  //Segundo a través de ese codigo de carrera filtro el nombre
                String nombreCarrera = conectarDAO.obtenerNombreCarreraPorCodCarrera(codigo);
                cbCarrera.setValue(nombreCarrera);
            } catch (SQLException e) {
                // Manejo de errores
                e.printStackTrace();
            }
        }

        activarBotones();
    }
    //Para filtrar por dni
    @FXML
    void buscar(ActionEvent event) throws SQLException {
        String dniAlumno = txtDNI.getText().trim();

        if (dniAlumno.isEmpty()) {
            AlertUtils.error("El campo DNI está vacío");
        } else {
            Alumno alumno = conectarDAO.buscarAlumnoPorDNI(dniAlumno);
            if (alumno == null) {
                AlertUtils.error("No se encontró ningún alumno con ese DNI");
            } else {
                txtNombre.setText(alumno.getNombre());
                txtTelefono.setText(alumno.getTelefono());
                txtEmail.setText(alumno.getEmail());
            }
        }
    }

    @FXML
    void eliminar(ActionEvent event) {
        Alumno alumno= tablaExamen.getSelectionModel().getSelectedItem();
        if (alumno==null){
            AlertUtils.error("No se ha seleccionado ningun alumno");
            return;
        }
        else{
            conectarDAO.eliminarAlumno(alumno);
            vaciarCampos();

        }
        initialize();
    }

    @FXML
    void insertar(ActionEvent event) throws SQLException {
        String dni=txtDNI.getText().toString();
        String nombre=txtNombre.getText().toString();
        String telefono=txtTelefono.getText().toString();
        String email=txtEmail.getText().toString();
        String carrera=cbCarrera.getValue();


        if(dni.isEmpty() || nombre.isEmpty() || telefono.isEmpty() || carrera.isEmpty()){
            AlertUtils.error("Todos los campos deben estar completos");
            vaciarCampos();
            return;
        }
        //Hago lo mismo pero en vez de filtrar por alumno, filtro por el nombre seleccionado y le asigno un codigo
        int codigo=conectarDAO.obtenerCodCarreraPorNombre(carrera);

            Alumno alumno=new Alumno(dni,nombre, telefono,email,codigo);
            conectarDAO.insertarAlumno(alumno);
        System.out.println(alumno.toString());
            vaciarCampos();

        initialize();
    }



    @FXML
    void modificar(ActionEvent event) throws SQLException {
        String dni=txtDNI.getText().toString();
        String nombre=txtNombre.getText().toString();
        String telefono=txtTelefono.getText().toString();
        String email=txtEmail.getText().toString();
        String carrera=cbCarrera.getValue();

        if (dni.isEmpty() || nombre.isEmpty() || telefono.isEmpty() || email.isEmpty() || carrera.isEmpty()){
            System.out.println("Debes rellenar todos los campos");
        } else{
            int codigo=conectarDAO.obtenerCodCarreraPorNombre(carrera);

            Alumno cambiarAlumno = new Alumno(dni,nombre,telefono,email, codigo);
            try {
                if(conectarDAO.modificarAlumno(cambiarAlumno)==1){
                    vaciarCampos();
                    System.out.println("Alumno modificado");
                    System.out.println(cambiarAlumno.toString());
                    initialize();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

    }
    @FXML
    void verAlumnos(ActionEvent event) throws SQLException {
        String nombreCarreraSeleccionada = cbCarrera.getValue();
        if (nombreCarreraSeleccionada != null) {
            int codCarreraSeleccionado = conectarDAO.obtenerCodCarreraPorNombre(nombreCarreraSeleccionada);
            List<Alumno> alumnos = conectarDAO.filtrarAlumnosPorCodCarrera(codCarreraSeleccionado);
            ObservableList<Alumno> data = FXCollections.observableArrayList(alumnos);

            tablaExamen.setItems(data);

            this.col_DNI.setCellValueFactory(new PropertyValueFactory("dni"));
            this.col_Nombre.setCellValueFactory(new PropertyValueFactory("nombre"));
            this.col_Telefono.setCellValueFactory(new PropertyValueFactory("telefono"));
            this.col_Email.setCellValueFactory(new PropertyValueFactory("email"));
        } else {

            AlertUtils.error("Por favor, seleccione una carrera antes de ver los alumnos.");
        }

    }
    public void activarBotones(){
        btBorrar.setDisable(false);
        btModificar.setDisable(false);
    }
    public void desactivarBotones(){
        btBorrar.setDisable(true);
        btModificar.setDisable(true);

    }
    public void vaciarCampos(){
        txtDNI.setText("");
        txtNombre.setText("");
        txtTelefono.setText("");
        txtEmail.setText("");
        cbCarrera.setValue("");
    }

}