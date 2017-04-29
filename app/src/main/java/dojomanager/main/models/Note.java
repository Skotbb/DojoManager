package dojomanager.main.models;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;

import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Scott on 4/28/2017.
 */

public class Note implements Serializable{
	private String message;
	private Date timestamp;
	private ColorDrawable systemColor;

	public Note() {
		this.message = "";
		this.timestamp = Calendar.getInstance().getTime();
		this.systemColor = new ColorDrawable(Color.WHITE);
	}

	public Note(String message, Date timestamp) {
		this.message = message;
		this.timestamp = timestamp;
	}

	public Note(String message, Date timestamp, ColorDrawable systemColor) {
		this.message = message;
		this.timestamp = timestamp;
		this.systemColor = systemColor;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public ColorDrawable getSystemColor() {
		return systemColor;
	}

	public void setSystemColor(ColorDrawable systemColor) {
		this.systemColor = systemColor;
	}

	public void setSystemColor(String colorString) {
		int color;
		try {
			color = Color.parseColor(colorString);
			this.systemColor = new ColorDrawable(color);
		} catch (InvalidParameterException e) {
			Log.e(this.toString(), e.getMessage());
		}
	}

	public void setSystemColor(int colorInt) {
		this.systemColor = new ColorDrawable(colorInt);
	}

	@Override
	public String toString() {
		return message;
	}
}
