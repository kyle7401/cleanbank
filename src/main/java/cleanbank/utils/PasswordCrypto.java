package cleanbank.utils;

/**
 * Created by hyoseop on 2015-11-06.
 */
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordCrypto {

    /*@Autowired
    private PasswordEncoder passwordEncoder;*/

    private PasswordEncoder passwordEncoder;

/*    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }*/

/*    @Bean
    public PasswordEncoder passwordEncoder() {
        if(passwordEncoder == null) {
            passwordEncoder = new BCryptPasswordEncoder();
        }

        return passwordEncoder;
    }*/

    private static PasswordCrypto instance;

    public static PasswordCrypto getInstance() {
        if(instance == null) {
            instance = new PasswordCrypto();
        }

        return instance;
    }

    public PasswordCrypto() {
        if(this.passwordEncoder == null) {
            this.passwordEncoder = new BCryptPasswordEncoder();
        }
    }

/*    public static PasswordEncoder getInstance() {
        if(passwordEncoder == null) {
            passwordEncoder = new BCryptPasswordEncoder();
        }

        return passwordEncoder;
    }*/

    public String encrypt(String str) {
        return passwordEncoder.encode(str);
    }

    public boolean equal(final CharSequence rawPassword, final String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}