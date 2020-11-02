package agency;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Set;
import rental.CarType;

public interface IManagerSession extends Remote {
	public void registerCompany(String company) throws RemoteException, NotBoundException;

	public void unregisterCompany(String company) throws RemoteException;

	public int getNumberOfReservationsForCarType(String company, String type) throws RemoteException;

	public Set<String> getBestClients() throws RemoteException;

	public int getNumberOfReservationsByRenter(String client) throws RemoteException;

	public CarType getMostPopularCarTypeInCRC(String Company, int year) throws RemoteException;
//	public Set<String> getRegisteredCompanies() throws RemoteException;

}
