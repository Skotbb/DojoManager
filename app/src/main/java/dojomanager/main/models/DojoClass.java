package dojomanager.main.models;

import java.io.Serializable;
import java.util.ArrayList;
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
}
