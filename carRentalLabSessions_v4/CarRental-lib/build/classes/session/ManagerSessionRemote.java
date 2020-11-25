package session;

import java.util.List;
import java.util.Set;
import javax.ejb.Remote;
import rental.CarType;

@Remote
public interface ManagerSessionRemote {

    public void loadRental(String dataFile);

    public void addCarTypeToRental(String name, int nbOfSeats, float trunkSpace, double rentalPricePerDay, boolean smokingAllowed, String company);

    public void addCarToRental(String type, String company);

    public List<CarType> getCarTypes(String company);

    public List<Integer> getCarIds(String company, String type);

    public Integer getNumberOfReservations(String company, String type, int carId);

    public Integer getNumberOfReservations(String company, String type);

    public Integer getNumberOfReservationsByClient(String client);

    public List<String> getAllRentalCompanies();

    public List<CarType> getAllCars();

    public List<Integer> getAllReservations();

    public CarType getMostPopularCarTypeInCompanyInYear(String company, Integer year);

    public Set<String> getBestClients();
}
