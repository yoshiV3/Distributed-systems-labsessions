package rental;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;


@Entity 
public class CarRentalCompany {

    private static Logger logger = Logger.getLogger(CarRentalCompany.class.getName());
    @Id
    private String name;
    @OneToMany
    private Collection<Car> cars;
    private Collection<String> regions;
    @OneToMany
    private Collection<CarType> carTypes;

	
    /***************
     * CONSTRUCTORS *
     ***************/
    public CarRentalCompany(){}
    
    public CarRentalCompany(String name, List<String> regions)
    {
        setName(name);
        setRegions(regions);
    }
    
    
    public CarRentalCompany(String name, List<String> regions, List<Car> cars) {
        logger.log(Level.INFO, "<{0}> Starting up CRC {0} ...", name);
        setName(name);
        this.cars = cars;
        setRegions(regions);
    }

    /********
     * NAME *
     ********/
    
    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    /***********
     * Regions *
     **********/
    private void setRegions(List<String> regions) {
        this.regions = regions;
    }
    
    public Collection<String> getRegions() {
        return this.regions;
    }


    /*********
     * CARS *
     *********/
    
    public Collection<Car> getCars() {
        return this.cars;
    }
     
     public void setCars(Collection<Car> cars)
     {
         this.cars = cars;
     }
     
     public void addCar(Car car)
     {
         this.cars.add(car);
     }
     
     public void removeCar(Car car)
     {
         this.cars.remove(car);
     }
     
     public Collection<CarType> getCarTypes()
     {
         return this.carTypes;
     }
     
     public void addCarType(CarType type)
     {
         this.carTypes.add(type);
     }
     
     public void removeCarType(CarType type)
     {
         this.carTypes.remove(type);
     }
}