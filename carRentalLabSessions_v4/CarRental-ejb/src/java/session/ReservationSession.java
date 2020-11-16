package session;

import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.ejb.EJBException;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import static javax.ejb.TransactionAttributeType.SUPPORTS;
import javax.persistence.EntityManager;
import rental.CarRentalCompany;
import rental.CarType;
import rental.Quote;
import rental.Reservation;
import rental.ReservationConstraints;
import rental.ReservationException;
import rental.ReservationPrint;

@Stateful
public class ReservationSession extends Session implements ReservationSessionRemote {

    private String renter;
    private List<Quote> quotes = new LinkedList<Quote>();

    @Override
    @TransactionAttribute(SUPPORTS)
    public Set<String> getAllRentalCompanies() {
        Set<String> companies = new HashSet();
        for (String company : this.getAllCarRentalCompanies())
        {
            companies.add(company);
        }
        return companies;
    }
    
    @Override
    @TransactionAttribute(SUPPORTS)
    public List<CarType> getAvailableCarTypes(Date start, Date end) {
        List<CarType> availableCarTypes = new LinkedList(); 
        for (String company : this.getAllCarRentalCompanies())
        {
            EntityManager e = this.getEntityManager();
            CarRentalCompany crc = e.find(CarRentalCompany.class, company);
            for (CarType type : crc.getAvailableCarTypes(start, end))
            {
                availableCarTypes.add(type);
            }
        }
        return availableCarTypes;
    }
    
    

    @Override
    public Quote createQuote(String company, ReservationConstraints constraints) throws ReservationException {
        try
        {  
            if(! this.getAllCarRentalCompanies().contains(company))
            {
                this.getEJBContext().setRollbackOnly();
                throw new IllegalArgumentException("Not a valid  company name");
            }
            CarRentalCompany crc = this.getEntityManager().find(CarRentalCompany.class, company);
            Quote createdQuote = crc.createQuote(constraints, this.getRenterName());
            this.quotes.add(createdQuote);
            return createdQuote;
        }
        catch(Exception ex)
        {
            throw new EJBException(ex);
        }
    }

    @Override
    @TransactionAttribute(SUPPORTS)
    public List<Quote> getCurrentQuotes() {
        return quotes;
    }

    @Override
    public List<ReservationPrint> confirmQuotes() throws ReservationException {
        try
        {
            List<ReservationPrint> reservations = new LinkedList();
            for(Quote quote : this.quotes)
            {
                CarRentalCompany crc = this.getEntityManager().find(CarRentalCompany.class,quote.getRentalCompany());
                reservations.add(crc.confirmQuote(quote).toReservationPrint());
            }
            return reservations;
        }
        catch(Exception ex)
        {
            throw new EJBException(ex);
        }
        
    
    }

    @Override
    public void setRenterName(String name) {
        if (renter != null) {
            throw new IllegalStateException("name already set");
        }
        renter = name;
    }

    @Override
    public String getRenterName() {
        return renter;
    }
}