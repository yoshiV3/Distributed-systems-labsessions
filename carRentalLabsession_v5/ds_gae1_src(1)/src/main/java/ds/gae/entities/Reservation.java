package ds.gae.entities;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import com.google.cloud.Timestamp;
import com.google.cloud.datastore.*;

public class Reservation extends Quote {

    private long carId;

    /***************
     * CONSTRUCTOR *
     ***************/

    public Reservation(Quote quote, long carId) {
        this(
                quote.getRenter(),
                quote.getStartDate(),
                quote.getEndDate(),
                quote.getRentalCompany(),
                quote.getCarType(),
                quote.getRentalPrice()
        );
        this.carId = carId;
    }

    private Reservation(
            String renter,
            Date start,
            Date end,
            String rentalCompany,
            String carType,
            double rentalPrice) {
        super(renter, start, end, rentalCompany, carType, rentalPrice);
    }
    
    @Override
    public Entity persist(Datastore ds)
    {
    	KeyFactory kf = ds.newKeyFactory().setKind("Reservation")
    			          .addAncestors(PathElement.of("Car", this.carId));
    	Key k         = ds.allocateId(kf.newKey());
    	Entity r    = Entity.newBuilder(k)
    			        .set("renter", this.getRenter())
    			        .set("company", this.getRentalCompany())
    			        .set("startDate", Timestamp.of(this.getStartDate()))
    			        .set("endDate", Timestamp.of((this.getEndDate())))
    			        .set("carType", this.getCarType())
    			        .set("rentalPrice", this.getRentalPrice())
    			        .build();
    	ds.put(r);
    	return r;    	
    }

    /******
     * ID *
     ******/

    public long getCarId() {
        return carId;
    }

    /*************
     * TO STRING *
     *************/

    @Override
    public String toString() {
        return String.format(
                "Reservation for %s from %s to %s at %s\nCar type: %s\tCar: %s\nTotal price: %.2f",
                getRenter(),
                getStartDate(),
                getEndDate(),
                getRentalCompany(),
                getCarType(),
                getCarId(),
                getRentalPrice()
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getCarId());
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) {
            return false;
        }
        Reservation other = (Reservation) obj;
        if (getCarId() != other.getCarId()) {
            return false;
        }
        return true;
    }

	public static Reservation fromEntityToReservation(Entity result)
    {
    	if (! result.getKey().getKind().equals("Reservation"))
    	{
    		throw new IllegalArgumentException("Not a Car type");
    	}
    	Quote quote =  new Quote (result.getString("renter")
    			           ,result.getTimestamp("startDate").toDate()
    			           ,result.getTimestamp("endDate").toDate()
    			           ,result.getString("company")
    			           ,result.getString("carType")
    			           ,result.getDouble("rentalPrice"));
    	return new Reservation(quote, result.getKey().getId());
    }
}
