package com.emergentes.controlador;

import com.emergentes.modelo.Libro;
import com.emergentes.utiles.ConexionDB;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "MainController", urlPatterns = {"/MainController"})
public class MainController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {

            String op;
            int id;
            op = (request.getParameter("op") != null) ? request.getParameter("op") : "list";

            ArrayList<Libro> lista = new ArrayList<Libro>();
            ConexionDB canal = new ConexionDB();
            Connection conn = canal.conectar();
            PreparedStatement ps;
            ResultSet rs;

            if (op.equals("list")) {

                //obtener la lista de registros
                String sql = "select * from libros";
                ps = conn.prepareStatement(sql);
                rs = ps.executeQuery();

                while (rs.next()) {
                    Libro lib = new Libro();

                    lib.setId(rs.getInt("id"));
                    lib.setIsbn(rs.getString("isbn"));
                    lib.setTitulo(rs.getString("titulo"));
                    lib.setCategoria(rs.getString("categoria"));

                    lista.add(lib);
                }
                request.setAttribute("lista", lista);
                request.getRequestDispatcher("index.jsp").forward(request, response);

            }
            if (op.equals("nuevo")) {
                //nuevo registro
                //Instanciar un objeto de la clase
                Libro li = new Libro();

                System.out.println(li.toString());

                //el objeto se pone como atributo de request
                request.setAttribute("op", op);

                request.setAttribute("lib", li);
                //redireccionar a editar
                request.getRequestDispatcher("editar.jsp").forward(request, response);
            }
            if (op.equals("eliminar")) {
                //eliminar
                id = Integer.parseInt(request.getParameter("id"));
                //realiza r la eliminacion en la base de datos
                String sql = "delete from libros where id = ?";
                ps = conn.prepareStatement(sql);
                ps.setInt(1, id);

                ps.executeUpdate();
                //redireccionar a MainController
                response.sendRedirect("MainController");
            }

            if (op.equals("editar")) {
                request.setAttribute("op", op);
                Libro lib1 = new Libro();
                id = Integer.parseInt(request.getParameter("id"));
                try {
                    ps = conn.prepareStatement("select * from libros where id=?");
                    ps.setInt(1, id);
                    rs = ps.executeQuery();
                    if (rs.next()) {
                        lib1.setId(rs.getInt("id"));
                        lib1.setIsbn(rs.getString("isbn"));
                        lib1.setTitulo(rs.getString("titulo"));
                        lib1.setCategoria(rs.getString("categoria"));
                    }
                    request.setAttribute("lib", lib1);
                    request.getRequestDispatcher("editar.jsp").forward(request, response);

                } catch (SQLException ex) {
                    Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (SQLException ex) {
            System.out.println("ERROR AL CONECTAR" + ex.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {

            int id = Integer.parseInt(request.getParameter("id"));
            String isbn = request.getParameter("isbn");
            String titulo = request.getParameter("titulo");
            String categoria = request.getParameter("categoria");

            Libro lib = new Libro();

            lib.setIsbn(isbn);
            lib.setId(id);
            lib.setTitulo(titulo);
            lib.setCategoria(categoria);

            ConexionDB canal = new ConexionDB();
            Connection conn = canal.conectar();

            PreparedStatement ps;

            if (id == 0) {
                //nuevo registro
                String sql = "insert into libros (isbn,titulo,categoria)values (?,?,?)";

                ps = conn.prepareStatement(sql);
                ps.setString(1, lib.getIsbn());
                ps.setString(2, lib.getTitulo());
                ps.setString(3, lib.getCategoria());

                ps.executeUpdate();

                //response.sendRedirect("MainController");
            } else {

                String sql1 = "UPDATE libros SET isbn=?,titulo=?,categoria=? where id=?";
                try {
                    ps = conn.prepareStatement(sql1);
                    ps.setString(1, lib.getIsbn());
                    ps.setString(2, lib.getTitulo());
                    ps.setString(3, lib.getCategoria());
                    ps.setInt(4, lib.getId());
                    ps.executeUpdate();
                } catch (SQLException ex) {
                    Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            response.sendRedirect("MainController");

        } catch (SQLException e) {
            System.out.println("Error en SQL" + e.getMessage());
        }
    }
}
