package com.github.vahundos.breezer.config;

import com.github.vahundos.breezer.web.filter.DenyBasicAuthenticationFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
            .authorizeRequests()
                .antMatchers("/").permitAll()
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

        http.addFilterBefore(new DenyBasicAuthenticationFilter(), BasicAuthenticationFilter.class);
        // @formatter:on
    }
}
