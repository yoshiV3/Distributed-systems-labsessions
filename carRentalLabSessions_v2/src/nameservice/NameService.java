package nameservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.invoke.MethodHandles;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rental.ICarRentalCompany;
//import rental.CarRentalCompany;
import agency.ICarRentalAgency;
//import agency.CarRentalAgency;
import agency.IReservationSession;

public class NameService implements INameService {

	private HashMap<String, ICarRentalCompany> carRentalCompanyList = new HashMap<String, ICarRentalCompany>();
	private Registry registry = null;
	private List<String> clientList;

	public NameService() {
		try {
			this.registry = LocateRegistry.getRegistry();
		} catch (RemoteException e) {
			System.exit(-1);
		}
	}

	public synchronized void registerCRC(String crcname) throws RemoteException, NotBoundException {
		if (!this.carRentalCompanyList.containsKey(crcname)) {
			ICarRentalCompany crc = (ICarRentalCompany) this.registry.lookup(crcname);
			this.carRentalCompanyList.put(crcname, crc);
		}
	}

	public synchronized void unregisterCRC(String company) {
		this.carRentalCompanyList.remove(company);
	}

	public HashMap<String, ICarRentalCompany> getRegisteredCRCList() {
		return this.carRentalCompanyList;
	}

	public ICarRentalCompany getRegisteredCRCStub(String company) throws RemoteException {
		ICarRentalCompany companyStub = null;
		if (this.carRentalCompanyList.containsKey(company)) {
			companyStub = (ICarRentalCompany) this.carRentalCompanyList.get(company);
		}
		return companyStub;
	}

	public HashMap<String, ICarRentalCompany> getAllRegisteredCRCNames() {
		return (this.carRentalCompanyList);
	}

	public List<ICarRentalCompany> getAllRegisteredCRCStubs() throws RemoteException {
		List<ICarRentalCompany> result = new ArrayList<ICarRentalCompany>();
		for (String name : this.carRentalCompanyList.keySet()) {
			result.add(this.carRentalCompanyList.get(name));
		}
		return result;
	}
}
