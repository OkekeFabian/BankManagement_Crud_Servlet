/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bank;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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
 * Servlet used to update banks
 * @author fabianOkeke
 * @version 1.0
 */
public class UpdateBankServlet extends HttpServlet {

    /**
     * Ejb injection
     */
    @EJB
    BankServiceBean bankService;


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
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Update bank</title>");
            out.println("<h2>leave field blank if no change is required</h2>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Updating a Bank</h1>");
            out.println("<form action=\"Bank/update\" method=\"post\" >");
            out.println("<br/><label>Enter an id of bank you want to update, make sure it exists.</label></br>");
            out.println("<br/><input style='margin: 10px' type=\"number\" placeholder=\"Bank id...\" name=\"bankId\"/>");
            out.println("<br/><input style='margin: 10px' type=\"text\" placeholder=\"New Bank name...\" name=\"bankName\"/>");
            out.println("<br/><input style='margin: 10px' type=\"text\" placeholder=\"New Bank Location...\" name=\"bankLocation\"/>");
            out.println("<br/><input style='margin: 10px' type=\"submit\" value=\"UPDATE\"/>");
            out.println("</form>");
            
            out.println("<h3>Total amount of operations = " + getOperationCounter(request) + "</h3>");
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
        List<String> errors = new ArrayList<>();

      

        try (PrintWriter out = response.getWriter()) {
            String bankId = request.getParameter("bankId");
            String newBankName = request.getParameter("bankName");
            String newBankLocation = request.getParameter("bankLocation");

            Integer id = 0;
             if (!bankId.isEmpty()) {
            try {
                id = Integer.parseInt(bankId);
                 
            } catch (NumberFormatException nfe) {
                errors.add("Wrong id format, can't be empty or float.");
                request.setAttribute("errors", errors);
                request.getRequestDispatcher("/error/delete").forward(request, response);
            }
             }
            Bank bank = bankService.findByBankId(id);
            if (bank == null) {
                errors.add("Bank with given id doesn't exist!");
                request.setAttribute("errors", errors);
                request.getRequestDispatcher("/error/delete").forward(request, response);
            }
            if (!newBankName.equals("")) {
                bank.setName(newBankName);
            }
            if (!newBankLocation.equals("")) {
                bank.setLocation(newBankLocation);
            }
            bankService.updateBank(bank);
              incrementCounter(request);
            
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>[CRUD]Update bank</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Updating a bank</h1>");
            out.println("<p>Bank updated successfully!</p>");
            out.println("</br><a href=\"" + request.getContextPath() + "/\">Go back</a>");
            out.println("</body>");
            out.println("</html>");
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
        String name = "update_bank";
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
    private int getOperationCounter(HttpServletRequest request) {
        HttpSession session = request.getSession();

        Map<String, Integer> bankCounter
                = (Map<String, Integer>) session.getAttribute("bankCounter");
        if (bankCounter == null) {
            return 0;
        }
        String name = "update_bank";
        if (bankCounter.containsKey(name)) {
            return bankCounter.get(name);
        } else {
            return 0;
        }
    }
}
