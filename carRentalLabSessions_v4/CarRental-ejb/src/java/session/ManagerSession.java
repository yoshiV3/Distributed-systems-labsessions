package session;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import rental.Car;
import rental.CarRentalCompany;
import rental.CarType;
import rental.RentalStore;
import rental.Reservation;

@Stateless
public class ManagerSession implements ManagerSessionRemote {
    
    @Override
    public Set<CarType> getCarTypes(String company) {
        return null;
    }

    @Override
    public Set<Integer> getCarIds(String company, String type) {
        return null;
    }

    @Override
    public int getNumberOfReservations(String company, String type, int id) {
        return 0;
    }

    @Override
    public int getNumberOfReservations(String company, String type) {
        return 0;
    }

}