package com.perfect.electronic.store.config;
import com.perfect.electronic.store.security.JwtAuthenticationEntryPoint;
import com.perfect.electronic.store.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtAuthenticationEntryPoint authenticationEntryPoint;
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    //hard coded authentication
//    @Bean
//    public UserDetailsService userDetailsService(){
//        //user create
//       UserDetails normal= User.builder()
//                .username("Zaid")
//                .password(passwordEncoder().encode("Admin"))
//                .roles("Normal")
//                .build();
//      UserDetails admin= User.builder()
//               .username("Shakir")
//               .password(passwordEncoder().encode("Admin"))
//              .roles("Admin")
//               .build();
//      return new InMemoryUserDetailsManager(normal,admin);
//    }

    /*Start Basic Authentication :- Basic Authentication send data in header to authenticate user.
     it needs to send username and password in header at every request which is less secure
    */
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//      http.
//              csrf().
//              disable().
//              cors().
//              disable().
//              authorizeRequests()
//              .anyRequest()
//              .authenticated()
//              .and()
//              .httpBasic();
//        return http.build();
//    }
    //Basic Authentication End

    //JWT Configuration
    /*

    authorizeRequests() is deprecated and .antMatchers() has been removed
    . Use .authorizeHttpRequests() and .requestMatchers() instead

    */
    private final String[] PUBLIC_URL={
            "/swagger-ui/**",
            "/swagger-ui.html/**",
            "/webjars/**",
            "v3/api-docs/**",
            "swagger-ui-custom.html/",
            "/swagger-resources/**"
    };
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        System.out.println("called security Chain");
        //                .cors(cors->cors.disable())
        http
                .csrf(csrf->csrf.disable())
                .authorizeHttpRequests(auth->auth.requestMatchers("/home/**").authenticated()
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/error").permitAll()
                        .requestMatchers("/users/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE,"/users").hasRole("ADMIN")
                        .requestMatchers(PUBLIC_URL).permitAll()
                        .anyRequest().authenticated())
                .exceptionHandling(ex->ex.authenticationEntryPoint(authenticationEntryPoint))
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
                http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
//        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        /*
        jwtAuthenticationFilter: This method define in securityconfig class and validating token
        which is passing by user in header
        */
        return http.build();
    }
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf->csrf.disable())
//                .cors(cors->cors.disable())
//                .authorizeHttpRequests(auth->auth.requestMatchers("/home/**").authenticated().requestMatchers("/auth/**").permitAll().anyRequest().authenticated())
//                .exceptionHandling(ex->ex.authenticationEntryPoint(authenticationEntryPoint))
//                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//      http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
//      System.out.println("DDD");
//        return http.build();
//    }




    /*
    * */
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//      http.authorizeRequests().
//              anyRequest().
//              authenticated().
//              and().
//              formLogin().
//              loginPage("login.html").
//              loginProcessingUrl("/process-url").
//              defaultSuccessUrl("/dashboard").
//              failureUrl("error").
//              and().
//              logout().
//              logoutUrl("/logout");
//        return http.build();
//    }
    //DaoAuthenticationProvider :- This is responsible for authenticating user

    /*This method is running automatically*/
//    IF we dont provide this method then it will call automatic

/*    If you create your own UserDetailsService bean, there is no need to manually define a bean
        for AuthenticationProvider, cos by default a DaoAuthenticationProvider bean will be automatically
    created for us, which will automatically pick up your defined UserDetailsService bean.
    */

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider=new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(this.userDetailsService);
        return daoAuthenticationProvider;
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception {
        return builder.getAuthenticationManager();
    }
//    CORS Configuration
    @Bean
    public FilterRegistrationBean corsFilter() {
         UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
         CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
//        configuration.addAllowedOriginPattern("*");
        configuration.addAllowedHeader("Authorization");
        configuration.addAllowedHeader("Content-Type");
        configuration.addAllowedHeader("Accept");
        configuration.addAllowedMethod("GET");
        configuration.addAllowedMethod("PUT");
        configuration.addAllowedMethod("POST");
        configuration.addAllowedMethod("DELETE");
        configuration.addAllowedMethod("OPTIONS");
        configuration.setMaxAge(3600L);
        source.registerCorsConfiguration("/**",configuration);
        FilterRegistrationBean filterRegistrationBean=new FilterRegistrationBean(new CorsFilter(source));
        //For priority of registring bean
       filterRegistrationBean.setOrder(-1);
        return filterRegistrationBean;
    }










}
 /*
 Error:- (No AuthenticationProvider found for org.springframework.security.authentication.
 UsernamePasswordAuthenticationToken)
        I found the root cause.
        If you create your own UserDetailsService bean, there is no need to manually define a bean
d        for AuthenticationProvider, cos by default a DaoAuthenticationProvider bean will be automatically
        created for us, which will automatically pick up your defined UserDetailsService bean.
        But if you define 2 or more UserDetailsService beans, then u need to define your own
        Authenticationprovider. I made a mistake, as i
        don't realize I have another class that implements UserDetailsService interface and annotated with @service ,
        which create a second UserDetailsService bean.

  */
