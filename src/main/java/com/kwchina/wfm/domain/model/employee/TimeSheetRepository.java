package com.kwchina.wfm.domain.model.employee;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.kwchina.wfm.domain.common.BaseRepository;
import com.kwchina.wfm.domain.model.employee.TimeSheet.ActionType;
import com.kwchina.wfm.domain.model.organization.Unit;
import com.kwchina.wfm.domain.model.shift.WorkOrder;
import com.kwchina.wfm.interfaces.organization.web.command.ArchiveTimeSheetCommand;
import com.kwchina.wfm.interfaces.organization.web.command.QueryTimeSheetByPropertyCommand;
import com.kwchina.wfm.interfaces.organization.web.command.QueryActualTimeSheetCommand;

public interface TimeSheetRepository extends BaseRepository<TimeSheet> {

	void generateMonthTimeSheet(String month);
//	void generateMonthTimeSheet(String month, Unit unit);

	List<TimeSheet> getMonthTimeSheet(String month, Unit unit, TimeSheet.ActionType actionType);
	List<TimeSheet> getDayTimeSheet(String day, Unit unit, ActionType actionType);
	List<TimeSheet> getActualMonthTimeSheet(String month, Unit unit);

	List<Map<String, Object>> queryActualTimeSheet(QueryActualTimeSheetCommand command);
	List<Map<String, Object>> queryTimeSheetByProperty(QueryTimeSheetByPropertyCommand command);

	void disable(TimeSheet ts);

	void archive(ArchiveTimeSheetCommand command);

	void importWorkOrder(WorkOrder workOrder);

	List<TimeSheet> getEmployeeDayTimeSheet(Date date, Employee employee, ActionType actionType);
}
