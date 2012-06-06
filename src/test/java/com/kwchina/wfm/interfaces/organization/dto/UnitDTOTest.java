package com.kwchina.wfm.interfaces.organization.dto;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.kwchina.wfm.domain.model.organization.Unit;
import com.kwchina.wfm.domain.model.organization.UnitRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/context-test.xml"})
public class UnitDTOTest {
	
	@Autowired
	private UnitRepository unitRepository;

	@Test
	public void testSerializeUnitDTO() throws JsonGenerationException, JsonMappingException, IOException {
		UnitDTO uo = new UnitDTO();
		uo.setId("1");
		uo.setText("Shanghai");
		
		StringWriter sw = new StringWriter();

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.writeValue(sw, uo);
		System.out.println(sw.toString());

		assertTrue(0 != sw.toString().length());
	}
	
	@Test
	@Transactional
	public void testSerializeUnitDTOs() throws JsonGenerationException, JsonMappingException, IOException
	{
		Unit root = unitRepository.findRoot();
		if (null == root) {
			buildTree();
			root = unitRepository.findRoot();
		}
		
		UnitDTO uo = new UnitDTO(root);
		List<UnitDTO> uos = new ArrayList<UnitDTO>();
		uos.add(uo); 
		
		StringWriter sw = new StringWriter();

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.writeValue(sw, uos);
		System.out.println(sw.toString());

		assertTrue(0 != sw.toString().length());
		
	}
	
	private void buildTree()
	{
		Unit root = unitRepository.getRoot("S");
		assertEquals("S", root.getUriName());
		
		Unit l1 = new Unit("L1");
		unitRepository.addChild(root, l1);
		assertEquals("S-L1", l1.getUriName());
		
		Unit l11 = new Unit("L11");
		unitRepository.addChild(l1, l11);
		assertEquals("S-L1-L11", l11.getUriName());

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
	}

}
