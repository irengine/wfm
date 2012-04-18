package com.kwchina.wfm.domain.model.system;

import java.util.List;

import com.kwchina.wfm.domain.common.BaseRepository;
import com.kwchina.wfm.domain.model.system.SystemAction.ScopeType;

public interface SystemActionRepository extends BaseRepository<SystemAction> {

	void addAction(SystemAction.ScopeType scope, String key, String type, String message);
	void removeAction(SystemAction.ScopeType scope, String key);
	List<SystemAction> getActions(SystemAction.ScopeType scope);
	List<SystemAction> getActions(ScopeType scope, String key);
}
