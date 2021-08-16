package opgg.backend.gmakersserver.domain.account.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import opgg.backend.gmakersserver.domain.auditing.BaseEntity;
import opgg.backend.gmakersserver.domain.profile.entity.Profile;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;

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

	@OneToMany(mappedBy = "account", fetch = FetchType.LAZY, cascade = ALL)
	private List<Profile> profile = new ArrayList<>();

}
