package com.example.examen.DAO;
import com.example.examen.Model.Alumno;
import com.example.examen.Model.Carrera;
import com.example.examen.conexion.R;


import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
public class LogicaMysql {
    private static Connection conexion;
    public void conectar() throws ClassNotFoundException, SQLException, IOException {
        Properties configuration = new Properties();
        configuration.load(R.getProperties("configBBDD.properties"));
        String host = configuration.getProperty("host");
        String port = configuration.getProperty("port");
        String name = configuration.getProperty("name");
        String username = configuration.getProperty("username");
        String password = configuration.getProperty("password");

        Class.forName("com.mysql.cj.jdbc.Driver");
        conexion = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + name + "?serverTimezone=UTC",
                username, password);
    }
    public void desconectar() throws ClassNotFoundException, SQLException, IOException{

        conexion.close();
    }
    public List<String> cargarNombresCarreras() throws SQLException {
        List<String> nombresCarreras = new ArrayList<>();

        try (Statement statement = conexion.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT NombreCarrera FROM carreras")) {

            while (resultSet.next()) {
                nombresCarreras.add(resultSet.getString("NombreCarrera"));
            }
        }

        return nombresCarreras;
    }

     public int insertarAlumno(Alumno alumno){
        int alumnosAnadidos = 0;
        try {
            String sql="INSERT INTO alumnos (DNIAlumno,nombreAlumno,telefonoAlumno,emailAlumno, CodCarrera) VALUES (?, ?, ?, ?, ?)";

            PreparedStatement sentencia=conexion.prepareStatement(sql);
            sentencia.setString(1, alumno.getDni());
            sentencia.setString(2, alumno.getNombre());
            sentencia.setString(3, alumno.getTelefono());
            sentencia.setString(4, alumno.getEmail());
            sentencia.setString(5, String.valueOf(alumno.getCodigoCarrera()));

            alumnosAnadidos = sentencia.executeUpdate();

        }catch (SQLException exception){
            System.out.println(exception.getMessage());
        }

        return alumnosAnadidos;
    }



        public int eliminarAlumno(Alumno alumno){

        try {
            String sql= "delete from alumnos where dniAlumno=?";

            PreparedStatement sentencia=conexion.prepareStatement(sql);
            sentencia.setString(1, alumno.getDni());
            int borrado=sentencia.executeUpdate();

            return borrado;

        }catch (SQLException ex){
            System.out.println(ex.getMessage());
        }

        return 0;
    }



    public int modificarAlumno(Alumno alumno) throws SQLException, IOException, ClassNotFoundException {
        try {
            String sql = "UPDATE alumnos SET DNIAlumno=?, NombreAlumno=?, TelefonoAlumno=?, EmailAlumno=?, CodCarrera=? WHERE DNIAlumno=?";
            PreparedStatement sentencia = conexion.prepareStatement(sql);
            sentencia.setString(1, alumno.getDni());
            sentencia.setString(2, alumno.getNombre());
            sentencia.setString(3, alumno.getTelefono());
            sentencia.setString(4, alumno.getEmail());
            sentencia.setString(5, String.valueOf(alumno.getCodigoCarrera()));

            sentencia.setString(6, alumno.getDni());

            int alumnoActualizado = sentencia.executeUpdate();
            System.out.println(alumnoActualizado);
            return alumnoActualizado;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return 0;
    }



    public List<Alumno> listadoAlumnos() throws SQLException, IOException, ClassNotFoundException {

        List<Alumno> alumnos=new ArrayList<>();

        String sql="select * from alumnos";


        PreparedStatement sentencia=conexion.prepareStatement(sql);
        ResultSet resultado=sentencia.executeQuery();
        while (resultado.next()){
            Alumno alumno=new Alumno();
            alumno.setDni(resultado.getString(1));
            alumno.setNombre(resultado.getString(2));
            alumno.setTelefono(resultado.getString(3));
            alumno.setEmail(resultado.getString(4));


            alumnos.add(alumno);

        }

        return alumnos;
    }
    public int obtenerCodCarreraPorAlumno(Alumno alumno) throws SQLException {
        int codCarrera = -1; // Valor por defecto en caso de no encontrar el código de carrera
        try {
            String sql = "SELECT CodCarrera FROM Alumnos WHERE DNIAlumno = ?";
            PreparedStatement statement = conexion.prepareStatement(sql);
            statement.setString(1, alumno.getDni());

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                codCarrera = resultSet.getInt("CodCarrera");
            }
        } catch (SQLException ex) {
            // Manejo de excepciones
            System.out.println("Error al obtener el código de carrera por alumno: " + ex.getMessage());
            throw ex;
        }
        return codCarrera;
    }
    public Alumno buscarAlumnoPorDNI(String dni) throws SQLException {
        Alumno alumno = null;
        try {
            String sql = "SELECT * FROM alumnos WHERE DNIAlumno = ?";
            PreparedStatement statement = conexion.prepareStatement(sql);
            statement.setString(1, dni);
            ResultSet resultado = statement.executeQuery();
            if (resultado.next()) {
                alumno = new Alumno(
                        resultado.getString("DNIAlumno"),
                        resultado.getString("NombreAlumno"),
                        resultado.getString("TelefonoAlumno"),
                        resultado.getString("EmailAlumno"),
                        resultado.getInt("CodCarrera")
                );
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return alumno;
    }
    public int obtenerCodCarreraPorNombre(String nombreCarrera) throws SQLException {
        int codCarrera = 0;

        try {
            String sql = "SELECT CodCarrera FROM Carreras WHERE NombreCarrera = ?";
            PreparedStatement statement = conexion.prepareStatement(sql);
            statement.setString(1, nombreCarrera);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                codCarrera = resultSet.getInt("CodCarrera");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw ex;
        }
        return codCarrera;
    }
    public List<Alumno> filtrarAlumnosPorCodCarrera(int codCarrera) throws SQLException {
        List<Alumno> alumnos = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Alumnos WHERE CodCarrera = ?";
            PreparedStatement statement = conexion.prepareStatement(sql);
            statement.setInt(1, codCarrera);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Alumno alumno = new Alumno(
                        resultSet.getString("DNIAlumno"),
                        resultSet.getString("NombreAlumno"),
                        resultSet.getString("TelefonoAlumno"),
                        resultSet.getString("EmailAlumno"),
                        resultSet.getInt("CodCarrera")
                );
                alumnos.add(alumno);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw ex;
        }
        return alumnos;
    }
    public String obtenerNombreCarreraPorCodCarrera(int codCarrera) throws SQLException {
        String nombreCarrera = null;
        try {
            String sql = "SELECT NombreCarrera FROM Carreras WHERE CodCarrera = ?";
            PreparedStatement statement = conexion.prepareStatement(sql);
            statement.setInt(1, codCarrera);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                nombreCarrera = resultSet.getString("NombreCarrera");
            }
        } catch (SQLException ex) {
            // Manejo de excepciones
            System.out.println("Error al obtener el nombre de la carrera por codCarrera: " + ex.getMessage());
            throw ex;
        }
        return nombreCarrera;
    }
    
}


