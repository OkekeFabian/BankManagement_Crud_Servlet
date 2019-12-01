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
import pl.polsl.lab1.model.CustomerServiceBean;

/**
 * Servlet used to delete banks
 * @author fabianokeke
 * @version 1.0
 */
public class DeleteCustomerServlet extends HttpServlet {

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
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Delete Customer</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Deleting an Customer</h1>");
            out.println("<form action=\"Customer/delete\" method=\"POST\" >");
            out.println("<br/><label>Enter an id of an customer you want to delete, make sure it exists.</label></br>");
            out.println("<input type=\"number\" placeholder=\"Customer id\" name=\"customerId\"/>");
            out.println("<br/><input type=\"submit\" value=\"DELETE\"/>");
            out.println("</form>");
            
            out.println("<h3>Total amount of operations = " + getOperationCounter(request) + "</h3>");
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
     * @param request servlet request
     */
    private void incrementCounter(HttpServletRequest request) {
        HttpSession session = request.getSession();

        Map<String, Integer> customerCounter
                = (Map<String, Integer>) session.getAttribute("customerCounter");

        if (customerCounter == null) {
            customerCounter = new HashMap<>();
        }
        String name = "deleteCustomer";
        if (customerCounter.containsKey(name)) {
           customerCounter.put(name, customerCounter.get(name) + 1);
        } else {
           customerCounter.put(name, 1);
            session.setAttribute("customerCounter",customerCounter);
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

        //COunter
        incrementCounter(request);

        try (PrintWriter out = response.getWriter()) {
            String customerId = request.getParameter("customerId");
            Integer id = 0;
            List<String> errors = new ArrayList<>();
            
            try {
                id = Integer.parseInt(customerId);
            } catch (NumberFormatException nfe) {
                errors.add("Wrong id format, can't be empty or float.");
                request.setAttribute("errors", errors);
                request.getRequestDispatcher("/error/delete").forward(request, response);
                
            }
            
            if (customerService.findCustomerById(id) == null) {
                errors.add("Customer with given id doesn't exist!");
                request.setAttribute("errors", errors);
                request.getRequestDispatcher("/error/delete").forward(request, response);
            } else {
                customerService.delete(id);
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>[CRUD]Delete Customer...</title>");
                out.println("</head>");
                out.println("<body>");
                out.println("<h1>Deleting a Customer...</h1>");
                out.println("<p>Customer deleted successfully!</p>");
                out.println("</br><a href=\"" + request.getContextPath() + "/\">Go back</a>");
                out.println("</body>");
                out.println("</html>");
            }
        }
    }

   
    
    /**
     * Method returns counter of performed operations
     * @param request servlet request
     * @return number of performed operations
     */
    private int getOperationCounter(HttpServletRequest request){
        HttpSession session = request.getSession();

        Map<String, Integer> customerCounter
                = (Map<String, Integer>) session.getAttribute("customerCounter");
        if (customerCounter== null) {
            return 0;
        } 
        String name = "delete_Bank";
        if (customerCounter.containsKey(name)) {
            return customerCounter.get(name);
        } else return 0;
    }

}