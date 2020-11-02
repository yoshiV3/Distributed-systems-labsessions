package agency;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.invoke.MethodHandles;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.StringTokenizer;

import nameservice.INameService;
//import nameservice.NameService;
//import client.Client;
import agency.ICarRentalAgency;
//import agency.CarRentalAgency;

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
