package agency;

import rental.ICarRentalCompany;
import rental.Reservation;

public class AgencyReservation extends AgencyQuote {

	public AgencyReservation(Reservation reservation, ICarRentalCompany company) {
		super(reservation, company);
	}

	public Reservation getReservation() {
		return super.getQuote();
	}

}
