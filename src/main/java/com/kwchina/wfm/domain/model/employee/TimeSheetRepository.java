package com.kwchina.wfm.domain.model.employee;

import java.util.List;
import java.util.Map;

import com.kwchina.wfm.domain.common.BaseRepository;
import com.kwchina.wfm.domain.model.organization.Unit;
import com.kwchina.wfm.interfaces.organization.web.command.QueryActualTimeSheetCommand;

public interface TimeSheetRepository extends BaseRepository<TimeSheet> {

	void generateMonthTimeSheet(String month, Unit unit);

	List<TimeSheet> getMonthTimeSheet(String month, Unit unit, TimeSheet.ActionType actionType);

	List<TimeSheet> getDayTimeSheet(String day, Unit unit);

	void disable(TimeSheet ts);

	List<Map<String, Object>> queryActualTimeSheet(QueryActualTimeSheetCommand command);

}
