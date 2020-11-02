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
import java.rmi.AccessException;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
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

//import client.Client;
//import agency.ManagerSession;
//import agency.ReservationSession;
import agency.IManagerSession;
import agency.IReservationSession;
//import nameservice.NameService;
import nameservice.INameService;

public class CarRentalAgency implements ICarRentalAgency {

	private INameService namingService = null;
	private Registry registry = null;
	private List<String> carRentalCompanyList = new ArrayList<String>();
	private List<String> clientList = new ArrayList<String>();

	private HashMap<String, IReservationSession> reservationSessions = new HashMap<String, IReservationSession>();
	private HashMap<String, IManagerSession> managerSessions = new HashMap<String, IManagerSession>();

	public CarRentalAgency(INameService namingService) {
		this.namingService = namingService;
		try {
			this.registry = LocateRegistry.getRegistry();
		} catch (RemoteException e) {
			System.exit(-1);
		}
	}

	public IReservationSession openReservationSession(String client) throws RemoteException {
		IReservationSession session = this.reservationSessions.get(client);
		if (session != null)
			return session;
		else {
			try {
				ReservationSession createsession = new ReservationSession(client, this, this.namingService);
				this.reservationSessions.put(client, createsession);
				this.addNewClient(client);
				IReservationSession stub;
				stub = (IReservationSession) UnicastRemoteObject.exportObject(createsession, 0);
				// this.registry.rebind(client,stub);
				return stub;
			} catch (RemoteException e) {
				e.printStackTrace();
				return null;
			}

		}
	}

	public IManagerSession openManagerSession(String manager) throws RemoteException {
		IManagerSession session = this.managerSessions.get(manager);
		if (session != null)
			return session;
		else {
			try {
				ManagerSession createsession = new ManagerSession(manager, this);
				this.managerSessions.put(manager, createsession);
				IManagerSession stub;
				stub = (IManagerSession) UnicastRemoteObject.exportObject(createsession, 0);
				// this.registry.rebind(manager, stub);
				return stub;
			} catch (RemoteException e) {
				e.printStackTrace();
				return null;
			}
		}
	}

	public void closeReservationSession(String client) throws RemoteException, NotBoundException {
		IReservationSession session = this.reservationSessions.get(client);
		if (session != null) {
			this.registry.unbind(client);
			UnicastRemoteObject.unexportObject(session, true);
			this.reservationSessions.remove(client);
		}
	}

	public void closeManagerSession(String manager) throws RemoteException, NotBoundException {
		IManagerSession session = this.managerSessions.get(manager);
		if (session != null) {
			this.registry.unbind(manager);
			UnicastRemoteObject.unexportObject(session, true);
			this.managerSessions.remove(manager);
		}
	}

	public List<String> getAllClients() throws RemoteException {
		return this.clientList;
	}

	public boolean isNewClient(String client) {
		return this.clientList.contains(client);
	}

	public void addNewClient(String client) {
		if (!this.isNewClient(client)) {
			this.clientList.add(client);
		}
	}

	public INameService getNameService() throws RemoteException {
		return this.namingService;
	}

	public int getNumberOfReservationsBy(String client) throws RemoteException {
		int total = 0;
		List<ICarRentalCompany> stubs = this.namingService.getAllRegisteredCRCStubs();
		for (ICarRentalCompany stub : stubs) {
			total = total + stub.getNumberOfReservationsFromRenter(client);
		}

		return total;
	}
}
