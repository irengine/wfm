package com.kwchina.wfm.domain.model.shift;

import java.util.HashMap;
import java.util.Map;

public class ShiftPolicyFactory {
	
	private static ShiftPolicyFactory factory = null;

	private Map<String, ShiftPolicy> policyMap = new HashMap<String, ShiftPolicy>();
	private ShiftTypeRepository shiftTypeRepository;
	
	private ShiftPolicyFactory(ShiftTypeRepository shiftTypeRepository) {
		this.shiftTypeRepository = shiftTypeRepository;
	}
	
	public static ShiftPolicyFactory getInstance(ShiftTypeRepository shiftTypeRepository) {
		if (null == factory)
			factory = new ShiftPolicyFactory(shiftTypeRepository);
		
		return factory;
	}

	public ShiftPolicy getShiftPolicy(String policyType, String policyParameters) {
		
		String key = String.format("%s;%s", policyType, policyParameters);
		if (!policyMap.containsKey(key)) {
			ShiftPolicy shiftPolicy;
			if (policyType.equals("CustomShiftPolicy"))
				shiftPolicy = shiftTypeRepository.getCustomShiftPolicy(policyParameters);
			else
				shiftPolicy = shiftTypeRepository.getDailyShiftPolicy(policyParameters);
			policyMap.put(key, shiftPolicy);
		}
		return policyMap.get(key);
	}
}
