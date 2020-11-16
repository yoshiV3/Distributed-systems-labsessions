package rental;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import static javax.persistence.TemporalType.DATE;
import rental.Quote;


@Entity
public class Reservation {
    
    
    
    @Id
    @GeneratedValue
    private int id;
    @ManyToOne
    private Car car;
    @Temporal(DATE)
    private Date startDate;
    @Temporal(DATE)
    private Date endDate;
    private String carRenter;
    private String rentalCompany;
    private String carType;
    private double rentalPrice;
    
    /***************
     * CONSTRUCTOR *
     ***************/

    
    public Reservation(){}
    
    public Reservation(Quote quote, Car car) {
    	getInfoFromQuote(quote.getCarRenter(), quote.getStartDate(), quote.getEndDate(), 
    		quote.getRentalCompany(), quote.getCarType(), quote.getRentalPrice());
        this.car = car;
    }
    
    private void getInfoFromQuote(String carRenter, Date start, Date end, String rentalCompany, String carType, double rentalPrice)
    {
        this.carRenter = carRenter;
        this.startDate = start;
        this.endDate = end;
        this.rentalCompany = rentalCompany;
        this.carType = carType;
        this.rentalPrice = rentalPrice;        
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
    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public String getCarRenter() {
        return carRenter;
    }

    public String getRentalCompany() {
        return rentalCompany;
    }

    public double getRentalPrice() {
        return rentalPrice;
    }
    
    public String getCarType() {
	return carType;
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
        return new ReservationPrint(quote, this.car.getId(), this.getId());
    }
}