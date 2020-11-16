package session;

import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.ejb.Remote;
import rental.CarType;
import rental.Quote;
import rental.ReservationPrint;
import rental.ReservationConstraints;
import rental.ReservationException;

@Remote
public interface ReservationSessionRemote {
    
    public void setRenterName(String name);
    
    public String getRenterName();
    
    public Set<String> getAllRentalCompanies();
    
    public List<CarType> getAvailableCarTypes(Date start, Date end);
    
    public Quote createQuote(String company, ReservationConstraints constraints) throws ReservationException;
    
    public List<Quote> getCurrentQuotes();
    
    public List<ReservationPrint> confirmQuotes() throws ReservationException;
    
    public List<ReservationPrint> getMyReservations();
    
}