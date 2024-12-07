package Users;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import Annonces.Annonce;
import Annonces.Note;
import Annonces.Rental;
import Bikes.Bike;
import static Bikes.bikesDB.bikes;
public class UserDatabase {
	 public static List<User> users = new ArrayList<>();
	 static {
	        User defaultUser = new User(
	            1, // id
	            12345678, // CIN
	            "John", // First Name
	            "Doe", // Last Name
	            "admin", // Email
	            "123", // Password
	            "123 Main St", // Address
	            LocalDate.of(1990, 1, 1), // Birth Date
	            "default_profile.jpg", // Image Profile
	            RoleType.EMPLOYEE, // Role
	            123456789, // Phone Number
	            new ArrayList<Annonce>(), // Annonces
	            new ArrayList<Note>(),
	            bikes,
	            new ArrayList<Rental>()
	        );
	        User User1 = new User(
		            2, // id
		            87654321, // CIN
		            "aziz", // First Name
		            "loukil", // Last Name
		            "mohamedazizloukil@gmail.com", // Email
		            "123", // Password
		            "123 Main St", // Address
		            LocalDate.of(1990, 1, 1), // Birth Date
		            "default_profile.jpg", // Image Profile
		            RoleType.EMPLOYEE, // Role
		            123456789, // Phone Number
		            new ArrayList<Annonce>(), // Annonces
		            new ArrayList<Note>(),
		            bikes,
		            new ArrayList<Rental>()
		        );
	        User User2 = new User(
		            3, // id
		            87614621, // CIN
		            "mohamed", // First Name
		            "loukil", // Last Name
		            "azouzloukil@gmail.com", // Email
		            "123", // Password
		            "123 Main St", // Address
		            LocalDate.of(1990, 1, 1), // Birth Date
		            "default_profile.jpg", // Image Profile
		            RoleType.EMPLOYEE, // Role
		            123456789, // Phone Number
		            new ArrayList<Annonce>(), // Annonces
		            new ArrayList<Note>(),
		            bikes,
		            new ArrayList<Rental>()
		        );
	        users.add(defaultUser);
	        users.add(User1);
	        users.add(User2);
	    }
}
