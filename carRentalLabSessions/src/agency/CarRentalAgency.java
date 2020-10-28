package agency;


import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.invoke.MethodHandles;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import rental.CarType;
import rental.HashSet;
import rental.ICarRentalCompany;
import rental.Quote;
import rental.Reservation;
import rental.ReservationConstraints;
import rental.ReservationException;

public class CarRentalAgency implements IcarRentalAgency {

	private NameService book;
	private  Registry registry = null;
	
	
	public CarRentalAgency()
	{
		this.book = new NameService();
		this.reservationSessions = new ArrayList<>();
		this.managerSessions     = new ArrayList<>();
		try{
			this.registry = LocateRegistry.getRegistry(); 
		}catch(RemoteException e){
			System.exit(-1);
		}
	}
	
	public IReservationSession openReservationSession(String client)
	{
		IReservationSession ses = new RservationSession(client, this);
		this.book.addNewClient(client);
		IReservationSession stub;
		stub = (IReservationSession ) UnicastRemoteObject.exportObject(ses, 0); 
		this.registry.rebind(client, stub);
	}
	
	public IManagerSession  openManagerSession(String manager)
	{
		IManagerSession ses  = new ManagerSession(manager, this);
		IManagerSession stub;
		stub = (IReservationSession ) UnicastRemoteObject.exportObject(ses, 0); 
		this.registry.rebind(client, stub);	
	}
	
	package NameService getNameService()
	{
		return this.book;
	}
	
	package AgencyQuote createQuote(ReservationConstraints constraints, String client)
	{
		List<ICarRentalCompany> stubs = this.book.getAllCompanyStubs();
		double price                  = 90000;
		IRentalCompany company        = null;
		for (ICarRentalCompany stub: stubs)
		{
			if(stub.canReserve(constraints))
			{
				if (price > stub.getRentalPricePerDay(constaints.getCarType()))
				{
					company = stub;
				}
			}
		}
		if (company==null)
		{
			throw new ReservationException("<" + name
					+ "> No cars available to satisfy the given constraints.");			
		}
		Quote quote = stub.createQuote(createQuote(constraints, client));
		return new AgencyQuote(Quote, stub);
	}
	
	package Set<AgencyReservation> confirmQuotes(List<Agency Quote>  quotes)
	{
		Set<AgencyReservation> reservations = new HashSet<AgencyReservation>();
		for (AgencyQuote quote: quotes)
		{
			ICarRentalCompany stub = quote.getCompany();
			try
			{
				Reservation res = stub.confirmQuote(quote.getQuote());
				reservations.add(new AgencyReservation(res, stub));
				
			}
			catch(ReservationException e)
			{
				for(AgencyReservation res : reservation)
				{
					res.getCompany().cancelReservation(res);
				}
				return null;
			}
			
		}
		return reservvations;
	}
	
	package Set<CarType> getAvailableCarTypes(Date start, Date end)
	{
		List<ICarRentalCompany> stubs = this.book.getAllCompanyStubs();
		result = Set<CarType> = new HashSet<CarType>();
		for(ICarRentalCompany stub: stubs)
		{
			result.add(stub.getAvailableCarTypes(start, end));
		}
		return result;
	}
	
	package CarType getCheapestCarType(Data start, Date end, String region)
	{
		List<ICarRentalCompany> stubs = this.book.getAllCompanyStubs();
		CarType minType;
		double  currentMinPrice = 8000;
		for(ICarRentalCompany stub: stubs)
		{
			if (stub.operatesInRegion(region)
			{
				CarType type = stub.getCheapestCarType(start, end);
				if (type.getRentalPricePerDay() < currentMinPrice)
				{
					minType = type;
				}
			}
		}
		return minType;
		
	}
	
	package int getNumberOfReservations(String company, String type)
	{
		ICarRentalCompany stub this.book.getCompanyStub(type);
		return stub.getNumberOfReservationsFOrType(type);
	}
	
	package int getNumberOfReservationsBy(String client) 
	{
		int total = 0;
		List<ICarRentalCompany> stubs = this.book.getAllCompanyStubs();
		for(ICarRentalCompany stub: stubs)
		{
			total= total + stub.getNumberOfReservationsFromRenter(client);
		}
	}
	
	
	
	package Set<String> getBestCustomers()
	{
		Set<String> result = new HashSet<String>();
		int number = 0;
		for (String client :this.book.getAllClients())
		{
			int nb = this.getNumberOfReservationsBy(client)
			if (number < nb)
			{
				result = new HashSet<String>();
				result.add(client);
			}
			else if (nb == number)
			{
				result.add(client);
			}
		}
		return ressult;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
