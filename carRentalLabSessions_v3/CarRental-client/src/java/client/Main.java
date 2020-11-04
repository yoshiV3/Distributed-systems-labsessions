package client;

import javax.ejb.EJB;
import session.ReservationSessionRemote;
import java.util.Date;
import rental.ReservationConstraints;
import rental.Quote;

public class Main {
    
    @EJB
    static ReservationSessionRemote session;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        session.initialize("Yoshi");
        System.out.println("found rental companies: "+session.getAllRentalCompanies());
        System.out.println("found available car types: "+session.getAvailableCarTypes(new Date(2020,10,3), new Date(2020,10,20)));
        System.out.println("created this quote:" + session.createQuote(new ReservationConstraints(new Date(2020,10,3), new Date(2020,10,20),"Top","FlemishBrabant")) );
    }

}
