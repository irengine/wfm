package com.kwchina.wfm.infrastructure.common;

import static org.junit.Assert.*;

import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.Test;

import com.kwchina.wfm.domain.model.organization.Preference;

public class CollectionTest {

	@Test
	public void testPreferenceSet() {
		Set<Preference> preferences = new LinkedHashSet<Preference>();
		Preference p1 = new Preference("1", "1", "1");
		Preference p2 = new Preference("1", "1", "2");
		preferences.add(p1);
		preferences.add(p2);
		
		assertTrue(2 == preferences.size());
		
		assertTrue(preferences.contains(p1));
		assertTrue(preferences.contains(p2));
	}

}
