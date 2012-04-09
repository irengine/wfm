package com.kwchina.wfm.infrastructure.common;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.kwchina.wfm.domain.model.employee.Vacation;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/context-test.xml"})
public class PropertiesHelperTest {

	@Test
	public void testGetValue() {
		assertEquals("ANNUAL_LEAVE", Vacation.Type.ANNUAL_LEAVE.name());
		assertEquals("年休假",PropertiesHelper.getProperty(Vacation.Type.ANNUAL_LEAVE.name()));
	}

}
