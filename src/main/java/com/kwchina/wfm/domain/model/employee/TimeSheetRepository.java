package com.kwchina.wfm.domain.model.employee;

import java.util.List;

import com.kwchina.wfm.domain.common.BaseRepository;
import com.kwchina.wfm.domain.model.organization.Unit;

public interface TimeSheetRepository extends BaseRepository<TimeSheet> {

	void generateMonthTimeSheet(String month, Unit unit);

	List<TimeSheet> getMonthTimeSheet(String month, Unit unit);

	List<TimeSheet> getDayTimeSheet(String day, Unit unit);

	void disable(TimeSheet ts);

}
