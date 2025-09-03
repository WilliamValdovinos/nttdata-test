package cl.nttdata.mantenedor;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    /*
     * Bean de configuración de información básica para Swagger
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Mantenedor de Usuarios")
                        .version("1.0.0")
                        .description("API para gestionar usuarios con JWT y validaciones, Prueba Técnica para NTT Data")
                        .contact(new Contact()
                                .name("William Valdovinos")
                                .email("william.valdovinos@gmail.com")));
    }
}
