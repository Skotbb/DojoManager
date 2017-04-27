package dojomanager.main.models;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

import com.android.thompson.scott.dojomanager.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Scott on 4/24/2017.
 */

public class DojoClass  implements Serializable{
	private UUID classId;
	private String className;
	private Date classDate;
	private ArrayList<Student> attendance;
	private double classDuration;
	private int color;

	public DojoClass() {
		this.classId = UUID.randomUUID();
		this.classDate = Calendar.getInstance().getTime();
		this.color = Color.WHITE;
		this.attendance = new ArrayList<Student>();
	}

	public DojoClass(Date classDate) {
		this.classId = UUID.randomUUID();
		this.classDate = classDate;
		this.color = Color.WHITE;
		this.attendance = new ArrayList<Student>();
	}

	public DojoClass(String className, Date classDate, ArrayList<Student> attendance, double duration) {
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

	public ArrayList<Student> getAttendance() {
		return attendance;
	}

	public void setAttendance(ArrayList<Student> attendance) {
		this.attendance = attendance;
	}

	public double getClassDuration() {
		return classDuration;
	}

	public void setClassDuration(double classDuration) {
		this.classDuration = classDuration;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}
}
