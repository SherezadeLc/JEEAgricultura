/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.agriculturaJ2EE;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Sherezade
 */
public class Controlador extends HttpServlet {

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
        // Se establece el tipo de contenido de la respuesta
        response.setContentType("text/html;charset=UTF-8");
        
        
        try {
            // Se obtiene el parámetro "accion" para determinar la operación solicitada
            String accion = request.getParameter("accion");
            if (accion == null) {
                accion = "menu"; // Acción por defecto
            }
            
            RequestDispatcher rd = null;
            switch (accion) {
                case "menu":
                    // Se asigna un atributo opcional para mostrar un mensaje en el menú
                    request.setAttribute("resultado", "Bienvenido al menú principal");
                    rd = getServletContext().getRequestDispatcher("/menu.jsp");
                    rd.forward(request, response);
                    break;
                case "login":
                    rd = getServletContext().getRequestDispatcher("/login.jsp");
                    rd.forward(request, response);
                    break;
                case "registro":
                    rd = getServletContext().getRequestDispatcher("/registro.jsp");
                    rd.forward(request, response);
                    break;
                case "logout":
                    rd = getServletContext().getRequestDispatcher("/logout.jsp");
                    rd.forward(request, response);
                    break;
                case "loginProcess":
                    // Aquí se implementaría la lógica para procesar el login
                    response.sendRedirect("Controlador?accion=menu");
                    break;
                case "registroProcess":
                    // Aquí se implementaría la lógica para procesar el registro
                    response.sendRedirect("Controlador?accion=menu");
                    break;
            }
        } finally {
            // Bloque finally para realizar tareas de limpieza si fuese necesario.
            // En este caso, no se requiere ninguna acción.
        }
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
        return "Short description";
    }// </editor-fold>

}
