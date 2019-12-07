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
 * Servlet used to find Banks
 *
 * @author fabianOkeke
 * @version 1.0
 */
public class FindBankServlet extends HttpServlet {

    /**
     * Ejb injection of the bank bean
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
            out.println("<title>Find Banks</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Finding a Bank</h1>");
            out.println("<form action='Bank/find?op=1' method='post\'>");
            out.println("<input type='submit' value='Click here to see all Banks created'/><br/>");
            out.println("</form><br/>");
            out.println("<form action='Bank/find?op=2' method='post\'>");
            out.println("<label>Find Bank By Id</label><br/>");
            out.println("<input type='number' name='bankId' placeholder='Bank id...'/><br/>");
            out.println("<input type='submit' value='Find'/>");
            out.println("</form><br/>");

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
        incrementCounter(request);

        String op = request.getParameter("op");
        List<String> errors = new ArrayList<>();
        List<Bank> banks = new ArrayList<>();
        if (op.equals("1")) {
            banks = bankService.getBankList();

        } else if (op.equals("2")) {

            String bankId = request.getParameter("bankId");
            Integer id = 0;
            try {
                id = Integer.parseInt(bankId);
            } catch (NumberFormatException err) {
                errors.add("Wrong id format, can't be empty or float.");
                request.setAttribute("errors", errors);
                request.getRequestDispatcher("/error/delete").forward(request, response);
                return;
            }

            Bank bank = bankService.findByBankId(id);
            if (bank != null) {
                banks.add(bank);
            } else {
                errors.add("Bank with given id doesn't exist");
                request.setAttribute("errors", errors);
                request.getRequestDispatcher("/error/delete").forward(request, response);
                return;
            }
        }

        try (PrintWriter out = response.getWriter()) {

            out.println("<table width='100%' border='1'>");
            out.println("<tr><th>id</th><th>Bank Name</th><th>Bank Location</th></tr>");
            for (Bank b : banks) {
                out.println("<tr><td>" + b.getId() + "</td>");
                out.println("<td>" + b.getName() + "</td>");
                out.println("<td>" + b.getLocation() + "</td>");
            }
            out.println("</table>");
            out.println("</br><a href=\"" + request.getContextPath() + "/\">Go back</a>");
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
        String name = "find_bank";
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
        String name = "find_bank";
        if (bankCounter.containsKey(name)) {
            return bankCounter.get(name);
        } else {
            return 0;
        }
    }

}
