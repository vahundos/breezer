package com.github.vahundos.breezer.config;

import com.github.vahundos.breezer.web.filter.BasicAuthenticationDenyFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final BasicAuthenticationDenyFilter basicAuthenticationDenyFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
            .authorizeRequests()
                .antMatchers("/", "/users/register").permitAll()
                .anyRequest().authenticated()
                .and()
            .formLogin()
                .disable()
            .httpBasic()
                .and()
            .logout()
                .permitAll()
                .and()
            .csrf().disable();

        http.addFilterBefore(basicAuthenticationDenyFilter, BasicAuthenticationFilter.class);
        // @formatter:on
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
