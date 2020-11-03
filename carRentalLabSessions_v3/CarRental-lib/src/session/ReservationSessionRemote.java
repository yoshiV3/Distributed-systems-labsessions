package session;


import  rental.CarType;

import java.util.Set;
import javax.ejb.Remote;
import java.util.Date;

@Remote
public interface ReservationSessionRemote {

    Set<String> getAllRentalCompanies();
    
    Set<CarType> getAvailableCarTypes(Date start, Date end);
    
    
}
