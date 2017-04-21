package dojomanager.storage.models;

import android.content.Context;

import java.util.ArrayList;

import dojomanager.main.models.Student;

/**
 * Created by Scott on 4/20/2017.
 */

public interface DojoStorageInterface {
	ArrayList<Student> readStudents();
	boolean writeStudents();
}
