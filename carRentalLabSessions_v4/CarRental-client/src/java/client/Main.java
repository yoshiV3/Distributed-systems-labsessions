package client;

import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.naming.InitialContext;
import rental.CarType;
import rental.ReservationPrint;
import rental.ReservationConstraints;
import session.ManagerSessionRemote;
import session.ReservationSessionRemote;

public class Main extends AbstractTestManagement<ReservationSessionRemote, ManagerSessionRemote> {

    public Main(String scriptFile) {
        super(scriptFile);
    }

    public static void main(String[] args) throws Exception {
        // TODO: use updated manager interface to load cars into companies

        ManagerSessionRemote sessionM = (ManagerSessionRemote) (new InitialContext()).lookup(ManagerSessionRemote.class.getName());
//        ReservationSessionRemote session = (ReservationSessionRemote) (new InitialContext()).lookup(ReservationSessionRemote.class.getName());
//        ReservationSessionRemote session2 = (ReservationSessionRemote) (new InitialContext()).lookup(ReservationSessionRemote.class.getName());
//        ReservationSessionRemote session3 = (ReservationSessionRemote) (new InitialContext()).lookup(ReservationSessionRemote.class.getName());
        sessionM.loadRental("dockx.csv");
        sessionM.loadRental("hertz.csv");
        new Main("trips").run();
//        sessionM.addCarTypeToRental("yoshi", 0, 0, 0, true, "Dockx");
//        System.out.println(sessionM.getAllRentalCompanies());
//        System.out.println("changed this - " + sessionM.getCarTypes("Dockx"));
//        session.setRenterName("Te");
//        session2.setRenterName("Tejas");
//        session3.setRenterName("Tej");

//        //System.out.println("getting all cars "+sessionM.getCarTypesAtCompany
//        System.out.println("getting all cars " + sessionM.getAllCars() + "\n number = " + sessionM.getAllCars().size());
//          System.out.println("getting cehapest : " + session.getCheapestCarType(new Date(2019, 11, 31), new Date(2020, 1, 4), "Brussels")); 
//        System.out.println("available cars" + session.getAvailableCarTypes(new Date(2019, 11, 31), new Date(2020, 1, 4)) + "\n size =" + session.getAvailableCarTypes(new Date(2019, 11, 31), new Date(2020, 1, 4)).size());
//        ReservationConstraints constr = new ReservationConstraints(new Date(2019, 11, 31), new Date(2020, 1, 4), "Compact", "Brussels");
//        session.createQuote(constr);
//        ReservationConstraints constr2 = new ReservationConstraints(new Date(2019, 11, 31), new Date(2020, 1, 4), "Compact", "Brussels");
//        session2.createQuote(constr2);
//        ReservationConstraints const3 = new ReservationConstraints(new Date(2019, 11, 31), new Date(2020, 1, 4), "Compact", "Brussels");
//        session3.createQuote(const3);
//        System.out.println(session.getCurrentQuotes());
//        System.out.println(session3.confirmQuotes());
//        System.out.println(session2.confirmQuotes());
//        System.out.println(session.confirmQuotes());
//        System.out.println("checking available cars again " + session.getAvailableCarTypes(new Date(2019, 11, 31), new Date(2020, 1, 4)) + "\n size= " + session.getAvailableCarTypes(new Date(2019, 11, 31), new Date(2020, 1, 4)).size());
//        System.out.println("my reservation: " + session.getMyReservations());
//        System.out.println("all reservations " + sessionM.getAllReservations());


//            System.out.println("best client"+sessionM.getBestClients());
//            System.out.println("best client"+sessionM.getMostPopularCarTypeInCompanyInYear("Test",2020));
//        System.out.println(sessionM.getCarIds("Dockx", "Eco"));
//        System.out.println(sessionM.getCarIds("Test", "Mini"));
//
//        System.out.println(sessionM.getNumberOfReservations("Test", "Mini", 50));
//        System.out.println("test " + sessionM.getNumberOfReservations("Test", "Mini"));

    }

    @Override
    protected Set<String> getBestClients(ManagerSessionRemote ms) throws Exception {
        return ms.getBestClients();
    }

    @Override
    protected String getCheapestCarType(ReservationSessionRemote session, Date start, Date end, String region) throws Exception {
        return session.getCheapestCarType(start, end, region);

    }

    @Override
    protected CarType getMostPopularCarTypeIn(ManagerSessionRemote ms, String carRentalCompanyName, int year) throws Exception {
        return ms.getMostPopularCarTypeInCompanyInYear(carRentalCompanyName, year);

    }

    @Override
    protected ReservationSessionRemote getNewReservationSession(String name) throws Exception {
        ReservationSessionRemote session = (ReservationSessionRemote) (new InitialContext()).lookup(ReservationSessionRemote.class.getName());
        session.setRenterName(name);
        return session;
    }

    @Override
    protected ManagerSessionRemote getNewManagerSession(String name) throws Exception {
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
    protected void createQuote(ReservationSessionRemote session, String name, Date start, Date end, String carType, String region) throws Exception {
        System.out.println("Creating Quote for [" + name + "] between [" + start + " to " + end + "] for CarType <" + carType + "> in Region <" + region + ">");
        session.createQuote(new ReservationConstraints(start, end, carType, region));
    }

    @Override
    protected List<ReservationPrint> confirmQuotes(ReservationSessionRemote session, String name) throws Exception {
        //System.out.println("Confirming Quotes for [" + name + "]");
        //List<Reservation> quotes = session.confirmQuotes();
        //System.out.println(quotes);
        return session.confirmQuotes();

//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected int getNumberOfReservationsBy(ManagerSessionRemote ms, String clientName) throws Exception {
        return ms.getNumberOfReservationsByClient(clientName);
    }

    @Override
    protected int getNumberOfReservationsByCarType(ManagerSessionRemote ms, String carRentalName, String carType) throws Exception {
        return ms.getNumberOfReservations(carRentalName, carType);
    }
}
