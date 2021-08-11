package opgg.backend.gmakersserver.domain.account.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import opgg.backend.gmakersserver.domain.auditing.BaseEntity;

@Entity
@Getter
@Table(name = "account")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account extends BaseEntity {

	@Builder
	public Account(String username, String password, boolean activated, Role role) {
		this.username = username;
		this.password = password;
		this.activated = activated;
		this.role = role;
	}

	@JsonIgnore
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "account_id")
	private Long accountId;

	@JsonIgnore
	@Column(name = "username", nullable = false, unique = true, length = 50)
	private String username;

	@Column(name = "password", nullable = false, length = 100)
	private String password;

	@JsonIgnore
	@Column(name = "activated")
	private boolean activated;

	@Enumerated(EnumType.STRING)
	private Role role;

}
