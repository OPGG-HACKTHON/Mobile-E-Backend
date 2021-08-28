package opgg.backend.gmakersserver.domain.account.entity;

import static javax.persistence.CascadeType.*;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import opgg.backend.gmakersserver.domain.auditing.BaseEntity;
import opgg.backend.gmakersserver.domain.profile.entity.Profile;

@Entity
@Getter
@Table(name = "account")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account extends BaseEntity {

	@Builder
	public Account(String username, String password, boolean activated, Role role) {
		this.username 	= username;
		this.password 	= password;
		this.activated 	= activated;
		this.role 		= role;
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

	@OneToMany(mappedBy = "account", fetch = FetchType.LAZY, cascade = ALL)
	private List<Profile> profile = new ArrayList<>();

	public boolean isPasswordMatch(PasswordEncoder passwordEncoder, String requestPassword) {
		return passwordEncoder.matches(requestPassword, password);
	}

}
