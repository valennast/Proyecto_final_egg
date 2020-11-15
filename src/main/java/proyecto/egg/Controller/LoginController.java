package proyecto.egg.Controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {

	
	@GetMapping("/login")
	public String login(@RequestParam(value="error", required = false) String erro,
			@RequestParam(value="logout", required = false) String logout,
			Model model, Principal principal,RedirectAttributes flash) {
		if (principal!=null) {
			flash.addFlashAttribute("info","Ya se inicio sesion con ese ususario");
			return"redirect:/";
		}
		
		if (erro!=null) {
			model.addAttribute("error", "Error en el Login: Nombre o contraseña incorrectos,vuelva a intentar");
		}
		
		if (logout!=null) {
			model.addAttribute("success", "ha cerrado sesion con exito");
		}
		
		return ("login");
	}
	
}
