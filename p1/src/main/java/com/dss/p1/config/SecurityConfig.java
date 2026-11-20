package com.dss.p1.config;

import org.springframework.boot.security.autoconfigure.web.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/index", "/index.html", "/login", "/error",
                                 "/css/**", "/js/**", "/images/**", "/webjars/**").permitAll()
                .requestMatchers("/api/**").permitAll()   // API REST pública (cliente Android)
                .requestMatchers(HttpMethod.GET, "/products", "/cart", "/cart/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/cart/**").permitAll()
                .requestMatchers("/admin/**",
                                 "/products/add", "/products/edit/**", "/products/delete/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login").permitAll()
                .defaultSuccessUrl("/", true))
            .logout(logout -> logout.logoutUrl("/login?logout").logoutSuccessUrl("/").permitAll())
            .csrf(csrf -> csrf
                .ignoringRequestMatchers(PathRequest.toH2Console())
                .ignoringRequestMatchers("/api/**"));     // la app Android no envía token CSRF
        return http.build();
    }

    @Bean
    WebSecurityCustomizer ignoringCustomizer() {
        return web -> web.ignoring().requestMatchers("/h2-console/**");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService users(PasswordEncoder encoder) {
        UserDetails admin = User.builder()
            .username("admin").password(encoder.encode("admin")).roles("ADMIN").build();
        UserDetails user = User.builder()
            .username("user").password(encoder.encode("user")).roles("USER").build();
        return new InMemoryUserDetailsManager(admin, user);
    }
}