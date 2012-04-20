package com.kwchina.wfm.infrastructure.persistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.kwchina.wfm.domain.model.employee.Employee;
import com.kwchina.wfm.domain.model.employee.EmployeeRepository;
import com.kwchina.wfm.domain.model.organization.Unit;
import com.kwchina.wfm.domain.model.organization.UnitRepository;
import com.kwchina.wfm.infrastructure.common.DateHelper;
import com.kwchina.wfm.interfaces.organization.web.command.QueryVacationCommand;

@Repository
public class EmployeeRepositoryImpl extends BaseRepositoryImpl<Employee> implements EmployeeRepository {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	UnitRepository unitRepository;
	
	@Override
	public void disable(Employee employee) {
	
		employee.setEnable(false);

		entityManager.persist(employee);
		entityManager.flush();
	}
	
	@Override
	public Employee findByCode(String code) {
		Employee employee = (Employee) entityManager.createNamedQuery("employee.findByCode")
								.setParameter("code", code)
								.getSingleResult();
		return employee;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Employee> findByUnitId(Long unitId) {
		List<Employee> employees = entityManager.createNamedQuery("employee.findByUnitId")
								.setParameter("unitId", unitId)
								.getResultList();
		return employees;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Employee> findAll() {
		List<Employee> employees = entityManager.createNamedQuery("employee.findAll")
								.getResultList();
		return employees;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Employee> findAllByUnitId(Long unitId) {
		List<Employee> employees = entityManager.createNamedQuery("employee.findAllByUnitId")
								.setParameter("unitId", unitId)
								.getResultList();
		return employees;
	}
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public void calculateVacation(Date currentMonth, Long vacationId) {
		// delete current vacations
		String deleteSql = String.format("delete from t_employee_vacations where month = '%s' and type = 'ANNUAL_LEAVE' ", DateHelper.getString(currentMonth));

		// create current vacations
		String createSql = String.format("insert into t_employee_vacations(employeeId, type, month, lastBalance, balance, amount)" +
				"	select e.Id, 'ANNUAL_LEAVE', '%s', " +
				"ifnull(case when month('%s') = 1 then lastBalance + balance - amount when month('%s') > 3 then 0 else lastbalance end, 0), " +
				"ifnull(case when month('%s') = 1 then getVacationDays(e.beginDateOfWork, '%s') else v.balance end, 0) as balance, " +
				"0 as amount " +
				"from t_employees e left join (select * from t_employee_vacations where month = date_sub('%s', INTERVAL 1 MONTH)) v on e.Id = v.employeeId ", 
				DateHelper.getString(currentMonth), 
				DateHelper.getString(currentMonth), 
				DateHelper.getString(currentMonth), 
				DateHelper.getString(currentMonth), 
				DateHelper.getString(currentMonth), 
				DateHelper.getString(currentMonth));
		
		// update current vacations amount
		String updateSql = String.format("update t_employee_vacations v inner join " +
				"(select employeeId, count(ts.attendanceTypeId) as cnt from t_timesheet ts " +
				"where ts.enable = true and ts.lastActionType is null and ts.attendanceTypeId = %d " +
				"and ts.date >= '%s' and ts.date < date_add('%s', INTERVAL 1 MONTH) " +
				"group by employeeId, attendanceTypeId) ts on v.employeeId = ts.employeeId " +
				"set v.amount = ts.cnt " +
				"where v.month = '%s' ",
				0, 
				DateHelper.getString(currentMonth), 
				DateHelper.getString(currentMonth), 
				DateHelper.getString(currentMonth));
		
		jdbcTemplate.batchUpdate(new String [] {deleteSql, createSql, updateSql});
	}
	
	@Override
	public List<Map<String, Object>> queryVacation(QueryVacationCommand command) {
		
		String[] unitIds = command.getUnitIds().split(",");
		
		if (0 == unitIds.length) {
			command.setUnitId(null);
			List<Map<String, Object>> rows = jdbcTemplate.queryForList(command.toSQL(null, null));
			
			return rows;
		}
		else {
			List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
			for (String unitId : unitIds) {
				command.setUnitId(Long.parseLong(unitId));
				
				Long leftId = null;
				Long rightId = null;
				if (!(null == command.getUnitId() || command.getUnitId().equals(0))) {
					Unit unit = unitRepository.findById(command.getUnitId());
					leftId = unit.getLeft();
					rightId = unit.getRight();
				}
				
				rows.addAll(jdbcTemplate.queryForList(command.toSQL(leftId, rightId)));
			}
			
			return rows;
		}
	}
}
