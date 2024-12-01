package Users;

import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import Annonces.Annonce;
import Annonces.Note;

public class User implements Serializable{

	
	private int id;
	private int cin;
	private String first_name;
	private String last_name;
	private String email;
	private String password;
	private String address;
	private LocalDate birth_date;
	private String image_profile;
	private RoleType role;
	private int PhoneNumber;
	private List<Annonce> annonces;
	private List<Note> notes;
	
	public User() {
	
		
	}
	
	public User(int id, int cin, String first_name, String last_name, String email, String password, String address,
			LocalDate birth_date, String image_profile, RoleType role, int phoneNumber, List<Annonce> annonces,List<Note> notes) {
		
		this.id = id;
		this.cin = cin;
		this.first_name = first_name;
		this.last_name = last_name;
		this.email = email;
		this.password = password;
		this.address = address;
		this.birth_date = birth_date;
		this.image_profile = image_profile;
		this.role = role;
		this.PhoneNumber = phoneNumber;
		this.annonces = annonces;
		this.notes = notes;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCin() {
		return cin;
	}
	public void setCin(int cin) {
		this.cin = cin;
	}
	public String getFirst_name() {
		return first_name;
	}
	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}
	public String getLast_name() {
		return last_name;
	}
	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public LocalDate getBirth_date() {
		return birth_date;
	}
	public void setBirth_date(LocalDate birth_date) {
		this.birth_date = birth_date;
	}
	public String getImage_profile() {
		return image_profile;
	}
	public void setImage_profile(String image_profile) {
		this.image_profile = image_profile;
	}
	public RoleType getRole() {
		return role;
	}
	public void setRole(RoleType role) {
		this.role = role;
	}
	public int getPhoneNumber() {
		return PhoneNumber;
	}
	public void setPhoneNumber(int phoneNumber) {
		PhoneNumber = phoneNumber;
	}
	
	public List<Annonce> getAnnonces() {
		return annonces;
	}

	public void setAnnonces(List<Annonce> annonces) {
		this.annonces = annonces;
	}

	public List<Note> getNotes() {
		return notes;
	}

	public void setNotes(List<Note> notes) {
		this.notes = notes;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", cin=" + cin + ", first_name=" + first_name + ", last_name=" + last_name
				+ ", email=" + email + ", password=" + password + ", address=" + address + ", birth_date=" + birth_date
				+ ", image_profile=" + image_profile + ", role=" + role + ", PhoneNumber=" + PhoneNumber + ", annonces="
				+ annonces + ", notes=" + notes + "]";
	}

	

	
	
}
