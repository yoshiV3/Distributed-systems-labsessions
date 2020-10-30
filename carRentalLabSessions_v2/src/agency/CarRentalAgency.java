package agency;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.invoke.MethodHandles;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import rental.CarType;
import rental.ICarRentalCompany;
import rental.Quote;
import rental.Reservation;
import rental.ReservationConstraints;
import rental.ReservationException;

import client.Client;
import agency.ManagerSession;
import agency.ReservationSession;
import agency.IManagerSession;
import agency.IReservationSession;
import nameservice.NameService;
import nameservice.INameService;

public class CarRentalAgency implements ICarRentalAgency {

	private INameService namingService = null;
	private Registry registry = null;
	private List<String> carRentalCompanyList = new ArrayList<String>();
	private List<String> clientList = new ArrayList<String>();

	private HashMap<String, ReservationSession> reservationSessions = new HashMap<String, ReservationSession>();
	private HashMap<String, ReservationSession> managerSessions = new HashMap<String, ReservationSession>();
//	private reservationSessions=new ArrayList<>();
//	private managerSessions =new ArrayList<>();

	public CarRentalAgency(INameService namingService) {
		this.namingService = namingService;
//		this.reservationSessions = new ArrayList<>();
//		this.managerSessions = new ArrayList<>();
		try {
			this.registry = LocateRegistry.getRegistry();
		} catch (RemoteException e) {
			System.exit(-1);
		}
	}

	public IReservationSession openReservationSession(String client) {
		IReservationSession session = this.reservationSessions.get(client);
		if (session != null)
			return session;
		else {
			IReservationSession createsession = new ReservationSession(client, this);
			this.reservationSessions.put(client, createsession);
			this.addNewClient(client);
			IReservationSession stub;
			stub = (IReservationSession) UnicastRemoteObject.exportObject(createsession, 0);
			this.registry.rebind(client, stub);
			return stub;
		}
	}

	public IManagerSession openManagerSession(String manager) {
		IManagerSession session = this.managerSessions.get(manager);
		if (session != null)
			return session;
		else {
			IManagerSession createsession = new ManagerSession(manager, this);
			this.managerSessions.put(manager, createsession);
			IManagerSession stub;
			stub = (IManagerSession) UnicastRemoteObject.exportObject(createsession, 0);
			this.registry.rebind(manager, stub);
			return stub;
		}
//		IManagerSession session = new ManagerSession(manager, this);
//		IManagerSession stub;
//		stub = (IReservationSession) UnicastRemoteObject.exportObject(session, 0);
//		this.registry.rebind(client, stub);
	}

	public void closeReservationSession(String client) {
		IReservationSession session = this.reservationSessions.get(client);
		if (session != null) {
			this.registry.unbind(client);
			UnicastRemoteObject.unexportObject(session, true);
			this.reservationSessions.remove(client);
		}
	}

	public void closeManagerSession(String manager) {
		IManagerSession session = this.managerSessions.get(manager);
		if (session != null) {
			this.registry.unbind(manager);
			UnicastRemoteObject.unexportObject(session, true);
			this.managerSessions.remove(manager);
		}
	}

	public List<String> getAllClients() {
		return this.clientList.copy();
	}

	public boolean isNewClient(String client) {
		return this.clientList.contains(client);
	}

	public void addNewClient(String client) {
		if (!this.isNewClient(client)) {
			this.clientList.add(client);
		}
	}

//	public Map<String, Set<CarType>> getAvailableCarTypes(Date start, Date end) {
//	Map<String, Set<CarType>> cartypes=new HashMap<CarType>();
//	List<ICarRentalCompany> stubs = this.namingService.getAllRegisteredCRCStubs();
//	//Set<CarType> result = new HashSet<CarType>();
//	for (ICarRentalCompany stub : stubs) {
//		cartypes.put(stub, stub.getAvailableCarTypes(start, end)); 
//		//result.add(stub.getAvailableCarTypes(start, end));
//	}
//	return cartypes;
//}

//	public IReservationSession openReservationSession(String client) {
//		IReservationSession session = new ReservationSession(client, this);
//		if (this.namingService.addNewClient(client)) {
//			IReservationSession stub;
//			stub = (IReservationSession) UnicastRemoteObject.exportObject(session, 0);
//			this.registry.rebind(client, stub);
//		}
//	}

	public NameService getNameService() {
		return this.namingService;
	}

//	public AgencyQuote createQuote(ReservationConstraints constraints, String client) {
//		List<ICarRentalCompany> stubs = this.namingService.getAllRegisteredCRCStubs();
//		double price = 90000;
//		
//		ICarRentalCompany company = null;
//		for (ICarRentalCompany stub : stubs) {
//			if (stub.canReserve(constraints)) {
//				if (price > stub.getRentalPricePerDay(constaints.getCarType())) {
//					company = stub;
//				}
//			}
//		}
//		if (company == null) {
//			throw new ReservationException("<" + name + "> No cars available to satisfy the given constraints.");
//		}
//		Quote quote = company.createQuote(createQuote(constraints, client));
//		return new AgencyQuote(quote, company);
//	}
//
//	public Set<AgencyReservation> confirmQuotes(List<AgencyQuote> quotes) {
//		Set<AgencyReservation> reservations = new HashSet<AgencyReservation>();
//		for (AgencyQuote quote : quotes) {
//			ICarRentalCompany stub = quote.getCompany();
//			try {
//				Reservation res = stub.confirmQuote(quote.getQuote());
//				reservations.add(new AgencyReservation(res, stub));
//
//			} catch (ReservationException e) {
//				for (AgencyReservation res : reservation) {
//					res.getCompany().cancelReservation(res);
//				}
//				return null;
//			}
//
//		}
//		return reservations;
//	}
//

//
//	public CarType getCheapestCarType(Date start, Date end, String region) {
//		List<ICarRentalCompany> stubs = this.namingService.getAllRegisteredCRCStubs();
//		CarType minType;
//		double currentMinPrice = 8000;
//		for (ICarRentalCompany stub : stubs) {
//			if (stub.operatesInRegion(region)) {
//				CarType type = stub.getCheapestCarType(start, end);
//				if (type.getRentalPricePerDay() < currentMinPrice) {
//					minType = type;
//				}
//			}
//		}
//		return minType;
//
//	}
//
//	public int getNumberOfReservations(String company, String type) {
//		ICarRentalCompany stub = this.namingService.getRegisteredCRCStub(type);
//		return stub.getNumberOfReservationsFOrType(type);
//	}
//
//	public int getNumberOfReservationsBy(String client) {
//		int total = 0;
//		List<ICarRentalCompany> stubs = this.namingService.getAllRegisteredCRCStubs();
//		for (ICarRentalCompany stub : stubs) {
//			total = total + stub.getNumberOfReservationsFromRenter(client);
//		}
//	}
//
//	public Set<String> getBestClients() {
//		Set<String> result = new HashSet<String>();
//		int number = 0;
//		for (String client : this.namingService.getAllClients()) {
//			int nb = this.getNumberOfReservationsBy(client);
//			if (number < nb) {
//				result = new HashSet<String>();
//				result.add(client);
//			} else if (nb == number) {
//				result.add(client);
//			}
//		}
//		return result;
//	}
//
}
