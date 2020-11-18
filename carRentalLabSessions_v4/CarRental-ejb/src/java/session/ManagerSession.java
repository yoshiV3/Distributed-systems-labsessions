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
import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import static javax.ejb.TransactionAttributeType.MANDATORY;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import rental.Car;
import rental.CarRentalCompany;
import rental.CarType;
import rental.Reservation;
    

@Stateless
public class ManagerSession extends Session implements ManagerSessionRemote {

    private void addCar(String company, Car car) {
        try {
            CarRentalCompany comp = getEntityManager().find(CarRentalCompany.class, company);
            this.addCar(comp, car);
        } catch (Exception ex) {
            throw new EJBException(ex);
        }
    }

    private void addCar(CarRentalCompany company, Car car) {
        try {
            if (company == null) {
                this.getEJBContext().setRollbackOnly();
                throw new IllegalArgumentException("Not a supported company");
            }
            if (car == null) {
                this.getEJBContext().setRollbackOnly();
                throw new IllegalArgumentException("Not a valid car");
            }
            if (company.getCarTypes().contains(car.getType())) {
                company.addCar(car);
            } else {
                this.getEJBContext().setRollbackOnly();
                throw new IllegalArgumentException("Not a valid car");
            }

        } catch (Exception ex) {
            throw new EJBException(ex);
        }
    }

    private Car createCar(CarType type) {
        try {
            Car car = new Car(type);
            getEntityManager().persist(car);
            return car;
        } catch (Exception ex) {
            throw new EJBException(ex);
        }
    }

    private void addCarType(CarRentalCompany company, CarType type) {
        try {
            if (company == null) {
                this.getEJBContext().setRollbackOnly();
                throw new IllegalArgumentException("Not a supported company");
            }
            if (type == null) {
                this.getEJBContext().setRollbackOnly();
                throw new IllegalArgumentException("Not a valid car type");
            }
            Collection<CarType> types = company.getCarTypes();
            if (!types.contains(type)) {
                company.addCarType(type);
            } else {
                this.getEJBContext().setRollbackOnly();
                throw new IllegalArgumentException("Type name already in use");
            }

        } catch (Exception ex) {
            throw new EJBException(ex);
        }
    }

    private CarType createCarType(String name, int nbOfSeats, float trunkSpace, double rentalPricePerDay, boolean smokingAllowed) {
        try {
            CarType type = new CarType(name, nbOfSeats, trunkSpace, rentalPricePerDay, smokingAllowed);
            getEntityManager().persist(type);
            return type;
        } catch (Exception ex) {
            throw new EJBException(ex);
        }
    }

    private CarRentalCompany createCRC(String name, List<String> regions) {
        try {
            CarRentalCompany crc = new CarRentalCompany(name, regions);
            getEntityManager().persist(crc);
            return crc;

        } catch (Exception ex) {
            throw new EJBException(ex);
        }
    }

    @Override
    public List<String> getAllRentalCompanies() {
        return super.getAllCarRentalCompanies();
    }

    @Override
    public List<CarType> getCarTypes(String company) {
        return super.getCarTypesAtCompany(company);
    }

    @Override
    public List<Integer> getCarIds(String company, String type) {
        return super.getCarIds(company, type);
    }

    @Override
    public Integer getNumberOfReservations(String company, String type, int id) {
        return super.getNumberOfReservations(company, type, id);
    }

    @Override
    public Integer getNumberOfReservations(String company, String type) {
        return super.getNumberOfReservations(company, type);
    }

    @Override
    public void loadRental(String dataFile) {
        try {
            System.out.println("***************in load rental********");
            CrcData data = this.loadData(dataFile);

            CarRentalCompany crc = this.createCRC(data.name, data.regions);

            System.out.println("company name " + crc.getName());
            System.out.println("company regions " + crc.getRegions());
            System.out.println("company name " + data.carTypes);

            for (CarType type : data.carTypes) {
                this.addCarType(crc, type);
            }
            for (Car car : data.cars) {
                System.out.println("cars " + car.getType());
                this.addCar(crc, car);
            }
        } catch (NumberFormatException ex) {
            throw new EJBException(ex);
        } catch (IOException ex) {
            throw new EJBException(ex);
        }
    }

    @Override
    public void addCarTypeToRental(String name, int nbOfSeats, float trunkSpace, double rentalPricePerDay, boolean smokingAllowed, String company) {
        CarRentalCompany crc = getEntityManager().find(CarRentalCompany.class, company);
        CarType type = this.createCarType(name, nbOfSeats, trunkSpace, rentalPricePerDay, smokingAllowed);
        this.addCarType(crc, type);
    }

    @Override
    public void addCarToRental(String type, String company) {
        boolean added = false;
        CarRentalCompany crc = getEntityManager().find(CarRentalCompany.class, company);
        for (CarType t : crc.getCarTypes()) {
            if (t.getName().equals(type)) {
                Car car = this.createCar(t);
                this.addCar(crc, car);
                added = true;
            }
        }
        if (!added) {
            this.getEJBContext().setRollbackOnly();
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
                        out.cars.add(this.createCar(type));
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
        public List<String> regions = new LinkedList<String>();
        public List<CarType> carTypes = new LinkedList<CarType>();
    }
}
