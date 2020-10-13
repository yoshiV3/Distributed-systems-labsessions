package rental;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Car {

    private int id;
    private CarType type;
    private List<Reservation> reservations;

    /***************
     * CONSTRUCTOR *
     ***************/
    
    public Car(int uid, CarType type) {
    	this.id = uid;
        this.type = type;
        this.reservations = new ArrayList<Reservation>();
    }

    /******
     * ID *
     ******/
    
    public int getId() {
    	return id;
    }
    
    /************
     * CAR TYPE *
     ************/
    
    public CarType getType() {
        return type;
    }

    /****************
     * RESERVATIONS *
     ****************/

    public boolean isAvailable(Date start, Date end) {
        if(!start.before(end))
            throw new IllegalArgumentException("Illegal given period");

        for(Reservation reservation : reservations) {
            if(reservation.getEndDate().before(start) || reservation.getStartDate().after(end))
                continue;
            return false;
        }
        return true;
    }
    
    public Set<Reservation> getReservationsFromClient(String client)
    { 
    	Set<Reservation> reservationsClient = new HashSet<Reservation>();
		for( Reservation reservation: this.reservations)
		{
			if( reservation.getCarRenter().equals(client))
			{
				reservationsClient.add(reservation);
			}
		}    	
		return reservationsClient;
    }
    
    public void addReservation(Reservation res) {
        reservations.add(res);
    }
    
    public void removeReservation(Reservation reservation) {
        // equals-method for Reservation is required!
        reservations.remove(reservation);
    }

	public List<Reservation> getReservations() {
		 List<Reservation> reservationsReturn =  new ArrayList<Reservation>();
		 Iterator<Reservation> iterator = this.reservations.iterator();
		 while (iterator.hasNext())
		 {
			 reservationsReturn.add((Reservation) iterator.next());  
		 }  
		 return reservationsReturn;
	}
}