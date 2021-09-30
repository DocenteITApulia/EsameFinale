package it.apulia.Esercitazione3.accessManagement.security;

import it.apulia.Esercitazione3.accessManagement.filter.CustomAuthenticationFilter;
import it.apulia.Esercitazione3.accessManagement.filter.CustomAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

//altro esempio su https://www.bezkoder.com/spring-boot-jwt-auth-mongodb/

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManagerBean());
        customAuthenticationFilter.setFilterProcessesUrl("/accessManager/login");
        http.csrf().disable(); //disabilita il cross site request forgery
        http.sessionManagement().sessionCreationPolicy(STATELESS);
        //manca nell'implementazione il ruolo del super admin
        //http.authorizeRequests().anyRequest().hasAnyAuthority("ROLE_SUPER_ADMIN"); //permette al super admin di fare qualunque operazione
        http.authorizeRequests().antMatchers("/accessManager/login/**", "/accessManager/token/refresh/**").permitAll();
        //NON AUTENTICATO - registrazione passeggero e info tabelloni
        http.authorizeRequests().antMatchers(POST,"/clientManagement").permitAll();
/*
        //PERMESSI ADMIN
        http.authorizeRequests().antMatchers( "/accessManager/utenti/**", "/accessManager/roles/**").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers( "/agencymng/passengers/listall","/flights/**").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(GET, "/agencymng/passengers/**").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(PUT, "/agencymng/passengers/**").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(DELETE, "/agencymng/passengers/**").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(GET, "/agencymng/bookings/listall").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(PUT, "/agencymng/bookings/**").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(DELETE, "/agencymng/bookings/**").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeRequests().antMatchers(PUT, "/agencymng/bookings/searchbydate").hasAnyAuthority("ROLE_ADMIN");
*/
        //TODO fare check su questa, eventualmente decommentare, dovrebbe bastare l'authenticated sotto
        //http.authorizeRequests().antMatchers(GET, "/agencymng/bookings/**").hasAnyAuthority("ROLE_USER");
        //http.authorizeRequests().antMatchers(GET, "/agencymng/passengers/**").hasAnyAuthority("ROLE_USER");
        //POST dovrebbero potera fare tutti gli autenticati

        http.authorizeRequests().anyRequest().hasAnyAuthority("ROLE_ADMIN"); //verificare poi accesso per utenti se non lo specifico
        http.addFilter(customAuthenticationFilter);
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

    }

    //creato perchè serve come parametro al filtro custom
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
