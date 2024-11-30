package Cart;

public class BikeForSale {
    private int id;
    private String model;
    private String brand;
    private String color;
    private double salePrice; // This is the field for the bike's sale price
    private String image; // Image URL or path for the bike

    // No-argument constructor (required for deserialization)
    public BikeForSale() {
    }

    // Parameterized constructor
    public BikeForSale(int id, String model, double salePrice, String image) {
        this.id = id;
        this.model = model;
        this.salePrice = salePrice;
        this.image = image;
    }
    

    public double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    
    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

 
}
