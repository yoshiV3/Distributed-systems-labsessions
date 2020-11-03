package session;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.ejb.Stateful;
import rental.CarType;
import rental.RentalStore;
import rental.CarRentalCompany;

@Stateful
public class ReservationSession implements ReservationSessionRemote {

    @Override
    public Set<String> getAllRentalCompanies() {
        return new HashSet<String>(RentalStore.getRentals().keySet());
    }
    
    @Override
    public Set<CarType> getAvailableCarTypes(Date start, Date end)
    {
        Set<CarType> availableCarTypes = new HashSet<CarType>();
        for (CarRentalCompany comp : RentalStore.getRentals().values())
        {
            availableCarTypes.addAll(comp.getAvailableCarTypes(start, end));
        }
        return availableCarTypes;
    }

    
    
}
