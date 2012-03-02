package com.kwchina.wfm.infrastructure.persistence;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
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
		List<Date> days = DateHelper.getDaysOfMonth(month);
		ShiftType defaultShiftType = unit.getShiftType();

		List<Employee> employees = employeeRepository.findByUnitId(unit.getId());
		for(Employee employee : employees) {
			
			ShiftType shiftType = null == employee.getShiftType() ? defaultShiftType : employee.getShiftType();
			
			ShiftPolicy shiftPolicy = ShiftPolicyFactory.getInstance(shiftTypeRepository)
									.getShiftPolicy(shiftType.getStrategyClassName(), shiftType.getStrategyClassParameters());
			
			for(Date day : days) {
				AttendanceType attendanceType = shiftPolicy.getAttendanceType(day);
				TimeSheet record = new TimeSheet(unit, employee, day, attendanceType.getBeginTime(), attendanceType.getBeginTime(), attendanceType, TimeSheet.ActionType.MONTH_PLAN);
				entityManager.persist(record);
			}
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
		
		List<TimeSheet> ts = entityManager.createQuery("from TimeSheet ts where ts.unit.id = :unitId and ts.date >= :beginDate and ts.date < :endDate order by ts.employee.employeeId, ts.date")
				.setParameter("unitId", unit.getId())
				.setParameter("beginDate", beginDate)
				.setParameter("endDate", endDate)
				.getResultList();
		return ts;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<TimeSheet> getDayTimeSheet(String day, Unit unit) {
		Date date = DateHelper.getDate(day);
		
		List<TimeSheet> ts = entityManager.createQuery("from TimeSheet ts where ts.unit.id = :unitId and ts.date = :date order by ts.employee.employeeId, ts.date")
				.setParameter("unitId", unit.getId())
				.setParameter("beginDate", date)
				.getResultList();
		return ts;
	}
}