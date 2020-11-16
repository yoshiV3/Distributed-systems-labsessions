/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author yoshi
 */
public abstract class Session {
    
    @PersistenceContext 
    private  EntityManager em;
    
    protected EntityManager getEntityManager()
    {
        return em;
    }
    
    protected  List<String> getAllCarRentalCompanies()
    {
        return em.createQuery("SELECT crc.name FROM CarRentalCompany crc", String.class).getResultList();
                
    }
    
}
