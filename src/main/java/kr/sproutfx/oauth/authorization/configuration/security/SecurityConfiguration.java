package kr.sproutfx.oauth.authorization.configuration.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsUtils;

import kr.sproutfx.oauth.authorization.configuration.security.firewall.AnnotatingHttpFirewall;
import kr.sproutfx.oauth.authorization.configuration.security.jwt.JwtAuthenticationFilter;
import kr.sproutfx.oauth.authorization.configuration.security.jwt.JwtProvider;
import kr.sproutfx.oauth.authorization.configuration.security.property.HttpSecurityPermitAllProperties;
import kr.sproutfx.oauth.authorization.configuration.security.property.WebSecurityIgnoreProperties;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    JwtProvider jwtProvider;
    WebSecurityIgnoreProperties webSecurityIgnore;
    HttpSecurityPermitAllProperties authorizeRequestsPermitAll;

    @Autowired
    public SecurityConfiguration(JwtProvider jwtProvider, WebSecurityIgnoreProperties webSecurityIgnore, HttpSecurityPermitAllProperties authorizeRequestsPermitAll) {
        this.jwtProvider = jwtProvider;
        this.webSecurityIgnore = webSecurityIgnore;
        this.authorizeRequestsPermitAll = authorizeRequestsPermitAll;
    }
    
    @Override
    public void configure(WebSecurity webSecurity) throws Exception {
        for (String pattern : webSecurityIgnore.getPatterns()) {
            webSecurity.ignoring().antMatchers(pattern);
        }

        webSecurity
            .ignoring()
            .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
        
        webSecurity
            .httpFirewall(new AnnotatingHttpFirewall());
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        for (String pattern : authorizeRequestsPermitAll.getPatterns()) {
            httpSecurity
                .authorizeRequests()
                .antMatchers(pattern)
                .permitAll();
        }

        httpSecurity
            .addFilterBefore(new JwtAuthenticationFilter(this.jwtProvider), UsernamePasswordAuthenticationFilter.class)
            .csrf().disable().cors().and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            .authorizeRequests().requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
            .anyRequest().authenticated();
    }
}
