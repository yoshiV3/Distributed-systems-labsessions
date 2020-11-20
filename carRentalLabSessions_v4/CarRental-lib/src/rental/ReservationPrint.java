
package rental;

import java.io.Serializable;

/**
 *
 * @author Yoshi and Tejas
 */
public class ReservationPrint extends Quote implements Serializable{
    
    
    private int carId;
    private int reservationId;
    
    public ReservationPrint(Quote quote, int carId, int reservationId) {
    	super(quote.getCarRenter(), quote.getStartDate(), quote.getEndDate(), 
    		quote.getRentalCompany(), quote.getCarType(), quote.getRentalPrice());
        this.carId = carId;
        this.reservationId = reservationId;
    }
    
    public int getReservationId()
    {
        return this.reservationId;
    }
    public int getCarId()
    {
        return this.carId;
    }
    
    
    @Override
    public String toString() {
        return String.format("Reservation for %s from %s to %s at %s\nCar type: %s\tCar: %s\nTotal price: %.2f", 
                getCarRenter(), getStartDate(), getEndDate(), getRentalCompany(), getCarType(), this.getCarId(), getRentalPrice());
    }
    
}
