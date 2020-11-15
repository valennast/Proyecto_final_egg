package proyecto.egg.Controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import proyecto.egg.Entidades.Usuario;
import proyecto.egg.Servicios.UsuarioService;

@Controller
@RequestMapping("")
public class UsuarioControllador {

	protected final Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	private UsuarioService servicio;

	private final static String UPLOADS_FOLDER = "uploads";

	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping(value = "/ver/{id}")
	public String ver(@PathVariable(value = "id") Long id, Model model, RedirectAttributes flash) {
		Usuario usuario = servicio.mostrar_uno(id);

		if (usuario == null) {
			flash.addAttribute("error", "El usuario no existe en la base de datos");
			return "redirect:listar";
		}

		model.addAttribute("usuario", usuario);
		model.addAttribute("titulo", "Vista Usuario: " + usuario.getNombre());

		return "ver";
	}

	@GetMapping({ "/listar", "/" })
	public String mostrar(Model model, Authentication authentication, HttpServletRequest request) {
		List<Usuario> users = null;

		if (authentication != null) {

			logger.info("Hola usuario autenticado, tu user name es: " + authentication.getName());
		}

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		if (hasRole("ROLE_ADMIN")) {
			logger.info("HOLA " + authentication.getName() + " tienes acceso");
		} else {
			logger.info("HOLA " + auth.getName() + " no tienes acceso");
		}
		
		SecurityContextHolderAwareRequestWrapper securitycon = new SecurityContextHolderAwareRequestWrapper(request, "ROLE_");
		
		if(securitycon.isUserInRole("ADMIN")) {
			logger.info("forma usando HTTPSERVLETREQUEST HOLA " + auth.getName() + " tienes acceso");
		}else {
			logger.info(" forma usando HTTPSERVLETREQUEST HOLA " + auth.getName() + " no tienes acceso");
		}
		
		
		if(request.isUserInRole("ROLE_ADMIN")) {
			logger.info("forma usando HTTPSERVLETREQUEST HOLA " + auth.getName() + " tienes acceso");
		}else {
			logger.info(" forma usando HTTPSERVLETREQUEST HOLA " + auth.getName() + " no tienes acceso");
		}
				

		users = servicio.mostrar();
		model.addAttribute("titulo", "Listado de clientes");
		model.addAttribute("usuarios", users);
		return "vista_usuario.html";
	}

	@Secured("ROLE_ADMIN")
	@GetMapping(value = "/form")
	public String crear(ModelMap model) {
		Usuario usuario = new Usuario();
		model.put("usuario", usuario);
		model.put("titulo", "Formulario de usuario");
		return "form";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/form", method = RequestMethod.POST)
	public String guardar(@Valid Usuario usuario, BindingResult result, ModelMap model,
			@RequestParam("file") MultipartFile foto, RedirectAttributes flash) {
		if (result.hasErrors()) {
			System.out.println("paso");
			model.put("titulo", "Formulario de Usuario");
			return "form";
		}
		if (!foto.isEmpty()) {

			if (usuario.getId() != null && usuario.getId() > 0 && usuario.getFoto() != null
					&& usuario.getFoto().length() > 0) {

				Path rootPath = Paths.get(UPLOADS_FOLDER).resolve(usuario.getFoto()).toAbsolutePath();
				File archivo = rootPath.toFile();

				if (archivo.exists() && archivo.canRead()) {
					archivo.delete();
				}

			}

			String uniqueFilename = UUID.randomUUID().toString() + "_" + foto.getOriginalFilename();

			Path rootpath = Paths.get(UPLOADS_FOLDER).resolve(uniqueFilename);

			Path rootAbsolutPath = rootpath.toAbsolutePath();

			try {
				Files.copy(foto.getInputStream(), rootAbsolutPath);
				flash.addFlashAttribute("info", "Has subido correctamente '" + uniqueFilename + "'");
				usuario.setFoto(uniqueFilename);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		String mensajeflash = (usuario.getId() != null) ? "Cliente editado con exito!" : "Cliente creado con exito!";
		servicio.guardar(usuario);
		flash.addFlashAttribute("success", mensajeflash);
		return ("redirect:listar");
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@RequestMapping(value = "/form/{id}")
	public String editar(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {

		Usuario usuario = null;

		if (id > 0) {
			usuario = servicio.mostrar_uno(id);
			if (usuario == null) {
				flash.addFlashAttribute("error", "El id del usuario no existe en la base de datos");
				return "redirect:/listar";
			}
		} else {
			flash.addFlashAttribute("error", "El id del usuario no puede ser 0");
			return "redirect:/listar";
		}

		model.put("usuario", usuario);
		model.put("titulo", "Editar Usuario");

		return "form";
	}

	@Secured("ROLE_ADMIN")
	@GetMapping(value = "/eliminar/{id}")
	public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash) {

		if (id > 0) {
			Usuario usuario = servicio.mostrar_uno(id);

			servicio.borrar(id);
			flash.addFlashAttribute("success", "USUARIO ELIMINADO");

			Path rootPath = Paths.get(UPLOADS_FOLDER).resolve(usuario.getFoto()).toAbsolutePath();
			File archivo = rootPath.toFile();

			if (archivo.exists() && archivo.canRead()) {
				if (archivo.delete()) {
					flash.addFlashAttribute("info", "foto" + usuario.getFoto() + "eliminado con exito");
				}
			}

		}

		return ("redirect:/listar");
	}

	private boolean hasRole(String role) {
		SecurityContext secu = SecurityContextHolder.getContext();

		if (secu == null) {
			return false;
		}

		Authentication auth = secu.getAuthentication();

		if (auth == null) {
			return false;
		}

		Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();

		for (GrantedAuthority authority : authorities) {
			if (role.equals(authority.getAuthority())) {
				logger.info("HOLA usuario " + auth.getName() + " tu rol es" + authority.getAuthority());
				return true;
			}
		}

		return false;
	}

}
