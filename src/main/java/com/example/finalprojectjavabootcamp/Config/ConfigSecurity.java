package com.example.finalprojectjavabootcamp.Config;

import com.example.finalprojectjavabootcamp.Service.MyUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class ConfigSecurity {

    private final MyUserDetails myUserDetailsService;

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider  daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(myUserDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder());
        return daoAuthenticationProvider;

    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and()
                .authenticationProvider(daoAuthenticationProvider())
                .authorizeHttpRequests()

                // Auth & Register
                .requestMatchers("/api/v1/buyer/register/**", "/api/v1/seller/register/**",
                        "/api/v1/seller/register/request-otp", "/api/v1/seller/register/confirm-otp",
                        "/api/v1/buyer/register/confirm-otp","/api/v1/buyer/register/request-otp").permitAll()

                // USER Management
                .requestMatchers("/api/v1/user/get-all", "/api/v1/user/get-all-dto",
                        "/api/v1/user/sellers", "/api/v1/user/buyers",
                        "/api/v1/user/get-user-by-id/**", "/api/v1/user/get-blocked-users",
                        "/api/v1/user/delete/**", "/api/v1/user/delete/**",
                        "/api/v1/user/block-user/**","/api/v1/user/active-user/**").hasAuthority("ADMIN")


                // Buyer
                .requestMatchers("/api/v1/buyer/update/**").hasAuthority("BUYER")
                .requestMatchers("/api/v1/buyer/filter").permitAll()


                // Seller
                .requestMatchers("/api/v1/seller/update/**", "/api/v1/seller/listings/filter/**",
                        "/api/v1/seller/negotiations/stats").hasAuthority("SELLER")


                // Calls
                .requestMatchers("/api/v1/call/**").hasAnyAuthority("SELLER","BUYER")

                // Listings (Car, Real Estate, General)
                .requestMatchers("/api/v1/car-listing/**",
                        "/api/v1/real-estate-listing/**",
                        "/api/v1/listing/**").hasAnyAuthority("SELLER","BUYER")

                // Contact Us
                .requestMatchers("/api/v1/contact-us/**").permitAll()

                // Invoice
                .requestMatchers("/api/v1/invoice/**").hasAnyAuthority("SELLER", "ADMIN","BUYER")

                // Negotiations
                .requestMatchers("/api/v1/negotiations/**",
                        "/api/v1/negotiation-message/**").hasAnyAuthority("SELLER","BUYER")

                // Payment
                .requestMatchers("/api/v1/payment/**").hasAnyAuthority("SELLER","BUYER")


                // Rating
                .requestMatchers("/api/v1/rating/**").hasAnyAuthority("SELLER","BUYER")

                // Search
                .requestMatchers("/api/v1/search/**").hasAuthority("BUYER")


                // Subscription
                .requestMatchers("/api/v1/subscription/**").hasAuthority("SELLER")



                .anyRequest().authenticated()

                .and()
                .logout().logoutUrl("/api/v1/auth/logout")
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .and()
                .httpBasic();

        return http.build();
    }

}