package client;

import javax.ejb.EJB;
import java.util.Date;
import java.util.List;
import javax.naming.InitialContext;
import rental.CarType;
import rental.Reservation;
import rental.ReservationConstraints;
import rental.ReservationException;
import session.ManagerSessionRemote;
import session.ReservationSessionRemote;

public class Main extends AbstractTestAgency<ReservationSessionRemote, ManagerSessionRemote> {

    //similar to that of RMI
    public Main(String scriptFile) {
        super(scriptFile);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        Main client = new Main("simpleTrips");
        client.run();
    }

    @Override
    protected ReservationSessionRemote getNewReservationSession(String name)
            throws Exception {
        System.out.println("New Reservation session for [" + name + "]");
        ReservationSessionRemote session = (ReservationSessionRemote) (new InitialContext()).lookup(ReservationSessionRemote.class.getName());
        session.initialize(name);
        return session;
    }

    @Override
    protected ManagerSessionRemote getNewManagerSession(String name)
            throws Exception {
        System.out.println("New Manager session for [" + name + "]");
        ManagerSessionRemote session = (ManagerSessionRemote) (new InitialContext()).lookup(ManagerSessionRemote.class.getName());
        return session;
    }

    @Override
    protected void getAvailableCarTypes(ReservationSessionRemote session, Date start, Date end) throws Exception {
        System.out.println("Available Car Types between [" + start + " to " + end + "] are:  [");
        for (CarType cartypes : session.getAvailableCarTypes(start, end)) {
            System.out.print(cartypes.getName());
        }
        System.out.println("]");
    }

    @Override
    protected void createQuote(ReservationSessionRemote session, String name,
            Date start, Date end, String carType, String region) throws Exception {
        System.out.println("Creating Quote for [" + name + "] between [" + start + " to " + end + "] for CarType <" + carType + "> in Region <" + region + ">");
        session.createQuote(new ReservationConstraints(start, end, carType, region));
    }

    @Override
    protected List<Reservation> confirmQuotes(ReservationSessionRemote session, String name) throws Exception {
        System.out.println("Confirming Quotes for [" + name + "]");
        List<Reservation> quotes = session.confirmQuotes();
        System.out.println(quotes);
        return quotes;
    }

    @Override
    protected int getNumberOfReservationsBy(ManagerSessionRemote session, String clientName) throws Exception {
        return session.getNumberOfReservationsByRenter(clientName);
    }

    @Override
    protected int getNumberOfReservationsForCarType(ManagerSessionRemote session, String companyName, String carType) throws Exception {
        return session.getNumberOfReservationsForCarTypeAtCompany(companyName, carType);
    }

}
