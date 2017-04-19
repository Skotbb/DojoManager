package dojomanager.main.models;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Scott on 10/15/2016.
 */

public class Student {
    private UUID _id;
    private String firstName, lastName;
    private Calendar birthDate;
    private int age;
    private Rank rank;
    private boolean isPaidUp;
    private ArrayList<String> studentNotes;

    public Student(String firstName, String lastName, Date birthDate, Rank rank) {
        this._id = UUID.randomUUID();
        this.firstName = firstName;
        this.lastName = lastName;
        this.rank = rank;
        this.isPaidUp = true;
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


    // Unit Testing.
    public static void main(String[] args) {
		Student scott = new Student("Scott", "Thompson", new Date(1985, 9, 23), new Rank(4, Rank.RankType.Dan));

		System.out.println(scott.getFullName());
		System.out.println(scott.getAge());
	}
}
