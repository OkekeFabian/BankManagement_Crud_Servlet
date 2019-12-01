/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bank;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import pl.polsl.lab1.model.Bank;
import pl.polsl.lab1.model.BankServiceBean;

/**
 *Servlet used to add Banks
 * @author fabianokeke
 * @version 1.0
 */
public class AddBankServlet extends HttpServlet {

    /**
     * Ejb injection
     */
    @EJB
    BankServiceBean bankService;

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
            throws ServletException, IOException{ 
            
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<h1>ADD A NEW BANK</h1>");
          out.println("<form action=\"Bank/add\" method=\"post\" >");
            out.println("<input type=\"text\" placeholder=\"Enter Bank name\" name=\"bankName\"/>");
            out.println("<input type=\"text\" placeholder=\"Enter Bank location\" name=\"bankLocation\"/>");
            out.println("<input type=\"submit\" value=\"Enter\"/>");
            out.println("</form>");
             out.println("<h3>Amount of operations entered = " + getCreationCount(request) + "</h3>");
            out.println("</body>");
            out.println("</html>");
           

        }
        
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
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
           incrementCounter(request);

            String bankName = request.getParameter("bankName");
            String bankLocation = request.getParameter("bankLocation");
            List<String> errors = new ArrayList<>();
            if (bankName.equals("")) {
                errors.add("Department name cannot be empty!");
            }
            if (bankLocation.equals("")) {
                errors.add("Department address cannot be empty");
            }
            if (!errors.isEmpty()) {
                request.setAttribute("errors", errors);
                request.getRequestDispatcher("/error/add").forward(request, response);
            }
            Bank bank = new Bank(bankName, bankLocation);
            bankService.create(bank);
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>[CRUD]Add Bank </title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Bank added!</h1>");
            out.println("<p>" + bank.toString() + "</p>");

            out.println("</br><a href=\"" + request.getContextPath() + "/\">Go back</a>");
            
            out.println("</body>");
            out.println("</html>");
           
        
           
        }
    }

    /**
     * Increment operation counter
     *
     * @param request servlet request
     */
    private void incrementCounter(HttpServletRequest request) {
        HttpSession session = request.getSession();
        
        Map<String, Integer> bankCounter
                = (Map<String, Integer>) session.getAttribute("bankCounter");
        
        if (bankCounter == null) {
            bankCounter = new HashMap<>();
        }
        String name = "createBank";
        if (bankCounter.containsKey(name)) {
            bankCounter.put(name, bankCounter.get(name) + 1);
        } else {
            bankCounter.put(name, 1);
            session.setAttribute("bankCounter", bankCounter);
        }
    }

    /**
     * Method returns counter of performed operations
     *
     * @param request servlet request
     * @return number of performed operations
     */
    private int getCreationCount(HttpServletRequest request) {
        HttpSession session = request.getSession();
        
        Map<String, Integer> bankCounter
                = (Map<String, Integer>) session.getAttribute("bankCounter");
        if (bankCounter == null) {
            return 0;
        }
        String name = "createBank";
        if (bankCounter.containsKey(name)) {
            return bankCounter.get(name);
        } else {
            return 0;
        }
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
