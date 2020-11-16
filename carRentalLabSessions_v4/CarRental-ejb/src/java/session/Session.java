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
import rental.CarType;
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
    
    protected List<CarType> getCarTypesQ(String company)
    {
       return em.createQuery("SELECT  ct FROM CarRentalCompany crc JOIN crc.carTypes ct  WHERE crc.name = :name").setParameter("name", company).getResultList();
    }
    
    protected List<Integer>  getCarIdsQ(String company, String type)
    {
        return em.createQuery("SELECT car.id FROM CarRentalCompany crc JOIN crc.cars car WHERE car.type.name = :tname AND crc.name = :cname", Integer.class).setParameter("tname", type).setParameter("cname", company).getResultList();
    }
    
    protected List<Reservation> getReservationsBy(String client)
    {
        return em.createQuery("SELECT res FROM Reservation res WHERE res.carRenter = :name  ").setParameter("name", client).getResultList();
    }
    
    protected int getNumberOfReservationsBy(String client)
    {
        return em.createQuery("SELECT count(res) FROM Reservation res WHERE res.carRenter = :name  ").setParameter("name", client).getFirstResult();
    }
    
    protected String getBestClient()
    {
        return em.createQuery("SELECT max((count(res)) FROM Reservation res GROUP BY res.carRenter", String.class).getSingleResult();
    }
}
