package proyecto.egg.Repositorios;





import javax.print.DocFlavor.STRING;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import proyecto.egg.Entidades.Person;
import proyecto.egg.Entidades.Usuario;

@Repository
public interface Usuariodao extends CrudRepository<Person, Long>   {

	public Person findByUsername(String  username);
	
}
