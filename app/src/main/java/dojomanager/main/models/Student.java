package dojomanager.main.models;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Scott on 10/15/2016.
 */

public class Student implements Serializable, Comparable<Student>{
    private UUID _id;
    private String firstName, lastName;
    private Calendar birthDate;
    private int age;
    private Rank rank;
    private boolean isPaidUp;
    private ArrayList<String> studentNotes;

    public Student() {
		this._id = UUID.randomUUID();
		this.rank = new Rank();
		this.studentNotes = new ArrayList<>();
		this.isPaidUp = false;
		setBirthDate(Calendar.getInstance().getTime());
	}

	public Student(String firstName, String lastName, Date birthDate, Rank rank) {
        this._id = UUID.randomUUID();
        this.firstName = firstName;
        this.lastName = lastName;
        this.rank = rank;
        this.isPaidUp = false;
        this.studentNotes = new ArrayList<>();

		setBirthDate(birthDate);
    }

    public UUID getId() {
        return _id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFullName() {return this.firstName +" "+ this.lastName;}

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Calendar getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
		Calendar toDate = Calendar.getInstance();	// Use the passed date to create Students Calendar
		toDate.setTime(birthDate);
		this.birthDate = toDate;

        Calendar today = Calendar.getInstance();

        this.age = today.get(Calendar.YEAR) - toDate.get(Calendar.YEAR);
    }

    public int getAge() {
        Calendar today = Calendar.getInstance();

		return today.get(Calendar.YEAR) - this.birthDate.get(Calendar.YEAR);
    }

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public boolean isPaidUp() {
        return isPaidUp;
    }

    public void setPaidUp(boolean paidUp) {
        isPaidUp = paidUp;
    }

	public ArrayList<String> getStudentNotes() {
		return studentNotes;
	}

	public void setStudentNotes(ArrayList<String> notes) {this.studentNotes = notes;}

	public void addNote(String note) {
		this.studentNotes.add(note);
	}

	@Override
	public String toString() {
		String str;
		str = String.format("%s, %d \n" +
				"Rank: %s \n" +
				"Birthday: %s \n", this.getFullName(), this.getAge(), this.getRank().toString(), this.getBirthDate().getTime().toString());
		return str;
	}

	@Override
	public int compareTo(@NonNull Student another) {
		int last = this.getRank().getRank().toString().compareTo(another.getRank().getRank().toString());
		return last != 0 ? last : String.valueOf(another.getRank().getRankLevel()).compareTo(String.valueOf(this.getRank().getRankLevel()));
	}
}
