package cleanbank.controllers.manage;

import cleanbank.Service.TbManagerService;
import cleanbank.domain.TbManager;
import cleanbank.domain.notEqual;
import cleanbank.repositories.TbManagerRepository;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.IsNull;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Or;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
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

/**
 * Created by hyoseop on 2015-11-16.
 */
@RestController
@RequestMapping("/manage/manager")
public class ManagerRestController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private TbManagerService tbManagerService;

    @Autowired
    public void setTbManagerService(TbManagerService tbManagerService) {
        this.tbManagerService = tbManagerService;
    }

    @Autowired
    private TbManagerRepository tbManagerRepository;
    //    ----------------------------------------------------------------------------------------------------------------

    /**
     * 관리자 이메일 중복 체크
     * @param mgEmail
     * @return
     */
    @RequestMapping("/check-email")
    public String checkMgEmail(@RequestParam(value="mgEmail") String mgEmail) {
        String isPass = "true";

        /*List<TbManager> list = tbManagerService.findByMgEmail(mgEmail);

        if(list != null && list.size() > 0) {
            isPass = "false";
        }*/

        TbManager list = tbManagerService.findByMgEmail(mgEmail);

        if(list != null) {
            isPass = "false";
        }

        return isPass;
    }

    /**
     * 관리자 삭제 처리
     * @param id
     * @return
     */
    @RequestMapping("/delete-user")
    public String deleteUser(@RequestParam(value="no") Integer id) {
        tbManagerService.deleteTbManager2(id);
        return "ok";
    }

    @RequestMapping(value = "/list2")
    public Iterable<TbManager> filterList(
            @And({@Spec(path = "bnCd", spec = Equal.class)}) Specification<TbManager> spec1,
            @Or({@Spec(path="delYn", spec = notEqual.class, constVal = "Y"),
                    @Spec(path="delYn", spec = IsNull.class)}) Specification<TbManager> spec2,
            @PageableDefault(sort = {"bnCd", "mgNm"}, size = 1000) Pageable pageable) {


        Specification<TbManager> spec = Specifications.where(spec1).and(spec2);
        Iterable<TbManager> list = tbManagerRepository.findAll(spec, pageable);
        return list;
//        return this.fnProductList(list);
    }

    /**
     * 관리자계정관리
     * /manage/manager/managers
     * @return
     */
/*    @RequestMapping("/list")
//    public Iterable<TbManager> managerList(@RequestParam(value="bnCd", defaultValue="0") Integer bnCd) {
    public List<TbManager> managerList(@RequestParam(value = "bnCd", defaultValue = "0") Integer bnCd) {
        Iterable<TbManager> list = null;

        if(bnCd <= 0) { // 지점 검색조건이 없을경우
            list = tbManagerService.listAllTbManagers();
        } else  { // 지점 검색조건이 있을경우
            list = tbManagerService.findByBnCd(bnCd);
        }

//        http://www.leveluplunch.com/java/examples/find-element-in-list/
//        http://stackoverflow.com/questions/23932061/convert-iterable-to-stream-using-java-8-jdk
//        http://www.drdobbs.com/jvm/lambdas-and-streams-in-java-8-libraries/240166818
//        삭제여부가 Y인 데이터는 제외, id순으로 정렬
        List<TbManager> managers = StreamSupport.stream(list.spliterator(), false)
                .filter(t -> (t.getDelYn() == null || !t.getDelYn().equals("Y")))
                .sorted((t1, t2) -> t1.getMgEmail().compareTo(t2.getMgEmail()))
                .collect(Collectors.toList());

        return managers;
    }*/
}
