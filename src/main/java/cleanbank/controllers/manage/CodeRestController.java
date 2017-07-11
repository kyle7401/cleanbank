package cleanbank.controllers.manage;

import cleanbank.Service.TbCodeService;
import cleanbank.domain.TbCode;
import cleanbank.domain.notEqual;
import cleanbank.repositories.TbCodeRepository;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.IsNull;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Or;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by hyoseop on 2015-11-16.
 */
@RestController
@RequestMapping("/code")
public class CodeRestController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private TbCodeService tbCodeService;

    @Autowired
    public void setTbCodeService(TbCodeService tbCodeService) {
        this.tbCodeService = tbCodeService;
    }

    @Autowired
    private TbCodeRepository tbCodeRepository;

    //    ----------------------------------------------------------------------------------------------------------------

    /**
     * 코드 삭제
     *
     * @param id
     * @return
     */
    @RequestMapping("/delete-code")
    public String deleteCode(@RequestParam(value="no") Integer id) {
        tbCodeService.deleteTbCode(id);
        return "ok";
    }

    /**
     * 코드정보관리 : 검색조건 없음
     * /manage/code/list
     * @return
     */
/*    @RequestMapping("/list")
    public List<TbCode> codeList(@RequestParam(value = "cdGp", defaultValue = "0") Integer bnCd) {
        Iterable<TbCode> list = null;

//        if(bnCd <= 0) { // 지점 검색조건이 없을경우
            list = tbCodeService.listAllTbCodes();
        *//*} else  { // 지점 검색조건이 있을경우
            list = tbCodeService.findByBnCd(bnCd);
        }*//*

        return this.fnCodeList(list);
    }*/

    /**
     * 그룹코드로 검색
     *
     * @param spec
     * @return
     */
/*    @RequestMapping(value = "/list", params = { "cdGp" })
    public List<TbCode> filterCdGp(
            @Spec(path = "cdGp", spec = Equal.class) Specification<TbCode> spec) {

        Iterable<TbCode> list = tbCodeRepository.findAll(spec);

        return this.fnCodeList(list);
    }*/

    /**
     * 코드명으로 검색
     *
     * @param
     * @return
     */
/*    @RequestMapping(value = "/list", params = { "cdNm" })
    public List<TbCode> filterCdNm(
            @Spec(path = "cdNm", spec = Like.class) Specification<TbCode> spec) {

        Iterable<TbCode> list = tbCodeRepository.findAll(spec);

        return this.fnCodeList(list);
    }*/

    @RequestMapping(value = "/list")
    public Iterable<TbCode> filterList(
            @And({@Spec(path = "cdGp", spec = Equal.class),
                    @Spec(path = "cdNm", spec = Like.class)}) Specification<TbCode> spec1,
            @Or({@Spec(path="delYn", spec = notEqual.class, constVal = "Y"),
                    @Spec(path="delYn", spec = IsNull.class)}) Specification<TbCode> spec2,
            @PageableDefault(sort = {"cdGp", "cdSort", "cdIt"}, size = 1000) Pageable pageable) {


        Specification<TbCode> spec = Specifications.where(spec1).and(spec2);
        Iterable<TbCode> list = tbCodeRepository.findAll(spec, pageable);
        return list;
//        return this.fnProductList(list);
    }

/*    @RequestMapping(value = "/list")
    public List<TbCode> filterCdGpCdNm(
            @And({@Spec(path = "cdGp", spec = Equal.class),
                    @Spec(path = "cdNm", spec = Like.class)}) Specification<TbCode> spec) {

        List<TbCode> list = tbCodeRepository.findAll(spec);

        return this.fnCodeList(list);
    }

    private List<TbCode> fnCodeList(Iterable<TbCode> list) {
//        정렬순서가 null일 경우에 처리 필요
//        http://www.leveluplunch.com/java/examples/find-element-in-list/
//        http://stackoverflow.com/questions/23932061/convert-iterable-to-stream-using-java-8-jdk
//        http://www.drdobbs.com/jvm/lambdas-and-streams-in-java-8-libraries/240166818
//        삭제여부가 Y인 데이터는 제외, id순으로 정렬
        List<TbCode> codes = StreamSupport.stream(list.spliterator(), false)
                .filter(t -> (t.getDelYn() == null || !t.getDelYn().equals("Y")))
                .sorted((t1, t2) -> t1.getCdGp().compareTo(t2.getCdGp()))
//                .sorted((t1, t2) -> t1.getCdSort().compareTo(t2.getCdSort()))
                .collect(Collectors.toList());

        return  codes;
    }*/

    /**
     * 상위코드, 중위코드 중복 여부 확인
     *
     * @param spec
     * @return
     */
//    @RequestMapping(value = "/check-code", params = { "cdGp", "cdIt" })
    @RequestMapping("/check-code")
    public String checkMgEmail(@And({@Spec(path = "cdGp", spec = Equal.class),
            @Spec(path = "cdIt", spec = Equal.class)}) Specification<TbCode> spec) {

        String isPass = "true";

//        ((Conjunction)spec).innerSpecs()

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String cdGp = request.getParameter("cdGp");
        String cdIt = request.getParameter("cdIt");

        log.debug("cdGp = {}, cdIt = {}", cdGp, cdIt);

//        상위코드및 하위코드를 모두 입력했을때만 검사
        if(StringUtils.isNotEmpty(cdGp) && StringUtils.isNotEmpty(cdIt)) {

            List<TbCode> list = tbCodeRepository.findAll(spec);

            if (list != null && list.size() > 0) {
                isPass = "false";
            }
        }

        return isPass;
    }
}
