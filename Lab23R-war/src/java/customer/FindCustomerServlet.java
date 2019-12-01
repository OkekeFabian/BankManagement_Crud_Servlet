/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer;

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

import pl.polsl.lab1.model.Customer;
import pl.polsl.lab1.model.CustomerServiceBean;

/**
 * Servlet used to find Banks
 * @author fabianOkeke
 * @version 1.0
 */
public class FindCustomerServlet extends HttpServlet {

    
    /**
     * Ejb injection
     */
    @EJB
    CustomerServiceBean customerService;

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
            out.println("<title>[CRUD]Find Customers</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Finding a Customer</h1>");
            out.println("<form action='Customer/find?op=1' method='post\'>");
            out.println("<input type='submit' value='Find All Customers'/><br/>");
            out.println("</form><br/>");
            out.println("<form action='Customer/find?op=2' method='post\'>");
            out.println("<label>Find Customer By Id</label><br/>");
            out.println("<input type='number' name='findById' placeholder='Customer id'/><br/>");
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
        List<Customer> customers = new ArrayList<>();
        
        if (op.equals("1")) {
             customers = customerService.getAllCustomers();
        }
         else if (op.equals("2")) {
             Integer id = 0;
             String findById = request.getParameter("findById");
           try {
                id = Integer.parseInt(findById);
            } catch (NumberFormatException nfe) {
                errors.add("Wrong id format, can't be empty or float.");
                request.setAttribute("errors", errors);
                request.getRequestDispatcher("/error/delete").forward(request, response);
            }
           
            Customer customer = customerService.findCustomerById(id);
            if ( customer== null) {
                errors.add("Customer with given id doesn't exist!");
                request.setAttribute("errors", errors);
                request.getRequestDispatcher("/error/delete").forward(request, response);
            } else {
                customers.add(customer);
            }
        }
        
        try (PrintWriter out = response.getWriter()) {
            out.println("<table width='100%' border='1'>");
            out.println("<tr><th>id</th><th>Customer FirstName</th><th>Customer LastName</th><th>Customer BankInformation</th></tr>");
            for (Customer c : customers) {
                out.println("<tr><td>" + c.getId() + "</td>");
                out.println("<td>" + c.getFirstName() + "</td>");
                
                out.println("<td>" + c.getLastName() + "</td>");
                out.println("<td>" + c.getBank() + "</td>");
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

        Map<String, Integer> customerCounter
                = (Map<String, Integer>) session.getAttribute("customerCounter");

        if (customerCounter == null) {
           customerCounter = new HashMap<>();
        }
        String firstName = "find_customer";
        if (customerCounter.containsKey(firstName)) {
            customerCounter.put(firstName, customerCounter.get(firstName) + 1);
        } else {
            customerCounter.put(firstName, 1);
            session.setAttribute("customerCounter", customerCounter);
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

        Map<String, Integer> customerCounter
                = (Map<String, Integer>) session.getAttribute("customerCounter");
        if ( customerCounter== null) {
            return 0;
        }
        String name = "find_customer";
        if (customerCounter.containsKey(name)) {
            return customerCounter.get(name);
        } else {
            return 0;
        }
    }

}
