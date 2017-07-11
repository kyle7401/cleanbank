package cleanbank.configuration;

/**
 * Created by hyoseop on 2015-11-05.
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Created by hyoseop on 2015-10-22.
 * \resources\static 에 접근할때는 로그인 하지 않아도 되도록 : http://stackoverflow.com/questions/24916894/serving-static-web-resources-in-spring-boot-spring-security-application
 * \resources\static 에 절대경로(/)로 접근하는 방법?
 * @Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
 */

/**
 * 아래소스는 웹에서 검색한 내요을 짜집기 한것으로 잘 이해가 되지 않아
 * 가장 빨리 만나는 스프링 부트 책을 참고하여 SecurityConfig.java 파일로 대체함
 */
public class SecurityConfiguration {
/*@Configuration
@EnableWebMvcSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {*/

    //    @Autowired
    private static PasswordEncoder encoder;

    @Bean
    public PasswordEncoder passwordEncoder() {
        if(encoder == null) {
            encoder = new BCryptPasswordEncoder();
        }

        return encoder;
    }

//    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
    }

    @Autowired
    private UserDetailsService customUserDetailsService;

    /*@Autowired
    private CustomUserDetailsService customUserDetailsService;*/

//    http://stackoverflow.com/questions/30490862/configuring-spring-boot-security-to-use-bcrypt-password-encoding-in-grails-3-0
    /*@Autowired
    BCryptPasswordEncoder bcryptEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService).passwordEncoder(bcryptEncoder);
    }*/

/*
    /*CustomUserDetailsService customUserDetailsService;

    @Bean
    public CustomUserDetailsService customUserDetailsService() {
        if(customUserDetailsService == null) {
            customUserDetailsService = new CustomUserDetailsService();
        }

        return customUserDetailsService;
    }*/

    /*@Autowired
    public void setUserDetailsService(UserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }*/

//    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        // @formatter:off
/*        httpSecurity.authorizeRequests().antMatchers("/login").permitAll().anyRequest()
                .fullyAuthenticated().and().formLogin().loginPage("/login")
                .failureUrl("/login?error").and().logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout")).and()
                .exceptionHandling().accessDeniedPage("/access?error");*/
        // @formatter:on

//        httpSecurity.authorizeRequests().antMatchers("/").permitAll();

        // @formatter:off
        httpSecurity
                .authorizeRequests()
                .antMatchers("/console*")
                .permitAll()
        .and()
                .authorizeRequests()
                .antMatchers("/", "/css*", "/js*", "/images*", "/home")
                .permitAll()
        .and()
                .authorizeRequests()
                .antMatchers("/login")
                .permitAll()
                /*.anyRequest()
                .fullyAuthenticated()*/
        .and()
                .formLogin()
                .loginPage("/login")
                .failureUrl("/login?error")
        .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
        .and()
                .exceptionHandling()
                .accessDeniedPage("/access?error")
                /*.anyRequest().authenticated() // 이외에는 모두 인증필요?
            .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
            .and()
                .logout()
                .permitAll()*/;
        // @formatter:on

//        http://gokgo.tistory.com/112
       /* httpSecurity
                .authorizeRequests()
                .antMatchers("/")
                .permitAll()
                .and()
                .authorizeRequests()
                .antMatchers("/console*//**")
         .permitAll();*/

        httpSecurity.csrf().disable();
        httpSecurity.headers().frameOptions().disable();
    }
}