package com.kwchina.wfm.infrastructure.common;

import static org.junit.Assert.*;

import org.junit.Test;

import com.kwchina.wfm.interfaces.common.ReportHelper;

public class ReportHelperTest {

	@Test
	public void testGetHours() {
		assertTrue(0 == ReportHelper.getYesterdayHours(8, 16));
		assertTrue(8 == ReportHelper.getTodayHours(8, 16));
		assertTrue(0 == ReportHelper.getTomorrowHours(8, 16));

		assertTrue(8 == ReportHelper.getYesterdayHours(-8, 8));
		assertTrue(8 == ReportHelper.getTodayHours(-8, 8));
		assertTrue(0 == ReportHelper.getTomorrowHours(8, 16));

		assertTrue(0 == ReportHelper.getYesterdayHours(20, 32));
		assertTrue(4 == ReportHelper.getTodayHours(20, 32));
		assertTrue(8 == ReportHelper.getTomorrowHours(20, 32));
	}

}
