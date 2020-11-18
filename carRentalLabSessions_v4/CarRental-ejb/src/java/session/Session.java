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
    private EntityManager em;

    @Resource
    EJBContext context;

    protected EJBContext getEJBContext() {
        return context;
    }

    protected EntityManager getEntityManager() {
        return em;
    }

    protected List<String> getAllCarRentalCompanies() {
        System.out.println("inside session for getAllCarRentalCompanies");
        return em.createNamedQuery("getAllCarRentalCompanies", String.class).getResultList();

    }

    protected List<CarType> getCarTypesAtCompany(String company) {
        System.out.println("inside session for getCarTypesAtCompany");
        return em.createNamedQuery("getAllCarTypesAtCompany")
                .setParameter("company", company)
                .getResultList();
    }

    protected List<Integer> getCarIds(String company, String type) {
        return em.createNamedQuery("getCarIds", Integer.class)
                .setParameter("company", company)
                .setParameter("type", type)
                .getResultList();
    }

    protected List<Reservation> getReservationsByClient(String client) {
        System.out.println("Making query --");
        return em.createNamedQuery("getReservationsByClient")
            .setParameter("name", client)
            .getResultList();
    }

    protected Integer getNumberOfReservationsByClient(String client) {
        return em.createNamedQuery("getNumberOfReservationsByClient")
                .setParameter("name", client)
                .getFirstResult();
    }

    protected String getBestClient() {
        return em.createQuery("getBestClient", String.class)
                .getSingleResult();
    }

    protected Integer getNumberOfReservations(String company, String type, Integer id) {        
//                Number answer = (Number) em.createQuery("SELECT count(res) FROM CarRentalCompany crc JOIN crc.cars car Join car.reservations res WHERE car.type.name = :tname AND crc.name = :cname AND car.id = :id").setParameter("tname", type).setParameter("cname", company).setParameter("id",new Integer(id)).getSingleResult();
//                return answer.intValue();
            return em.createNamedQuery("getNumberOfReservationsByCRCByCarId", Long.class)
            .setParameter("type", type)
            .setParameter("company", company)
            .setParameter("id", id)
            .getSingleResult()
            .intValue();
             //return ans.intValue();
    }
    
    protected Integer getNumberOfReservations(String company, String type) {        
//        System.out.println("Making query -d-" +em.createNamedQuery("getNumberOfReservationsByCRCByType")
//            .setParameter("type", type)
//            .setParameter("company", company).getFirstResult());
//        return 0;
        return em.createNamedQuery("getNumberOfReservationsByCRCByType", Long.class)
            .setParameter("type", type)
            .setParameter("company", company)
                .getSingleResult()
                .intValue();
    }

}
