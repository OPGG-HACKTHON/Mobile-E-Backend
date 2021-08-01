package opgg.backend.gmakersserver.domain.account.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@Table(name = "account")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Account_id")
    private Long id;

    @Column(name = "login_id", nullable = false, unique = true, length = 30)
    private String loginId;

    @Column(name = "password", nullable = false)
    private String password;
}
