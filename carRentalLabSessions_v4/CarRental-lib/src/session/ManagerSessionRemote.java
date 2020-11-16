package session;

import java.util.List;
import java.util.Set;
import javax.ejb.Remote;
import rental.CarType;
import rental.ReservationPrint;

@Remote
public interface ManagerSessionRemote {
    
    
    public void loadRental(String dataFile);
    
    public void addCarTypeToRental(String name, int nbOfSeats, float trunkSpace, double rentalPricePerDay, boolean smokingAllowed, String company);
    
    public void addCarToRental(String type, String  company);
    
    
    public Set<CarType> getCarTypes(String company);
    
    public Set<Integer> getCarIds(String company,String type);
    
    public int getNumberOfReservations(String company, String type, int carId);
    
    public int getNumberOfReservations(String company, String type);
    
    public List<String> getAllRentalCompanies();
    
      
}