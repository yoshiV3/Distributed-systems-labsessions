package client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import rental.CarRentalCompany;
import rental.CarType;
import rental.ICarRentalCompany;
import rental.Quote;
import rental.Reservation;
import rental.ReservationConstraints;
import agency.IReservationSession;
import agency.IManagerSession;
import agency.ICarRentalAgency;

public class Client extends AbstractTestManagement<IReservationSession, IManagerSession> {

	/********
	 * MAIN *
	 ********/

	private static Logger logger = Logger.getLogger(Client.class.getName());
	private Registry registry = null;
	private ICarRentalAgency rentalAgency = null;
	private static ICarRentalCompany company = null;
	private final static int LOCAL = 0;
	private final static int REMOTE = 1;

	/**
	 * The `main` method is used to launch the client application and run the test
	 * script.
	 */
	public static void main(String[] args) throws Exception {

		if (System.getSecurityManager() != null)
			System.setSecurityManager(null);

		System.out.println("\n=============== Starting Client Process ===============\n");

		// The first argument passed to the `main` method (if present)
		// indicates whether the application is run on the remote setup or not.
		int localOrRemote = (args.length == 1 && args[0].equals("REMOTE")) ? REMOTE : LOCAL;

		String carRentalAgency = "testagency";

		Client client = new Client("trips", carRentalAgency, localOrRemote);
		client.setupManager();
		client.run();
	}

	/***************
	 * CONSTRUCTOR
	 * 
	 * @throws NotBoundException
	 * @throws RemoteException *
	 ***************/

	public Client(String scriptFile, String carRentalAgency, int localOrRemote)
			throws NotBoundException, RemoteException {
		super(scriptFile);

		logger.log(Level.INFO, "Connecting with <{0}> ", carRentalAgency);

		try {
			this.registry = LocateRegistry.getRegistry();
		} catch (RemoteException e) {
			System.exit(-1);
		}

		try {
			this.rentalAgency = (ICarRentalAgency) this.registry.lookup(carRentalAgency);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

	}

	protected IReservationSession getNewReservationSession(String client) throws Exception {
		System.out.println("--> New Reservation Session");
		return this.rentalAgency.openReservationSession(client);
	}

	protected IManagerSession getNewManagerSession(String manager) throws Exception {
		System.out.println("--> New Manager Session");
		return this.rentalAgency.openManagerSession(manager);
	}

	protected void checkForAvailableCarTypes(IReservationSession session, Date start, Date end) throws Exception {
		System.out.println("************Available cars ***********");
		Map<String, Set<CarType>> cartypes = session.getAvailableCarTypes(start, end);
		System.out.println("		CRC     -   CarType");
		for (String company : cartypes.keySet()) {
			for (CarType carType : cartypes.get(company)) {
				System.out.println("		" + company + "   -   " + carType.getName());
			}
		}
	}

	private void setupManager() throws Exception {
		System.out.println("Setting up manager");
		IManagerSession session = getNewManagerSession("manager");
		session.registerCompany("Hertz");
		session.registerCompany("Dockx");
	}

	protected void addQuoteToSession(IReservationSession session, String client, Date start, Date end, String carType,
			String region) throws Exception {
		ReservationConstraints rconstraints = new ReservationConstraints(start, end, carType, region);
		session.createQuote(rconstraints, client);
	}

	protected List<Reservation> confirmQuotes(IReservationSession session, String client) throws Exception {
		return session.confirmQuotes();
	}

	protected int getNumberOfReservationsByRenter(IManagerSession session, String clientName) throws Exception {
		return session.getNumberOfReservationsByRenter(clientName);
	}

	protected int getNumberOfReservationsForCarType(IManagerSession session, String carRentalName, String carType)
			throws Exception {
		return session.getNumberOfReservationsForCarType(carRentalName, carType);
	}

	protected Set<String> getBestClients(IManagerSession session) throws Exception {
		return session.getBestClients();
	}

	protected String getCheapestCarType(IReservationSession session, Date start, Date end, String region)
			throws Exception {
		return session.getCheapestCarType(start, end, region).getName();
	}

	protected CarType getMostPopularCarTypeInCRC(IManagerSession session, String carRentalCompanyName, int year)
			throws Exception {
		return session.getMostPopularCarTypeInCRC(carRentalCompanyName, year);
	}

}
