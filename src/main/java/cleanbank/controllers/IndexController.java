package cleanbank.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by hyoseop on 2015-11-05.
 */
@Controller
public class IndexController {
/*    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SynapseCM synapseCM;*/

    @RequestMapping("/")
    public String index(){
//        System.out.println("\n\n\n############## IndexController 실행!!!!");
/*        log.debug("\n\n\n############## log.debug : IndexController 실행!!!!");
        log.error("\n\n\n############## log.error : IndexController 실행!!!!");*/

        /*String UserNM = synapseCM.getAuthenticatedUserID();
        log.debug("\n\n\n############## 로그인 사용자명 : {}", UserNM);*/

/*        String authority = synapseCM.getAuthorityString();
        log.debug("## 로그인 사용자 권한 = {}", authority);*/

        return "index";
    }
}
