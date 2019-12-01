
package pl.polsl.lab1.model ;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * In charge of every transaction done to the table and it is extended by BankDAO and CustomerDAO..
 * @author fabianokeke
 * @version 1.0
 * @param <T> holding the transactions
 */
public abstract class AbstractDAO<T> {
    
    /**
     * creating an instance of connection Holder
     */
    @PersistenceContext
    protected EntityManager em;
    
    
    /**
     * To create a transaction
     * @param entity to determine if an entity is created
     */
    public void create (T entity){
        em.persist(entity);
    }
    
    /**
     * To find in the class of Customer or bank by providing id
     * @param clazz holding the entities
     * @param id of the entity
     * @return  the found class members
     */
    public T find(Class<T> clazz, Integer id){
        return em.find(clazz, id);
    }
    
    /**
     * To find entity
     * @param name name of members of entity
     * @return found members
     */
    public abstract List<T> find(String name);

}
