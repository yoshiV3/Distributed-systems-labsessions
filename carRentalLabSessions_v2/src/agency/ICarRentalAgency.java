package agency;
import java.rmi.Remote;
import java.rmi.RemoteException;
import nameservice.INameService;

public interface ICarRentalAgency extends Remote {
	
	
	public IReservationSession openReservationSession(String client) throws RemoteException;
	public IManagerSession     openManagerSession(String manager) throws RemoteException;
	public IReservationSession closeReservationSesstion(IReservationSession stub) throws RemoteException;
	public IManagerSession     closeManagerSession(IManagerSession stub) throws RemoteException;
	public INameService getNameService() throws RemoteException;
	public List<String> getAllClients() throws RemoteException;
}
