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
        //new Main("trips").run();
        ManagerSessionRemote sessionM = (ManagerSessionRemote) (new InitialContext()).lookup(ManagerSessionRemote.class.getName());
        ReservationSessionRemote session = (ReservationSessionRemote) (new InitialContext()).lookup(ReservationSessionRemote.class.getName());
        ReservationSessionRemote session2 = (ReservationSessionRemote) (new InitialContext()).lookup(ReservationSessionRemote.class.getName());
        sessionM.loadRental("dockx.csv");
        sessionM.loadRental("test.csv");
        sessionM.addCarTypeToRental("yoshi", 0, 0, 0, true, "Dockx");
        System.out.println(sessionM.getAllRentalCompanies());
        session.setRenterName("Yoshi");
        session2.setRenterName("Tejas");
        System.out.println(session.getAvailableCarTypes(new Date(2019,11,31), new Date(2020,1,4)));
        ReservationConstraints constr = new ReservationConstraints(new Date(2019,11,31), new Date(2020,1,4), "Mini", "Brussels");
        session.createQuote("Test", constr);
        System.out.println(session.getCurrentQuotes());
        System.out.println(session.confirmQuotes());
        System.out.println(session.getMyReservations());
        System.out.println(sessionM.getCarTypes("Test"));
    }

    @Override
    protected Set<String> getBestClients(ManagerSessionRemote ms) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected String getCheapestCarType(ReservationSessionRemote session, Date start, Date end, String region) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected CarType getMostPopularCarTypeIn(ManagerSessionRemote ms, String carRentalCompanyName, int year) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected ReservationSessionRemote getNewReservationSession(String name) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected ManagerSessionRemote getNewManagerSession(String name) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void getAvailableCarTypes(ReservationSessionRemote session, Date start, Date end) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void createQuote(ReservationSessionRemote session, String name, Date start, Date end, String carType, String region) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected List<ReservationPrint> confirmQuotes(ReservationSessionRemote session, String name) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected int getNumberOfReservationsBy(ManagerSessionRemote ms, String clientName) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected int getNumberOfReservationsByCarType(ManagerSessionRemote ms, String carRentalName, String carType) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}