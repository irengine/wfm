package com.kwchina.wfm.infrastructure.persistence;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "DUMMYOBJECT")
public class DummyObject {

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;
	
	@Column(name = "UUID", unique = true)
	String uniqueId;

	public DummyObject() {
	
	}

	public DummyObject(String uuid) {
		this.uniqueId = uuid;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
