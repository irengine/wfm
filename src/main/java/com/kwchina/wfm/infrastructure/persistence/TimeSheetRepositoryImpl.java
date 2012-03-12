package com.kwchina.wfm.infrastructure.persistence;

import java.util.Calendar;
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
import com.kwchina.wfm.domain.model.employee.TimeSheet;
import com.kwchina.wfm.domain.model.employee.TimeSheetRepository;
import com.kwchina.wfm.domain.model.organization.Unit;
import com.kwchina.wfm.domain.model.organization.UnitRepository;
import com.kwchina.wfm.domain.model.shift.AttendanceType;
import com.kwchina.wfm.domain.model.shift.ShiftPolicy;
import com.kwchina.wfm.domain.model.shift.ShiftPolicyFactory;
import com.kwchina.wfm.domain.model.shift.ShiftType;
import com.kwchina.wfm.domain.model.shift.ShiftTypeRepository;
import com.kwchina.wfm.infrastructure.common.DateHelper;
import com.kwchina.wfm.interfaces.organization.web.command.QueryTimeSheetByPropertyCommand;
import com.kwchina.wfm.interfaces.organization.web.command.QueryActualTimeSheetCommand;

@Repository
public class TimeSheetRepositoryImpl extends BaseRepositoryImpl<TimeSheet> implements TimeSheetRepository {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	UnitRepository unitRepository;
	
	@Autowired
	EmployeeRepository employeeRepository;
	
	@Autowired
	ShiftTypeRepository shiftTypeRepository;

	@Override
	public void generateMonthTimeSheet(String month, Unit unit) {
		// Remove old time sheet entries
		removeMonthTimeSheet(month, unit);
		
		List<Date> days = DateHelper.getDaysOfMonth(month);
		ShiftType defaultShiftType = unit.getShiftType();

		List<Employee> employees = employeeRepository.findByUnitId(unit.getId());
		for(Employee employee : employees) {
			
			ShiftType shiftType = null == employee.getShiftType() ? defaultShiftType : employee.getShiftType();
			
			ShiftPolicy shiftPolicy = ShiftPolicyFactory.getInstance(shiftTypeRepository)
									.getShiftPolicy(shiftType.getStrategyClassName(), shiftType.getStrategyClassParameters());
			
			for(Date day : days) {
				AttendanceType attendanceType = shiftPolicy.getAttendanceType(day);
				TimeSheet record = new TimeSheet(unit, employee, day, attendanceType.getBeginTime(), attendanceType.getEndTime(), attendanceType, TimeSheet.ActionType.MONTH_PLAN);
				entityManager.persist(record);
			}
		}
		
		entityManager.flush();
	}
	
	private void removeMonthTimeSheet(String month, Unit unit) {
		
		// Get first and last day for month
		Date date = DateHelper.getDate(month);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		Date beginDate = calendar.getTime();
		calendar.add(Calendar.MONTH, 1);
		Date endDate = calendar.getTime();
		
		entityManager.createQuery("delete from TimeSheet ts where ts.unit.id = :unitId " +
				"and ts.date >= :beginDate and ts.date < :endDate and " +
				"ts.actionType <= :actionType")
				.setParameter("unitId", unit.getId())
				.setParameter("beginDate", beginDate)
				.setParameter("endDate", endDate)
				.setParameter("actionType", TimeSheet.ActionType.MONTH_PLAN_ADJUST)
				.executeUpdate();

		entityManager.flush();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<TimeSheet> getMonthTimeSheet(String month, Unit unit, TimeSheet.ActionType actionType) {
		Date date = DateHelper.getDate(month);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		Date beginDate = calendar.getTime();
		calendar.add(Calendar.MONTH, 1);
		Date endDate = calendar.getTime();
		
		List<TimeSheet> ts = entityManager.createQuery("select ts from TimeSheet ts, Unit u " +
							"where u.id = :unitId and u.left <= ts.unit.left and u.right >= ts.unit.right and " +
							"ts.date >= :beginDate and ts.date < :endDate and ts.enable = true and " +
							"((ts.lastActionType = null and ts.actionType <= :actionType) or ts.lastActionType > :actionType) " +
							"order by ts.employee.employeeId, ts.date, ts.actionType, ts.updatedAt")
				.setParameter("unitId", unit.getId())
				.setParameter("beginDate", beginDate)
				.setParameter("endDate", endDate)
				.setParameter("actionType", actionType)
				.getResultList();
		
		return ts;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<TimeSheet> getDayTimeSheet(String day, Unit unit, TimeSheet.ActionType actionType) {
		Date date = DateHelper.getDate(day);
		
		List<TimeSheet> ts = entityManager.createQuery("select ts from TimeSheet ts, Unit u " +
							"where u.id = :unitId and u.left <= ts.unit.left and u.right >= ts.unit.right and " +
							"ts.date = :date and ts.enable = true and " +
							"((ts.lastActionType = null and ts.actionType <= :actionType) or ts.lastActionType > :actionType) " +
							"order by ts.employee.employeeId, ts.date, ts.actionType, ts.updatedAt")
				.setParameter("unitId", unit.getId())
				.setParameter("date", date)
				.setParameter("actionType", actionType)
				.getResultList();
		
		return ts;
	}
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public List<Map<String, Object>> queryActualTimeSheet(QueryActualTimeSheetCommand command) {
		Long leftId = null;
		Long rightId = null;
		if (!(null == command.getUnitId() || command.getUnitId().equals(0))) {
			Unit unit = unitRepository.findById(command.getUnitId());
			leftId = unit.getLeft();
			rightId = unit.getRight();
		}
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(command.toSQL(leftId, rightId));
		
		return rows;
	}

	@Override
	public List<Map<String, Object>> queryTimeSheetByProperty(QueryTimeSheetByPropertyCommand command) {
		Long leftId = null;
		Long rightId = null;
		if (!(null == command.getUnitId() || command.getUnitId().equals(0))) {
			Unit unit = unitRepository.findById(command.getUnitId());
			leftId = unit.getLeft();
			rightId = unit.getRight();
		}
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(command.toSQL(leftId, rightId));
		
		return rows;
	}

	@Override
	public void disable(TimeSheet ts) {
		
		ts.setEnable(false);

		entityManager.persist(ts);
		entityManager.flush();
	}
}
