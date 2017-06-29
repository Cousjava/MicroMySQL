package com.sleightholme.micromysql;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author jonathan coustick
 */
@WebServlet(name = "MysqlServlet", urlPatterns = {"/"})
public class Mysqlservlet extends HttpServlet {

    private Connection conn;
    private PrintWriter out;
    
    private static final String CREATETABLE = "CREATE TABLE IF NOT EXISTS microtest (id INTEGER NOT NULL AUTO_INCREMENT, name VARCHAR(50), PRIMARY KEY(id))";
    private static final String DROPTABLE = "DROP TABLE IF EXISTS microtest";
    private static final String INSERTONE = "INSERT INTO microtest VALUES (1,'first record')";
    private static final String GETONE = "SELECT * FROM microtest";

    @Override
    public void init() throws ServletException {
        super.init();
        DBC dbc = new DBC();
        dbc.connect();
        conn = dbc.getConnection();
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        out = response.getWriter();
        /* TODO output your page here. You may use following sample code. */
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Servlet Mysqlservlet</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Servlet Mysqlservlet at " + request.getContextPath() + "</h1>");
        runQueries();
        out.println("</body>");
        out.println("</html>");
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Test page for MySQL";
    }// </editor-fold>

    public void runQueries() {
        try {
            PreparedStatement ps = conn.prepareStatement(DROPTABLE);
            ps.execute();
            ps.execute(CREATETABLE);
            out.println("Created table<br>");
            ps = conn.prepareCall(INSERTONE);
            ps.execute();
            out.println("Entered record<br>");
            ps = conn.prepareStatement(GETONE);
            ResultSet rs = ps.executeQuery();
            while  (rs.next()){
                out.print("ID: " + rs.getInt(1));
                out.print(" value: " + rs.getString(2) + "<br>");
            }
            ps.execute(DROPTABLE);
        } catch (SQLException ex) {
            ex.printStackTrace(out);
        }
    }

}
