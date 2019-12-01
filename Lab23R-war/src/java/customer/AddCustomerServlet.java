/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer;

import java.util.Date;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import pl.polsl.lab1.model.Customer;
import pl.polsl.lab1.model.CustomerServiceBean;

/**
 *In charge of adding customers
 * @author fabianokeke
 * @version 1.0
 */
public class AddCustomerServlet extends HttpServlet {

    /**
     * Ejb injection
     */
    @EJB
    BankServiceBean bankService;
    @EJB
    CustomerServiceBean customerService;

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
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Add Customer</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Adding a  new Customer....</h1>");

            out.println("<form action=\"Customer/add\" method=\"post\" >");
            out.println("<input type=\"text\" placeholder=\"Enter Customer First name\" name=\"customerFirstName\"/>");
            out.println("<input type=\"text\" placeholder=\"Enter Customer Last name\" name=\"customerLastName\"/>");

            out.println("<input type=\"text\" placeholder=\"Enter Date of Birth in format dd/MM/yyyy\" name=\"customerBirthdate\"/>");
            out.println("<input type=\"text\" placeholder=\"Enter Bank Id\" name=\"customerBankId\"/>");
            out.println("<input type=\"submit\" value=\"Enter\"/>");
            out.println("</form>");

            out.println("<h3>Amount of operations entered = " + getCreationCount(request) + "</h3>");
            out.println("</body>");
            out.println("</html>");
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        incrementCounter(request);

        //adding
        try (PrintWriter out = response.getWriter()) {
            String customerFirstName = request.getParameter("customerFirstName");
            String customerLastName = request.getParameter("customerLastName");
            String customerBirthdate = request.getParameter("customerBirthdate");
            String customerBankId = request.getParameter("customerBankId");
            List<String> errors = new ArrayList<>();
            if (customerFirstName.equals("")) {
                errors.add("Customer First name cannot be empty!");
            }
            if (customerLastName.equals("")) {
                errors.add("Customer Last name cannot be empty");
            }
            if (customerBirthdate.equals("")) {
                errors.add("Customer Birth Date cannot be empty");
            }
            if (customerBankId.equals("")) {
                errors.add("Customer Bank Id cannot be empty");
            }

            if (!errors.isEmpty()) {
                request.setAttribute("errors", errors);
                request.getRequestDispatcher("/error/add").forward(request, response);
            }
            Date birthdate;
            try {
                birthdate = new SimpleDateFormat("dd/MM/yyyy").parse(customerBirthdate);
            } catch (ParseException e) {
                birthdate = new Date();
            }

            int bankId;
            try {

                bankId = Integer.parseInt(customerBankId.trim());

            } catch (NumberFormatException nfe) {
                bankId = 0;
                System.out.println("NumberFormatException: " + nfe.getMessage());
            }
            Bank bank = bankService.findByBankId(bankId);
            Customer customer = new Customer(customerFirstName, customerLastName, birthdate, bank);
            customerService.create(customer);
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>[CRUD]Add Customer </title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Customer added!</h1>");
            out.println("<p>" + customer.toString() + "</p>");
            out.println("</br><a href=\"" + request.getContextPath() + "/\">Go back</a>");
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Increment operation counter
     *
     * @param request servlet request
     */
    private void incrementCounter(HttpServletRequest request) {
        HttpSession session = request.getSession();

        Map<String, Integer> customerCounter
                = (Map<String, Integer>) session.getAttribute("customerCounter");

        if (customerCounter == null) {
            customerCounter = new HashMap<>();
        }
        String name = "createBank";
        if (customerCounter.containsKey(name)) {
            customerCounter.put(name, customerCounter.get(name) + 1);
        } else {
            customerCounter.put(name, 1);
            session.setAttribute("customerCounter", customerCounter);
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

        Map<String, Integer> customerCounter
                = (Map<String, Integer>) session.getAttribute("customerCounter");
        if (customerCounter == null) {
            return 0;
        }
        String name = "createCustomer";
        if (customerCounter.containsKey(name)) {
            return customerCounter.get(name);
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
