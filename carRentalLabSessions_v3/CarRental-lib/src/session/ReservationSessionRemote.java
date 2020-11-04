package session;


import  rental.CarType;

import java.util.Set;
import javax.ejb.Remote;
import java.util.Date;
import rental.Quote;
import rental.ReservationConstraints;

@Remote
public interface ReservationSessionRemote {

    Set<String> getAllRentalCompanies();
    
    Set<CarType> getAvailableCarTypes(Date start, Date end);
    
    Quote createQuote(ReservationConstraints constraint);
    
    void initialize(String guest_name);

    
    
}
