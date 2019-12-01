/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.polsl.lab1.model;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 * CustomerServiceBean class responsible for changes or transactions done in or to the customer class
 * 
 *
 * @author fabianokeke
 * @version 1.0 
 * 
 */

@Stateless
@LocalBean
public class CustomerServiceBean extends AbstractDAO<Customer> {

 

    /**
     * Finding customers by providing name
     *
     * @param name: Finding a customer by using the name inputted by the user
     * @return the customer found
     */
    
    public List<Customer> find(String name) {
        return em.createNamedQuery("Customer.findByName")
                .setParameter("name", name).getResultList();
    }

    /**
     * To find the customer by providing id
     *
     * @param id of the customer the user is trying to find
     * @return the customer found
     */
    public Customer findCustomerById(Integer id) {
    return (Customer) (em.find(Customer.class, id));

    }

    /**
     * To get all the customers present in the database
     *
     * @return all the customers present in the database
     */
    public List<Customer> getAllCustomers() {
        return em.createNamedQuery("Customer.getAll")
                .getResultList();
    }

    /**
     * Method to get the list of customer giving the id and Bank
     *
     * @param name of the customer to get
     * @param bank of the customer to get
     * @return the list of customers
     */
    public List<Customer> getCustomerList(String name, Bank bank) {

        return em.createQuery(getCriteriaQuery(name, bank)).getResultList();
    }

    /**
     * To delete customer
     *
     * @param id of the customer to be deleted
     */
    public void delete(int id) {
        em.remove(findCustomerById(id));
    }
/**
     * Method for creating a new customer after accepting the ID
     * @param customer: object of customer Class so as to be able to enter new customer
    
     */
    public void createCustomer(Customer customer) {
        em.persist(customer);
    }
    
    
    /**
     * To update Customers
     * @param customer to be updated
     */
    public void updateCustomer ( Customer customer){
    
    em.merge(customer);
    }
  
    /**
     * To query the customer table by providing customers name and the bank
     *
     * @param customerName name of the customer to be queried
     * @param bank of the customer to be queried
     * @return the found customer
     */
    private CriteriaQuery<Customer> getCriteriaQuery(String customerName, Bank bank) {
        Expression expr; // refers to the attributes of entity class
        Root<Customer> queryRoot; // entity/table from which the selection is performed
        CriteriaQuery<Customer> queryDefinition; // query being built
        List<Predicate> predicates = new ArrayList<>(); // list of conditions in the where clause
        CriteriaBuilder builder = em.getCriteriaBuilder(); // creates predicates

        queryDefinition = builder.createQuery(Customer.class);
        // defines the from part of the query
        queryRoot = queryDefinition.from(Customer.class);
        // defines the select part of the query
        // at this point we have a query select s from Student s (select * from student in SQL)
        queryDefinition.select(queryRoot);
        if (customerName != null) {
            expr = queryRoot.get("firstName");
            // creates condition of the form s.name LIKE name
            predicates.add(builder.like(expr, "%" + customerName + "%"));
        }

        if (bank != null) {
            expr = queryRoot.get("bank");
            predicates.add(builder.equal(expr, bank));
        }
        // if there are any conditions defined
        if (!predicates.isEmpty()) {
            // build the where part in which we combine the conditions using AND operator
            queryDefinition.where(
                    builder.or(predicates.toArray(
                            new Predicate[predicates.size()])));

        }
        return queryDefinition;
    }
}
