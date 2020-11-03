package client;

import javax.ejb.EJB;
import session.ReservationSessionRemote;
import java.util.Date;

public class Main {
    
    @EJB
    static ReservationSessionRemote session;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("found rental companies: "+session.getAllRentalCompanies());
        System.out.println("found available car types: "+session.getAvailableCarTypes(new Date(2020,10,3), new Date(2020,10,20)));
    }
}
