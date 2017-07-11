package cleanbank.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Created by hyoseop on 2015-12-15.
 */
//@EnableWebMvcSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true) // http://stackoverflow.com/questions/24865588/how-to-enable-secured-annotations-with-java-based-configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
/*    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Autowired
    private MySavedRequestAwareAuthenticationSuccessHandler authenticationSuccessHandler;*/

//    서버의 Tomcat에 배포하면 로그인은 성공하지만 에러 페이지가 나타나서 "/error", "access", "/login", 페이지 추가함
//    "/swagger-ui-2.1.1/**", "/swagger-resources/**", "/swagger-ui.html", "/swagger-resources/**", "/v2/api-docs", "/configuration/**",

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
                "/error", "/access", "/crud/**", "/api/**", "/webjars/**", "/css/**", "/js/**", "/img/**", "/bootstrap_3_datepicker_4.17.37/**",
                "/emp_img/**", "/ord_items_img/**", "/promotion_img/**", "/jservice_img/**", "/file/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
//                .antMatchers("/", "/loginForm").permitAll()
                .antMatchers("/loginForm", "/login").permitAll()
                .anyRequest().authenticated();

        http.formLogin()
            .loginProcessingUrl("/login")
            .loginPage("/loginForm")
            .failureUrl("/loginForm?error")
            .defaultSuccessUrl("/dashboard")
//            .usernameParameter("username").passwordParameter("password")
            .and();

        http.logout()
            .logoutRequestMatcher(new AntPathRequestMatcher("/logout**"))
            .logoutSuccessUrl("/loginForm?logout")
            .and();

        http.exceptionHandling()
            .accessDeniedPage("/access?error");

//        http://www.baeldung.com/2011/10/31/securing-a-restful-web-service-with-spring-security-3-1-part-3/
/*        http
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(restAuthenticationEntryPoint)
                .and()
                .authorizeRequests()
                .antMatchers("/api*//**").authenticated()
                .and()
                .formLogin()
                .loginProcessingUrl("/j_spring_security_check")
                .usernameParameter("j_username")
                .passwordParameter("j_password")
                .successHandler(authenticationSuccessHandler)
                .failureHandler(new SimpleUrlAuthenticationFailureHandler());*/

        //        상세 iframe 에서 에러가 발생하니 일단 넣어주자
//        https://docs.spring.io/spring-security/site/docs/current/reference/html/csrf.html
//        http://docs.spring.io/spring-security/site/docs/current/reference/html/headers.html
        http.csrf().disable();
        http.headers().frameOptions().disable();
    }

/*    @Bean
    public MySavedRequestAwareAuthenticationSuccessHandler mySuccessHandler(){
        return new MySavedRequestAwareAuthenticationSuccessHandler();
    }
    @Bean
    public SimpleUrlAuthenticationFailureHandler myFailureHandler(){
        return new SimpleUrlAuthenticationFailureHandler();
    }*/

    @Configuration
    static  class AuthenticationConfiguration extends GlobalAuthenticationConfigurerAdapter {
        @Autowired
        @Qualifier("customUserDetailsService")
        private UserDetailsService customUserDetailsService;

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

        @Override
        public void init(AuthenticationManagerBuilder auth) throws  Exception {
            auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
//            보안을 위해서 /api 경로만 inMemoryAuthentication 처리하는 방법 필요
//            http://qiita.com/jun-1/items/c9d1dcdc8d5ee519c138
            /*auth.inMemoryAuthentication()
                    .withUser("admin").password("admin").roles("ADMIN").and()
                    .withUser("user").password("user").roles("USER");*/
        }
    }
}