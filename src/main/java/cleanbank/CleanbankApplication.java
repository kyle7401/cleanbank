package cleanbank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.hateoas.UriTemplate;
import org.springframework.hateoas.hal.CurieProvider;
import org.springframework.hateoas.hal.DefaultCurieProvider;

//@EnableJpaRepositories(basePackages = "com.shruubi.repositories")
//@EntityScan(basePackages = "com.shruubi.models")
//@EnableAutoConfiguration
//@Configuration
//@PropertySource(value="classpath:missing.properties", ignoreResourceNotFound=true)
//@ComponentScan("cleanbank")
@PropertySource("classpath:config.properties") // http://stackoverflow.com/questions/31641020/using-spring-propertysource-in-a-maven-submodule
@SpringBootApplication
public class CleanbankApplication extends SpringBootServletInitializer {

//    https://github.com/olivergierke/spring-restbucks.git 참조
    public static String CURIE_NAMESPACE = "100min";

    public @Bean
    CurieProvider curieProvider() {
        return new DefaultCurieProvider(CURIE_NAMESPACE, new UriTemplate("http://localhost:8080/profile/{rel}"));
    }

    // Used when launching as an executable jar or war
    public static void main(String[] args) throws Exception {
//        Hibernate 파라메터 로그에 나타나도록
//        http://docs.jboss.org/hibernate/orm/5.0/topical/html/logging/Logging.html
//        https://github.com/spring-projects/spring-boot/pull/3817
//        Make sure Hibernate's internal logging uses SLF4J (and thus Log4j2)
        /*System.setProperty("file.encoding","UTF-8");
        System.setProperty("charset","UTF-8");*/
//        System.setProperty("org.jboss.logging.provider", "log4j2");

        // TODO : 이니시스 구버전 log4j 때문에 엉망이 되었다.(log4j 2.x 버전만 사용할때는 필요함)
        System.setProperty("org.jboss.logging.provider", "slf4j");
        SpringApplication.run(CleanbankApplication.class, args);
    }

    // Used when deploying to a standalone servlet container
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(CleanbankApplication.class);
    }
}

/*@SpringBootApplication
public class CleanbankApplication {

    public static void main(String[] args) {
        SpringApplication.run(CleanbankApplication.class, args);
    }
}*/
