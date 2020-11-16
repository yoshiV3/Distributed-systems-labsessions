/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import java.util.List;
import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import rental.Reservation;

/**
 *
 * @author yoshi
 */
public abstract class Session {
    
    @PersistenceContext 
    private  EntityManager em;
    
    @Resource
    EJBContext context;
    
    protected EJBContext getEJBContext()
    {
        return context;
    }
    
    protected EntityManager getEntityManager()
    {
        return em;
    }
    
    protected  List<String> getAllCarRentalCompanies()
    {
        return em.createQuery("SELECT crc.name FROM CarRentalCompany crc", String.class).getResultList();
                
    }
    
    protected List<Reservation> getReservationsBy(String client)
    {
        return em.createQuery("SELECT res FROM Reservation res WHERE res.carRenter = :name  ").setParameter("name", client).getResultList();
    }
    
    protected int etReservationsBy(String client)
    {
        return em.createQuery("SELECT count(res) FROM Reservation res WHERE res.carRenter = :name  ").setParameter("name", client).getFirstResult();
    }
}
