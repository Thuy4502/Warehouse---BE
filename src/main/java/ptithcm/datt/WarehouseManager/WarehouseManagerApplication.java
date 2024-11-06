package ptithcm.datt.WarehouseManager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "ptithcm.datt.WarehouseManager.repository")
public class WarehouseManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(WarehouseManagerApplication.class, args);
	}

}
