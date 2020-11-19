package rental;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.TransactionAttribute;
import static javax.ejb.TransactionAttributeType.MANDATORY;
import static javax.ejb.TransactionAttributeType.SUPPORTS;
import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

//return em.createQuery("SELECT crc.name FROM CarRentalCompany crc", String.class).getResultList();
@NamedQueries({
    @NamedQuery(
            name = "getAllCarRentalCompanies",
            query = "SELECT crc FROM CarRentalCompany crc"),

    @NamedQuery(
            name = "getAllCarRentalCompaniesByRegion",
            query = "SELECT crc.name FROM CarRentalCompany crc WHERE crc.regions=:region"),

    @NamedQuery(
            name = "getAllCarTypesAtCompany",
            query = "SELECT crc.carTypes FROM CarRentalCompany crc WHERE crc.name = :company"),
    @NamedQuery(
            name = "getCarType",
            query = "SELECT cartype FROM CarType cartype WHERE cartype.name = :type"),

    
    @NamedQuery(
            name = "getCarIds",
            query = "SELECT car.id FROM CarRentalCompany crc JOIN crc.cars car WHERE  crc.name = :company and car.type.name = :type"),

    @NamedQuery(
            name = "getReservationsByClient",
            query = "SELECT res FROM Reservation res WHERE res.carRenter = :name"),

    @NamedQuery(
            name = "getNumberOfReservationsByClient",
            query = "SELECT count(res) FROM Reservation res WHERE res.carRenter = :name"),
    @NamedQuery(
            name = "getNumberOfReservationsByCRCByCarId",
            query = "SELECT count(res) FROM CarRentalCompany crc JOIN crc.cars car JOIN car.reservations res WHERE car.type.name = :type AND crc.name = :company AND car.id = :id"),
    @NamedQuery(
            name = "getNumberOfReservationsByCRCByType",
            query = "SELECT count(res) FROM CarRentalCompany crc JOIN crc.cars car JOIN car.reservations res WHERE car.type.name = :type AND crc.name = :company"),
    @NamedQuery(
            name = "getBestClient",
            query = "SELECT res.carRenter, SUM(res.rentalPrice) as price FROM Reservation res GROUP BY res.carRenter ORDER BY price desc"),
            
    @NamedQuery(
            name = "getAllCars",
            query = "SELECT car.type from Car car"),
    @NamedQuery(
            name = "getAllReservations",
            query = "SELECT res.car.id FROM Reservation res"),
    @NamedQuery(
            name = "getAvailableCarTypes",
            query = "SELECT DISTINCT car.type from Car car WHERE car.id NOT IN (SELECT res.car.id FROM Reservation res WHERE res.endDate BETWEEN :startDate AND :endDate)"),

    @NamedQuery(
            name = "getCheapestCarTypeInRegionInDates",
            query = "SELECT DISTINCT car.type.name from CarRentalCompany crc JOIN Car car WHERE :region MEMBER OF crc.regions AND car IN (crc.cars) AND car.id NOT IN (SELECT res.car.id FROM Reservation res WHERE res.endDate BETWEEN :startDate AND :endDate) ORDER BY car.type.rentalPricePerDay asc"),
    @NamedQuery(
            name = "getMostPopularCarTypeInCompanyInYear",
            query = "SELECT res.carType, COUNT(DISTINCT res.id) as reservations FROM Reservation res WHERE res.rentalCompany=:company AND  (EXTRACT(YEAR from res.startDate)=:year OR  EXTRACT(YEAR from res.endDate)=:year) GROUP BY res.carType ORDER BY reservations DESC")
//,@NamedQuery(
//            name = "",
//            query = "") 
//,@NamedQuery(
//            name = "",
//            query = "") 
})
//
//     @NamedQuery(name = "getAllCarRentalCompanies", query
//            = "SELECT crc FROM CarRentalCompany crc")
//    ,
//
//    @NamedQuery(name = "getAvailableCarTypes", query
//            = "SELECT DISTINCT car.type FROM Car car "
//            + "WHERE car.id NOT IN "
//            + "("
//            + "SELECT res.carId FROM Reservation res "
//            + "WHERE res.startDate <= :endDate AND res.endDate >= :startDate"
//            + ")")
//    ,
//})

@Entity
public class CarRentalCompany {

    @Transient
    private static Logger logger = Logger.getLogger(CarRentalCompany.class.getName());

    @Id
    private String name;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Car> cars = new ArrayList<Car>();
    @ElementCollection
    private List<String> regions;
    @ManyToMany(cascade = CascadeType.ALL)
    private Set<CarType> carTypes = new HashSet<CarType>();

    /**
     * *************
     * CONSTRUCTORS * *************
     */
    public CarRentalCompany() {
    }

    public CarRentalCompany(String name) {//, List<String> regions) {
        setName(name);
    }

    public CarRentalCompany(String name, List<String> regions, List<Car> cars) {
        logger.log(Level.INFO, "<{0}> Starting up CRC {0} ...", name);
        setName(name);
        this.cars = cars;
        setRegions(regions);
    }

    /**
     * ******
     * NAME * ******
     */
    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    /**
     * *********
     * Regions * ********
     */
    public void setRegions(List<String> regions) {
        this.regions = regions;
    }

    public List<String> getRegions() {
        return this.regions;
    }

    /**
     * *******
     * CARS * *******
     */
    public List<Car> getCars() {
        return cars;
    }

    @TransactionAttribute(MANDATORY)
    public void addCar(Car car) {
        cars.add(car);
    }

    @TransactionAttribute(MANDATORY)
    public void removeCar(Car car) {
        cars.remove(car);
    }

    @TransactionAttribute(SUPPORTS)
    private List<Car> getAvailableCars(String carType, Date start, Date end) {
        List<Car> availableCars = new LinkedList<Car>();
        for (Car car : cars) {
            if (car.getType().getName().equals(carType) && car.isAvailable(start, end)) {
                availableCars.add(car);
            }
        }
        return availableCars;
    }

    /**
     * ************************
     *
     * CARTYPES
     */
    public Set<CarType> getCarTypes() {
        return carTypes;
    }

    @TransactionAttribute(SUPPORTS)
    public CarType getType(String carTypeName) {
        for (CarType type : getCarTypes()) {
            if (type.getName().equals(carTypeName)) {
                return type;
            }
        }
        return null;
    }

    @TransactionAttribute(SUPPORTS)
    public boolean isAvailable(CarType type, Date start, Date end) {
        if (type != null) {
            return getAvailableCarTypes(start, end).contains(type);
        }
        return false;
    }

    @TransactionAttribute(SUPPORTS)
    public Set<CarType> getAvailableCarTypes(Date start, Date end) {
        Set<CarType> availableCarTypes = new HashSet<CarType>();
        for (Car car : cars) {
            if (car.isAvailable(start, end)) {
                availableCarTypes.add(car.getType());
            }
        }
        return availableCarTypes;
    }

    public boolean operatesInRegion(String region) {
        return this.regions.contains(region);
    }

    public boolean canReserve(ReservationConstraints constraints) {

        boolean result = operatesInRegion(constraints.getRegion());
        result = result && isAvailable(getType(constraints.getCarType()), constraints.getStartDate(), constraints.getEndDate());
        return result;
    }

    @TransactionAttribute(MANDATORY)
    public void addCarType(CarType type) {

        carTypes.add(type);
    }

    @TransactionAttribute(MANDATORY)
    public void removeCarType(CarType type) {
        carTypes.remove(type);
    }

    @TransactionAttribute(MANDATORY)
    public Quote createQuote(ReservationConstraints constraints, String guest)
            throws ReservationException {
        logger.log(Level.INFO, "<{0}> Creating tentative reservation for {1} with constraints {2}",
                new Object[]{name, guest, constraints.toString()});
        CarType type = getType(constraints.getCarType());
        if (!this.regions.contains(constraints.getRegion()) || !isAvailable(type, constraints.getStartDate(), constraints.getEndDate())) {

            throw new ReservationException("<" + name
                    + "> No cars available to satisfy the given constraints.");
        }

        double price = calculateRentalPrice(type.getRentalPricePerDay(), constraints.getStartDate(), constraints.getEndDate());

        return new Quote(guest, constraints.getStartDate(), constraints.getEndDate(), getName(), constraints.getCarType(), price);
    }

    // Implementation can be subject to different pricing strategies
    public double calculateRentalPrice(double rentalPricePerDay, Date start, Date end) {
        return rentalPricePerDay * Math.ceil((end.getTime() - start.getTime())
                / (1000 * 60 * 60 * 24D));
    }

    @TransactionAttribute(MANDATORY)
    public Reservation confirmQuote(Quote quote) throws ReservationException {
        logger.log(Level.INFO, "<{0}> Reservation of {1}", new Object[]{name, quote.toString()});
        List<Car> availableCars = getAvailableCars(quote.getCarType(), quote.getStartDate(), quote.getEndDate());
        if (availableCars.isEmpty()) {
            throw new ReservationException("Reservation failed, all cars of type " + quote.getCarType()
                    + " are unavailable from " + quote.getStartDate() + " to " + quote.getEndDate());
        }
        Car car = availableCars.get((int) (Math.random() * availableCars.size()));

        Reservation res = new Reservation(quote, car);
        car.addReservation(res);
        return res;
    }
}
