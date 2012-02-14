package com.kwchina.wfm.domain.model.organization;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

import com.kwchina.wfm.infrastructure.common.SecurityHelper;

@Entity
@Table(name="T_USERS")
public class User implements com.kwchina.wfm.domain.common.Entity<User> {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@Column(nullable=false)
	private String code;
	
	@Column(nullable=false)
	private String name;
	
	@Transient
	private String password;

	@Column(nullable=false)
	private String encryptedPassword;

	@Column
	private String email;
	
	@Column(nullable=false)
	private boolean enable;
	
	public User() {
		this.setEnable(true);
	}
	
	public User(String code, String name, String password, String email) {
		this.setCode(code);
		this.setName(name);
		this.setPassword(password);
		this.setEmail(email);
		this.setEnable(true);
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
		if (!StringUtils.isEmpty(password))
			this.encryptedPassword = SecurityHelper.encrypt(password);
	}

	/**
	 * @return the encryptedPassword
	 */
	public String getEncryptedPassword() {
		return encryptedPassword;
	}

	/**
	 * @param encryptedPassword the encryptedPassword to set
	 */
	public void setEncryptedPassword(String encryptedPassword) {
		this.encryptedPassword = encryptedPassword;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the enable
	 */
	public boolean isEnable() {
		return enable;
	}

	/**
	 * @param enable the enable to set
	 */
	public void setEnable(boolean enable) {
		this.enable = enable;
	}
	
	public boolean validatePassword(String pwd) {
		return this.encryptedPassword.equals(SecurityHelper.encrypt(pwd));
	}

	@Override
	public boolean sameIdentityAs(final User other) {
		return other != null && getCode().equals(other.getCode());
	}

	@Override
	public boolean equals(final Object object) {
		if (this == object)
			return true;
		if (object == null || getClass() != object.getClass())
			return false;

		final User other = (User) object;
		return sameIdentityAs(other);
	}

	/**
	 * @return Hash code of tracking id.
	 */
	@Override
	public int hashCode() {
		return getCode().hashCode();
	}
}
