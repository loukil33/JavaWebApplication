package Users;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import Annonces.Annonce;
import Annonces.Note;

public class UserDatabase {
	 public static List<User> users = new ArrayList<>();
	 static {
	        User defaultUser = new User(
	            1, // id
	            12345678, // CIN
	            "John", // First Name
	            "Doe", // Last Name
	            "admin@gmail.com", // Email
	            "123", // Password
	            "123 Main St", // Address
	            LocalDate.of(1990, 1, 1), // Birth Date
	            "default_profile.jpg", // Image Profile
	            RoleType.EMPLOYEE, // Role
	            123456789, // Phone Number
	            new ArrayList<Annonce>(), // Annonces
	            new ArrayList<Note>() // Notes
	        );

	        users.add(defaultUser);
	    }
}
