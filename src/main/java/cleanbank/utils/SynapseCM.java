package cleanbank.utils;

import cleanbank.Service.LoginUserDetails;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by hyoseop on 2015-11-11.
 */
@Service
public class SynapseCM {
    private final static Logger log = LoggerFactory.getLogger(SynapseCM.class);

    /**
     * 검색조건: 주 현재일
     * @return
     */
    public static String getToday() {
        DateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
        return sdFormat.format(new Date());
    }

    /**
     * 검색조건: 주 시작일
     * @return
     */
    public static String getWeekFrom() {
        DateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");

        Calendar c = Calendar.getInstance();
        c.setTime(new Date()); // Now use today date.
        c.add(Calendar.DATE, -7); // Adding 5 days

        return sdFormat.format(c.getTime());
    }

    /**
     * 검색조건: 월 시작일
     * @return
     */
    public static String getMonthFrom() {
        DateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");

        Calendar c = Calendar.getInstance();
        c.setTime(new Date()); // Now use today date.
        c.add(Calendar.DATE, -30); // Adding 5 days

        return sdFormat.format(c.getTime());
    }

    public static String whereConcatenator(String strSQL, String strWhere) {

        if (strSQL.toUpperCase().indexOf("WHERE") > 0)
        {
            strWhere = " AND " + strWhere;
        }
        else
        {
            strWhere = " WHERE " + strWhere;
        }

        return strSQL + strWhere;
    }

    public  static String imgUpload(MultipartFile file, final String img_dir, final String name) {
        String strResult = null;

        if (!file.isEmpty()) {
//            폴더가 없으면 만들자
//            http://stackoverflow.com/questions/14666170/create-intermediate-folders-if-one-doesnt-exist
            File f = new File(img_dir);
            f.mkdirs();
//            f.mkdir();

            String newFileName = img_dir + name;
            String fileName = file.getOriginalFilename();
            String extension = "";
            int fileIndex = StringUtils.lastIndexOf(fileName, '.');

            // 파일명과 확장자를 분리
            if (fileIndex != -1) {
                extension = StringUtils.substring(fileName, fileIndex);
                newFileName += extension;
            }

            try {
                byte[] bytes = file.getBytes();
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(newFileName)));
                stream.write(bytes);
                stream.close();
//                return "You successfully uploaded !";
                log.debug("이미지 업로드 성공 {}", newFileName);
                strResult = name + extension; // 업로드한 파일명 리턴
            } catch (Exception e) {
//                return "You failed to upload " + " => " + e.getMessage();
                log.error("이미지 업로드 실패 => {}", e.getMessage());
            }
        } else {
//            return "You failed to upload because the file was empty.";
            log.error("ou failed to upload because the file was empty.");
        }

        return strResult;
    }

//    http://websystique.com/spring-security/spring-security-4-remember-me-example-with-hibernate/
//    http://websystique.com/spring-security/spring-security-4-method-security-using-preauthorize-postauthorize-secured-el/
//    http://websystique.com/spring-security/spring-security-4-password-encoder-bcrypt-example-with-hibernate/
/*    public enum UserProfileType {
        USER("USER"),
        DBA("DBA"),
        ADMIN("ADMIN");

        String userProfileType;

        private UserProfileType(String userProfileType){
            this.userProfileType = userProfileType;
        }

        public String getUserProfileType(){
            return userProfileType;
        }
    }*/

    /**
     * 로그인 사용자 유형 : 본사, 지사, 공장, 관리자
     */
    public enum AuthorityType {
        ROLE_ADMIN("ROLE_ADMIN"),
        ROLE_BRANCH("ROLE_BRANCH"),
        ROLE_CODI("ROLE_CODI"),
        ROLE_FACTORY("ROLE_FACTORY");

        String authorityType;

        private AuthorityType(String authorityType) {
            this.authorityType = authorityType;
        }

        public String getAuthorityType() {
            return authorityType;
        }
    }

    public static Integer getAuthenticatedBnCd() {
        Integer userBnCd = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            userBnCd = ((LoginUserDetails)principal).getUser().getBnCd();
        }/* else {
            userBnCd = principal.toString();
        }*/

        log.debug("## 로그인 사용자 지점 코드 = {}", userBnCd);

        return userBnCd;
    }

    /**
     * 사용자명 얻기
     * @return
     */
    public static String getAuthenticatedUserID() {
        String username = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }

        log.debug("## 로그인 사용자 ID = {}", username);

        return username;
    }

    /**
     * 권한 단일 문자열 얻기
     * @return
     */
    public static String getAuthorityString() {
        String authority = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            authority = ((LoginUserDetails)principal).getUser().getAuthority();
        }/* else {
            username = principal.toString();
        }*/

        log.debug("## 로그인 사용자 권한 = {}", authority);

        return authority;
    }

    /*public static Object getAuthenticatedUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();

        if (authentication == null) {
            log.debug("## authentication object is null!!");
            return null;
        }

        EgovUserDetails details = (EgovUserDetails) authentication.getPrincipal();

        log.debug("## EgovUserDetailsHelper.getAuthenticatedUser : AuthenticatedUser is "
                + details.getUsername());

        return details.getEgovUserVO();
    }*/
}
