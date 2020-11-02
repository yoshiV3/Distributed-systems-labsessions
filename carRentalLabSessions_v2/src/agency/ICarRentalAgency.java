package agency;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import nameservice.INameService;

public interface ICarRentalAgency extends Remote {
	public IReservationSession openReservationSession(String client) throws RemoteException;

	public IManagerSession openManagerSession(String manager) throws RemoteException;

	public void closeReservationSession(String client) throws RemoteException, NotBoundException;

	public void closeManagerSession(String manager) throws RemoteException, NotBoundException;

	public INameService getNameService() throws RemoteException;

	public List<String> getAllClients() throws RemoteException;

	public int getNumberOfReservationsBy(String client) throws RemoteException;
}
