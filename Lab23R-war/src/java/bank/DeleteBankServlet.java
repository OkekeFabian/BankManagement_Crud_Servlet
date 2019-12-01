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
import pl.polsl.lab1.model.BankServiceBean;

/**
 * Servlet used to delete banks
 * @author fabianokeke
 * @version 1.0
 */
public class DeleteBankServlet extends HttpServlet {

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
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Delete Bank</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Deleting a Bank...</h1>");
            out.println("<form action=\"Bank/delete\" method=\"post\" >");
                        out.println("<h3>Total amount of operations = " + getOperationCounter(request) + "</h3>");
            out.println("<br/><label>Enter an id of bank you want to delete, make sure it exists.</label></br>");
            out.println("<input type=\"number\" placeholder=\"Bank id...\" name=\"bankId\"/>");
            out.println("<br/><input type=\"submit\" value=\"DELETE\"/>");
            out.println("</form>");
            

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

        Map<String, Integer> bankCounter
                = (Map<String, Integer>) session.getAttribute("bankCounter");

        if (bankCounter == null) {
            bankCounter = new HashMap<>();
        }
        String name = "deleteBank";
        if (bankCounter.containsKey(name)) {
            bankCounter.put(name, bankCounter.get(name) + 1);
        } else {
            bankCounter.put(name, 1);
            session.setAttribute("bankCounter", bankCounter);
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

        try (PrintWriter out = response.getWriter()) {
            String bankId = request.getParameter("bankId");
            Integer id = 0;
            List<String> errors = new ArrayList<>();
            try {
                id = Integer.parseInt(bankId);
            } catch (NumberFormatException nfe) {
                errors.add("Wrong id format, can't be empty or float.");
                request.setAttribute("errors", errors);
                request.getRequestDispatcher("/error/delete").forward(request, response);
            }
            if (bankService.findByBankId(id) == null) {
                errors.add("Bank with given id doesn't exist!");
                request.setAttribute("errors", errors);
                request.getRequestDispatcher("/error/delete").forward(request, response);
            } else {
                bankService.delete(id);
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>[CRUD]Delete Bank...</title>");
                out.println("</head>");
                out.println("<body>");
                out.println("<h1>Deleting a Bank...</h1>");
                out.println("<p>Bank deleted successfully!</p>");
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

        Map<String, Integer> bankCounter
                = (Map<String, Integer>) session.getAttribute("bankCounter");
        if (bankCounter == null) {
            return 0;
        } 
        String name = "delete_Bank";
        if (bankCounter.containsKey(name)) {
            return bankCounter.get(name);
        } else return 0;
    }

}