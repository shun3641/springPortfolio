package com.example.demo.SecurityConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.demo.model.Items;
import com.example.demo.model.Users;

@Configuration
@EnableWebSecurity
public class config {

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}
	
	@Bean
    public Users myService() {
        return new Users();
    }
	
	@Bean
    public Items myItems() {
        return new Items();
    }
	
	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
            	//*** ログアウト後も表示できるものたち
            	.requestMatchers("/index","/login","/user/*","/css/Topheader.css","/image/logo.png"
            			,"/image/TopImage.jpg","/css/footer.css").permitAll()
            	// ***
            	
            	.requestMatchers("/admin/*").hasAuthority("ADMIN")
            	.anyRequest().authenticated()
            )
            .formLogin(login -> login
            		//viewControllerにURLをマッピングしないと
            		//無限リダイレクトのエラーを確認
            		.loginPage("/login")
                    .defaultSuccessUrl("/index", true)
                    .permitAll()
                    )
                    .logout(logout -> logout
                            .logoutUrl("/logout")
                            .logoutSuccessUrl("/logoutView")
                            .permitAll()
                        );

        	return http.build();
    }
}
