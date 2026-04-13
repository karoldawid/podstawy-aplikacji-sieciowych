package sfs.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter; // <--- WAŻNY IMPORT
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthFilter;

    public SecurityConfig(CustomUserDetailsService userDetailsService, JwtAuthenticationFilter jwtAuthFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // publiczne
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/v1/clients").permitAll()
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/v1/admins").hasRole("ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/v1/facility-managers").hasRole("ADMIN")


                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/v1/rentals/rent/self").authenticated()
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/v1/rentals/self").authenticated()
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/v1/rentals/rent").hasAnyRole("ADMIN", "FACILITYMANAGER")

                        .requestMatchers("/api/v1/rentals/client/**").hasAnyRole("ADMIN", "FACILITYMANAGER")

                        // wspólne dla zalogowanych (reszta)
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/v1/users/change-password").authenticated()
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/v1/facilities/**").authenticated()

                        .requestMatchers("/api/v1/rentals/**").authenticated()

                        // admin manager (reszta)
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/v1/facilities/**").hasAnyRole("FACILITYMANAGER", "ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.PUT, "/api/v1/facilities/**").hasAnyRole("FACILITYMANAGER", "ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/api/v1/facilities/**").hasAnyRole("FACILITYMANAGER", "ADMIN")
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/v1/users/**").hasAnyRole("ADMIN", "FACILITYMANAGER")
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/v1/clients/**").hasAnyRole("ADMIN", "FACILITYMANAGER")

                        // tylko admin
                        .requestMatchers("/api/v1/users/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/clients/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/admins/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/facility-managers/**").hasRole("ADMIN")

                        // reszta zablokowana domyślnie
                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // k rozwiazanie dla front
        configuration.setAllowedOrigins(List.of("http://localhost:5173"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        configuration.setExposedHeaders(List.of("ETag", "Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    //😊😊😊😊😊😊😊😊😊😊😊😊😊😊😊😊😊😊😊😊😊😊😊😊😊😊😊😊😊😊😊😊😊😊
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}