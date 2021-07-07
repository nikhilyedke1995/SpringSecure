package com.demo.secure.security;

import com.demo.secure.auth.ApplicationUserService;
import com.demo.secure.jwt.JwtConfig;
import com.demo.secure.jwt.JwtTokenVerifier;
import com.demo.secure.jwt.JwtUsernameAndPasswordAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.crypto.SecretKey;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;
    private final ApplicationUserService service;
    private final JwtConfig jwtConfig;
    private final SecretKey secretKey;

    @Autowired
    public ApplicationSecurityConfig(PasswordEncoder passwordEncoder, ApplicationUserService service, JwtConfig jwtConfig, SecretKey secretKey) {
        this.passwordEncoder = passwordEncoder;
        this.service = service;
        this.jwtConfig = jwtConfig;
        this.secretKey = secretKey;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(), jwtConfig, secretKey))
                .addFilterAfter(new JwtTokenVerifier(jwtConfig, secretKey),JwtUsernameAndPasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/","index","/css/*","/js/*").permitAll()
                .antMatchers("/api/**").hasRole(ApplicationUserRole.STUDENT.name())
                .anyRequest()
                .authenticated();
    }

    //    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .csrf().disable()
//                .authorizeRequests()
//                //add those url pattern which you want to allow without authentication
//                //order of antMatchers is important
//                .antMatchers("/","index","/css/*","/js/*").permitAll()
//                .antMatchers("/api/**").hasRole(ApplicationUserRole.STUDENT.name())
////                .antMatchers(HttpMethod.DELETE,"/management/api/**").hasAuthority(ApplicationUserPermission.COURSE_WRITE.getPermission())
////                .antMatchers(HttpMethod.POST,"/management/api/**").hasAuthority(ApplicationUserPermission.COURSE_WRITE.getPermission())
////                .antMatchers(HttpMethod.PUT,"/management/api/**").hasAuthority(ApplicationUserPermission.COURSE_WRITE.getPermission())
////                .antMatchers(HttpMethod.GET,"/management/api/**").hasAnyRole(ApplicationUserRole.ADMIN.name(),ApplicationUserRole.ADMINTRAINEE.name())
//                .anyRequest()
//                .authenticated()
//                .and()
//                .formLogin()
//                .loginPage("/login").permitAll();
//    }

//    @Override
//    @Bean
//    protected UserDetailsService userDetailsService() {
//        UserDetails nikhilUser = User.builder()
//                .username("Nikhil")
//                .password(passwordEncoder.encode("password"))
////                .roles(ApplicationUserRole.STUDENT.name()) //ROLE_STUDENT
//                .authorities(ApplicationUserRole.STUDENT.getGrantedAuthorities())
//                .build();
//
//        UserDetails priyaUser = User.builder()
//                .username("priya")
//                .password(passwordEncoder.encode("password123"))
////                .roles(ApplicationUserRole.ADMIN.name()) //ROLE_ADMIN
//                .authorities(ApplicationUserRole.ADMIN.getGrantedAuthorities())
//                .build();
//
//        UserDetails shubhamUser = User.builder()
//                .username("shubham")
//                .password(passwordEncoder.encode("password123"))
////                .roles(ApplicationUserRole.ADMINTRAINEE.name()) //ROLE_ADMINTRAINEE
//                .authorities(ApplicationUserRole.ADMINTRAINEE.getGrantedAuthorities())
//                .build();
//        return new InMemoryUserDetailsManager(nikhilUser,priyaUser,shubhamUser);
//    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);//helps to decode password
        provider.setUserDetailsService(service);
        return provider;
    }

}
