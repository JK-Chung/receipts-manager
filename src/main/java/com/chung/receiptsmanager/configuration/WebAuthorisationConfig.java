package com.chung.receiptsmanager.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebAuthorisationConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.httpBasic();   //TODO use better authentication method

        //TODO security disabled to allow h2-console to work (should be removed when proper DB used)
        httpSecurity.csrf().disable();
        httpSecurity.headers().frameOptions().disable();

        this.secureEndpoints(httpSecurity);
    }

    private void secureEndpoints(final HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeRequests()
                .antMatchers("/h2-console/**", "/api/auth/signup") // TODO remove h2-console when using big-boy DB
                .permitAll()
                .antMatchers("/api/image") // TODO remove
                .permitAll()
                .anyRequest().authenticated();
    }

}
