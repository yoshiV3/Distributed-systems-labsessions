package session;


import  rental.CarType;

import java.util.Set;
import javax.ejb.Remote;
import java.util.Date;
import java.util.List;
import rental.Quote;
import rental.Reservation;
import rental.ReservationConstraints;
import rental.ReservationException;

@Remote
public interface ReservationSessionRemote {

    Set<String> getAllRentalCompanies();
    
    Set<CarType> getAvailableCarTypes(Date start, Date end);
    
    Quote createQuote(ReservationConstraints constraint);
    
    void initialize(String clientName);
    
    public Set<Quote> getCurrentQuotes();
    
    public List<Reservation> confirmQuotes() throws ReservationException;
  
    
}
