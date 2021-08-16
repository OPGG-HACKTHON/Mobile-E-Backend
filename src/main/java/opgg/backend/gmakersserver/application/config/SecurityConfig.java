package opgg.backend.gmakersserver.application.config;

import static org.springframework.http.HttpMethod.*;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

import lombok.RequiredArgsConstructor;
import opgg.backend.gmakersserver.infra.jwt.ExceptionHandlerFilter;
import opgg.backend.gmakersserver.infra.jwt.JjwtService;
import opgg.backend.gmakersserver.infra.jwt.JwtFilter;
import opgg.backend.gmakersserver.infra.jwt.JwtSecurityConfig;

@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JjwtService jjwtService;
    private final CorsFilter corsFilter;
    private final ExceptionHandlerFilter exceptionHandlerFilter;
    private final JwtFilter jwtFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers(
                        "/favicon.ico"
                        ,"/error", "/swagger-ui/**", "/v3/api-docs/**"
                );
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf()
                .disable()
                .exceptionHandling()
                .authenticationEntryPoint(
        (request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                .accessDeniedHandler((request, response, accessDeniedException) -> response.sendError(
                        HttpServletResponse.SC_FORBIDDEN))
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()
                .antMatchers(POST, "/api/accounts/sign-up", "/api/accounts/sign-in").permitAll()
                .antMatchers("/swagger/**", "/swagger-ui/**", "/swagger-ui.html/**", "/openapi.yml/**", "/api/key").permitAll()
                .anyRequest().authenticated()

                .and()
                .apply(new JwtSecurityConfig(jjwtService));
        httpSecurity.addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class);
        httpSecurity.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        httpSecurity.addFilterBefore(exceptionHandlerFilter, JwtFilter.class);
    }
}
