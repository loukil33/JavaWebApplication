package Bikes;

import java.util.List;

public class Bike {
	 	private int id;
	    private String model;
	    private String brand;
	    private String condition;
	    private String color;
	    private boolean isAvailable;
	    private List<String> images;
	    private double rentalPrice;
	    private List<String> notes;
	    private boolean isForSale;
	    private double salePrice;
	    private boolean alreadyRented;
		@Override
		public String toString() {
			return "bike [id=" + id + ", model=" + model + ", brand=" + brand + ", condition=" + condition + ", color="
					+ color + ", isAvailable=" + isAvailable + ", images=" + images + ", rentalPrice=" + rentalPrice
					+ ", notes=" + notes + ", isForSale=" + isForSale + ", salePrice=" + salePrice + ", alreadyRented="
					+ alreadyRented + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()="
					+ super.toString() + "]";
		}
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
		public double getRentalPrice() {
			return rentalPrice;
		}
		public void setRentalPrice(double rentalPrice) {
			this.rentalPrice = rentalPrice;
		}
		public List<String> getNotes() {
			return notes;
		}
		public void setNotes(List<String> notes) {
			this.notes = notes;
		}
		public boolean isForSale() {
			return isForSale;
		}
		public void setForSale(boolean isForSale) {
			this.isForSale = isForSale;
		}
		public double getSalePrice() {
			return salePrice;
		}
		public void setSalePrice(double salePrice) {
			this.salePrice = salePrice;
		}
		public boolean isAlreadyRented() {
			return alreadyRented;
		}
		public void setAlreadyRented(boolean alreadyRented) {
			this.alreadyRented = alreadyRented;
		}
	    
	    
	    

}
