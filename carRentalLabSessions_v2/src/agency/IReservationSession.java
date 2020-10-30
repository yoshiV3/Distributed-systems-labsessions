package agency;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Set;
import java.util.Date;
import rental.CarType;
import rental.Quote;
import rental.Reservation;
import rental.ReservationConstraints;
import agency.AgencyQuote;
import agency.AgencyReservation;

public interface IReservationSession {
	/**
	 * A method to close a session
	 */
	public void close() throws RemoteException;

	/**
	 * create a quote for a given client based on given reservation constraints at
	 * one of the car rental companies
	 * 
	 * @param constraints the reservation constraints
	 * @param client      the client
	 * @return A quote for the given client based on the given constraints
	 * @throws RemoteException
	 */
	public AgencyQuote createQuote(ReservationConstraints constraints, String client) throws RemoteException;

	/**
	 * Confirm the quotes created in this ession
	 * 
	 * @return the reservations based
	 * @throws RemoteException
	 */
	public Set<AgencyReservation> confirmQuotes() throws RemoteException;

	public Set<AgencyQuote> getCurrentQuotes() throws Exception;

	/**
	 * Get the list of all available car types in a given period from all possible
	 * companies.
	 * 
	 * @param start: The start date of the period
	 * @param end:   The end date of the period
	 * @return The list of all available car types in the gicen period
	 * @throws RemoteException
	 */
	public Set<CarType> getAvailableCarTypes(Date start, Date end) throws RemoteException;

	/**
	 * Returns the cheapest available car type in a given period
	 * 
	 * @param start the first date of the period
	 * @param end   the last date of the period
	 * @return the cheapest car type
	 * @throws RemoteException
	 */
	public CarType getCheapestCarType(Date start, Date end, String region) throws RemoteException;

	public void closeReservationSession();

}
