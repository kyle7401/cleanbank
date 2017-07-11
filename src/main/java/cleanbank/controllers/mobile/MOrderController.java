package cleanbank.controllers.mobile;

import cleanbank.domain.TbMember;
import cleanbank.domain.notEqual;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.IsNull;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Or;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by hyoseop on 2015-12-28.
 */

/**
 * 수정 필요
 */
@RestController
@RequestMapping("/m/order")
public class MOrderController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    /*@Autowired
    private TbMemberRepository tbMemberRepository;*/

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseEntity<?> login(
            @And({
                    @Spec(path = "mbEmail", spec = Equal.class),
                    @Spec(path = "mbDeviceToken", spec = Equal.class)
            }) Specification<TbMember> spec1,
            @Or({
                    @Spec(path="delYn", spec = notEqual.class, constVal = "Y"),
                    @Spec(path="delYn", spec = IsNull.class)
            }) Specification<TbMember> spec2,
            @RequestParam(value="mbNicNm", required=false) final String mbNicNm,
            @RequestParam(value="mbPath", required=false) final String mbPath
    ) {

        TbMember member = new TbMember();

//        검색조건을 모두 입력하지 않았을 경우
        if(spec1 == null) return new ResponseEntity<>("mbEmail, mbDeviceToken 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);

//        누락된 파라메터 확인
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String mbEmail = request.getParameter("mbEmail");
        String mbDeviceToken = request.getParameter("mbDeviceToken");
//        String mbPath = request.getParameter("mbPath");

        if(StringUtils.isEmpty(mbEmail)) return new ResponseEntity<>("mbEmail 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);
        if(StringUtils.isEmpty(mbDeviceToken)) return new ResponseEntity<>("mbDeviceToken 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);
        if(StringUtils.isEmpty(mbNicNm)) return new ResponseEntity<>("mbNicNm 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);
        if(StringUtils.isEmpty(mbPath)) return new ResponseEntity<>("mbPath 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(member, HttpStatus.OK);
    }
}
