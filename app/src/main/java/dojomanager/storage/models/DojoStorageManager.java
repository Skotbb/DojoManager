package dojomanager.storage.models;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import dojomanager.main.models.Student;

/**
 * Created by Scott on 4/20/2017.
 */

public class DojoStorageManager implements DojoStorageInterface {
	private static final String DOJO_DIAG = "DojoStorageManager_diag";
	private static final String DOJO_STUDENTS_FILENAME = "DojoStudents.dat";

	private DojoManager dm;
	private Context mContext;

	public DojoStorageManager(Context context) {
		dm = DojoManager.getInstance();
		mContext = context;

		ArrayList<Student> temp = readStudents();
		if(temp != null) {
			dm.setStudents(temp);
		}
	}

	public DojoManager getDojoManager() {
		return dm;
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

	private void logMessage(String message) {
		Log.d(DOJO_DIAG, message);
	}
}



