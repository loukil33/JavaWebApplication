package Bikes;

import java.util.List;

public class Bike {
	 	private int id;
	 	private int userid;
	    private String model;
	    private String brand;
	    private String condition;
	    private String color;
	    private boolean isAvailable;
	    private List<String> images;
	    
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
		public String getBrand() {
			return brand;
		}
		public void setBrand(String brand) {
			this.brand = brand;
		}
		public String getCondition() {
			return condition;
		}
		public void setCondition(String condition) {
			this.condition = condition;
		}
		public String getColor() {
			return color;
		}
		public void setColor(String color) {
			this.color = color;
		}
		public int getUserid() {
			return userid;
		}
		public void setUserid(int userid) {
			this.userid = userid;
		}
		
		public boolean isAvailable() {
			return isAvailable;
		}
		public void setAvailable(boolean isAvailable) {
			this.isAvailable = isAvailable;
		}
		public List<String> getImages() {
			return images;
		}
		public void setImages(List<String> images) {
			this.images = images;
		}
		public Bike(int id,int userid, String model, String brand, String condition, String color, boolean isAvailable,
				List<String> images) {
			this.id = id;
			this.userid = userid;
			this.model = model;
			this.brand = brand;
			this.condition = condition;
			this.color = color;
			this.isAvailable = isAvailable;
			this.images = images;
		}
		
		
		public Bike(String model, String brand, String condition, String color, boolean isAvailable) {
			this.model = model;
			this.brand = brand;
			this.condition = condition;
			this.color = color;
			this.isAvailable = isAvailable;
		}
		

	    // Constructor for easier initialization
	    public Bike(int id, String model, String brand, String condition, String color, boolean isAvailable,
	                List<String> images, double rentalPrice, boolean isForSale, double salePrice) {
	        this.id = id;
	        this.model = model;
	        this.brand = brand;
	        this.condition = condition;
	        this.color = color;
	        this.isAvailable = isAvailable;
	        this.images = images;
	    }
		
		public Bike() {
		}
		@Override
		public String toString() {
			return "Bike [id=" + id + ", userid=" + userid + ", model=" + model + ", brand=" + brand + ", condition="
					+ condition + ", color=" + color + ", isAvailable=" + isAvailable + ", images=" + images + "]";
		}

}
