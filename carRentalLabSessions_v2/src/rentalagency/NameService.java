package agency;

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

import client.RemoteException;
import rental.ICarRentalCompany;

public class NameService {
	private List<String> companyList;
	private Registry registry = null;
	private List<String> clientList;

	public NameService() {
		this.companyList = new ArrayList<String>();
		this.clientList = new ArrayList<String>();
		try {
			this.registry = LocateRegistry.getRegistry();
		} catch (RemoteException e) {
			System.exit(-1);
		}
	}

	public List<String> getAllClients() {
		return this.clientList.copy();
	}

	public boolean isNewClient(String client) {
		return this.clientList.contains(client);
	}

	public void addNewClient(String client) {
		if (this.isNewClient(client)) {
			this.clientList.add(client);
		}
	}

	public void register(String company) {
		if (!this.companyList.contains(company)) {
			this.companyList.add(company);
		}
	}

	public void unregister(String company) {
		this.companyList.remove(company);
	}

	public List<String> getCompanyList() {
		return this.companyList;
	}

	public IcarRentalCompany getCompanyStub(String company) {
		if (this.companyList.contains(company)) {
			try {
				companyStub = (ICarRentalCompany) this.registry.lookup(company);
				return companyStub;
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public List<String> getAllCompanyNames() {
		return new ArrayList<>(this.companyList);
	}

	public List<ICarRentalCompany> getAllCompanyStubs() {
		List<ICarRentalCompany> result = new ArrayList<ICarRentalCOmpany>();
		for (String name : this.companyList) {
			result.add(this.getCompanyStub(name));
		}
		return result;
	}
}
