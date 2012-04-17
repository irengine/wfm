package com.kwchina.wfm.infrastructure.persistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import com.kwchina.wfm.domain.model.shift.AttendanceTypeRepository;
import com.kwchina.wfm.domain.model.shift.ShiftPolicy;
import com.kwchina.wfm.domain.model.shift.ShiftPolicyFactory;
import com.kwchina.wfm.domain.model.shift.ShiftType;
import com.kwchina.wfm.domain.model.shift.ShiftTypeRepository;
import com.kwchina.wfm.domain.model.shift.WorkOrder;
import com.kwchina.wfm.infrastructure.common.DateHelper;
import com.kwchina.wfm.interfaces.common.ReportHelper;
import com.kwchina.wfm.interfaces.organization.web.command.ArchiveTimeSheetCommand;
import com.kwchina.wfm.interfaces.organization.web.command.QueryActualTimeSheetCommand;
import com.kwchina.wfm.interfaces.organization.web.command.QueryTimeSheetByPropertyCommand;

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
	
	@Autowired
	AttendanceTypeRepository attendanceTypeRepository;

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
		
		Date beginDate = DateHelper.getBeginDateOfMonth(month);
		Date endDate = DateHelper.getEndDateOfMonth(month);
		
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
	
	public void importWorkOrder(WorkOrder order) {
		
		Map<String, AttendanceType> attendanceTypes = new HashMap<String, AttendanceType>();
		
		attendanceTypes.put("0", attendanceTypeRepository.findByName(ReportHelper.REPORT_COLUMN_IMPORT_0));
		attendanceTypes.put("1", attendanceTypeRepository.findByName(ReportHelper.REPORT_COLUMN_IMPORT_1));
		attendanceTypes.put("2", attendanceTypeRepository.findByName(ReportHelper.REPORT_COLUMN_IMPORT_2));
		attendanceTypes.put("3", attendanceTypeRepository.findByName(ReportHelper.REPORT_COLUMN_IMPORT_3));
		
		Employee employee = employeeRepository.findByCode(order.getEmployeeCode());
		AttendanceType attendanceType = attendanceTypes.get(order.getShiftCode());
		
		// delete
		entityManager.createQuery("delete from TimeSheet ts where ts.employee = :employee " +
				"and ts.date = :date ")
				.setParameter("employee", employee)
				.setParameter("date", DateHelper.getDate(order.getDate()))
				.executeUpdate();
		// insert
		TimeSheet record = new TimeSheet(employee.getJob().getUnit(), employee, DateHelper.getDate(order.getDate()), attendanceType.getBeginTime(), attendanceType.getEndTime(), attendanceType, TimeSheet.ActionType.IMPORT);
		entityManager.persist(record);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<TimeSheet> getMonthTimeSheet(String month, Unit unit, TimeSheet.ActionType actionType) {
		
		Date beginDate = DateHelper.getBeginDateOfMonth(month);
		Date endDate = DateHelper.getEndDateOfMonth(month);
		
		List<TimeSheet> ts = entityManager.createQuery("select ts from TimeSheet ts, Unit u " +
							"where u.id = :unitId and u.left <= ts.unit.left and u.right >= ts.unit.right and " +
							"ts.date >= :beginDate and ts.date < :endDate and ts.enable = true and " +
							"ts.actionType <= :actionType and (ts.lastActionType = null or ts.lastActionType > :actionType) " +
//							"((ts.lastActionType = null and ts.actionType <= :actionType) or ts.lastActionType > :actionType) " +
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
	public List<TimeSheet> getActualMonthTimeSheet(String month, Unit unit) {
		
		Date beginDate = DateHelper.getBeginDateOfMonth(month);
		Date endDate = DateHelper.getEndDateOfMonth(month);
		
		List<TimeSheet> ts = entityManager.createQuery("select ts from TimeSheet ts, Unit u " +
							"where u.id = :unitId and u.left <= ts.unit.left and u.right >= ts.unit.right and " +
							"ts.date >= :beginDate and ts.date < :endDate and ts.enable = true and " +
							"ts.lastActionType = null " +
							"order by ts.employee.employeeId, ts.date, ts.actionType, ts.updatedAt")
				.setParameter("unitId", unit.getId())
				.setParameter("beginDate", beginDate)
				.setParameter("endDate", endDate)
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
							"ts.actionType <= :actionType and (ts.lastActionType = null or ts.lastActionType > :actionType) " +
//							"((ts.lastActionType = null and ts.actionType <= :actionType) or ts.lastActionType > :actionType) " +
							"order by ts.employee.employeeId, ts.date, ts.actionType, ts.updatedAt")
				.setParameter("unitId", unit.getId())
				.setParameter("date", date)
				.setParameter("actionType", actionType)
				.getResultList();
		
		return ts;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<TimeSheet> getEmployeeDayTimeSheet(Date date, Employee employee, TimeSheet.ActionType actionType) {
		
		List<TimeSheet> ts = entityManager.createQuery("select ts from TimeSheet ts " +
							"where ts.employee = :employee and " +
							"ts.date = :date and ts.enable = true and " +
							"ts.actionType <= :actionType and (ts.lastActionType = null or ts.lastActionType > :actionType) " +
//							"((ts.lastActionType = null and ts.actionType <= :actionType) or ts.lastActionType > :actionType) " +
							"order by ts.date, ts.actionType")
				.setParameter("employee", employee)
				.setParameter("date", date)
				.setParameter("actionType", actionType)
				.getResultList();
		
		return ts;
	}
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public List<Map<String, Object>> queryActualTimeSheet(QueryActualTimeSheetCommand command) {
		
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

	@Override
	public List<Map<String, Object>> queryTimeSheetByProperty(QueryTimeSheetByPropertyCommand command) {
		
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

	@Override
	public void disable(TimeSheet ts) {
		
		ts.setEnable(false);

		entityManager.persist(ts);
		entityManager.flush();
	}
	
	@Override
	public void archive(ArchiveTimeSheetCommand command) {
		String archiveSQL = "select * into t_timesheet_archive from t_timesheet where date >= '%s' and date < '%s'\n"
							+ "delete from t_timesheet where date >= '%s' and date < '%s'";
		
		entityManager.createNativeQuery(String.format(archiveSQL, 
				command.getBeginTime(), 
				command.getEndTime(), 
				command.getBeginTime(), 
				command.getEndTime()))
				.executeUpdate();
		
	}
}
