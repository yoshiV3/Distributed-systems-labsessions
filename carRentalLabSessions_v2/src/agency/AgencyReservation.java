package agency;

import rental.ICarRentalCompany;
import rental.Quote;
import rental.Reservation;

public class AgencyReservation {
	private Reservation reservation;
	private ICarRentalCompany company;

	public AgencyReservation(Reservation reservation, ICarRentalCompany company) {
		this.reservation = reservation;
		this.company = company;
	}

	public Reservation getReservation() {
		return this.reservation;
	}

	public ICarRentalCompany getCompany() {
		return this.company;
	}

}
