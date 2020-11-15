package session;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import rental.Car;
import rental.CarRentalCompany;
import rental.CarType;
import rental.Reservation;

@Stateless
public class ManagerSession implements ManagerSessionRemote {
    
    
    @PersistenceContext 
    private EntityManager em;
    
    
    private void addCar(String company, Car car)
    {
        try 
        {
            CarRentalCompany comp = em.find(CarRentalCompany.class,  company);
            this.addCar(comp, car);
        }
        catch(Exception ex)
        {
            throw new EJBException(ex);
        }
    }
    
    private void addCar(CarRentalCompany company, Car car)
    {
        try 
        {
            if (company.getCarTypes().contains(car.getType()))
            {
                company.addCar(car);   
            }
            
        }
        catch(Exception ex)
        {
            throw new EJBException(ex);
        }        
    }
    
    private Car createCar(CarType type)
    {
        try
        {
            Car car = new Car(type);
            em.persist(car);
            return car;
        }
        catch(Exception ex)
        {
            throw new EJBException(ex);
        }
    }

    
    private void addCarType(CarRentalCompany company, CarType type)
    {
         try 
        {
            Collection<CarType> types = company.getCarTypes();
            if (types == null || !types.contains(type))
            {
                company.addCarType(type);   
            }
            
        }
        catch(Exception ex)
        {
            throw new EJBException(ex);
        }
    }
          
    
    private CarType createCarType(String name, int nbOfSeats, float trunkSpace, double rentalPricePerDay, boolean smokingAllowed)
    {
        try
        {
            CarType type = new CarType(name, nbOfSeats, trunkSpace, rentalPricePerDay, smokingAllowed);
            em.persist(type);
            return type;
        }
        catch(Exception ex)
        {
            throw new EJBException(ex);
        }       
    }
    
    
    private CarRentalCompany createCRC(String name, List<String> regions)
    {
        try 
        {
            CarRentalCompany crc = new CarRentalCompany(name, regions);
            em.persist(crc);
            return crc;
            
        }
        catch(Exception ex)
        {
            throw new EJBException(ex);
        } 
    }
    
    @Override
    public Set<CarType> getCarTypes(String company) {
        return null;
    }

    @Override
    public Set<Integer> getCarIds(String company, String type) {
        return null;
    }

    @Override
    public int getNumberOfReservations(String company, String type, int id) {
        return 0;
    }

    @Override
    public int getNumberOfReservations(String company, String type) {
        return 0;
    }

    @Override
    public void loadRental(String dataFile) {
        try 
        {
            CrcData data         = this.loadData(dataFile);
            CarRentalCompany crc = this.createCRC(data.name, data.regions);
            for (CarType type : data.carTypes)
            {
                this.addCarType(crc, type);
            }
            for (Car car: data.cars)
            {
                this.addCar(crc, car);
            }
        }
        catch (NumberFormatException ex) {
            throw new EJBException(ex);
        } catch (IOException ex) {
            throw new EJBException(ex);
        }
    }

    @Override
    public void addCarTypeToRental(String name, int nbOfSeats, float trunkSpace, double rentalPricePerDay, boolean smokingAllowed, String company) {
        CarRentalCompany crc = em.find(CarRentalCompany.class, company);
        for (CarType type : crc.getCarTypes())
        {
            if (type.getName().equals(name))
            {
                throw new IllegalArgumentException("Type name already in use for a type");
            }
        }
        CarType type = this.createCarType(name, nbOfSeats, trunkSpace, rentalPricePerDay, smokingAllowed);
        this.addCarType(crc, type);
    }

    @Override
    public void addCarToRental(String type, String company) {
        boolean added = false;
        CarRentalCompany crc = em.find(CarRentalCompany.class, company);
        for (CarType t : crc.getCarTypes())
        {
            if (t.getName().equals(type))
            {
                Car car = this.createCar(t);
                this.addCar(crc, car);
                added = true;
            }
        }
        if(!added)
        {
            throw new IllegalArgumentException("Type does not exist");
        }
    }
    private CrcData loadData(String datafile)
            throws NumberFormatException, IOException {

        CrcData out = new CrcData();
        StringTokenizer csvReader;
       
        //open file from jar
        BufferedReader in = new BufferedReader(new InputStreamReader(ManagerSession.class.getClassLoader().getResourceAsStream(datafile)));
        
        try {
            while (in.ready()) {
                String line = in.readLine();
                
                if (line.startsWith("#")) {
                    // comment -> skip					
                } else if (line.startsWith("-")) {
                    csvReader = new StringTokenizer(line.substring(1), ",");
                    out.name = csvReader.nextToken();
                    out.regions = Arrays.asList(csvReader.nextToken().split(":"));
                } else {
                    csvReader = new StringTokenizer(line, ",");
                    //create new car type from first 5 fields
                    CarType type = this.createCarType(csvReader.nextToken(),
                            Integer.parseInt(csvReader.nextToken()),
                            Float.parseFloat(csvReader.nextToken()),
                            Double.parseDouble(csvReader.nextToken()),
                            Boolean.parseBoolean(csvReader.nextToken()));
                    out.carTypes.add(type);
                    //create N new cars with given type, where N is the 5th field
                    for (int i = Integer.parseInt(csvReader.nextToken()); i > 0; i--) {
                        out.cars.add( this.createCar(type));
                    }        
                }
            } 
        } finally {
            in.close();
        }

        return out;
    }
    
    static class CrcData {
            public List<Car> cars = new LinkedList<Car>();
            public String name;
            public List<String> regions =  new LinkedList<String>();
            public List<CarType> carTypes =  new LinkedList<CarType>();
    }
}