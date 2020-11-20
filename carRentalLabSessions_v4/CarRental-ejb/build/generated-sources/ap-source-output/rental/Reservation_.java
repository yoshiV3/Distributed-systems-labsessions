package rental;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import rental.Car;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2020-11-20T02:02:44")
@StaticMetamodel(Reservation.class)
public class Reservation_ { 

    public static volatile SingularAttribute<Reservation, Double> rentalPrice;
    public static volatile SingularAttribute<Reservation, String> rentalCompany;
    public static volatile SingularAttribute<Reservation, String> carType;
    public static volatile SingularAttribute<Reservation, Car> car;
    public static volatile SingularAttribute<Reservation, Date> endDate;
    public static volatile SingularAttribute<Reservation, String> carRenter;
    public static volatile SingularAttribute<Reservation, Integer> id;
    public static volatile SingularAttribute<Reservation, Date> startDate;

}