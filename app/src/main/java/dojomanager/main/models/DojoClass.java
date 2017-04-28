package dojomanager.main.models;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

import com.android.thompson.scott.dojomanager.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Scott on 4/24/2017.
 */

public class DojoClass  implements Serializable{
	private UUID classId;
	private String className;
	private Date classDate;
	private HashMap<String, Student> attendance;
	private double classDuration;
	private String color;

	public DojoClass() {
		this.classId = UUID.randomUUID();
		this.classDate = Calendar.getInstance().getTime();
		this.color = "#ffffff";
		this.attendance = new HashMap<>();
	}

	public DojoClass(Date classDate) {
		this.classId = UUID.randomUUID();
		this.classDate = classDate;
		this.color = "#ffffff";
		this.attendance = new HashMap<>();
	}

	public DojoClass(String className, Date classDate, HashMap<String, Student> attendance, double duration) {
		this.classId = UUID.randomUUID();
		this.className = className;
		this.classDate = classDate;
		this.attendance = attendance;
		this.classDuration = duration;
	}

	public UUID getClassId() {
		return classId;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public Date getClassDate() {
		return classDate;
	}

	public void setClassDate(Date classDate) {
		this.classDate = classDate;
	}

	public HashMap<String, Student> getAttendance() {
		return attendance;
	}

	public void setAttendance(HashMap<String, Student> attendance) {
		this.attendance = attendance;
	}

	public double getClassDuration() {
		return classDuration;
	}

	public void setClassDuration(double classDuration) {
		this.classDuration = classDuration;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
}
