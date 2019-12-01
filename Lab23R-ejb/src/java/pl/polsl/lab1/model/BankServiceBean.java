/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.polsl.lab1.model;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;

import javax.persistence.criteria.Root;

/**
 * BankServiceBean class responsible for changes or transactions done in or to
 * the bank class
 *
 *
 * @author fabianokeke
 * @version 1.0
 *
 */
@Stateless
@LocalBean
public class BankServiceBean extends AbstractDAO<Bank> {

    /**
     * Finding Bank by providing name
     *
     * @param name:Finding a bank by using the name inputted by the user
     * @return the bank(s) found after the search
     */
    @Override
    public List<Bank> find(String name) {
        return em.createNamedQuery("Bank.findByName")
                .setParameter("name", name).getResultList();
    }

    /**
     * Finding Bank by providing ID
     *
     * @param id:Finding bank by the id provided by the user
     * @return the bank(s) found after the search
     */
    public Bank findByBankId(Integer id) {
        return (Bank) (em.find(Bank.class, id));

    }

    /**
     * To see the list of Banks in the database
     *
     * @return the bank list before and after changes are made
     */
    public List<Bank> getBankList() {
        return em.createNamedQuery("Bank.findAll").getResultList();

    }

    /**
     * Overloaded constructor of getting list of banks
     *
     * @param name: name of the bank that the user is trying to find
     * @param location:location of the bank that the user is trying to find
     * @return list of banks also when the user is trying to search and so has
     * to provide name and location
     *
     */
    public List<Bank> getBankList(String name, String location) {

        return em.createQuery(getCriteriaQuery(name, location)).getResultList();
    }

    /**
     * To delete a bank by providing id
     *
     * @param id the user has to enter to delete the bank
     *
     */
    public void delete(int id) {
        em.remove(findByBankId(id));
    }

    /**
     * create a bank
     *
     * @param bank to be created
     *
     */
    public void createBank(Bank bank) {
        em.persist(bank);
    }

    /**
     *To update the banks available
     * @param bank to be updated
     */
    public void updateBank(Bank bank) {

        em.merge(bank);
    }

    /**
     * Criteria query responsible for finding Bank after providing name and
     * location
     *
     *
     * @param name providing the name of the bank to be queried
     * @param location: user providing location to be able to access and query
     * the desired bank
     * @return the bank found
     */
    private CriteriaQuery<Bank> getCriteriaQuery(String name, String location) {
        Expression expr; // refers to the attributes of entity class
        Root<Bank> queryRoot; // entity/table from which the selection is performed
        CriteriaQuery<Bank> queryDefinition; // query being built
        List<Predicate> predicates = new ArrayList<>(); // list of conditions in the where clause

        CriteriaBuilder builder; // creates predicates
        builder = em.getCriteriaBuilder();

        queryDefinition = builder.createQuery(Bank.class);
        // defines the from part of the query
        queryRoot = queryDefinition.from(Bank.class);
        // defines the select part of the query
        // at this point we have a query select s from Student s (select * from student in SQL)
        queryDefinition.select(queryRoot);
        if (name != null) {
            // gets access to the field called name in the Student class
            expr = queryRoot.get("name");
            predicates.add(builder.like(expr, name));
        }

        if (location != null) {
            // gets access to the field called name in the Student class
            expr = queryRoot.get("location");
            // creates condition of the form s.average >= average
            predicates.add(builder.equal(expr, location));
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
