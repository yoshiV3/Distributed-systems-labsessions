package nameservice;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.StringTokenizer;
import rental.ICarRentalCompany;

public interface INameService extends Remote {

	public void registerCRC(String company) throws RemoteException;

	public void unregisterCRC(String company) throws RemoteException;

	public List<String> getRegisteredCRCList() throws RemoteException;

	public ICarRentalCompany getRegisteredCRCStub(String company) throws RemoteException;

	public List<String> getAllRegisteredCRCNames() throws RemoteException;

	public List<ICarRentalCompany> getAllRegisteredCRCStubs() throws RemoteException;

}
