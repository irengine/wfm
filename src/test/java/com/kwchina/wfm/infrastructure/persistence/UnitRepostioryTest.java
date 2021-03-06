package com.kwchina.wfm.infrastructure.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.kwchina.wfm.domain.model.organization.Preference;
import com.kwchina.wfm.domain.model.organization.Unit;
import com.kwchina.wfm.domain.model.organization.UnitRepository;
import com.kwchina.wfm.domain.model.shift.ShiftType;
import com.kwchina.wfm.domain.model.shift.ShiftTypeRepository;
import com.kwchina.wfm.interfaces.common.JacksonHelper;
import com.kwchina.wfm.interfaces.common.PageHelper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/context-test.xml"})
public class UnitRepostioryTest {

	@Autowired
	private UnitRepository unitRepository;
	
	@Autowired
	private ShiftTypeRepository shiftTypeRepository;
	
	@Test
	@Transactional
	public void testCreateRoot() {
		Unit rootBefore = unitRepository.getRoot("S");
		assertNotNull(rootBefore.getId());
		
		Unit rootAfter = unitRepository.findRoot();
		assertEquals(rootBefore, rootAfter);
	}
	
	@Test
	@Transactional
	public void testBuildTree() {
		Unit root = unitRepository.getRoot("S");
		
		Unit l1 = new Unit("L1");
		unitRepository.addChild(root, l1);
		
		Unit l11 = new Unit("L11");
		unitRepository.addChild(l1, l11);

		Unit l12 = new Unit("L12");
		unitRepository.addChild(l1, l12);

		Unit l2 = new Unit("L2");
		unitRepository.addChild(root, l2);
		
		Unit l21 = new Unit("L21");
		unitRepository.addChild(l2, l21);

		Unit l22 = new Unit("L22");
		unitRepository.addChild(l2, l22);

		Unit l23 = new Unit("L23");
		unitRepository.addChild(l2, l23);
		
		assertEquals(2, root.getChildren().size());
		assertEquals(2, l1.getChildren().size());
		assertEquals(3, l2.getChildren().size());
		
		assertTrue(0 != root.getLeft());
		assertTrue(0 != root.getRight());
		
		assertEquals(7, unitRepository.findAllChildren(root).size());
		
		assertEquals(0, unitRepository.findAllAncestor(root).size());
		assertEquals(1, unitRepository.findAllAncestor(l2).size());
		assertEquals(2, unitRepository.findAllAncestor(l21).size());
		
		JacksonHelper.getJson(root);
	}

	@Test
	@Transactional
	public void testUriNameInheritance() {
		Unit root = unitRepository.getRoot("S");
		
		Unit l1 = new Unit("L1");
		unitRepository.addChild(root, l1);
		
		Unit l11 = new Unit("L11");
		unitRepository.addChild(l1, l11);

		Unit l12 = new Unit("L12");
		unitRepository.addChild(l1, l12);

		Unit l2 = new Unit("L2");
		unitRepository.addChild(root, l2);
		
		Unit l21 = new Unit("L21");
		unitRepository.addChild(l2, l21);

		Unit l22 = new Unit("L22");
		unitRepository.addChild(l2, l22);

		Unit l23 = new Unit("L23");
		unitRepository.addChild(l2, l23);
		
		assertEquals(2, root.getChildren().size());
		assertEquals(2, l1.getChildren().size());
		assertEquals(3, l2.getChildren().size());
		
		assertTrue(0 != root.getLeft());
		assertTrue(0 != root.getRight());
		
		assertEquals(7, unitRepository.findAllChildren(root).size());
		
		assertEquals(0, unitRepository.findAllAncestor(root).size());
		assertEquals(1, unitRepository.findAllAncestor(l2).size());
		assertEquals(2, unitRepository.findAllAncestor(l21).size());
		
		root.setName("X");
		assertEquals("X-L2-L23", l23.getUriName());
		
		l2.setName("X2");
		assertEquals("X-X2-L23", l23.getUriName());
	}

	ShiftType dayShift;
	ShiftType nightShift;
	ShiftType fouthShift;
	private void initialShiftType() {
		dayShift = new ShiftType("日", 1, "", "", "");
		shiftTypeRepository.save(dayShift);
		nightShift = new ShiftType("夜", 2, "", "", "");
		shiftTypeRepository.save(nightShift);
		fouthShift = new ShiftType("全", 3, "", "", "");
		shiftTypeRepository.save(fouthShift);
	}
	
	@Test
	@Transactional
	public void testShiftTypeInheritance() {
		initialShiftType();
		
		Unit l = unitRepository.getRoot("L");
		
		Unit l1 = new Unit("L1");
		unitRepository.addChild(l, l1);
		
		Unit l11 = new Unit("L11");
		unitRepository.addChild(l1, l11);
		
		assertEquals(null, l.getShiftType());
		assertEquals(null, l1.getShiftType());
		assertEquals(null, l11.getShiftType());
		
		l.setShiftType(dayShift);
		
		assertEquals(dayShift, l.getShiftType());
		assertEquals(dayShift, l1.getShiftType());
		assertEquals(dayShift, l11.getShiftType());
		
		l1.setShiftType(nightShift);
		
		assertEquals(dayShift, l.getShiftType());
		assertEquals(nightShift, l1.getShiftType());
		assertEquals(nightShift, l11.getShiftType());
		
		l11.setShiftType(fouthShift);
		
		assertEquals(dayShift, l.getShiftType());
		assertEquals(nightShift, l1.getShiftType());
		assertEquals(fouthShift, l11.getShiftType());
	}
	
	@Test
	@Transactional
	public void testShiftTypeFlatView() {
		initialShiftType();
		
		Unit l = unitRepository.getRoot("L");
		
		Unit l1 = new Unit("L1");
		unitRepository.addChild(l, l1);
		
		Unit l11 = new Unit("L11");
		unitRepository.addChild(l1, l11);
		
		l1.setShiftType(dayShift);
		
		
		List<Unit> units = unitRepository.findAll();
		for(Unit u : units) {
			System.out.println(String.format("%s, %s", u.getUriName(), null == u.getShiftType() ? "-" : u.getShiftType().getName()));
		}
	}
	
	@Test
	@Transactional
	public void testPreferencesFlatView() {
		initialShiftType();
		
		Unit l = unitRepository.getRoot("L");
		
		Unit l1 = new Unit("L1");
		unitRepository.addChild(l, l1);
		
		Unit l11 = new Unit("L11");
		unitRepository.addChild(l1, l11);
		
		Set<Preference> preferences = new HashSet<Preference>();
		Preference p = new Preference("overtime", "true");
		preferences.add(p);
		l1.setPreferences(preferences);
		
		List<Unit> units = unitRepository.findAll();
		for(Unit u : units) {
			System.out.println(String.format("%s, %s", u.getUriName(), null == u.getPreference("overtime") ? "-" : u.getPreference("overtime").getValue()));
		}
	}
	
	@Test
	@Transactional
	public void testPaging() {
		Unit root = unitRepository.getRoot("S");
		
		Unit l1 = new Unit("L1");
		unitRepository.addChild(root, l1);
		
		Unit l11 = new Unit("L11");
		unitRepository.addChild(l1, l11);

		Unit l12 = new Unit("L12");
		unitRepository.addChild(l1, l12);

		Unit l2 = new Unit("L2");
		unitRepository.addChild(root, l2);
		
		Unit l21 = new Unit("L21");
		unitRepository.addChild(l2, l21);

		Unit l22 = new Unit("L22");
		unitRepository.addChild(l2, l22);

		Unit l23 = new Unit("L23");
		unitRepository.addChild(l2, l23);
		
		String whereClause = "";
		String orderByClause = "";
		
		int rowsCount = unitRepository.getRowsCount(whereClause).intValue();
		
		assertEquals(8, rowsCount);
		
		PageHelper pageHelper = new PageHelper(rowsCount, 3);
		pageHelper.doPaging();
		
		assertEquals(3, pageHelper.getPagesCount());
		
		pageHelper.setCurrentPage(1);
		List<Unit> page1 = unitRepository.getRows(whereClause, orderByClause, pageHelper.getStart(), pageHelper.getPageSize());
		assertEquals(3, page1.size());

		pageHelper.setCurrentPage(2);
		List<Unit> page2 = unitRepository.getRows(whereClause, orderByClause, pageHelper.getStart(), pageHelper.getPageSize());
		assertEquals(3, page2.size());

		pageHelper.setCurrentPage(3);
		List<Unit> page3 = unitRepository.getRows(whereClause, orderByClause, pageHelper.getStart(), pageHelper.getPageSize());
		assertEquals(2, page3.size());

	}
}
