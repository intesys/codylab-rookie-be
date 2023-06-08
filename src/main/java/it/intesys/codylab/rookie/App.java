package it.intesys.codylab.rookie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//    http://localhost:8080/v3/api-docs/codylab
//    http://localhost:8080/swagger-ui/index.html
@SpringBootApplication
public class App {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(App.class);
        app.run(args);
    }
}
