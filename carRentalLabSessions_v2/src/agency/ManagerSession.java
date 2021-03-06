package agency;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nameservice.INameService;
import rental.CarType;
import rental.ICarRentalCompany;

public class ManagerSession implements IManagerSession {

	private ICarRentalAgency agency;
	private INameService namingService = null;

	public ManagerSession(String manager, ICarRentalAgency agency) throws RemoteException {
		this.agency = agency;
		this.namingService = agency.getNameService();
	}

	public void registerCompany(String company) throws RemoteException, NotBoundException {
		this.namingService.registerCRC(company);
	}

	public void unregisterCompany(String company) throws RemoteException {
		this.namingService.unregisterCRC(company);
	}
//	public Set<String> getRegisteredCompanies() {
//		return new HashSet<String>(this.namingService.getRegisteredCRCList());
//	}

	public int getNumberOfReservationsForCarType(String company, String type) throws RemoteException {
		ICarRentalCompany stub = this.namingService.getRegisteredCRCStub(company);
		return stub.getNumberOfReservationsForType(type);
	}

	public int getNumberOfReservationsByRenter(String client) throws RemoteException {
		int total = 0;
		List<ICarRentalCompany> stubs = this.namingService.getAllRegisteredCRCStubs();
		for (ICarRentalCompany stub : stubs) {
			total = total + stub.getNumberOfReservationsFromRenter(client);
		}
		return total;
	}

	public Set<String> getBestClients() throws RemoteException {
		Set<String> result = new HashSet<String>();
		int number = 0;
		for (String client : this.agency.getAllClients()) {
			int nb = this.agency.getNumberOfReservationsBy(client);
			if (number < nb) {
				number = nb;
				result = new HashSet<String>();
				result.add(client);
			} else if (nb == number) {
				result.add(client);
			}
		}
		return result;
	}

	public CarType getMostPopularCarTypeInCRC(String company, int year) throws RemoteException {
		ICarRentalCompany stub = this.namingService.getRegisteredCRCStub(company);
		return stub.getMostPopularCarTypeInYear(year);
	}

}
