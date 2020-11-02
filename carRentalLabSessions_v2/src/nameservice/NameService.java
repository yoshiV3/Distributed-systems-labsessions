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


public class NameService implements INameService{
	
	private HashMap<String, ICarRentalCompany> carRentalCompanyList = new HashMap<String, ICarRentalCompany>();
	//private List<String> carRentalCompanyList;
	private Registry registry = null;
	private List<String> clientList;

	public NameService() {
//		this.carRentalCompanyList = new ArrayList<String>();
//		this.clientList = new ArrayList<String>();
		try {
			this.registry = LocateRegistry.getRegistry();
		} catch (RemoteException e) {
			System.exit(-1);
		}
	}

//	public List<String> getAllClients() {
//		return this.clientList.copy();
//	}
//
//	public boolean isNewClient(String client) {
//		return this.clientList.contains(client);
//	}
//
//	public void addNewClient(String client) {
//		if (!this.isNewClient(client)) {
//			this.clientList.add(client);			
//		}
//	}

	// @Override
	public void registerCRC(String crcname) throws RemoteException, NotBoundException {
		if (!this.carRentalCompanyList.containsKey(crcname)) {
			ICarRentalCompany crc = (ICarRentalCompany) this.registry.lookup(crcname);
			this.carRentalCompanyList.put(crcname, crc);
		}
	}

	// @Override
	public void unregisterCRC(String company) {
		this.carRentalCompanyList.remove(company);
	}

	@Override
	public HashMap<String, ICarRentalCompany> getRegisteredCRCList() {
		return this.carRentalCompanyList;
	}

	// @Override
	public ICarRentalCompany getRegisteredCRCStub(String company) throws RemoteException {
		if (this.carRentalCompanyList.containsKey(company)) {
			try {

				ICarRentalCompany companyStub = (ICarRentalCompany) this.registry.lookup(company);

				return companyStub;
			} catch (RemoteException | NotBoundException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	// @Override
	public HashMap<String, ICarRentalCompany> getAllRegisteredCRCNames() {
		return (this.carRentalCompanyList);
	}

	// @Override
	public List<ICarRentalCompany> getAllRegisteredCRCStubs() throws RemoteException {
		List<ICarRentalCompany> result = new ArrayList<ICarRentalCompany>();
		for (String name : this.carRentalCompanyList.keySet()) {
//			result.add(this.getRegisteredCRCStub(name));
			result.add(this.carRentalCompanyList.get(name));
		}
		return result;
	}
}
