package agency;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Set;
import java.util.Date;
import java.util.List;
import java.util.Map;

import rental.CarType;
import rental.Quote;
import rental.Reservation;
import rental.ReservationConstraints;
import agency.AgencyQuote;
//import agency.AgencyReservation;

public interface IReservationSession extends Remote {
	// public void close() throws RemoteException;
	public void createQuote(ReservationConstraints constraints, String client) throws RemoteException;

	public List<Reservation> confirmQuotes() throws RemoteException;

	// public Set<AgencyQuote> getCurrentQuotes() throws Exception;
	public Map<String, Set<CarType>> getAvailableCarTypes(Date start, Date end) throws RemoteException;

	public CarType getCheapestCarType(Date start, Date end, String region) throws RemoteException;
	// public CarType getMostPopularCarTypeInCRC(String carRentalCompanyName, int
	// year) throws RemoteException;
//	public void closeReservationSession();

}
