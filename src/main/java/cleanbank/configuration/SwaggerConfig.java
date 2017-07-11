package cleanbank.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by hyoseop on 2015-12-22.
 * http://kimseunghyun76.tistory.com/388
 */

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        ApiInfo apiInfo = new ApiInfo(
                "크린뱅크 REST API",
                "This documents describes about 100min API.",
                "v1",
                "hyoseop@synapsetech.co.kr",
                "synapsetech",
                "http://www.synapsetech.co.kr/", "1"
        );

        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo);
    }
}

/*http://blog.woniper.net/281
@EnableSwagger
@Configuration
public class SwaggerConfig {
    @Autowired private SpringSwaggerConfig springSwaggerConfig;

    @Bean
    public SwaggerSpringMvcPlugin swaggerSpringMvcPlugin() {
        return new SwaggerSpringMvcPlugin(this.springSwaggerConfig)
                .apiInfo(new ApiInfo("spring-boot-swagger", null, null, null, null, null))
                .useDefaultResponseMessages(false)
                .includePatterns("*//*");
    }
}*/
