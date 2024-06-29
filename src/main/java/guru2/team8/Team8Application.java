package guru2.team8;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
@EnableJpaAuditing
@SpringBootApplication
public class Team8Application {

	public static void main(String[] args) {
		SpringApplication.run(Team8Application.class, args);
	}

}
