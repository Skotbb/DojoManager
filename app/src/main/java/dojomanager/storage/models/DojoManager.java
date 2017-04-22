package dojomanager.storage.models;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import dojomanager.main.models.Student;

/**
 * Created by Scott on 4/18/2017.
 */

public class DojoManager {
	private static final DojoManager ourInstance = new DojoManager();
	private ArrayList<Student> mStudents;

	public static DojoManager getInstance() {
		return ourInstance;
	}

	private DojoManager() {
		mStudents = new ArrayList<>();

		//Test data
//		Date bDay = null;
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		try {
//			bDay = sdf.parse("1985-09-23");
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
//		Student scott = new Student("Scott", "Thompson", bDay, new Rank(4, Rank.RankType.Dan, 3000));
//		try {
//			bDay = sdf.parse("1987-10-05");
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
//		Student mark = new Student("Mark", "McGinty", bDay, new Rank(4, Rank.RankType.Dan, 100));
//		mStudents.add(scott);
//		mStudents.add(mark);
	}

	public void addStudent(Student kid) {
		if(!mStudents.contains(kid)) {
			mStudents.add(kid);
		}
	}

	public ArrayList<Student> getStudents() {return mStudents;}

	public Student getStudentById(UUID id) {
		for(Student cur : mStudents) {
			if(cur.getId().equals(id)) {
				return cur;
			}
		}
		return null;
	}

	public boolean setStudents(ArrayList<Student> students) {
		this.mStudents = students;
		Collections.sort(this.mStudents);
		return true;
	}

	public boolean replaceStudent(Student student) {
		for(Student cur : mStudents) {
			if(cur.getId().equals(student.getId())) {
				// Save any possible notes from student
				ArrayList<String> tempNotes = new ArrayList<>(cur.getStudentNotes());
				boolean didRemove = mStudents.remove(cur);
				// Save to updated Student
				student.setStudentNotes(tempNotes);
				boolean didAdd = mStudents.add(student);

				if (didAdd && didRemove) {
					return true;
				}
				return false;
			}
		}
		return false;
	}
}
