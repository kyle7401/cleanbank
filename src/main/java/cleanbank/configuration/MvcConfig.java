package cleanbank.configuration;

/**
 * Created by hyoseop on 2015-11-06.
 */
import net.kaczmarzyk.spring.data.jpa.web.SpecificationArgumentResolver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

//@EnableWebMvc : 이게 있으면 로그인등 기본적인 쿼리 실행시 에러 발생
//@EnableHypermediaSupport(type= {EnableHypermediaSupport.HypermediaType.HAL}) 이게 있으면 id 컬럼값이 넘어오지 않아서 script error 발생
@Configuration
@EnableSpringDataWebSupport
public class MvcConfig extends WebMvcConfigurerAdapter {

//    http://docs.spring.io/spring-hateoas/docs/current/reference/html/
/*    public static String CURIE_NAMESPACE = "100min";

    @Bean
    public CurieProvider curieProvider() {
        return new DefaultCurieProvider(CURIE_NAMESPACE, new UriTemplate("http://localhost:8080/rels/{rel}"));
    }*/

//    http://arahansa.github.io/docs_spring/jpa.html
    /*@Configuration
    @EnableJpaAuditing
    class Config {

        @Bean
        public AuditorAware<AuditableUser> auditorProvider() {
            return new AuditorAwareImpl();
        }
    }*/

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/test").setViewName("test/test");
        /*registry.addViewController("/").setViewName("home");
        registry.addViewController("/hello").setViewName("hello");
        registry.addViewController("/login").setViewName("login");*/
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/loginForm").setViewName("login"); // 12월15일 login -> loginForm 으로 수정
        registry.addViewController("/access").setViewName("access");
//        registry.addViewController("/construction").setViewName("construction");
        registry.addViewController("/error").setViewName("error");

        registry.addViewController("/api/privacy").setViewName("privacy");
        registry.addViewController("/api/term").setViewName("term");
    }

    //    config.properties : 직원(코디) 이미지 업로드 폴더및 http base url
    @Value("${emp_img_dir}")
    String emp_img_dir;

    @Value("${emp_img_url}")
    String emp_img_url;

    //    config.properties : 품목 리스트의 품목 정보 이미지 업로드 폴더및 http base url
    @Value("${ord_items_img_dir}")
    String ord_items_img_dir;

    @Value("${ord_items_img_url}")
    String ord_items_img_url;

    //    config.properties : 프로모션) 이미지 업로드 폴더및 http base url
    @Value("${promotion_img_dir}")
    String promotion_img_dir;

    @Value("${promotion_img_url}")
    String promotion_img_url;

    //    상품 관리 이미지 업로드 폴더및 http base url
    @Value("${jservice_img_dir}")
    String jservice_img_dir;

    @Value("${jservice_img_url}")
    String jservice_img_url;

    //    http://stackoverflow.com/questions/21123437/how-do-i-use-spring-boot-to-serve-static-content-located-in-dropbox-folder
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/emp_img/**").addResourceLocations("file:/home/cleanbank/emp_img/");
        registry.addResourceHandler(emp_img_url +"**").addResourceLocations("file:"+ emp_img_dir);
        registry.addResourceHandler(ord_items_img_url +"**").addResourceLocations("file:"+ ord_items_img_dir);
        registry.addResourceHandler(promotion_img_url +"**").addResourceLocations("file:"+ promotion_img_dir);
        registry.addResourceHandler(jservice_img_url +"**").addResourceLocations("file:"+ jservice_img_dir);
    }

    //    http://blog.kaczmarzyk.net/2014/03/23/alternative-api-for-filtering-data-with-spring-mvc-and-spring-data/
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new SpecificationArgumentResolver());
        argumentResolvers.add(new PageableHandlerMethodArgumentResolver());
    }


}

/*스프링 데이터 jpa 레퍼런스 번역
http://arahansa.github.io/docs_spring/jpa.html*/
