package dojomanager.storage.models;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import dojomanager.main.models.DojoClass;
import dojomanager.main.models.Student;

/**
 * Created by Scott on 4/20/2017.
 */

public class DojoStorageManager implements DojoStorageInterface {
	private static final String DOJO_DIAG = "DojoStorageManager_diag";
	private static final String DOJO_STUDENTS_FILENAME = "DojoStudents.dat";
	private static final String DOJO_ClASSES_FILENAME = "DojoClasses.dat";
	private static final String DOJO_ClASSDATES_FILENAME = "ClassDates.dat";

	private DojoManager dm;
	private Context mContext;

	public DojoStorageManager(Context context) {
		this.dm = DojoManager.getInstance();
		this.mContext = context;

		readAllData();
		for(String key : dm.getClasses().keySet()) {
			if (dm.getClasses().get(key).size() > 0) {
				SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM d hh:mm:ss z yyyy");
				Date tempDate = null;
				try {
					tempDate = sdf.parse(key);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				if(tempDate != null && !dm.getDates().contains(tempDate)) {
					dm.addDate(tempDate);
					writeClassDates();
				}
			}
		}
	}

	public DojoManager getDojoManager() {
		return this.dm;
	}

	public ArrayList<Student> getStudent() {
		return this.dm.getStudents();
	}
	public Student getStudentByID(UUID id) {
		return this.dm.getStudentById(id);
	}

	@Override
	public ArrayList<Student> readStudents() {
		ArrayList<Student> tempList;

		try {
			// Open file input stream
			FileInputStream fis = mContext.openFileInput(DOJO_STUDENTS_FILENAME);
			// Open object input stream
			ObjectInputStream in = new ObjectInputStream(fis);
			// Read mStudents array list
			tempList = (ArrayList<Student>) in.readObject();
			if(tempList == null) {
				return null;
			}
			dm.setStudents(tempList);
			return dm.getStudents();
		} catch (IOException | ClassNotFoundException e) {
			Log.e(DOJO_DIAG, e.getMessage());
			return null;
		}
	}

	@Override
	public boolean writeStudents() {
		if (!dm.getStudents().isEmpty()) {
			try {
				// Open file stream
				FileOutputStream fos = mContext.openFileOutput(DOJO_STUDENTS_FILENAME, Context.MODE_PRIVATE);
				// Open object output stream.
				ObjectOutputStream out = new ObjectOutputStream(fos);

				// Write mStudents array to file stream.
				logMessage("Writing to device.");
				out.writeObject(dm.getStudents());
				logMessage("Finished.");
				// Close streams.
				out.close();
				fos.close();
				logMessage("Closed.");
				return true;
			}catch(IOException e) {
				Log.e(DOJO_DIAG, e.getMessage());
				return false;
			}
		}
		Log.e(DOJO_DIAG, "Not saving an empty Student list");
		return false;
	}

	@Override
	public HashMap<String, HashSet<DojoClass>> readClasses() {
		HashMap<String, HashSet<DojoClass>> tempList;

		try {
			// Open file input stream
			FileInputStream fis = mContext.openFileInput(DOJO_ClASSES_FILENAME);
			// Open object input stream
			ObjectInputStream in = new ObjectInputStream(fis);
			// Read mStudents array list
			tempList = (HashMap<String, HashSet<DojoClass>>) in.readObject();
			if(tempList == null) {
				return null;
			}
			dm.setClasses(tempList);
			return dm.getClasses();
		} catch (IOException | ClassNotFoundException e) {
			Log.e(DOJO_DIAG, e.getMessage());
			return null;
		}
	}

	@Override
	public boolean writeClasses() {
		if (!dm.getClasses().isEmpty()) {
			try {
				// Open file stream
				FileOutputStream fos = mContext.openFileOutput(DOJO_ClASSES_FILENAME, Context.MODE_PRIVATE);
				// Open object output stream.
				ObjectOutputStream out = new ObjectOutputStream(fos);

				// Write mStudents array to file stream.
				logMessage("Writing to device.");
				out.writeObject(dm.getClasses());
				logMessage("Finished.");
				// Close streams.
				out.close();
				fos.close();
				logMessage("Closed.");
				return true;
			}catch(IOException e) {
				Log.e(DOJO_DIAG, e.getMessage());
				return false;
			}
		}
		Log.e(DOJO_DIAG, "Not saving an empty Class list");
		return false;
	}

	@Override
	public HashSet<Date> readClassDates() {
		HashSet<Date> tempList;

		try {
			// Open file input stream
			FileInputStream fis = mContext.openFileInput(DOJO_ClASSDATES_FILENAME);
			// Open object input stream
			ObjectInputStream in = new ObjectInputStream(fis);
			// Read mClasses HashSet
			tempList = (HashSet<Date>) in.readObject();
			if(tempList == null) {
				return null;
			}
			dm.setDates(tempList);
			return dm.getDates();
		} catch (IOException | ClassNotFoundException e) {
			Log.e(DOJO_DIAG, e.getMessage());
			return null;
		}
	}

	@Override
	public boolean writeClassDates() {
		if (!dm.getDates().isEmpty()) {
			try {
				// Open file stream
				FileOutputStream fos = mContext.openFileOutput(DOJO_ClASSDATES_FILENAME, Context.MODE_PRIVATE);
				// Open object output stream.
				ObjectOutputStream out = new ObjectOutputStream(fos);

				// Write mStudents array to file stream.
				logMessage("Writing to device.");
				out.writeObject(dm.getDates());
				logMessage("Finished.");
				// Close streams.
				out.close();
				fos.close();
				logMessage("Closed.");
				return true;
			}catch(IOException e) {
				Log.e(DOJO_DIAG, e.getMessage());
				return false;
			}
		}
		Log.e(DOJO_DIAG, "Not saving an empty Dates list");
		return false;
	}

	public boolean readAllData() {
		if (dm == null) {
			this.dm = DojoManager.getInstance();
		}

		ArrayList<Student> studList = readStudents();
		if(studList != null) {
			this.dm.setStudents(studList);
		} else {
			logMessage("Issue with students");
			return false;
		}
		HashMap<String, HashSet<DojoClass>> classList = readClasses();
		if(classList != null) {
			this.dm.setClasses(classList);
		}else {
			logMessage("Issue with classes.");
			return false;
		}
		HashSet<Date> dateList = readClassDates();
		if(dateList != null) {
			this.dm.setDates(dateList);
		}else {
			logMessage("Issue with class dates.");
			return false;
		}

		return true;
	}

	private void logMessage(String message) {
		Log.d(DOJO_DIAG, message);
	}
}



