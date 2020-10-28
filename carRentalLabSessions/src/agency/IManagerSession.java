package agency;


import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Set

public interface IManagerSession extends Remote{
	
	public void registerCompany(String company) throws RemoteException;
	public void unregisterCompany(String company) throws RemoteException;
	public Set<String> getRegisteredCompanies() throws RemoteException;
	public int getNumberOfReservations(String company, String type);
	public Set<String> getBestCustomers() throws RemoteException;
	public int getNumberOfReservationsBy(String client) throws RemoteException;
	public CarType getMostPolularCarType(String Company, int year) throws RemoteException;
	

}
