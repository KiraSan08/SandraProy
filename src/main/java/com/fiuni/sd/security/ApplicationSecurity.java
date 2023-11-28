package com.fiuni.sd.security;

import org.springframework.http.HttpMethod;
import com.fiuni.sd.security.jwt.JwtTokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

import com.fiuni.sd.domain.User;
import javax.servlet.http.HttpServletResponse;

@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class ApplicationSecurity extends WebSecurityConfigurerAdapter {
    @Autowired
    private com.fiuni.sd.dao.IUserDao userDao;

    @Autowired
    private JwtTokenFilter jwtTokenFilter;

    // usar este boolean para mostrar como funciona el filtro en cada request
    private final boolean requestWithoutAuthorization = false;

    /**
     * Como no es web clásica, se deshabilit CSRF y tampoco es necesario administrar
     * la sesión
     * configuracion WEB
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        if (requestWithoutAuthorization) {
            http.authorizeRequests().anyRequest().permitAll();
        } else {
            // permisos de endpoints
            http.authorizeRequests()
                    .antMatchers("/auth/login", "/auth/register").permitAll()
                    .antMatchers("/api/**").hasAuthority("admin")
                    .antMatchers(HttpMethod.DELETE, "/api/**").hasAnyAuthority("profesor")
                    .antMatchers(HttpMethod.GET, "/api/presence/{id}").hasAnyAuthority("profesor")
                    .antMatchers(HttpMethod.GET, "/api/presence/{id}").hasAnyAuthority("secretary")
                    .anyRequest().authenticated();

            // manejar excepciones
            http.exceptionHandling()
                    .authenticationEntryPoint((request, response, ex) -> {
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
                    });
            // se agrega el jwt token filter antes del Username password filter
            http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
        }
    }

    /**
     * configuracion del auth manager
     *
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(username -> userDao.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found.")));

    }

    /**
     * con esto se provee el algoritmo de encriptacion utilizado para encriptar el
     * password
     *
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
