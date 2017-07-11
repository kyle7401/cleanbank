package cleanbank.Service;

/**
 * Created by hyoseop on 2015-11-06.
 *
 * http://shruubi.com/2014/12/03/spring-boot-hibernate-and-spring-security-a-step-in-the-right-direction-for-java/
 *
 * http://websystique.com/spring-security/spring-security-4-hibernate-role-based-login-example/
 */

import cleanbank.domain.TbEmployee;
import cleanbank.domain.TbManager;
import cleanbank.domain.TbPartMember;
import cleanbank.utils.SynapseCM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

//@Qualifier("customUserDetailsService")
@Service("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ITbEmployeeService TbEmployeeService;

    @Autowired
    private ITbPartMemberService TbPartMemberService;

    @Autowired
    private ITbManagerService TbManagerService;

    @Value("${emp_default_pic}")
    String emp_default_pic;

    /**
     *
     * @param username
     * @return 권한 종류 : 본사관리자,	지점장, 코디, 공장
     * @throws UsernameNotFoundException
     */
    @Transactional(readOnly=true)
    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {

//      TODO : 사용자 유형에 따라서 Table이 다르기 때문에 구분하여 처리해야 한다
//        JFileController 참고
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String gubun = request.getParameter("user_gubun");

        log.debug("\n\n\n############## 사용자 유형 = {}", gubun);

//        cleanbank.domain.User user = userRepository.findByUsername(username);

        cleanbank.viewmodel.User user = null;

        switch (gubun) {
            case "1":    // 본사관리자
//                TbManager tmpUser1 = TbManagerService.findByMgEmail(username);
                TbManager tmpUser1 = TbManagerService.findByMgEmailAndMgType(username, "0001");

                if(tmpUser1 != null) {
                    user = new cleanbank.viewmodel.User();
                    user.setUsername(tmpUser1.getMgNm());
                    user.setPassword(tmpUser1.getMgPass());
                    user.setEmail(tmpUser1.getMgEmail());
//                    user.setAuthority("ROLE_ADMIN");
                    user.setAuthority(SynapseCM.AuthorityType.ROLE_ADMIN.getAuthorityType());
                    user.setPicture(emp_default_pic);   // 관리자는 디폴트 이미지
                    user.setId(tmpUser1.getId()); // 해당 사용자 pk
                    user.setBnCd(tmpUser1.getBnCd()); // 해당 사용자 지점 id
                }
                break;

            case "2":    // 지점장
                TbManager tmpUser2 = TbManagerService.findByMgEmail(username);

                if(tmpUser2 != null) {
                    user = new cleanbank.viewmodel.User();
                    user.setUsername(tmpUser2.getMgNm());
                    user.setPassword(tmpUser2.getMgPass());
                    user.setEmail(tmpUser2.getMgEmail());
//                    user.setAuthority("ROLE_BRANCH");
                    user.setAuthority(SynapseCM.AuthorityType.ROLE_BRANCH.getAuthorityType());
                    user.setPicture(emp_default_pic);   // 관리자는 디폴트 이미지
                    user.setId(tmpUser2.getId()); // 해당 사용자 pk
                    user.setBnCd(tmpUser2.getBnCd()); // 해당 사용자 지점 id
                }
                break;

            case "3":    // 코디
                TbEmployee tmpUser3 = TbEmployeeService.findByEpEmail(username);

                if(tmpUser3 != null) {
                    user = new cleanbank.viewmodel.User();
                    user.setUsername(tmpUser3.getEpNm());
                    user.setPassword(tmpUser3.getEpPass());
                    user.setEmail(tmpUser3.getEpEmail());
//                    user.setAuthority("ROLE_CODI");
                    user.setAuthority(SynapseCM.AuthorityType.ROLE_CODI.getAuthorityType());
                    user.setPicture(tmpUser3.getEpImg());
                    user.setId(tmpUser3.getEpCd()); // 해당 사용자 pk
                    user.setBnCd(tmpUser3.getBnCd()); // 해당 사용자 지점 id
                }
                break;

            case "4":    // 공장
                TbPartMember tmpUser4 = TbPartMemberService.findByPmEmail(username);

                if(tmpUser4 != null) {
                    user = new cleanbank.viewmodel.User();
                    user.setUsername(tmpUser4.getPmNm());
                    user.setPassword(tmpUser4.getPmPass());
                    user.setEmail(tmpUser4.getPmEmail());
//                    user.setAuthority("ROLE_FACTORY");
                    user.setAuthority(SynapseCM.AuthorityType.ROLE_FACTORY.getAuthorityType());
                    user.setId(tmpUser4.getId()); // 해당 사용자 pk
                    user.setBnCd(tmpUser4.getPtCd()); // 해당 사용자 지점 id
                }
                break;
        }

        if(user == null) {
            throw new UsernameNotFoundException("해당 사용자 정보를 찾을수 없습니다!");
        }

        /*List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER");

        if (username.equals("admin")) {
            authorities = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_ADMIN");
        }

        return buildUserForAuthentication(user, authorities);*/

        log.debug("LoginUserDetails 호출");

//        org.springframework.security.core.userdetails.User 에 없는 추가 정보 사용
        return new LoginUserDetails(user);
    }

    private User buildUserForAuthentication(cleanbank.viewmodel.User user, List<GrantedAuthority> authorities) {
//        return new User(user.getUsername(), user.getPassword(), authorities);
        final String encPwd = user.getPassword();
        return new org.springframework.security.core.userdetails.User(user.getUsername(), encPwd, authorities); // 여기서 비밀번호를 비교하는듯
    }

    /*private List<GrantedAuthority> buildUserAuthority(Set<UserRole> userRoles) {

        Set<GrantedAuthority> setAuths = new HashSet<GrantedAuthority>();

        // Build user's authorities
        for (UserRole userRole : userRoles) {
            setAuths.add(new SimpleGrantedAuthority(userRole.getRoleName()));
        }

        return new ArrayList<GrantedAuthority>(setAuths);
    }*/
}