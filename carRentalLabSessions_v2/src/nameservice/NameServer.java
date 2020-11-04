package nameservice;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class NameServer {

	private final static int LOCAL = 0;
	private final static int REMOTE = 1;

	public static void main(String[] args) throws RemoteException {

		System.out.println("\n=============== Starting Name Server Process ===============\n");

		if (System.getSecurityManager() != null) {
			System.setSecurityManager(null);
		}

		// The first argument passed to the `main` method (if present)
		// indicates whether the application is run on the remote setup or not.
		int localOrRemote = (args.length == 1 && args[0].equals("REMOTE")) ? REMOTE : LOCAL;

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
			registry.rebind("testnameservice", stub); // calling it as "nameservice" while lookup
			System.out.println("Done egistering the Name service name now");
		} catch (RemoteException e) {
			System.out.println("Namerservice register failed now");
			e.printStackTrace();
		}

	}
}
