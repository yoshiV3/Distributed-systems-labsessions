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
            query = "SELECT crc.name FROM CarRentalCompany crc") //works
    ,@NamedQuery(
            name = "getAllCarTypesAtCompany",
            query = "SELECT crc.carTypes FROM CarRentalCompany crc WHERE crc.name = :company") //works 
    ,@NamedQuery(
            name = "getCarIds",
            query = "SELECT car.id FROM CarRentalCompany crc JOIN crc.cars car WHERE  crc.name = :company and car.type.name = :type") //works 
    ,@NamedQuery(
            name = "getReservationsByClient",
            query = "SELECT res FROM Reservation res WHERE res.carRenter = :name") //works 
,@NamedQuery(
            name = "getNumberOfReservationsByClient",
            query = "SELECT count(res) FROM Reservation res WHERE res.carRenter = :name") 
,@NamedQuery(
            name = "getBestClient",
            query = "SELECT max((count(res))) FROM Reservation res GROUP BY res.carRenter") 
,@NamedQuery(
            name = "getNumberOfReservationsByCRCByCarId",
            query = "SELECT count(res) FROM CarRentalCompany crc JOIN crc.cars car Join car.reservations res WHERE car.type.name = :type AND crc.name = :company AND car.id = :id") 
,@NamedQuery(
            name = "getNumberOfReservationsByCRCByType",
            query = "SELECT count(res) FROM CarRentalCompany crc JOIN crc.cars car Join car.reservations res WHERE car.type.name = :type AND crc.name = :company") 

//,@NamedQuery(
//            name = "",
//            query = "") 
//,@NamedQuery(
//            name = "",
//            query = "") 
        
        
        
        
        ,})
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
    @OneToMany
    private List<Car> cars = new ArrayList<Car>();
    private List<String> regions;
    @ManyToMany
    private Set<CarType> carTypes = new HashSet<CarType>();

    /**
     * *************
     * CONSTRUCTORS *
     **************
     */
    public CarRentalCompany() {
    }

    public CarRentalCompany(String name, List<String> regions) {
        setName(name);
        setRegions(regions);
    }

    public CarRentalCompany(String name, List<String> regions, List<Car> cars) {
        logger.log(Level.INFO, "<{0}> Starting up CRC {0} ...", name);
        setName(name);
        this.cars = cars;
        setRegions(regions);
    }

    /**
     * ******
     * NAME *
     *******
     */
    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    /**
     * *********
     * Regions *
     *********
     */
    private void setRegions(List<String> regions) {
        this.regions = regions;
    }

    public Collection<String> getRegions() {
        return this.regions;
    }

    /**
     * *******
     * CARS *
     ********
     */
    public Collection<Car> getCars() {
        return this.cars;
    }

    @TransactionAttribute(MANDATORY)
    public void addCar(Car car) {
        this.cars.add(car);
    }

    @TransactionAttribute(MANDATORY)
    public void removeCar(Car car) {
        this.cars.remove(car);
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
    public Collection<CarType> getCarTypes() {
        return this.carTypes;
    }

    @TransactionAttribute(SUPPORTS)
    public CarType getType(String carTypeName) {
        for (CarType type : carTypes) {
            if (type.getName().equals(carTypeName)) {
                return type;
            }
        }
        throw new IllegalArgumentException("<" + carTypeName + "> No cartype of name " + carTypeName);
    }

    @TransactionAttribute(SUPPORTS)
    public boolean isAvailable(String carTypeName, Date start, Date end) {
        logger.log(Level.INFO, "<{0}> Checking availability for car type {1}", new Object[]{name, carTypeName});
        return getAvailableCarTypes(start, end).contains(getType(carTypeName));
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

    @TransactionAttribute(MANDATORY)
    public void addCarType(CarType type) {
        this.carTypes.add(type);
    }

    @TransactionAttribute(MANDATORY)
    public void removeCarType(CarType type) {
        this.carTypes.remove(type);
    }

    @TransactionAttribute(MANDATORY)
    public Quote createQuote(ReservationConstraints constraints, String guest)
            throws ReservationException {
        logger.log(Level.INFO, "<{0}> Creating tentative reservation for {1} with constraints {2}",
                new Object[]{name, guest, constraints.toString()});

        if (!this.regions.contains(constraints.getRegion()) || !isAvailable(constraints.getCarType(), constraints.getStartDate(), constraints.getEndDate())) {

            throw new ReservationException("<" + name
                    + "> No cars available to satisfy the given constraints.");
        }

        CarType type = getType(constraints.getCarType());

        double price = calculateRentalPrice(type.getRentalPricePerDay(), constraints.getStartDate(), constraints.getEndDate());

        return new Quote(guest, constraints.getStartDate(), constraints.getEndDate(), getName(), constraints.getCarType(), price);
    }

    // Implementation can be subject to different pricing strategies
    private double calculateRentalPrice(double rentalPricePerDay, Date start, Date end) {
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
