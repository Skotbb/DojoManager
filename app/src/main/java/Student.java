import java.util.ArrayList;

/**
 * Created by Scott on 10/15/2016.
 */

public class Student {
    private String firstName, lastName;
    private int age;
    private Rank rank;
    private boolean isPaidUp;
    private ArrayList<String> studentNotes;

    public Student(String firstName, String lastName, int age, Rank rank) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.rank = rank;
        this.isPaidUp = true;
        this.studentNotes = new ArrayList<>();
    }
}
