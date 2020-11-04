package agency;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import nameservice.INameService;

public class CarRentalAgencyServer {

	private static Registry registry = null;

	public static void main(String[] args) throws RemoteException, NotBoundException {
		System.out.println("\n=============== Starting Car Rental Agency Server Process ===============\n");

		if (System.getSecurityManager() != null) {
			System.setSecurityManager(null);
		}

		INameService namingService = getNameService("testnameservice");
		ICarRentalAgency rentalAgency = new CarRentalAgency(namingService);

		try {
			registry = LocateRegistry.getRegistry();
		} catch (RemoteException e) {
			System.exit(-1);
		}

		System.out.println("Registering the Agency now");
		// convert to stub
		ICarRentalAgency stub;
		try {
			stub = (ICarRentalAgency) UnicastRemoteObject.exportObject(rentalAgency, 0);
			registry.rebind("testagency", stub); // calling it as "agency" while lookup
		} catch (RemoteException e) {
			e.printStackTrace();
		}

	}

	public static INameService getNameService(String nameservice) throws RemoteException, NotBoundException {
		if (System.getSecurityManager() != null)
			System.setSecurityManager(null);

		INameService nameService = null;
		try {
			registry = LocateRegistry.getRegistry();
		} catch (RemoteException e) {
			System.exit(-1);
		}

		try {
			nameService = (INameService) registry.lookup(nameservice);
			System.out.println("Nameservice lookup success");
		} catch (RemoteException e) {
			e.printStackTrace();
			System.out.println("Nameservice lookup failed");
		}
		return nameService;
	}
}
