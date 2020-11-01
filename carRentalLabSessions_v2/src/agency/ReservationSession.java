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
import rental.ReservationException;
import rental.ICarRentalCompany;
import rental.CarRentalCompany;
import agency.ICarRentalAgency;
//import agency.CarRentalAgency;
//import agency.AgencyQuote;
import nameservice.INameService;
//import nameservice.NameService;


public class ReservationSession implements IReservationSession {

	private Registry registry = null;
	private String client;
	private ICarRentalAgency agency;
//	private List<AgencyQuote> quotes;
//
	private INameService namingService = null;

	// constructor
	public ReservationSession(String client, ICarRentalAgency agency, INameService namingService) {
		this.client = client;
		this.agency = agency;
		this.namingService = namingService;
//		this.quotes = new ArrayList<AgencyQuote>();
	}

//	@Override
//	public Map<String, Set<CarType>> getAvailableCarTypes(Date start, Date end) {
//		return this.agency.getAvailbaleCarTypes(start, end);
//	}

	@Override
	public Map<String, Set<CarType>> getAvailableCarTypes(Date start, Date end) throws RemoteException {
		Map<String, Set<CarType>> cartypes = new HashMap<String, Set<CarType>>();
		List<ICarRentalCompany> stubs = this.namingService.getAllRegisteredCRCStubs();
		// Set<CarType> result = new HashSet<CarType>();
		for (ICarRentalCompany stub : stubs) {
			cartypes.put(stub.getName(), stub.getAvailableCarTypes(start, end));
			// result.add(stub.getAvailableCarTypes(start, end));
		}
		return cartypes;
	}


	@Override
	public AgencyQuote createQuote(ReservationConstraints constraints, String client) throws RemoteException{
		List<ICarRentalCompany> stubs = this.namingService.getAllRegisteredCRCStubs();
		double price = 90000;
		
		ICarRentalCompany company = null;
		for (ICarRentalCompany stub : stubs) {
			if (stub.canReserve(constraints)) {
				if (price > stub.getRentalPricePerDay(constraints.getCarType())) {
					company = stub;
				}
			}
		}
		if (company == null) {
			throw new ReservationException("<" + client + "> No cars available to satisfy the given constraints.");
		}
		Quote quote = company.createQuote(constraints, client);
		return new AgencyQuote(quote, company);
				
		
//		AgencyQuote quote = this.agency.createQuote(constraints, client);
//		this.quotes.add(quote);
//		return quote;
	}

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
