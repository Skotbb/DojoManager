package dojomanager.main.models;

import android.support.annotation.NonNull;
import android.util.Log;

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
        return this._id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getFullName() {return this.firstName +" "+ this.lastName;}

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Calendar getBirthDate() {
        return this.birthDate;
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
        return this.rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public void setTimeInRank(Double time) {
		this.rank.setTimeInRank(time);
	}
	public Double getTimeInRank() {
		return this.rank.getTimeInRank();
	}

	public void setRankLevel(int rankLevel) {
		this.rank.setRankLevel(rankLevel);
	}
	public int getRankLevel() {
		return this.rank.getRankLevel();
	}

	public void setRankType(Rank.RankType rank) {
		this.rank.setRank(rank);
	}
	public Rank.RankType getRankType() {
		return this.rank.getRank();
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

	public String getNoteAt(int position) {
		try {
			 return this.studentNotes.get(position);
		} catch (NullPointerException e) {
			Log.e("STUDENT", e.getMessage());
		}
		return null;
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
	public int compareTo(@NonNull Student that) {
		int last = this.getRank().getRank().toString().compareTo(that.getRank().getRank().toString());

			// If rank type is the same.
		if(last == 0) {
			//  If their ranks are both Dan
			if(this.getRank().getRank().equals(Rank.RankType.Dan)) {
				// If their rank levels are the same
				if(this.getRank().getRankLevel() == that.getRank().getRankLevel()) {
					return this.getRank().getTimeInRank() < that.getRank().getTimeInRank() ? 1 : -1;
				} else {	// If their rank levels differ. Dan higher number is better.
					return this.getRank().getRankLevel() < that.getRank().getRankLevel() ? 1 : -1;
				}
			} else {	// If their ranks are both Kyu
				// If their rank levels are the same
				if(this.getRank().getRankLevel() == that.getRank().getRankLevel()) {
					return this.getRank().getTimeInRank() < that.getRank().getTimeInRank() ? 1 : -1;
				} else {	// If their rank levels differ. Kyu lower number is better.
					return this.getRank().getRankLevel() > that.getRank().getRankLevel() ? 1 : -1;
				}
			}
		} else {	// If rank types are different, return the compare.
			return last;
		}
	}
}
