package com.kwchina.wfm.infrastructure.persistence;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.kwchina.wfm.domain.model.shift.WorkOrder;
import com.kwchina.wfm.domain.model.shift.WorkOrderRepository;
import com.kwchina.wfm.infrastructure.common.DateHelper;

@Repository
public class WorkOrderRepositoryImpl extends BaseRepositoryImpl<WorkOrder> implements WorkOrderRepository {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public int removeAll(Date date) {
		int count = entityManager.createQuery("delete from WorkOrder o where o.date = :date")
					.setParameter("date", DateHelper.getString(date))
					.executeUpdate();
		
		return count;
	}
}
