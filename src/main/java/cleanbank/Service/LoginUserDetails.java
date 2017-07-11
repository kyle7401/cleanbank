package cleanbank.Service;

import cleanbank.viewmodel.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.AuthorityUtils;

/**
 * Created by hyoseop on 2015-12-15.
 */
//@Data
public class LoginUserDetails extends org.springframework.security.core.userdetails.User {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final User user;

    public User getUser() {
        return this.user;
    }

    /**
     * TODO: 최고 권한일 경우 하위 권한도 포함하여 설정을 해주어야 하는지 확인 필요
     * @param user
     */
    public LoginUserDetails(User user) {
//        super(user.getUsername(), user.getPassword(), AuthorityUtils.createAuthorityList("ROLE_USER"));
        super(user.getUsername(), user.getPassword(), AuthorityUtils.createAuthorityList(user.getAuthority()));
        this.user = user;
//        log.debug("LoginUserDetails 실행됨");
    }
}
