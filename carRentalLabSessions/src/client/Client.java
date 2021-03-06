package client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import rental.CarRentalCompany;
import rental.CarType;
import rental.ICarRentalCompany;
import rental.Quote;
import rental.Reservation;
import rental.ReservationConstraints;

public class Client extends AbstractTestBooking {

	/********
	 * MAIN *
	 ********/
	private static Logger logger = Logger.getLogger(Client.class.getName());
	private static Registry registry = null;
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

		String carRentalCompanyName = "Hertz";

		
		// An example reservation scenario on car rental company 'Hertz' would be...
		Client client = new Client("simpleTrips", carRentalCompanyName, localOrRemote);
		client.run();


		
	}

	/***************
	 * CONSTRUCTOR 
	 * @throws NotBoundException 
	 * @throws RemoteException *
	 ***************/

	public Client(String scriptFile, String carRentalCompanyName, int localOrRemote) throws NotBoundException, RemoteException {
		super(scriptFile);

		logger.log(Level.INFO, "This is <{0}> Car Rental Company ", carRentalCompanyName);
		
		try{
			registry = LocateRegistry.getRegistry(); 
		}catch(RemoteException e){
			System.exit(-1);
		}
		

		try{
			company=(ICarRentalCompany) registry.lookup(carRentalCompanyName);
		}catch(RemoteException e){
			e.printStackTrace();
		}
		

		//company.getAllCarTypes();
		CarType data= company.getCarType("Compact");
		logger.log(Level.INFO, "This is data response <{0}> Car Rental Company ", data);
		
		
		// TODO Auto-generated method stub
		//throw new UnsupportedOperationException("TODO");
	}
	
	

	/**
	 * Check which car types are available in the given period (across all companies
	 * and regions) and print this list of car types.
	 *
	 * @param start start time of the period
	 * @param end   end time of the period
	 * @throws Exception if things go wrong, throw exception
	 */
	@Override
	protected void checkForAvailableCarTypes(Date start, Date end) throws Exception {
		// TODO Auto-generated method stub
		
		Set<CarType> availablecartypesdata= company.getAvailableCarTypes(start,end);
		System.out.println("************Available cars ***********");
		for (CarType value : availablecartypesdata) { 
            System.out.println(value); 
        }		
		//throw new UnsupportedOperationException("TODO");
	}

	/**
	 * Retrieve a quote for a given car type (tentative reservation).
	 * 
	 * @param clientName name of the client
	 * @param start      start time for the quote
	 * @param end        end time for the quote
	 * @param carType    type of car to be reserved
	 * @param region     region in which car must be available
	 * @return the newly created quote
	 * 
	 * @throws Exception if things go wrong, throw exception
	 */
	@Override
	protected Quote createQuote(String clientName, Date start, Date end, String carType, String region)
			throws Exception {
		 System.out.println("************Creating Quote for <"+clientName+">***********");
		 ReservationConstraints args = new ReservationConstraints(start, end, carType, region);
		
		 Quote quote_value= company.createQuote(args, clientName);
		 System.out.println(quote_value);		 

		return quote_value;
		// TODO Auto-generated method stub
		//throw new UnsupportedOperationException("TODO");
	}

	/**
	 * Confirm the given quote to receive a final reservation of a car.
	 * 
	 * @param quote the quote to be confirmed
	 * @return the final reservation of a car
	 * 
	 * @throws Exception if things go wrong, throw exception
	 */
	@Override
	protected Reservation confirmQuote(Quote quote) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("************Confirmation Quote Data ***********");
		Reservation confirmation_data= company.confirmQuote(quote);
		System.out.println(confirmation_data);
		return confirmation_data;
//		throw new UnsupportedOperationException("TODO");
	}

	/**
	 * Get all reservations made by the given client.
	 *
	 * @param clientName name of the client
	 * @return the list of reservations of the given client
	 * 
	 * @throws Exception if things go wrong, throw exception
	 */
	@Override
//	protected List<Reservation> getReservationsByRenter(String clientName) throws Exception {
	protected Set<Reservation> getReservationsByRenter(String clientName) throws Exception {	
		
		System.out.println("************Getting reservation data for <"+clientName+">***********");
		Set<Reservation> getreservations_data = company.getReservationsFromRenter(clientName);				
		return getreservations_data;
	}

	/**
	 * Get the number of reservations for a particular car type.
	 * 
	 * @param carType name of the car type
	 * @return number of reservations for the given car type
	 * 
	 * @throws Exception if things go wrong, throw exception
	 */
	@Override
	protected int getNumberOfReservationsForCarType(String carType) throws Exception {
		System.out.println("************Getting the number of reservations for <"+carType+">***********");
		Set<Reservation> getreservationsforcar_data = company.getReservationsForCarType(carType);
		//System.out.println(getreservationsforcar_data.size());
		return getreservationsforcar_data.size();
		//throw new UnsupportedOperationException("TODO");
	}
}