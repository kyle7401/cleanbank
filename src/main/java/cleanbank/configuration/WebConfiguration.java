package cleanbank.configuration;

import nz.net.ultraq.thymeleaf.LayoutDialect;
import org.h2.server.web.WebServlet;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.TemplateResolver;

/**
 * Created by hyoseop on 2015-11-05.
 * 청춘fm은 이게 없어도 잘 실행 되었는데, 갑자기 백민 html이 깨진다
 */
//@Configuration
public class WebConfiguration {

//    https://spring.io/blog/2015/06/08/cors-support-in-spring-framework
    /*@Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/api*//**");
                registry.addMapping("*//**");
            }
        };
    }*/

    /**
     * http://docs.spring.io/spring/docs/current/spring-framework-reference/html/validation.html#core-convert-Spring-config
     * http://stackoverflow.com/questions/30039619/how-to-autowired-in-conversionservice-in-springboot
     * http://stackoverflow.com/questions/11273443/how-to-configure-spring-conversionservice-with-java-config
     *
     * @return
     */
/*    @Bean(name="conversionService")
    public ConversionService getConversionService() {
        ConversionServiceFactoryBean bean = new ConversionServiceFactoryBean();
        bean.setConverters(...); //add converters
        bean.afterPropertiesSet();
        return bean.getObject();
    }*/

//    http://blog.kaczmarzyk.net/2015/01/04/loading-view-templates-from-database-with-thymeleaf/
    @Bean
    ServletRegistrationBean h2servletRegistration() {
        ServletRegistrationBean registrationBean = new ServletRegistrationBean(new WebServlet());
        registrationBean.addUrlMappings("/console/*");
        return registrationBean;
    }

    @Bean
    public TemplateResolver springThymeleafTemplateResolver() {
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        resolver.setPrefix("classpath:/templates/");
        resolver.setSuffix(".html");
        resolver.setOrder(1);
        return resolver;
    }

    /*@Bean
    public DbTemplateResolver dbTemplateResolver() {
        DbTemplateResolver resolver = new DbTemplateResolver();
        resolver.setOrder(2);
        return resolver;
    }*/

    /**
     * http://www.thymeleaf.org/doc/articles/layouts.html
     *
     * @return
     */
    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
//        ...
        templateEngine.addDialect(new LayoutDialect());
        templateEngine.addDialect(springSecurityDialect()); // http://qiita.com/jun-1/items/c9d1dcdc8d5ee519c138

//        http://blog.kaczmarzyk.net/2015/01/04/loading-view-templates-from-database-with-thymeleaf/
//        templateEngine.setTemplateResolvers(Sets.newHashSet(springThymeleafTemplateResolver(), dbTemplateResolver()));
        templateEngine.setTemplateResolver(springThymeleafTemplateResolver());

        return templateEngine;
    }

    /**
     * http://qiita.com/jun-1/items/c9d1dcdc8d5ee519c138
     * @return
     */
    @Bean
    public IDialect springSecurityDialect() {
        SpringSecurityDialect dialect = new SpringSecurityDialect();
        return dialect;
    }

    private final String CHARACTER_ENCODING = "UTF-8";

    /**
     * http://qiita.com/jun-1/items/c9d1dcdc8d5ee519c138
     * @return
     */
    @Bean
    public ThymeleafViewResolver thymeleafViewResolver() {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(templateEngine());
        resolver.setCharacterEncoding(CHARACTER_ENCODING);
        return resolver;
    }
}