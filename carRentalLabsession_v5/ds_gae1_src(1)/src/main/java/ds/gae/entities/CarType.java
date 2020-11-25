package ds.gae.entities;

import java.util.Objects;

import com.google.cloud.datastore.*;
import com.google.cloud.datastore.StructuredQuery.CompositeFilter;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;

public class CarType {

    private String name;
    private int nbOfSeats;
    private boolean smokingAllowed;
    private double rentalPricePerDay;
    // trunk space in liters
    private float trunkSpace;
    
    
    static public CarType fromEntityToCarType(Entity ent)
    {
    	if (! ent.getKey().getKind().equals("CarType"))
    	{
    		throw new IllegalArgumentException("Not a Car type");
    	}
    	return new CarType((String) ent.getValue("name").get()
    			           ,(int) ent.getLong("nbOfSeats")
    			           ,(float) ent.getDouble("trunkSpace")
    			           ,ent.getDouble("rentalPricePerDay")
    			           ,ent.getBoolean("smokingAllowed"));
    }
    
    static public boolean companyHasCartype(Datastore ds, String type, String company)
    {
    	KeyFactory kf = ds.newKeyFactory().setKind("CarRentalCompany");
    	Key comp         =  kf.newKey(company);
        Query<Entity> query = Query.newEntityQueryBuilder()
                .setKind("CarType")
                .setFilter(CompositeFilter.and(PropertyFilter.hasAncestor(comp), PropertyFilter.eq("name", type)))
                .build();
        QueryResults<Entity> results = ds.run(query);   
        return results.hasNext();
    }

    /***************
     * CONSTRUCTOR *
     ***************/

    public CarType(
            String name,
            int nbOfSeats,
            float trunkSpace,
            double rentalPricePerDay,
            boolean smokingAllowed) {
        this.name = name;
        this.nbOfSeats = nbOfSeats;
        this.trunkSpace = trunkSpace;
        this.rentalPricePerDay = rentalPricePerDay;
        this.smokingAllowed = smokingAllowed;
    }

    public String getName() {
        return name;
    }

    public int getNbOfSeats() {
        return nbOfSeats;
    }

    public boolean isSmokingAllowed() {
        return smokingAllowed;
    }

    public double getRentalPricePerDay() {
        return rentalPricePerDay;
    }

    public float getTrunkSpace() {
        return trunkSpace;
    }
    
    public Entity persist(Datastore ds, String company)
    {
    	if (CarType.companyHasCartype(ds, getName(), company))
    	{
    		throw new IllegalStateException("ALready persisted");
    	}
    	KeyFactory kf = ds.newKeyFactory().addAncestors(PathElement.of("CarRentalCompany", company)).setKind("CarType");
    	Key k         =  ds.allocateId(kf.newKey());
    	Entity ct     = Entity.newBuilder(k)
    			        .set("name", this.name)
    			        .set("nbOfSeats", this.nbOfSeats)
    			        .set("smokingAllowed", this.smokingAllowed)
    			        .set("rentalPricePerDay", this.rentalPricePerDay)
    			        .set("trunkSpace", this.trunkSpace)
    			        .build();
    	ds.put(ct);
    	return ct;    	
    }
    

    /*************
     * TO STRING *
     *************/

    @Override
    public String toString() {
        return String.format(
                "Car type: %s \t[seats: %d, price: %.2f, smoking: %b, trunk: %.0fl]",
                getName(),
                getNbOfSeats(),
                getRentalPricePerDay(),
                isSmokingAllowed(),
                getTrunkSpace()
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        CarType other = (CarType) obj;
        if (!Objects.equals(name, other.name)) {
            return false;
        }
        return true;
    }
}
