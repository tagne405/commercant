package com.ecommerce.ecommerce2.Security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import lombok.AllArgsConstructor;

import static org.springframework.security.config.Customizer.withDefaults;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@AllArgsConstructor

public class CustomerConfiguration {
    
    private CustomerDetails customerDetails;

    // @Bean
    // public UserDetailsService userDetailsService(){
    //     return new CustomerServiceConfig();
    // }

//     private PasswordEncoder passwordEncoder;


    // public JdbcUserDetailsManager jdbcUserDetailsManager(DataSource dataSource){
    //     return new JdbcUserDetailsManager(dataSource);
    // }

    

    // @Bean
    // public DaoAuthenticationProvider provider(){
    //    DaoAuthenticationProvider provider= new DaoAuthenticationProvider();
    //    provider.setPasswordEncoder(passwordEncoder());
    //    provider.setUserDetailsService(userDetailsService());
    //    return provider;
    // }

    // protected void configure(AuthenticationManagerBuilder auth) throws Exception{
    //     auth.authenticationProvider(provider());
    // }

    @Bean
    SecurityFilterChain configure(HttpSecurity http) throws Exception{

        http.authorizeHttpRequests().requestMatchers("/**").permitAll()
                                .requestMatchers("/admin/**").hasAuthority("ADMIN")
                                .and()
                                .formLogin(login -> login
                                        .loginPage("/login"))
                                        // .loginProcessingUrl("/do-login")
                                        // .defaultSuccessUrl("/index"))
                                .logout(logout -> logout
                                        .invalidateHttpSession(true)
                                        .clearAuthentication(true)
                                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                        .logoutSuccessUrl("/index")
                                        .permitAll());


        // http.authorizeHttpRequests().requestMatchers("/admin/**").hasAuthority("ADMIN")
        //         .requestMatchers("/public/**").permitAll()
        //         .and()
        //         .formLogin(login -> login.loginPage("/public/login").defaultSuccessUrl("/public/index")
        //                 .permitAll())
        //         .logout(logout -> logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
        //                 .logoutSuccessUrl("/logout?logout").permitAll());
                                // .loginPage("/connect").permitAll());
                        //         .loginProcessingUrl("/do-login")
                                // .defaultSuccessUrl("/index"));
                        //  .logout()
                        //          .invalidateHttpSession(true)
                        //          .clearAuthentication(true)
                        //          .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        //          .logoutSuccessUrl("/login?logout")
                        //          .permitAll();
        //     } catch (Exception e) {
                
        //         e.printStackTrace();
        //     }
        // }).httpBasic(withDefaults());
        //     http.exceptionHandling(handling -> handling.accessDeniedPage("/index"));
            // http.authorizeHttpRequests().anyRequest().authenticated();

            http.authorizeHttpRequests().requestMatchers("/css/**").permitAll();
            http.authorizeHttpRequests().requestMatchers("/images/**").permitAll();
            http.authorizeHttpRequests().requestMatchers("/javascipt/**").permitAll();
            http.userDetailsService(customerDetails);
            return http.build();
        
    }

    // @Bean
    // public InMemoryUserDetailsManager userDetailsService() {
    //     UserDetails user = User.builder()
    //             .username("user")
    //             .password(passwordEncoder().encode("user"))
    //             .roles("USER")
    //             .build();
    //     UserDetails admin = User.builder()
    //             .username("admin")
    //             .password(passwordEncoder().encode("admin"))
    //             .roles("ADMIN")
    //             .build();

    //     return new InMemoryUserDetailsManager(user, admin);

    // }

    // @Bean
    // public InMemoryUserDetailsManager inMemoryUserDetailsManager(){
    //     return new InMemoryUserDetailsManager(
    //         User.withUsername("tyan").password(passwordEncoder.encode("1234")).roles("USER").build(),
    //         User.withUsername("yan").password(passwordEncoder.encode("1234")).roles("USER").build(),
    //         User.withUsername("admin").password(passwordEncoder.encode("1234")).roles("USER","ADMIN").build()
    //     );

    // }
}
