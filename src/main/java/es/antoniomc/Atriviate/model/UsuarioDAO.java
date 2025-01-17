package es.antoniomc.Atriviate.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import es.antoniomc.Atriviate.interfaces.IUsuarioDAO;
import es.antoniomc.Atriviate.utils.conexion;
import es.antoniomc.Atriviate.utils.consultas;
import es.antoniomc.Atriviate.utils.encoder;

public class UsuarioDAO extends Usuario implements IUsuarioDAO {

  public UsuarioDAO(double id, String nombre, String password, double puntos) {
    super(id, nombre, password, puntos);
  }

  public UsuarioDAO(String nombre, String password, double puntos) {
    super(nombre, password, puntos);
  }

  public UsuarioDAO() {
    super();
  }

  public UsuarioDAO(Usuario a) {
    this.id = a.id;
    this.nombre = a.nombre;
    this.password = a.password;
    this.puntos = a.puntos;
  }

  public UsuarioDAO(String nombre) {
    // getByID from mariaDB
    // Conexion
    super();
    Connection con = conexion.getConexion();
    // Stament
    if (con != null) {
      try {
        Statement st = con.createStatement();
        String q = consultas.USUARIOGETBYNOMBRE.getConsulta() + "'" + nombre + "'";
        ResultSet rs = st.executeQuery(q);
        while (rs.next()) {
          this.id = rs.getDouble("id");
          this.nombre = rs.getString("nombre");
          this.password = rs.getString("password");
          this.puntos = rs.getDouble("puntos");
        }
      } catch (SQLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }

  @Override
  public boolean deleteUsuario(String name) {
    // TODO Auto-generated method stub
    return false;
  }


  public static List<Usuario> getUsuarios() {
    List<Usuario> usuarios = new ArrayList<>();
    Connection con = conexion.getConexion();

    if (con != null) {
      try {
        Statement st = con.createStatement();
        String q = consultas.USUARIOSELECTALL.getConsulta();
        ResultSet rs = st.executeQuery(q);
        while (rs.next()) {
          Usuario dummy = new Usuario();
          dummy.setNombre(rs.getString("nombre"));
          dummy.setPassword(rs.getString("password"));
          dummy.setPuntos(rs.getDouble("puntos"));
          usuarios.add(dummy);
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return usuarios;
  }

  @Override
  public int save() {
    int rs = 0;
    Connection con = conexion.getConexion();
    if (con != null) {
      try {
        PreparedStatement q = con.prepareStatement(consultas.USUARIOINSERTUPDATE.getConsulta());
        q.setString(1, this.nombre);
        q.setString(2, encoder.encrypt(this.password));
        q.setDouble(3, this.puntos);
        q.setDouble(4, this.puntos);

        rs = q.executeUpdate();
      } catch (SQLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    return rs;
  }

  /**
   * Método que hace una consulta de todos los nombres de usuario en la base de datos
   * y los devuelve en una lista de strings
   * 
   * @return List<String> nombres de usuarios
   */
  public static List<String> getNombres() {
    List<String> getNombres = new ArrayList<>();

    Connection con = conexion.getConexion();
    if (con != null) {
      try {
        Statement st = con.createStatement();
        String q = consultas.USUARIOSELECTNOMBRE.getConsulta();
        ResultSet rs = st.executeQuery(q);
        while (rs.next()) {
          getNombres.add(rs.getString("nombre"));
        }
      } catch (SQLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    return getNombres;
  }
}
