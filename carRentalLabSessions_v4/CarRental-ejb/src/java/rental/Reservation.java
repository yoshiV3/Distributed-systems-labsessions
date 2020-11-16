package rental;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import rental.Quote;


@Entity
public class Reservation extends Quote {
    
    
    
    @Id
    @GeneratedValue
    private int id;
    @ManyToOne
    private Car car;
    
    /***************
     * CONSTRUCTOR *
     ***************/

    
    public Reservation(){}
    
    public Reservation(Quote quote, Car car) {
    	super(quote.getCarRenter(), quote.getStartDate(), quote.getEndDate(), 
    		quote.getRentalCompany(), quote.getCarType(), quote.getRentalPrice());
        this.car = car;
    }
    
    /******
     * ID *
     ******/
    public int getId()
    {
        return this.id;
    }
  
    public Car getCar() {
    	return this.car;
    }
    
    /*************
     * TO STRING *
     *************/
    
    @Override
    public String toString() {
        return String.format("Reservation for %s from %s to %s at %s\nCar type: %s\tCar: %s\nTotal price: %.2f", 
                getCarRenter(), getStartDate(), getEndDate(), getRentalCompany(), getCarType(), this.car.getId(), getRentalPrice());
    }
    
    public ReservationPrint toReservationPrint()
    {
        Quote quote = new Quote(this.getCarRenter(), this.getStartDate(), this.getEndDate(), this.getRentalCompany(), this.getCarType(), this.getRentalPrice());
        return new ReservationPrint(quote, this.car.getId());
    }
}