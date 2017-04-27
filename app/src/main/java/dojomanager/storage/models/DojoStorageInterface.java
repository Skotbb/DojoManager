package dojomanager.storage.models;

import android.content.Context;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import dojomanager.main.models.DojoClass;
import dojomanager.main.models.Student;

/**
 * Created by Scott on 4/20/2017.
 */

public interface DojoStorageInterface {
	ArrayList<Student> readStudents();
	boolean writeStudents();

	HashMap<String, HashSet<DojoClass>> readClasses();
	boolean writeClasses();

	HashSet<Date> readClassDates();
	boolean writeClassDates();
}
