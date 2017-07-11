package cleanbank.controllers.manage;

import cleanbank.Service.ITbProductService;
import cleanbank.domain.TbProduct;
import cleanbank.domain.notEqual;
import cleanbank.repositories.TbProductRepository;
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

import java.util.List;

/**
 * Created by hyoseop on 2015-11-16.
 */
@RestController
@RequestMapping("/product")
public class ProductRestController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private ITbProductService tbProductService;

    @Autowired
    public void setITbProductService(ITbProductService tbProductService) {
        this.tbProductService = tbProductService;
    }

    @Autowired
    private TbProductRepository tbProductRepository;

    //    ----------------------------------------------------------------------------------------------------------------

    /**
     * 목록
     *
     * @param spec
     * @return
     */
/*    @RequestMapping(value = "/list")
    public List<TbProduct> filterList(
            @And({@Spec(path = "pdLvl1", spec = Equal.class),
                    @Spec(path = "pdLvl2", spec = Equal.class),
                    @Spec(path = "pdLvl2", spec = Equal.class),
                    @Spec(path = "delYn", constVal = "Y", spec = notEqual.class)}) Specification<TbProduct> spec,
            @PageableDefault(sort = {"pdLvl1", "pdLvl2", "pdSort", "pdLvl3"}) Pageable pageable) {

//        Specification<TbProduct> ProductSpec = Specifications.where().and(nameSpec);

        Iterable<TbProduct> list = tbProductRepository.findAll(spec, pageable);
//        return list;
        return this.fnProductList(list);
    }*/

    /**
     * net.kaczmarzyk.example.web.CustomerController 참고
     *
     * @param spec1
     * @param spec2
     * @param pageable
     * @return
     */
    @RequestMapping(value = "/list")
    public Iterable<TbProduct> filterList(
            @And({@Spec(path = "pdLvl1", spec = Equal.class),
                    @Spec(path = "pdLvl2", spec = Equal.class),
                    @Spec(path = "pdLvl3", spec = Equal.class),
                    @Spec(path = "pdNm", spec = Like.class)
            }) Specification<TbProduct> spec1,
            @Or({@Spec(path="delYn", spec = notEqual.class, constVal = "Y"),
                    @Spec(path="delYn", spec = IsNull.class)}) Specification<TbProduct> spec2,
            @PageableDefault(sort = {"pdLvl1", "pdLvl2", "pdSort", "pdLvl3"}, size = 1000) Pageable pageable) {


        Specification<TbProduct> spec = Specifications.where(spec1).and(spec2);
        Iterable<TbProduct> list = tbProductRepository.findAll(spec, pageable);
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
    public Iterable<TbProduct> filterList2(
            @And({@Spec(path = "pdLvl1", spec = notEqual.class),
                    @Spec(path = "pdLvl2", spec = notEqual.class),
                    @Spec(path = "pdLvl3", spec = Equal.class)
//                    ,@Spec(path = "delYn", constVal = "Y", spec = notEqual.class)})
            }) Specification<TbProduct> spec1,
            @Or({@Spec(path="delYn", spec = notEqual.class, constVal = "Y"),
                    @Spec(path="delYn", spec = IsNull.class)}) Specification<TbProduct> spec2,
            @PageableDefault(sort = {"pdLvl1", "pdLvl2", "pdSort", "pdLvl3"}, size = 1000) Pageable pageable) {


        Specification<TbProduct> spec = Specifications.where(spec1).and(spec2);
        Iterable<TbProduct> list = tbProductRepository.findAll(spec, pageable);
        return list;
    }

/*    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        Class<?> typeOnPath = path(root).getJavaType();
        return cb.notEqual(path(root), converter.convert(expectedValue, typeOnPath));
    }*/

//    http://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.special-parameters
//    http://www.petrikainulainen.net/programming/spring-framework/spring-data-jpa-tutorial-part-four-jpa-criteria-queries/
/*    public static Specification<TbProduct> isLongTermCustomer() {
        return new Specification<TbProduct>() {
            public Predicate toPredicate(Root<TbProduct> root, CriteriaQuery<?> query,
                                         CriteriaBuilder builder) {

                return builder.notEqual(root.get(_TbProduct.createdAt), "Y");
            }
        };
    }*/

/*    private List<TbProduct> fnProductList(Iterable<TbProduct> list) {
//        정렬순서가 null일 경우에 처리 필요
//        http://www.leveluplunch.com/java/examples/find-element-in-list/
//        http://stackoverflow.com/questions/23932061/convert-iterable-to-stream-using-java-8-jdk
//        http://www.drdobbs.com/jvm/lambdas-and-streams-in-java-8-libraries/240166818
//        삭제여부가 Y인 데이터는 제외, id순으로 정렬
        List<TbProduct> locations = StreamSupport.stream(list.spliterator(), false)
                .filter(t -> ( *//*!t.getPdLvl2().equals("00") && !t.getPdLvl3().equals("00") &&*//* (t.getDelYn() == null || !t.getDelYn().equals("Y")) ))
//                .sorted((t1, t2) -> t1.getPdLvl1().compareTo(t2.getPdLvl2()))
                .collect(Collectors.toList());

        return  locations;
    }*/

    /**
     * 중복확인
     *
     * @param pdLvl1
     * @param pdLvl2
     * @param pdLvl3
     * @return
     */
    @RequestMapping("/check-code")
    public java.lang.String checkDups(@RequestParam(value="pdLvl1", defaultValue = "") java.lang.String pdLvl1,
                                      @RequestParam(value="pdLvl2", defaultValue = "") java.lang.String pdLvl2,
                                      @RequestParam(value="pdLvl3", defaultValue = "") java.lang.String pdLvl3) {
        java.lang.String isPass = "true";
        List<TbProduct> products = null;

        if(StringUtils.isNotEmpty(pdLvl1) && StringUtils.isNotEmpty(pdLvl2) && StringUtils.isNotEmpty(pdLvl3)) {
            products = tbProductRepository.checkDups(pdLvl1, pdLvl2, pdLvl3);
        }

        if(products != null && products.size() > 0) {
            isPass = "false";
        }

        return isPass;
    }

    /**
     * 삭제
     *
     * @param id
     * @return
     */
    @RequestMapping("/delete-product")
    public java.lang.String deleteCode(@RequestParam(value="no") Integer id) {
        tbProductService.deleteTbProduct2(id);
        return "ok";
    }

}
