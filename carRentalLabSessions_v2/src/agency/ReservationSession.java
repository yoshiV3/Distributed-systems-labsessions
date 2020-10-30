package agency;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Date;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import rental.CarType;
import rental.Quote;
import rental.Reservation;
import rental.ReservationConstraints;
import agency.AgencyQuote;

public class ReservationSession {

	private Registry registry = null;
	private String client;
	private CarRentalAgency agency;
	private List<AgencyQuote> quotes;

	// constructor
	public ReservationSession(String client, CarRentalAgency agency) {
		this.client = client;
		this.agency = agency;
		this.quotes = new ArrayList<AgencyQuote>();
	}

	@Override
	public AgencyQuote createQuote(ReservationConstraints constraints, String client) {
		AgencyQuote quote = this.agency.createQuote(constraints, client);
		this.quotes.add(quote);
		return quote;
	}

	@Override
	public Set<AgencyReservation> confirmQuotes() {
		this.agency.confirmQuotes(quotes);
	}

	@Override
	public Set<AgencyQuote> getCurrentQuotes() {
		return new HashSet<AgencyQuote>(this.quotes);
	}

	@Override
	public Set<CarType> getAvailableCarTypes(Date start, Date end) {
		return this.agency.getAvailbaleCarTyeps(start, end);
	}

	@Override
	public CarType getCheapestCarType(Date start, Date end, String region) {
		return this.agency.getCheapestCarType(start, end, region);
	}

	public void closeReservationSession() {
		registry.unbind(this.client);
		UnicastRemoteObject.unexportObject(this, true);
	}

}
