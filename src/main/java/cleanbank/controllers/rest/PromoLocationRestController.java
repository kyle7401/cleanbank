package cleanbank.controllers.rest;

import cleanbank.Service.ITbPromoLocationService;
import cleanbank.domain.TbPromoLocation;
import cleanbank.domain.notEqual;
import cleanbank.repositories.TbPromoLocationRepository;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;

/**
 * Created by hyoseop on 2015-11-16.
 */
@RestController
@RequestMapping("/promolocation")
public class PromoLocationRestController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private ITbPromoLocationService tbPromoLocationService;

    @Autowired
    public void setITbPromoLocationService(ITbPromoLocationService tbPromoLocationService) {
        this.tbPromoLocationService = tbPromoLocationService;
    }

    @Autowired
    private TbPromoLocationRepository tbPromoLocationRepository;

    //    ----------------------------------------------------------------------------------------------------------------

    @RequestMapping(value = "/savePromoLocation", method = RequestMethod.POST)
    public String saveSalesInfo(@Valid TbPromoLocation promolocation, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            log.error("\n\n\n############### 저장시 bindingResult.hasErrors()\n{}", bindingResult.toString());
//            return "error";
        }

        tbPromoLocationService.saveTbPromoLocation(promolocation);
        return "ok";
    }

    /**
     * net.kaczmarzyk.example.web.CustomerController 참고
     *
     * @param spec1
     * @param spec2
     * @param pageable
     * @return
     */
    @RequestMapping(value = "/list")
    public Iterable<TbPromoLocation> filterList(
            @And({@Spec(path = "poCd", spec = Equal.class)}) Specification<TbPromoLocation> spec1,
            @Or({@Spec(path="delYn", spec = notEqual.class, constVal = "Y"),
                    @Spec(path="delYn", spec = IsNull.class)}) Specification<TbPromoLocation> spec2,
            @PageableDefault(sort = {"id"}, size = 1000) Pageable pageable) {

        Iterable<TbPromoLocation> list = null;

        if(spec1 == null) {
            list = new ArrayList<TbPromoLocation>();
        } else {
            Specification<TbPromoLocation> spec = Specifications.where(spec1).and(spec2);
            list = tbPromoLocationRepository.findAll(spec, pageable);
        }

        return list;
    }

    /**
     * 주문정보 -> 품목 리스트 : 품목명칭 표시용
     *
     * @param spec1
     * @param spec2
     * @param pageable
     * @return
     */
    @RequestMapping(value = "/list2")
    public Iterable<TbPromoLocation> filterList2(
            @And({@Spec(path = "pdLvl1", spec = notEqual.class),
                    @Spec(path = "pdLvl2", spec = notEqual.class),
                    @Spec(path = "pdLvl3", spec = Equal.class)
//                    ,@Spec(path = "delYn", constVal = "Y", spec = notEqual.class)})
            }) Specification<TbPromoLocation> spec1,
            @Or({@Spec(path="delYn", spec = notEqual.class, constVal = "Y"),
                    @Spec(path="delYn", spec = IsNull.class)}) Specification<TbPromoLocation> spec2,
            @PageableDefault(sort = {"pdLvl1", "pdLvl2", "pdSort", "pdLvl3"}, size = 1000) Pageable pageable) {


        Specification<TbPromoLocation> spec = Specifications.where(spec1).and(spec2);
        Iterable<TbPromoLocation> list = tbPromoLocationRepository.findAll(spec, pageable);
        return list;
    }

    /**
     * 중복확인
     *
     * @param pdLvl1
     * @param pdLvl2
     * @param pdLvl3
     * @return
     */
/*    @RequestMapping("/check-code")
    public String checkDups(@RequestParam(value="pdLvl1", defaultValue = "") String pdLvl1,
                                      @RequestParam(value="pdLvl2", defaultValue = "") String pdLvl2,
                                      @RequestParam(value="pdLvl3", defaultValue = "") String pdLvl3) {
        String isPass = "true";
        List<TbPromoLocation> promolocations = null;

        if(StringUtils.isNotEmpty(pdLvl1) && StringUtils.isNotEmpty(pdLvl2) && StringUtils.isNotEmpty(pdLvl3)) {
            promolocations = tbPromoLocationRepository.checkDups(pdLvl1, pdLvl2, pdLvl3);
        }

        if(promolocations != null && promolocations.size() > 0) {
            isPass = "false";
        }

        return isPass;
    }*/

    /**
     * 삭제
     *
     * @param id
     * @return
     */
    @RequestMapping("/delete-promolocation")
    public String deleteCode(@RequestParam(value="no") Long id) {
        tbPromoLocationService.deleteTbPromoLocation2(id);
        return "ok";
    }

    /**
     * 목록
     *
     * @param spec
     * @return
     */
/*    @RequestMapping(value = "/list")
    public List<TbPromoLocation> filterList(
            @And({@Spec(path = "pdLvl1", spec = Equal.class),
                    @Spec(path = "pdLvl2", spec = Equal.class),
                    @Spec(path = "pdLvl2", spec = Equal.class),
                    @Spec(path = "delYn", constVal = "Y", spec = notEqual.class)}) Specification<TbPromoLocation> spec,
            @PageableDefault(sort = {"pdLvl1", "pdLvl2", "pdSort", "pdLvl3"}) Pageable pageable) {

//        Specification<TbPromoLocation> PromoLocationSpec = Specifications.where().and(nameSpec);

        Iterable<TbPromoLocation> list = tbPromoLocationRepository.findAll(spec, pageable);
//        return list;
        return this.fnPromoLocationList(list);
    }*/

    /*    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        Class<?> typeOnPath = path(root).getJavaType();
        return cb.notEqual(path(root), converter.convert(expectedValue, typeOnPath));
    }*/

//    http://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.special-parameters
//    http://www.petrikainulainen.net/programming/spring-framework/spring-data-jpa-tutorial-part-four-jpa-criteria-queries/
/*    public static Specification<TbPromoLocation> isLongTermCustomer() {
        return new Specification<TbPromoLocation>() {
            public Predicate toPredicate(Root<TbPromoLocation> root, CriteriaQuery<?> query,
                                         CriteriaBuilder builder) {

                return builder.notEqual(root.get(_TbPromoLocation.createdAt), "Y");
            }
        };
    }*/

/*    private List<TbPromoLocation> fnPromoLocationList(Iterable<TbPromoLocation> list) {
//        정렬순서가 null일 경우에 처리 필요
//        http://www.leveluplunch.com/java/examples/find-element-in-list/
//        http://stackoverflow.com/questions/23932061/convert-iterable-to-stream-using-java-8-jdk
//        http://www.drdobbs.com/jvm/lambdas-and-streams-in-java-8-libraries/240166818
//        삭제여부가 Y인 데이터는 제외, id순으로 정렬
        List<TbPromoLocation> locations = StreamSupport.stream(list.spliterator(), false)
                .filter(t -> ( *//*!t.getPdLvl2().equals("00") && !t.getPdLvl3().equals("00") &&*//* (t.getDelYn() == null || !t.getDelYn().equals("Y")) ))
//                .sorted((t1, t2) -> t1.getPdLvl1().compareTo(t2.getPdLvl2()))
                .collect(Collectors.toList());

        return  locations;
    }*/

}
