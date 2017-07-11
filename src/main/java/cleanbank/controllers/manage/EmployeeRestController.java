package cleanbank.controllers.manage;

import cleanbank.Service.TbEmployeeService;
import cleanbank.domain.TbEmployee;
import cleanbank.repositories.TbEmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by hyoseop on 2015-11-16.
 */
@RestController
@RequestMapping("/employee")
public class EmployeeRestController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private TbEmployeeService tbEmployeeService;

    @Autowired
    public void setTbEmployeeService(TbEmployeeService tbEmployeeService) {
        this.tbEmployeeService = tbEmployeeService;
    }

    @Autowired
    private TbEmployeeRepository tbEmployeeRepository;

    //    ----------------------------------------------------------------------------------------------------------------

    /*@RequestMapping("/list")
    public Iterable<TbEmployee> filterList(
            @And({@Spec(path = "bnCd", spec = Equal.class), @Spec(path = "epNm", spec = Like.class), @Spec(path = "epPart", spec = Equal.class)}) Specification<TbEmployee> spec1,
            @Or({@Spec(path="delYn", spec = notEqual.class, constVal = "Y"),
                    @Spec(path="delYn", spec = IsNull.class)}) Specification<TbEmployee> spec2,
            @PageableDefault(sort = {"bnCd", "epNm", "epCd"}, size = 1000) Pageable pageable) {

        Specification<TbEmployee> spec = Specifications.where(spec1).and(spec2);
        Iterable<TbEmployee> list = tbEmployeeRepository.findAll(spec, pageable);
        return list;
    }*/

    @RequestMapping(value = "/list")
    public List<?> filterList(
            @RequestParam(value="bncd", required=false) final String bncd,
            /*@RequestParam(value="from", required=false) final String from,
            @RequestParam(value="to", required=false) final String to,*/
            @RequestParam(value="keyword", required=false) final String keyword,
            @RequestParam(value="condition", required=false) final String condition,
            @RequestParam(value="epPart", required=false) final String epPart
    ) {
        List<?> list = tbEmployeeService.getList(bncd, /*from, to,*/ keyword, condition, epPart);
        return list;
    }

    /**
     * 직원(코디) Email 중복체크
     * @param epEmail
     * @return
     */
    @RequestMapping("/check-email")
    public String checkEpEmail(@RequestParam(value="epEmail") String epEmail) {
        String isPass = "true";

        /*List<TbEmployee> list = tbEmployeeService.findByEpEmail(epEmail);

        if(list != null && list.size() > 0) {
            isPass = "false";
        }*/
        TbEmployee list = tbEmployeeService.findByEpEmail(epEmail);

        if(list != null) {
            isPass = "false";
        }

        return isPass;
    }

    /**
     * 직원(코디) 목록
     * @param bnCd
     * @return
     */
//    @RequestMapping("/list")
//    public List<TbEmployee> employeeList(@RequestParam(value = "bnCd", defaultValue = "0") Integer bnCd) {
//        Iterable<TbEmployee> list = null;
//
///*        if(bnCd <= 0) { // 지점 검색조건이 없을경우
//            list = tbEmployeeService.listAllTbManagers();
//        } else  { // 지점 검색조건이 있을경우
//            list = tbManagerService.findByBnCd(bnCd);
//        }*/
//
//        list = tbEmployeeService.listAllTbEmployees();
//
////        http://www.leveluplunch.com/java/examples/find-element-in-list/
////        http://stackoverflow.com/questions/23932061/convert-iterable-to-stream-using-java-8-jdk
////        http://www.drdobbs.com/jvm/lambdas-and-streams-in-java-8-libraries/240166818
////        삭제여부가 Y인 데이터는 제외, id순으로 정렬
//        List<TbEmployee> employees = StreamSupport.stream(list.spliterator(), false)
//                .filter(t -> (t.getDelYn() == null || !t.getDelYn().equals("Y")))
//                .sorted((t1, t2) -> t1.getEpNm().compareTo(t2.getEpNm()))
//                .collect(Collectors.toList());
//
//        return employees;
//    }

    /**
     * 코디 삭제
     *
     * @param id
     * @return
     */
    @RequestMapping("/delete-user")
    public String deleteEmployee(@RequestParam(value="no") Integer id) {
        tbEmployeeService.deleteTbEmployee2(id);
        return "ok";
    }

}
