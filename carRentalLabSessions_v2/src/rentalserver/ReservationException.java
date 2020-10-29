package rental;

import java.rmi.RemoteException;

public class ReservationException extends RemoteException {

	public ReservationException(String string) {
		super(string);
	}
}