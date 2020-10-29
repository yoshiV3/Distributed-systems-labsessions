package agency;

import java.uitl.Set;
import java.util.Hashset;
import java.util.String;

public class ManagerSession {

	private String manager;
	private ICarRentalAgency agency;

	public ManagerSession(String manager, ICarRentalAgency agency) {
		this.manager = manager;
		this.agency = agency;
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
		return this.agency.getNumberOfReservations(company, type);
	}

	public int getNumberOfReservationsBy(String client) {
		this.agency.getNumberOfReservationsBy(client);
	}

	public Set<String> getBestCustomers() {
		return this.agency.getBestCustomers();
	}

}
