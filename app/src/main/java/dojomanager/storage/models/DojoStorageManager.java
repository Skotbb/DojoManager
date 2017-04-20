package dojomanager.storage.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
		Date bDay = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			bDay = sdf.parse("1985-09-23");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Student scott = new Student("Scott", "Thompson", bDay, new Rank(4, Rank.RankType.Dan, 3000));
		try {
			bDay = sdf.parse("1987-10-05");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Student mark = new Student("Mark", "McGinty", bDay, new Rank(4, Rank.RankType.Dan, 100));
		mStudents.add(scott);
		mStudents.add(mark);
	}

	public void addStudent(Student kid) {
		if(!mStudents.contains(kid)) {
			mStudents.add(kid);
		}
	}

	public List<Student> getStudents() {return mStudents;}

	public Student getStudentById(UUID id) {
		for(Student cur : mStudents) {
			if(cur.getId().equals(id)) {
				return cur;
			}
		}
		return null;
	}

	public void writeStudents() {

	}
}
