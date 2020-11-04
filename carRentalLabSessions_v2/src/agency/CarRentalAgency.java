package agency;

import java.util.HashMap;
import java.util.List;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import nameservice.INameService;
import rental.ICarRentalCompany;

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
		if (session != null) {
			System.out.println("--> Opening Reservation Session for [" + client + "]");
			return session;
		} else {
			try {
				ReservationSession createsession = new ReservationSession(client, this, this.namingService);
				this.reservationSessions.put(client, createsession);
				this.addNewClient(client);
				IReservationSession stub;
				stub = (IReservationSession) UnicastRemoteObject.exportObject(createsession, 0);
				// this.registry.rebind(client,stub);
				System.out.println("--> New Reservation Session for [" + client + "]");
				return stub;
			} catch (RemoteException e) {
				e.printStackTrace();
				return null;
			}

		}
	}

	public IManagerSession openManagerSession(String manager) throws RemoteException {
		IManagerSession session = this.managerSessions.get(manager);
		if (session != null) {
			System.out.println("--> Opening Manager Session for [" + manager + "]");
			return session;
		} else {
			try {
				ManagerSession createsession = new ManagerSession(manager, this);
				this.managerSessions.put(manager, createsession);
				IManagerSession stub;
				stub = (IManagerSession) UnicastRemoteObject.exportObject(createsession, 0);
				// this.registry.rebind(manager, stub);
				System.out.println("--> New Manager Session for [" + manager + "]");
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
