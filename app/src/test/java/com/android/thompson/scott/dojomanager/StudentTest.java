package com.android.thompson.scott.dojomanager;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import dojomanager.main.models.Rank;
import dojomanager.main.models.Student;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;


public class StudentTest {
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	Date bDay = sdf.parse("1985-09-23");
	private Student scott = new Student("Scott", "Thompson", bDay, new Rank(4, Rank.RankType.Dan));

	public StudentTest() throws ParseException {
	}

	@Test
    public void checkStudentName() throws Exception {
		assertThat(scott.getFullName(), is("Scott Thompson"));
    }

    @Test
	public void checkStudentRank() {
		assertEquals("Rank is incorrect", "4th Dan", scott.getRank().toString());
		printActual(scott.getRank().toString());
	}

	@Test
	public void checkStudentAge() {
		assertEquals("Actual: " + String.valueOf(scott.getAge()), 2017-1985, scott.getAge());
	}

	@Test
	public void checkStudentBday() {
		System.out.println(scott.toString());
		System.out.println(bDay.toString());
		assertThat(scott.getBirthDate().getTime().toString(), allOf(containsString("Sep"), containsString("23"), containsString("1985")));
	}

	private void printActual(String actualStr) {
		System.out.println("Actual: " + actualStr);
	}
}