package agency;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ICarRentalAgency extends Remote {
	
	
	public IReservationSession openReservationSession(String client) throws RemoteException;
	public IManagerSession     openManagerSession(String manager) throws RemoteException;
	public IReservationSession closeReservationSesstion(IReservationSession stub) throws RemoteException;
	public IManagerSession     closeManagerSession(IManagerSession stub) throws RemoteException;
	public INameService getNameService();
}
