package proyecto.egg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class Application {

	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);

	}

	
	public void runs() {
		
		String password= "12345";
		
		for (int i = 0; i < 2; i++) {
			String bcriptPassword= passwordEncoder.encode(password);
			System.out.println(bcriptPassword);
		}
		
	}
	
}
