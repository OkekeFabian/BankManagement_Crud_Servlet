/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
 * Servlet used to update banks
 *
 * @author fabianOkeke
 * @version 1.0
 */
public class UpdateCustomerServlet extends HttpServlet {

    /**
     * Ejb injection
     */
    @EJB
    CustomerServiceBean customerService;
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
            out.println("<title>Update Customer</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Updating a Customer</h1>");
            out.println("<h2>leave field blank if no change is required</h2>");
            out.println("<form action=\"Customer/update\" method=\"post\" >");
            out.println("<br/><label>Enter an id of customer you want to update, make sure it exists.</label></br>");
            out.println("<br/>Id of the customer "
                    + "<input style='margin: 10px' type=\"number\" placeholder=\"Customer id\" name=\"customerId\"/>");
            out.println("<br/>New Customer First name "
                    + "<input style='margin: 10px' type=\"text\" placeholder=\"New Customer First name...\" name=\"customerFirstName\"/>");
            out.println("<br/>New Customer Last name "
                    + "<input style='margin: 10px' type=\"text\" placeholder=\"New Customer Last name...\" name=\"customerLastName\"/>");
            out.println("<br/>New birthdate     "
                    + "<input style='margin: 10px' type=\"text\" placeholder=\"mm/dd/yyyy\" name=\"customerBirthdate\"/>");
            out.println("<br/>New Bank id "
                    + "<input style='margin: 10px' type=\"number\" placeholder=\"New Bank id...\" name=\"customerBankId\"/>");
            out.println("<br/><input style='margin: 20px' type=\"submit\" value=\"UPDATE\"/>");
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

        try (PrintWriter out = response.getWriter()) {
            List<String> errors = new ArrayList<>();
            String customerId = request.getParameter("customerId");
            String bankNewId = request.getParameter("customerBankId");
            String customerNewFirstName = request.getParameter("customerFirstName");
            String customerNewLastName = request.getParameter("customerLastName");
            String customerNewBirthdate = request.getParameter("customerBirthdate");

            Integer cusId = 0;
            Integer bankId;

            try {
                cusId = Integer.parseInt(customerId);
            } catch (NumberFormatException nfe) {
                errors.add("Wrong customer id format, can't be empty or float.");
                request.setAttribute("errors", errors);
                request.getRequestDispatcher("/error/delete").forward(request, response);
            }

            Customer customer = customerService.findCustomerById(cusId);

            if (customer == null) {
                errors.add("Customer with given id doesn't exist!");
                request.setAttribute("errors", errors);
                request.getRequestDispatcher("/error/delete").forward(request, response);
            }
            if (!customerNewFirstName.equals("")) {
                customer.setFirstName(customerNewFirstName);
            }
            if (!customerNewLastName.equals("")) {
                customer.setLastName(customerNewLastName);
            }
            if (!customerNewBirthdate.equals("")) {
                try {
                    Date date = new SimpleDateFormat("MM/dd/yyyy").parse(customerNewBirthdate);
                    customer.setBirthDate(date);
                } catch (ParseException e) {
                    errors.add("Invalid date input!");
                }
            }
            try {
                bankId = Integer.parseInt(bankNewId);
                Bank bank = bankService.findByBankId(bankId);
                if (bank != null) {
                    customer.setBank(bank);
                } else {
                    errors.add("Bank with given id doesn't exist!");
                }
            } catch (NumberFormatException nfe) {
                errors.add("Bank with given id doesn't exist!");
                request.setAttribute("errors", errors);
                request.getRequestDispatcher("/error/delete").forward(request, response);
            }

            customerService.updateCustomer(customer);

            incrementCounter(request);

            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Update customer</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Updating a customer</h1>");
            out.println("<p>Customer updated successfully!</p>");
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

        Map<String, Integer> customerCounter
                = (Map<String, Integer>) session.getAttribute("customerCounter");

        if (customerCounter == null) {
            customerCounter = new HashMap<>();
        }
        String name = "update_Customer";
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
    private int getOperationCounter(HttpServletRequest request) {
        HttpSession session = request.getSession();

        Map<String, Integer> customerCounter
                = (Map<String, Integer>) session.getAttribute("customerCounter");
        if (customerCounter == null) {
            return 0;
        }
        String name = "update_customer";
        if (customerCounter.containsKey(name)) {
            return customerCounter.get(name);
        } else {
            return 0;
        }
    }
}
