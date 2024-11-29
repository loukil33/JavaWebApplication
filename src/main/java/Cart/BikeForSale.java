package Cart;

public class BikeForSale {
	
	 	private int id;
	    private String name;
	    private String description;
	    private double price;
	    private boolean isAvailable;
	    // Constructor
	    public BikeForSale(int id, String name, String description, double price, boolean isAvailable) {
	        this.id = id;
	        this.name = name;
	        this.description = description;
	        this.price = price;
	        this.isAvailable = isAvailable;
	    }

	    // Getters and setters
	    public int getId() {
	        return id;
	    }

	    public void setId(int id) {
	        this.id = id;
	    }

	    public String getName() {
	        return name;
	    }

	    public void setName(String name) {
	        this.name = name;
	    }

	    public String getDescription() {
	        return description;
	    }

	    public void setDescription(String description) {
	        this.description = description;
	    }

	    public double getPrice() {
	        return price;
	    }

	    public void setPrice(double price) {
	        this.price = price;
	    }

	    public boolean isAvailable() {
	        return isAvailable;
	    }

	    public void setAvailable(boolean available) {
	        isAvailable = available;
	    }

	    // Override toString for debugging or logging
	    @Override
	    public String toString() {
	        return "BikeForSale{" +
	                "id=" + id +
	                ", name='" + name + '\'' +
	                ", description='" + description + '\'' +
	                ", price=" + price +
	                ", isAvailable=" + isAvailable +
	                '}';
	    }

}
