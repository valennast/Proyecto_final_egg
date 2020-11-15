package proyecto.egg.Servicios;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import proyecto.egg.Entidades.Usuario;
import proyecto.egg.Repositorios.UsuarioRepositorio;

@Service
public class UsuarioService {
	
	@Autowired
	public UsuarioRepositorio repositorio;

	@Transactional(readOnly = true)
	public List<Usuario> mostrar() {
		return repositorio.findAll();
	}

	@Transactional
	public void guardar(Usuario usuario) {
			repositorio.save(usuario);
	}
	
	@Transactional(readOnly = true)
	public Usuario mostrar_uno(Long id) {
		return repositorio.getOne(id);
	}
	
	@Transactional
	public void borrar(Long id) {
		repositorio.deleteById(id);
	}
	
}
