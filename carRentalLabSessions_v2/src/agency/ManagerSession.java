package agency;

import java.util.Set;
import java.util.HashSet;
import java.util.StringTokenizer;

import agency.ICarRentalAgency;
import agency.CarRentalAgency;
import nameservice.INameService;

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
		this.agency.getNameService().register(company);
	}

	public void unregisterCompany(String company) {
		this.agency.getNameService().unregister(company);
	}

	public Set<String> getRegisteredCompanies() {
		return new HashSet<String>(this.agency.getNameService().getCompantList());
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
