package com.kwchina.wfm.domain.model.employee;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.kwchina.wfm.domain.common.ValueObject;

@Embeddable
public class Vacation {

	@Column(nullable=false)
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(iso=ISO.DATE)
	private Date month;
	
	@Column(nullable=false)
	@Enumerated(EnumType.STRING)
	private Type type;
	
	@Column(nullable = false, precision=5, scale=1)
	private double lastBalance;
	
	@Column(nullable = false, precision=5, scale=1)
	private double balance;
	
	@Column(nullable = false, precision=5, scale=1)
	private double amount;
	
	public enum Type implements ValueObject<Type> {
		ANNUAL_LEAVE,
		OVERTIME;

		@Override
		public boolean sameValueAs(Type other) {
			return this.equals(other);
		}
	}
	
	public Vacation() {
		this.type = Type.ANNUAL_LEAVE;
	}

	public Date getMonth() {
		return month;
	}

	public void setMonth(Date month) {
		this.month = month;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public double getLastBalance() {
		return lastBalance;
	}

	public void setLastBalance(double lastBalance) {
		this.lastBalance = lastBalance;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	
}
