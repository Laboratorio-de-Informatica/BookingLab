package edu.eci.labinfo.bookinglab.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Clase que define la configuración de seguridad de la aplicación
 *
 * @author Daniel Antonio Santanilla
 * @author Andres Camilo Oniate
 * @version 1.0
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    public static final String LOGIN_PAGE = "/login.xhtml";
    private final UserDetailsServiceImpl userDetailsService;

    public SecurityConfig(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * Configura la seguridad de la aplicación permitiendo el acceso
     * al formulario de login y a los recursos de JSF
     *
     * @param httpSecurity Configuración de seguridad
     * @return Filtro de seguridad
     * @throws Exception Si ocurre un error al configurar la seguridad
     */
    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        HttpSessionRequestCache requestCache = new HttpSessionRequestCache();
        requestCache.setMatchingRequestParameterName(null);
        httpSecurity
            .csrf(AbstractHttpConfigurer::disable)
            .cors(AbstractHttpConfigurer::disable)
            .requestCache(cache -> cache.requestCache(requestCache))
            .authorizeHttpRequests(request -> {
                request.requestMatchers(new AntPathRequestMatcher("/jakarta.faces.resource/**")).permitAll();
                request.anyRequest().authenticated();
            })
            .formLogin(formLogin -> formLogin
                .loginPage(LOGIN_PAGE)
                .permitAll()
                .defaultSuccessUrl("/index.xhtml")
                .failureUrl("/login.xhtml?error=true"))
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl(LOGIN_PAGE)
                .permitAll()
                .deleteCookies("JSESSIONID"))
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .sessionFixation().migrateSession()
                .maximumSessions(1)
                .expiredUrl(LOGIN_PAGE));
        return httpSecurity.build();
    }

    @Bean
    AuthenticationManager authenticationManager(HttpSecurity httpSecurity) throws Exception {
        AuthenticationManagerBuilder authManagerBuilder = httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
        authManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
        return authManagerBuilder.build();
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
