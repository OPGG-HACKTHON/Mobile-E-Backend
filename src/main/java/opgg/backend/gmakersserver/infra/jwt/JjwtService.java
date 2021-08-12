package opgg.backend.gmakersserver.infra.jwt;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.extern.slf4j.Slf4j;
import opgg.backend.gmakersserver.domain.account.entity.Account;
import opgg.backend.gmakersserver.domain.account.repository.AccountRepository;
import opgg.backend.gmakersserver.error.exception.account.AccountNotFoundException;
import opgg.backend.gmakersserver.error.exception.jwt.AuthorityInfoNotFoundException;
import opgg.backend.gmakersserver.error.exception.jwt.ExpiredJwtTokenException;
import opgg.backend.gmakersserver.error.exception.jwt.InvalidJwtSignatureException;
import opgg.backend.gmakersserver.error.exception.jwt.InvalidJwtTokenException;
import opgg.backend.gmakersserver.error.exception.jwt.UnsupportedJwtTokenException;

@Slf4j
@Component
public class JjwtService {

	private static final String AUTHORITIES_KEY = "auth";
	private final long accessTime;
	private final String headerType;
	private final Key key;
	private final String issuer;
	private final AccountRepository accountRepository;

	public JjwtService(@Value("${jwt.token.header-type}") String headerType,
			@Value("${jwt.token.issuer}") String issuer,
			@Value("${jwt.token.secret}") String secret,
			@Value("${jwt.token.access-time}") long accessTime,
			AccountRepository accountRepository) {

		byte[] keyBytes = Decoders.BASE64.decode(secret);
		this.key = Keys.hmacShaKeyFor(keyBytes);
		this.headerType = headerType;
		this.issuer = issuer;
		this.accessTime = accessTime;
		this.accountRepository = accountRepository;
	}

	public String createToken(Account account) {
		long now = (new Date()).getTime();
		Date issuedAt = new Date();
		Date expiration = new Date(now + accessTime);
		return Jwts.builder()
				.signWith(key, SignatureAlgorithm.HS512)
				.setHeaderParam("typ", headerType)
				.setIssuer(issuer)
				.setSubject(String.valueOf(account.getAccountId()))
				.setExpiration(expiration)
				.setIssuedAt(issuedAt)
				.claim(AUTHORITIES_KEY, account.getRole())
				.compact();
	}

	public JwtAuthentication getAuthentication(String token) {
		Claims claims = parseClaims(token);
		long accountId = Long.parseLong(claims.getSubject());

		Collection<? extends GrantedAuthority> authorities =
				Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
						.map(SimpleGrantedAuthority::new)
						.collect(Collectors.toList());
		return new JwtAuthentication(accountId, "", authorities);
	}

	private Claims parseClaims(String token) {
		try {
			return Jwts.parserBuilder()
					.setSigningKey(key)
					.build()
					.parseClaimsJws(token)
					.getBody();
		} catch (ExpiredJwtException e) {
			throw new AuthorityInfoNotFoundException();
		}
	}

	public Account getAccount(String token) {
		try {
			Claims claims = Jwts.parserBuilder()
					.setSigningKey(key)
					.build()
					.parseClaimsJws(token)
					.getBody();
			Long accountId = Long.parseLong(claims.getSubject());
			return accountRepository.findByAccountId(accountId).orElseThrow(AccountNotFoundException::new);
		} catch (SecurityException | MalformedJwtException e) {
			throw new InvalidJwtSignatureException();
		} catch (ExpiredJwtException e) {
			throw new ExpiredJwtTokenException();
		} catch (UnsupportedJwtException e) {
			throw new UnsupportedJwtTokenException();
		} catch (IllegalArgumentException e) {
			throw new InvalidJwtTokenException();
		}
	}

}