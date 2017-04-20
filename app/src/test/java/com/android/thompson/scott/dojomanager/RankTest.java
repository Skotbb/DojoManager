package com.android.thompson.scott.dojomanager;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import dojomanager.main.models.Rank;
import dojomanager.main.models.Student;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class RankTest {
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	Date bDay = sdf.parse("1985-09-23");
	private Student scott = new Student("Scott", "Thompson", bDay, new Rank(4, Rank.RankType.Dan));

	public RankTest() throws ParseException {
	}

	@Test
	public void testStudentRankChange() {
		scott.getRank().promote();
		System.out.println(scott.getRank().toString());
		assertThat(scott.getRank().toString(), allOf(containsString("5th"), containsString("Dan")));
	}

	@Test
	public void testBBChange() {
		try {
			bDay = sdf.parse("1991-03-20");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Student test = new Student("John", "Smith", bDay, new Rank(1, Rank.RankType.Kyu, 35));
		test.getRank().promote();
		assertThat(test.getRank().getRank().toString(), containsString("Dan"));
		assertThat(test.getRank().getTimeInRank(), equalTo(0.0));
	}
}
