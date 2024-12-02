package Annonces;

public class Note {

	private int id_note;
	private int id_user;
	private String comment;
	
	public Note(int id_note, int id_user, String comment) {
		
		this.id_note = id_note;
		this.id_user = id_user;
		this.comment = comment;
	}

	public int getId_note() {
		return id_note;
	}

	public void setId_note(int id_note) {
		this.id_note = id_note;
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

	@Override
	public String toString() {
		return "Note [id_note=" + id_note + ", id_user=" + id_user + ", comment=" + comment + "]";
	}
	
	
}
