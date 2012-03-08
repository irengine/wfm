package com.kwchina.wfm.interfaces.organization.facade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.kwchina.wfm.domain.model.organization.Unit;
import com.kwchina.wfm.domain.model.organization.UnitRepository;
import com.kwchina.wfm.domain.model.organization.User;
import com.kwchina.wfm.domain.model.organization.UserRepository;
import com.kwchina.wfm.interfaces.common.JacksonHelper;
import com.kwchina.wfm.interfaces.common.Page;
import com.kwchina.wfm.interfaces.common.PageHelper;
import com.kwchina.wfm.interfaces.common.QueryHelper;
import com.kwchina.wfm.interfaces.organization.web.command.ActionCommand;
import com.kwchina.wfm.interfaces.organization.web.command.QueryCommand;
import com.kwchina.wfm.interfaces.organization.web.command.SaveUserCommand;

@Component
public class UserServiceFacadeImpl implements UserServiceFacade {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	UnitRepository unitRepository;
	
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveUser(SaveUserCommand command) {
		
		if (command.getCommandType().equals(ActionCommand.ADD)) {
			User user;
			
			if (null == command.getId() || command.getId().equals(0))
				user = new User();
			else
				user = userRepository.findById(command.getId());
			
			user.setCode(command.getCode());
			user.setName(command.getName());
			user.setPassword(command.getPassword());
			user.setEmail(command.getEmail());
			
			Collection<Unit> units = new LinkedHashSet<Unit>();
			String[] us = StringUtils.split(command.getUnitIds(), ",");
			for(String u: us) {
				Long unitId = Long.parseLong(u);
				Unit unit = unitRepository.findById(unitId);
				units.add(unit);
			}
			
			user.setUnits(units);
			
			Set<String> roles = new LinkedHashSet<String>();
			String[] rs = StringUtils.split(command.getRoleIds(), ",");
			for(String r: rs) {
				roles.add(r);
			}
			
			user.setRoles(roles);
			
			userRepository.save(user);
		}
		else if (command.getCommandType().equals(ActionCommand.DELETE)) {
			String[] ids = StringUtils.split(command.getIds(), ActionCommand.ID_SEPARATOR);
			for (String id : ids) {
				User user = userRepository.findById(Long.parseLong(id));
				userRepository.disable(user);
			}
		}
	}
	
	@Transactional(propagation=Propagation.SUPPORTS)
	public User findById(Long id) {
		return userRepository.findById(id);
	}
	
	@Transactional(propagation=Propagation.SUPPORTS)
	public String queryUsersWithJson(QueryCommand command) {
		List<String> conditions = new ArrayList<String>();
		String whereClause = "";
		String orderByClause = String.format(" ORDER BY %s %s ", command.getSidx(), command.getSord());
		
		if (Boolean.parseBoolean(command.getSearch())) {
			whereClause = QueryHelper.getWhereClause(command.getFilters(), conditions);
		}
		else {
			whereClause = QueryHelper.getWhereClause("", conditions);
		}
		
		int rowsCount = userRepository.getRowsCount(whereClause).intValue();
		
		PageHelper pageHelper = new PageHelper(rowsCount, command.getRows());
		pageHelper.setCurrentPage(command.getPage());
		
		List<User> rows = userRepository.getRows(whereClause, orderByClause, pageHelper.getStart(), pageHelper.getPageSize());
		Page page = new Page(pageHelper.getCurrentPage(), pageHelper.getPagesCount(), rowsCount, rows);
		
		return JacksonHelper.getUserJsonWithFilters(page);
	}

}
