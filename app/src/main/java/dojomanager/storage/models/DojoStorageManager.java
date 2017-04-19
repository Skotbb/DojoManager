package dojomanager.storage.models;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import dojomanager.main.models.Rank;
import dojomanager.main.models.Student;

/**
 * Created by Scott on 4/18/2017.
 */

public class DojoStorageManager {
	private static final DojoStorageManager ourInstance = new DojoStorageManager();
	private List<Student> mStudents;

	public static DojoStorageManager getInstance() {
		return ourInstance;
	}

	private DojoStorageManager() {
		mStudents = new ArrayList<>();

		//Test data
		Student scott = new Student("Scott", "Thompson", new Date(1985, 9, 23), new Rank(4, Rank.RankType.Dan));
		mStudents.add(scott);
	}

	public void addStudent(Student kid) {
		if(!mStudents.contains(kid)) {
			mStudents.add(kid);
		}
	}

	public List<Student> getStudents() {return mStudents;}

	public Student getStudentById(UUID id) {
		for(Student cur : mStudents) {
			if(cur.getId() == id) {
				return cur;
			}
		}
		return null;
	}

	public void writeStudents() {

	}
}
