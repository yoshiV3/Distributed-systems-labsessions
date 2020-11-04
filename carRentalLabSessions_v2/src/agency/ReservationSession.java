package agency;

import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Date;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import rental.ICarRentalCompany;
import rental.CarType;
import rental.Quote;
import rental.Reservation;
import rental.ReservationConstraints;
import rental.ReservationException;
import nameservice.INameService;

public class ReservationSession implements IReservationSession {

	private Registry registry = null;
	private String client;
	private ICarRentalAgency agency;
	private List<AgencyQuote> quotes;
	private INameService namingService = null;

	// constructor
	public ReservationSession(String client, ICarRentalAgency agency, INameService namingService) {
		this.client = client;
		this.agency = agency;
		this.namingService = namingService;
		this.quotes = new ArrayList<AgencyQuote>();
	}

	@Override
	public Map<String, Set<CarType>> getAvailableCarTypes(Date start, Date end) throws RemoteException {

		Map<String, Set<CarType>> cartypes = new HashMap<String, Set<CarType>>();
		List<ICarRentalCompany> stubs = this.namingService.getAllRegisteredCRCStubs();
		for (ICarRentalCompany stub : stubs) {
			cartypes.put(stub.getName(), stub.getAvailableCarTypes(start, end));
		}
		return cartypes;
	}

	@Override
	public synchronized void createQuote(ReservationConstraints constraints, String client) throws RemoteException {
		List<ICarRentalCompany> stubs = this.namingService.getAllRegisteredCRCStubs();
		double price = 90000;

		ICarRentalCompany company = null;
		for (ICarRentalCompany stub : stubs) {
			if (stub.canReserve(constraints)) {
				if (price > stub.getRentalPricePerDay(constraints.getCarType())) {
					company = stub;
					break; // stopping to check here, we can add another check to pick the lowest priced
							// crc
				}
			}
		}
		if (company == null) {
			System.out.println("<" + client + "> No cars available to satisfy the given constraints.");
			return;
		}
		System.out.println("selected quote for " + company.getName() + constraints.getCarType());
		Quote quote = company.createQuote(constraints, client);
		quotes.add(new AgencyQuote(quote, company));
	}

	@Override
	public List<Reservation> confirmQuotes() throws RemoteException {
		Set<AgencyReservation> reservations = new HashSet<AgencyReservation>();
		List<Reservation> reservationlist = new ArrayList<Reservation>();
		for (AgencyQuote quote : quotes) {
			ICarRentalCompany stub = quote.getCompany();
			try {
				Reservation res = stub.confirmQuote(quote.getQuote());
				reservations.add(new AgencyReservation(res, stub));
				reservationlist.add(res);

			} catch (ReservationException e) {
				for (AgencyReservation res : reservations) {
					res.getCompany().cancelReservation(res.getReservation());
				}
				return null;
			}

		}
		return reservationlist;

	}

	@Override
	public CarType getCheapestCarType(Date start, Date end, String region) throws RemoteException {
		List<ICarRentalCompany> stubs = this.namingService.getAllRegisteredCRCStubs();
		CarType minType = null;
		double currentMinPrice = 8000;
		for (ICarRentalCompany stub : stubs) {
			if (stub.operatesInRegion(region)) {
				CarType type = stub.getCheapestCarType(start, end);
				if (type.getRentalPricePerDay() < currentMinPrice) {
					currentMinPrice = type.getRentalPricePerDay();
					minType = type;
				}
			}
		}
		return minType;

	}

}
