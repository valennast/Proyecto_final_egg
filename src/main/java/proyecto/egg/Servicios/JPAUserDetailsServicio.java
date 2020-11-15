package proyecto.egg.Servicios;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.validator.internal.IgnoreForbiddenApisErrors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import proyecto.egg.Entidades.Person;
import proyecto.egg.Entidades.Role;
import proyecto.egg.Repositorios.Usuariodao;

@Service("jPAUserDetailsServicio")
public class JPAUserDetailsServicio implements UserDetailsService {

	@Autowired
	private Usuariodao repos;

	private Logger logger = LoggerFactory.getLogger(JPAUserDetailsServicio.class);

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Person persons = repos.findByUsername(username);

		if (persons == null) {
			logger.error("Error login: No existe el usuario '" + username + "'");
			throw new UsernameNotFoundException("Username" + username + " no existe en el sistema un usuario");
		}

		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

		for (Role role : persons.getRoles()) {
			logger.info("ROLE: " + role.getAuthority());
			authorities.add(new SimpleGrantedAuthority(role.getAuthority()));
		}
		if (authorities.isEmpty()) {
			logger.error("Error login: usuario '" + username + "' no tiene roles asignados");
			throw new UsernameNotFoundException("Error login: usuario '"+ username  +"' no tiene roles asignados");
		}

		return new User(persons.getUsername(), persons.getPassword(), persons.getEnable(), true, true, true,
				authorities);
	}

}
