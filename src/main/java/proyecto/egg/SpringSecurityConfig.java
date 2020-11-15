package proyecto.egg;

import javax.annotation.security.PermitAll;

import org.aspectj.weaver.ast.And;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import proyecto.egg.auth.handler.LoginSucessHandler;

@EnableGlobalMethodSecurity(securedEnabled = true,prePostEnabled = true)
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private LoginSucessHandler successhandler;
	
	
@Autowired
private BCryptPasswordEncoder PasswordEncoder;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/", "/css/**", "/js/**", "/images/**", "/listar").permitAll()
//				.antMatchers("/ver/**").hasAnyRole("USER")
//				.antMatchers("/uploads/**").hasAnyRole("USER")
//				.antMatchers("/form/**").hasAnyRole("ADMIN")
//				.antMatchers("/eliminar/**").hasAnyRole("ADMIN")
				.anyRequest().authenticated()
				.and()
				 	.formLogin()
				 	.successHandler(successhandler)
				 	.loginPage("/login")
				 	.permitAll()
				.and()
					.logout()
					.permitAll()
				.and()
				.exceptionHandling().accessDeniedPage("/error_403");
					

	}

	@Autowired
	public void configurerGlobal(AuthenticationManagerBuilder builder) throws Exception {
		

		PasswordEncoder encoder = this.PasswordEncoder;
		UserBuilder user = User.builder().passwordEncoder(encoder::encode);

		builder.inMemoryAuthentication().withUser(user.username("admin").password("12345").roles("ADMIN", "USER"))
				.withUser(user.username("valen").password("12345").roles("USER"));

	}

}
