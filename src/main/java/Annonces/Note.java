package Annonces;

public class Note {

	
	private int id_user;
	private String comment;
	private String username;
	
	public Note() {
		
	}
	
	public Note(int id_user, String comment,String username) {
		
		
		this.id_user = id_user;
		this.comment = comment;
		this.username = username;
	}

	

	public int getId_user() {
		return id_user;
	}

	public void setId_user(int id_user) {
		this.id_user = id_user;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		return "Note [id_user=" + id_user + ", comment=" + comment + ", username=" + username + "]";
	}

	
	
}
