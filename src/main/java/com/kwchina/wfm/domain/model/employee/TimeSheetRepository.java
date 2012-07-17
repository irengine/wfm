package com.kwchina.wfm.domain.model.employee;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.kwchina.wfm.domain.common.BaseRepository;
import com.kwchina.wfm.domain.model.employee.TimeSheet.ActionType;
import com.kwchina.wfm.domain.model.shift.WorkOrder;
import com.kwchina.wfm.interfaces.organization.web.command.ArchiveTimeSheetCommand;
import com.kwchina.wfm.interfaces.organization.web.command.QueryActualTimeSheetCommand;
import com.kwchina.wfm.interfaces.organization.web.command.QueryTimeSheetByPropertyCommand;

public interface TimeSheetRepository extends BaseRepository<TimeSheet> {

	void generateDayTimeSheet(String day);
	void generateMonthTimeSheet(String month);
//	void generateMonthTimeSheet(String month, Unit unit);

	List<TimeSheet> getMonthTimeSheet(String month, List<Long> unitIds, TimeSheet.ActionType actionType);
	List<TimeSheet> getMonthTimeSheet(String month, String employeeName, ActionType actionType);

	List<TimeSheet> getDayTimeSheet(String day, List<Long> unitIds, ActionType actionType);
	
	List<TimeSheet> getActualMonthTimeSheet(String month,  List<Long> unitIds);

	List<Map<String, Object>> queryActualTimeSheet(QueryActualTimeSheetCommand command);
	List<Map<String, Object>> queryTimeSheetByProperty(QueryTimeSheetByPropertyCommand command);

	void disable(TimeSheet ts);

	void archive(ArchiveTimeSheetCommand command);

	void importWorkOrder(WorkOrder workOrder);

	List<TimeSheet> getEmployeeDayTimeSheet(Date date, Employee employee, ActionType actionType);
}
