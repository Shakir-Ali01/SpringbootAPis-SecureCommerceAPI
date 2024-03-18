package com.perfect.electronic.store;

import com.perfect.electronic.store.entities.Role;
import com.perfect.electronic.store.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;
import java.util.stream.Collectors;


@SpringBootApplication
@EnableWebMvc
public class ElectronicStoreApplication implements CommandLineRunner {
	public static void main(String[] args) {
		SpringApplication.run(ElectronicStoreApplication.class, args);
	}
    @Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private RoleRepository roleRepository;

	@Value("${normal.role.id}")
	private String role_normal_id;

	@Value("${admin.role.id}")
	private String role_admin_id;

	@Override
	public void run(String... args) throws Exception {
		System.out.println(passwordEncoder.encode("Abc"));
		System.out.println(passwordEncoder.encode(role_admin_id));
		try{
			Role role_admin=Role.builder().roleId(role_admin_id).roleName("ROLL_ADMIN").build();
			Role role_normal=Role.builder().roleId(role_normal_id).roleName("ROLL_NORMAL").build();
			roleRepository.save(role_normal);
			roleRepository.save(role_admin);
		}catch (Exception e){
			e.printStackTrace();
		}
	}

}
