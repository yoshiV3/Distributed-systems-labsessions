package nameservice;

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
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import nameservice.INameService;


public class NameServer {

//	private final static int LOCAL = 0;
//	private final static int REMOTE = 1;

	public static void main(String[] args) throws  RemoteException {

		System.out.println("\n=============== Starting Name Server Process ===============\n");

		if (System.getSecurityManager() != null) {
			System.setSecurityManager(null);
		}

		// The first argument passed to the `main` method (if present)
		// indicates whether the application is run on the remote setup or not.
		//int localOrRemote = (args.length == 1 && args[0].equals("REMOTE")) ? REMOTE : LOCAL;


		INameService nameservice = new NameService();

		Registry registry = null;
		try {
			registry = LocateRegistry.getRegistry();
		} catch (RemoteException e) {
			System.exit(-1);
		}

		System.out.println("Registering the Name service name now");
		// convert to stub
		INameService stub;
		try {
			stub = (INameService) UnicastRemoteObject.exportObject(nameservice, 0);
			registry.rebind("testnameservice", stub); //calling it as "nameservice" while lookup
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
		
		
	}
}