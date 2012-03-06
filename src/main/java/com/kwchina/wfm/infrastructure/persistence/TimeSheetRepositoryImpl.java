package com.kwchina.wfm.infrastructure.persistence;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.kwchina.wfm.domain.model.shift.AttendanceType;
import com.kwchina.wfm.domain.model.shift.ShiftPolicy;
import com.kwchina.wfm.domain.model.shift.ShiftPolicyFactory;
import com.kwchina.wfm.domain.model.shift.ShiftType;
import com.kwchina.wfm.domain.model.shift.ShiftTypeRepository;
import com.kwchina.wfm.infrastructure.common.DateHelper;
import com.kwchina.wfm.interfaces.organization.web.command.QueryActualTimeSheetCommand;

@Repository
public class TimeSheetRepositoryImpl extends BaseRepositoryImpl<TimeSheet> implements TimeSheetRepository {
	
	@PersistenceContext
	private EntityManager entityManager;
	
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
	
	@SuppressWarnings("unchecked")
	private void removeMonthTimeSheet(String month, Unit unit) {
		
		// TODO: refactory using native sql
		Date date = DateHelper.getDate(month);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		Date beginDate = calendar.getTime();
		calendar.add(Calendar.MONTH, 1);
		Date endDate = calendar.getTime();
		
		List<TimeSheet> ts = entityManager.createQuery("select ts from TimeSheet ts, Unit u where u.id = :unitId and u.left <= ts.unit.left and u.right >= ts.unit.right and ts.date >= :beginDate and ts.date < :endDate and ts.actionType <= :actionType order by ts.employee.employeeId desc, ts.date desc, ts.actionType desc, ts.updatedAt desc")
				.setParameter("unitId", unit.getId())
				.setParameter("beginDate", beginDate)
				.setParameter("endDate", endDate)
				.setParameter("actionType", TimeSheet.ActionType.MONTH_PLAN_ADJUST)
				.getResultList();

		for(TimeSheet t : ts) {
			entityManager.remove(t);
		}
		
		entityManager.flush();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<TimeSheet> getMonthTimeSheet(String month, Unit unit) {
		Date date = DateHelper.getDate(month);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		Date beginDate = calendar.getTime();
		calendar.add(Calendar.MONTH, 1);
		Date endDate = calendar.getTime();
		
		List<TimeSheet> ts = entityManager.createQuery("select ts from TimeSheet ts, Unit u where u.id = :unitId and u.left <= ts.unit.left and u.right >= ts.unit.right and ts.date >= :beginDate and ts.date < :endDate and ts.actionType <= :actionType order by ts.employee.employeeId, ts.date, ts.actionType, ts.updatedAt")
				.setParameter("unitId", unit.getId())
				.setParameter("beginDate", beginDate)
				.setParameter("endDate", endDate)
				.setParameter("actionType", TimeSheet.ActionType.MONTH_PLAN_ADJUST)
				.getResultList();

		Set<TimeSheet> rts = new HashSet<TimeSheet>();
		for(TimeSheet t : ts) {
			if (t.isEnable())
				rts.add(t);
			if (null != t.getReferTo())
				rts.remove(t.getReferTo());
		}
		
		return new ArrayList<TimeSheet>(rts);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<TimeSheet> getDayTimeSheet(String day, Unit unit) {
		Date date = DateHelper.getDate(day);
		
		List<TimeSheet> ts = entityManager.createQuery("select ts from TimeSheet ts, Unit u where u.id = :unitId and u.left <= ts.unit.left and u.right >= ts.unit.right and ts.date = :date and ts.actionType <= :actionType order by ts.employee.employeeId, ts.date, ts.actionType, ts.updatedAt")
				.setParameter("unitId", unit.getId())
				.setParameter("date", date)
				.setParameter("actionType", TimeSheet.ActionType.DAY_PLAN_ADJUST)
				.getResultList();
		
		Set<TimeSheet> rts = new HashSet<TimeSheet>();
		for(TimeSheet t : ts) {
			if (t.isEnable())
				rts.add(t);
			if (null != t.getReferTo())
				rts.remove(t.getReferTo());
		}
		
		return new ArrayList<TimeSheet>(rts);
	}
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryActualTimeSheet(QueryActualTimeSheetCommand command) {
		
		//TODO: refactory sql
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(command.toSQL());
//		
//		List<TimeSheet> ts = entityManager.createNativeQuery(command.toSQL())
//				.getResultList();
//		
//		Set<TimeSheet> rts = new HashSet<TimeSheet>();
//		for(TimeSheet t : ts) {
//			if (t.isEnable())
//				rts.add(t);
//			if (null != t.getReferTo())
//				rts.remove(t.getReferTo());
//		}
		
		return rows;
	}

	@Override
	public void disable(TimeSheet ts) {
		
		ts.setEnable(false);

		entityManager.persist(ts);
		entityManager.flush();
	}
}
