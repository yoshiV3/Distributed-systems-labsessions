package agency;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import rental.ICarRentalCompany;
import rental.CarType;
import rental.Quote;
import rental.Reservation;
import rental.ReservationConstraints;
import rental.ICarRentalCompany;
import rental.CarRentalCompany;
import agency.ICarRentalAgency;
import agency.CarRentalAgency;
import agency.AgencyQuote;
import nameservice.INameService;
import nameservice.NameService;

import agency.ICarRentalAgency;
import agency.CarRentalAgency;
import nameservice.INameService;
import rental.ICarRentalCompany;
import rental.CarRentalCompany;


public class ManagerSession implements IManagerSession {

	private String manager;
	private ICarRentalAgency agency;
	private INameService namingService = null;

	public ManagerSession(String manager, ICarRentalAgency agency) {
		this.manager = manager;
		this.agency = agency;
		this.namingService = agency.getNameService();
	}

	public void registerCompany(String company) {
		this.namingService.registerCRC(company);
	}

	public void unregisterCompany(String company) {
		this.namingService.unregisterCRC(company);
	}

	public Set<String> getRegisteredCompanies() {
		return new HashSet<String>(this.namingService.getRegisteredCRCList());
	}

	public int getNumberOfReservations(String company, String type) {
		ICarRentalCompany stub = this.namingService.getRegisteredCRCStub(type);
		return stub.getNumberOfReservationsFOrType(type);
	}

	public int getNumberOfReservationsBy(String client) {
		int total = 0;
		List<ICarRentalCompany> stubs = this.namingService.getAllRegisteredCRCStubs();
		for (ICarRentalCompany stub : stubs) {
			total = total + stub.getNumberOfReservationsFromRenter(client);
		}
	}

	public Set<String> getBestClients() {
		Set<String> result = new HashSet<String>();
		int number = 0;
		for (String client : this.agency.getAllClients()) {
			int nb = this.getNumberOfReservationsBy(client);
			if (number < nb) {
				result = new HashSet<String>();
				result.add(client);
			} else if (nb == number) {
				result.add(client);
			}
		}
		return result;
	}

//	public Set<String> getBestClients()
//	{
//		return this.agency.getBestClients();
//	}

//	public CarType getMostPopularCarTypeInCRC(String carRentalCompanyName, int year) {
//		
//	}

}
