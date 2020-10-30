package agency;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import rental.ICarRentalCompany;
import rental.CarType;
import rental.Quote;
import rental.Reservation;
import rental.ReservationConstraints;
import agency.AgencyQuote;
import nameservice.INameService;
import nameservice.NameService;

public class ReservationSession implements IReservationSession {

	private Registry registry = null;
	private String client;
	private CarRentalAgency agency;
//	private List<AgencyQuote> quotes;
//
	private INameService namingService = null;

	// constructor
	public ReservationSession(String client, ICarRentalAgency agency) {
		this.client = client;
		this.agency = agency;
		this.namingService = agency.getNameService();
		this.quotes = new ArrayList<AgencyQuote>();
	}

//	@Override
//	public Map<String, Set<CarType>> getAvailableCarTypes(Date start, Date end) {
//		return this.agency.getAvailbaleCarTypes(start, end);
//	}

	@Override
	public Map<String, Set<CarType>> getAvailableCarTypes(Date start, Date end) {
		Map<String, Set<CarType>> cartypes = new HashMap<String, Set<CarType>>();
		List<ICarRentalCompany> stubs = this.namingService.getAllRegisteredCRCStubs();
		// Set<CarType> result = new HashSet<CarType>();
		for (ICarRentalCompany stub : stubs) {
			cartypes.put(stub, stub.getAvailableCarTypes(start, end));
			// result.add(stub.getAvailableCarTypes(start, end));
		}
		return cartypes;
	}

//
//	@Override
//	public AgencyQuote createQuote(ReservationConstraints constraints, String client) {
//		AgencyQuote quote = this.agency.createQuote(constraints, client);
//		this.quotes.add(quote);
//		return quote;
//	}
//
//	@Override
//	public Set<AgencyReservation> confirmQuotes() {
//		this.agency.confirmQuotes(quotes);
//	}
//
//	@Override
//	public Set<AgencyQuote> getCurrentQuotes() {
//		return new HashSet<AgencyQuote>(this.quotes);
//	}
//
//	@Override
//	public Set<CarType> getAvailableCarTypes(Date start, Date end) {
//		return this.agency.getAvailbaleCarTypes(start, end);
//	}
//
//	@Override
//	public CarType getCheapestCarType(Date start, Date end, String region) {
//		return this.agency.getCheapestCarType(start, end, region);
//	}
//
//	public void closeReservationSession() {
//		registry.unbind(this.client);
//		UnicastRemoteObject.unexportObject(this, true);
//	}

}
