/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.TransactionAttribute;
import static javax.ejb.TransactionAttributeType.NEVER;
import static javax.ejb.TransactionAttributeType.SUPPORTS;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;
import rental.CarRentalCompany;
import rental.CarType;
import rental.Reservation;
import rental.ReservationPrint;

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

    @TransactionAttribute(SUPPORTS)
    protected List<CarRentalCompany> getAllCarRentalCompanies() {
        System.out.println("inside session for getAllCarRentalCompanies");
        return em.createNamedQuery("getAllCarRentalCompanies")
                .getResultList();

    }
    @TransactionAttribute(SUPPORTS)
    protected List<String> getAllCarRentalCompanyNames() {
        List<String> crcnames = new ArrayList();
        List<CarRentalCompany> crcs = getAllCarRentalCompanies();
        for (CarRentalCompany crc : crcs) {
            crcnames.add(crc.getName());
        }
        return crcnames;

    }
    @TransactionAttribute(SUPPORTS)
    protected List<String> getAllCarRentalCompaniesByRegion(String region) {
        System.out.println("inside session for getAllCarRentalCompaniesByRegion");
        return em.createNamedQuery("getAllCarRentalCompaniesByRegion", String.class)
                .setParameter("region", region)
                .getResultList();

    }
    @TransactionAttribute(SUPPORTS)
    protected List<CarType> getAllCars() {
        return em.createNamedQuery("getAllCars")
                .getResultList();

    }
    @TransactionAttribute(SUPPORTS)
    protected List<CarType> getCarTypesAtCompany(String company) {
        System.out.println("inside session for getCarTypesAtCompany");
        return em.createNamedQuery("getAllCarTypesAtCompany")
                .setParameter("company", company)
                .getResultList();
    }
    @TransactionAttribute(SUPPORTS)
    protected List<Integer> getCarIds(String company, String type) {
        return em.createNamedQuery("getCarIds", Integer.class)
                .setParameter("company", company)
                .setParameter("type", type)
                .getResultList();
    }
    @TransactionAttribute(SUPPORTS)
    protected List<Integer> getAllReservations() {
        System.out.println("Making query --");
        return em.createNamedQuery("getAllReservations")
                .getResultList();
    }
    @TransactionAttribute(SUPPORTS)
    protected List<CarType> getAvailableCarTypes(Date start, Date end) {
        return em.createNamedQuery("getAvailableCarTypes")
                .setParameter("startDate", start)
                .setParameter("endDate", end)
                .getResultList();
    }
    @TransactionAttribute(SUPPORTS)
    protected List<Reservation> getReservationsByClient(String client) {
        System.out.println("Making query --");
        return em.createNamedQuery("getReservationsByClient")
                .setParameter("name", client)
                .getResultList();
    }
    @TransactionAttribute(SUPPORTS)
    protected Integer getNumberOfReservationsByClient(String client) {
        return  new Integer(((Number) em.createNamedQuery("getNumberOfReservationsByClient")
                .setParameter("name", client)
                .getSingleResult()).intValue());
    }
    @TransactionAttribute(SUPPORTS)
    protected Set<String> getBestClient() {
        Set<String>  bestClients = new HashSet(); 
        List<Object[]> client = em.createNamedQuery("getBestClient").getResultList();
        Integer max = new Integer(((Number) client.get(0)[1]).intValue());
        for (Object[] a : client) {
            if ((new Integer(((Number) a[1]).intValue())).equals(max)) {
                bestClients.add((String) a[0]);
            }
        }
        return bestClients;
    }
    @TransactionAttribute(SUPPORTS)
    protected CarType getMostPopularCarTypeInCompanyInYear(String company, Integer year) {
        List<Object[]> popularcartype = em.createNamedQuery("getMostPopularCarTypeInCompanyInYear")
                .setParameter("company", company)
                .setParameter("year", year) //3919
                .getResultList();
        String type = (String) popularcartype.get(0)[0];

        return getCarType(type);
    }
    @TransactionAttribute(SUPPORTS)
    protected Integer getNumberOfReservations(String company, String type, Integer id) {
        return em.createNamedQuery("getNumberOfReservationsByCRCByCarId", Long.class)
                .setParameter("type", type)
                .setParameter("company", company)
                .setParameter("id", id)
                .getSingleResult()
                .intValue();
    }
    @TransactionAttribute(SUPPORTS)
    protected Integer getNumberOfReservations(String company, String type) {
        return em.createNamedQuery("getNumberOfReservationsByCRCByType", Long.class)
                .setParameter("type", type)
                .setParameter("company", company)
                .getSingleResult()
                .intValue();
    }
    @TransactionAttribute(SUPPORTS)
    protected CarType getCarType(String type) {
        return em.createNamedQuery("getCarType", CarType.class)
                .setParameter("type", type)
                .setMaxResults(1)
                .getSingleResult();

    }
    @TransactionAttribute(SUPPORTS)
    protected String getCheapestCarTypeInRegionInDates(Date start, Date end, String region) {
        return em.createNamedQuery("getCheapestCarTypeInRegionInDates", String.class)
                .setParameter("startDate", start)
                .setParameter("endDate", end)
                .setParameter("region", region)
                .setMaxResults(1)
                .getSingleResult();

    }

}
