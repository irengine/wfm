package com.kwchina.wfm.domain.model.employee;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class JobTest {

	@Test
	public void testCompareJob() {
		Job job1, job2;
		job1 = new Job(null, null, Collections.<JobPosition>emptyList(), JobStatus.UNKNOWN, null);
		job2 = Job.UNKNOWN;
		assertTrue(job1.sameValueAs(job2));
		
		job1 = new Job(null, null, Collections.<JobPosition>emptyList(), JobStatus.UNKNOWN, null);
		job2 = Job.UNKNOWN;
		List<JobPosition> positions = new ArrayList<JobPosition>();
		positions.add(new JobPosition("CTO"));
		job1.setPositions(positions);
		assertFalse(job1.sameValueAs(job2));
		
		job1 = new Job(null, null, Collections.<JobPosition>emptyList(), JobStatus.UNKNOWN, null);
		job2 = Job.UNKNOWN;
		job1.setTitle(new JobTitle("M", "Manager", 1));
		job1.setPositions(positions);
		assertFalse(job1.sameValueAs(job2));
	}

}
