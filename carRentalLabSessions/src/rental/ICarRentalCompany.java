package rental;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

public interface ICarRentalCompany extends Remote {
	
	/**
	 * @return Returns the name of this Car rental company
	 * @throws RemoteException
	 */
	public String getName() throws RemoteException;
	
	
	
	
	
	public  boolean canReserve(ReservationConstraints constraints) throws RemoteException;
	public  void    cancelReservation(Reservation reservation) throws RemoteException;
	public  double  getRentalPricePerDay(String typeName) throws RemoteException;
	/**
	 * Get the list of all available car types in a given period. 
	 * @param start: The start date of the period
	 * @param end:   The end date of the period
	 * @return       The list of all available car types in the gicen period
	 * @throws RemoteException
	 */
	public Set<CarType> getAvailableCarTypes(Date start, Date end) throws RemoteException;
	
	
	/**
	 * Check if a car of given car type is available in the given period
	 * @param carTypeName  The car type
	 * @param start        The start data of the period
	 * @param end          The end data of the period
	 * @return             True if a car of given type is available during the given period
	 *                     False, otherwise 
	 * @throws             RemoteException
	 * 
	 * @throws             IllegalArgumentException 
	 *                     Given car type does not exist
	 */
	public boolean isAvailable(String carTypeName, Date start, Date end) throws RemoteException;
	
	/**
	 *  create a quote for a given client based on given reservation constraints
	 * @param constraints the reservation constraints
	 * @param client      the client 
	 * @return            A quote for the given client based on the given constraints 
	 * @throws RemoteException
	 */
	public Quote createQuote(ReservationConstraints constraints, String client) throws RemoteException;
		
	/**
	 * Confirm a quote	
	 * @param quote the quote to confirm
	 * @return   the reservation based on the quote
	 * @throws RemoteException
	 */
	public Reservation confirmQuote(Quote quote) throws RemoteException; 
	
	
	/**
	 * A method to retrieve all the reservations from a certain client
	 * @param client the relevant client
	 * @return The requested reservations
	 * @throws RemoteException
	 */
	public Set<Reservation> getReservationsFromRenter(String client) throws RemoteException;
	
	public int getNumberOfReservationsFromRenter(String client) throws RemoteException;
	
	/**
	 * Retrieve all the reservations for a certain  car type 
	 * @param type the relevant car type
	 * @return all the relevant reservations
	 * @throws RemoteException
	 */
	public Set<Reservation> getReservationsForCarType(String type) throws RemoteException; 
	
	
	public CarType getCarType(String carTypeName) throws RemoteException;
	public Collection<CarType> getAllCarTypes() throws RemoteException;
	public CarType getCheapestCarType(Date start, Date end) throws RemoteException;
	public int     getNumberOfReservationsFOrType(String type) throws  RemoteException; 
	public boolean operatesInRegion(String region) throws RemoteException;

}
