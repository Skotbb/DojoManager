package dojomanager.storage.models;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.UUID;

import dojomanager.main.models.DojoClass;
import dojomanager.main.models.Student;

/**
 * Created by Scott on 4/18/2017.
 */

public class DojoManager {
	private static final DojoManager ourInstance = new DojoManager();
	private ArrayList<Student> mStudents;
	private HashSet<Date> mDates;
	private HashMap<String, HashSet<DojoClass>> mClasses;

	public static DojoManager getInstance() {
		return ourInstance;
	}

	private DojoManager() {
		mStudents = new ArrayList<>();
		mDates = new HashSet<>();
		mClasses = new HashMap<>();

//		CalendarDay day = new CalendarDay(Calendar.getInstance());
//		mDates.add(day);
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

	//////////////////////////////////////////
			/*		Students		*/
	//////////////////////////////////////////
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
	//////////////////////////////////////////
			/*		Dates		*/
	//////////////////////////////////////////

	public HashSet<Date> getDates() {
		return mDates;
	}

	public void setDates(HashSet<Date> mDates) {
		this.mDates = mDates;
	}

	public void addDate(Date date) {
		//CalendarDay newDate = new CalendarDay(date);
		mDates.add(date);
	}

	///////////////////////////////////////////
			/*		Classes		*/
	//////////////////////////////////////////

	public HashMap<String, HashSet<DojoClass>> getClasses() {
		return mClasses;
	}

	public void setClasses(HashMap<String, HashSet<DojoClass>> mClasses) {
		this.mClasses = mClasses;
	}

	public void addClass(DojoClass dojoClass) {
		if(mClasses.containsKey(dojoClass.getClassDate().toString())){
			mClasses.get(dojoClass.getClassDate().toString()).add(dojoClass);
			if(!mDates.contains(dojoClass.getClassDate())) {
				addDate(dojoClass.getClassDate());
			}
		} else {
			HashSet<DojoClass> temp = new HashSet<>();
			temp.add(dojoClass);
			mClasses.put(dojoClass.getClassDate().toString(), temp);
			addDate(dojoClass.getClassDate());
		}

	}

	public DojoClass getClassById(UUID id) {
		for(String key : mClasses.keySet()) {
			HashSet<DojoClass> classSet = mClasses.get(key);
			Iterator<DojoClass> itr = classSet.iterator();
			while (itr.hasNext()) {
				DojoClass cur = itr.next();
				if(cur.getClassId().equals(id)) {
					return cur;
				}
			}
		}
		return null;
	}

	public boolean removeClassById(UUID id) {
		for(String key : mClasses.keySet()) {
			HashSet<DojoClass> classSet = mClasses.get(key);
			Iterator<DojoClass> itr = classSet.iterator();
			while (itr.hasNext()) {
				DojoClass cur = itr.next();
				if(cur.getClassId().equals(id)) {
					if(classSet.size() == 1) {
						mDates.remove(cur.getClassDate());
					}
					classSet.remove(cur);
					return true;
				}
			}
		}
		return false;
	}
}
