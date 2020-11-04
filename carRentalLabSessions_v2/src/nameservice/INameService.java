package nameservice;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import rental.ICarRentalCompany;

public interface INameService extends Remote {
	public void registerCRC(String company) throws RemoteException, NotBoundException;

	public void unregisterCRC(String company) throws RemoteException;

	public HashMap<String, ICarRentalCompany> getRegisteredCRCList() throws RemoteException;

	public ICarRentalCompany getRegisteredCRCStub(String company) throws RemoteException;

	public HashMap<String, ICarRentalCompany> getAllRegisteredCRCNames() throws RemoteException;

	public List<ICarRentalCompany> getAllRegisteredCRCStubs() throws RemoteException;
}
