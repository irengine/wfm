package com.kwchina.wfm.interfaces.organization.facade;

import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kwchina.wfm.domain.model.organization.User;
import com.kwchina.wfm.domain.model.organization.UserRepository;
import com.kwchina.wfm.interfaces.common.Page;
import com.kwchina.wfm.interfaces.common.PageHelper;
import com.kwchina.wfm.interfaces.common.QueryHelper;

@Component
public class UserServiceFacadeImpl implements UserServiceFacade {

	@Autowired
	UserRepository userRepository;
	
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveUser(User user) {
		userRepository.save(user);
	}
	
	@Transactional(propagation=Propagation.SUPPORTS)
	public User findById(Long id) {
		return userRepository.findById(id);
	}
	
	@Transactional(propagation=Propagation.SUPPORTS)
	public String queryUsersWithJson(Map<String, String> parameters, int currentPage, int pageSize, List<String> conditions) {
	
		String whereClause = "";
		String orderByClause = String.format(" ORDER BY %s %s ", parameters.get(QueryHelper.SORT_FIELD), parameters.get(QueryHelper.SORT_DIRECTION));
		
		if (Boolean.parseBoolean(parameters.get(QueryHelper.IS_INCLUDE_CONDITION))) {
			whereClause = QueryHelper.getWhereClause(parameters.get(QueryHelper.FILTERS), conditions);
		}
		else {
			whereClause = QueryHelper.getWhereClause("", conditions);
		}
		
		int rowsCount = userRepository.getRowsCount(whereClause).intValue();
		
		PageHelper pageHelper = new PageHelper(rowsCount, pageSize);
		pageHelper.setCurrentPage(currentPage);
		
		List<User> rows = userRepository.getRows(whereClause, orderByClause, pageHelper.getStart(), pageHelper.getPageSize());
		
		Page page = new Page(pageHelper.getCurrentPage(), pageHelper.getPagesCount(), rowsCount, rows);
		
		StringWriter sw = new StringWriter();

		try {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.writeValue(sw, page);
		}
		catch(Exception e) {
			
		}
		
		return sw.toString();

	}

}
