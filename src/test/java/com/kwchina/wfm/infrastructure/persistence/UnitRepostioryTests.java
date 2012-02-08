package com.kwchina.wfm.infrastructure.persistence;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.kwchina.wfm.domain.model.organization.ShiftType;
import com.kwchina.wfm.domain.model.organization.Unit;
import com.kwchina.wfm.domain.model.organization.UnitRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/context-test.xml"})
public class UnitRepostioryTests {

	@Autowired
	private UnitRepository unitRepository;
	
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

		unitRepository.printTree(root);
		
		assertEquals(7, unitRepository.findAllChildren(root).size());
		
		assertEquals(0, unitRepository.findAllAncestor(root).size());
		assertEquals(1, unitRepository.findAllAncestor(l2).size());
		assertEquals(2, unitRepository.findAllAncestor(l21).size());
	}
	
	ShiftType dayShift;
	ShiftType nightShift;
	ShiftType fouthShift;
	private void initialShiftType() {
		dayShift = new ShiftType();
		nightShift = new ShiftType();
		fouthShift = new ShiftType();
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
	
}
