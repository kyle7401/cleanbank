package cleanbank.controllers.manage;

import cleanbank.Service.TbLocationService;
import cleanbank.domain.TbLocation;
import cleanbank.domain.notEqual;
import cleanbank.repositories.TbLocationRepository;
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
 * Created by hyoseop on 2015-11-19.
 */
@RestController
@RequestMapping("/location")
public class LocationRestController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private TbLocationService tbLocationService;

    @Autowired
    public void setTbLocationService(TbLocationService tbLocationService) {
        this.tbLocationService = tbLocationService;
    }

    @Autowired
    private TbLocationRepository tbLocationRepository;
    //    ----------------------------------------------------------------------------------------------------------------

    @RequestMapping(value = "/list")
    public Iterable<TbLocation> filterList(
            @And({@Spec(path = "loc1", spec = Equal.class), @Spec(path = "loc2", spec = Equal.class), @Spec(params = "nm", path = "name", spec = Like.class)}) Specification<TbLocation> spec1,
            @Or({@Spec(path="delYn", spec = notEqual.class, constVal = "Y"),
                    @Spec(path="delYn", spec = IsNull.class)}) Specification<TbLocation> spec2,
            @PageableDefault(sort = {"loc1", "loc2", "sortOrder", "loc3"}, size = 1000) Pageable pageable) {


        Specification<TbLocation> spec = Specifications.where(spec1).and(spec2);
        Iterable<TbLocation> list = tbLocationRepository.findAll(spec, pageable);
        return list;
    }

    /**
     * 목록
     *
     * @param spec
     * @return
     */
/*    @RequestMapping(value = "/list")
    public List<TbLocation> filterLocation(
            @And({@Spec(params = "nm", path = "name", spec = Like.class)}) Specification<TbLocation> spec,
            @PageableDefault(sort = "loc1, loc2, loc3") Pageable pageable) {

        List<TbLocation> list = tbLocationRepository.findAll(spec);
//        return list;
        return this.fnLocationList(list);
    }

    private List<TbLocation> fnLocationList(Iterable<TbLocation> list) {
//        정렬순서가 null일 경우에 처리 필요
//        http://www.leveluplunch.com/java/examples/find-element-in-list/
//        http://stackoverflow.com/questions/23932061/convert-iterable-to-stream-using-java-8-jdk
//        http://www.drdobbs.com/jvm/lambdas-and-streams-in-java-8-libraries/240166818
//        삭제여부가 Y인 데이터는 제외, id순으로 정렬
        List<TbLocation> locations = StreamSupport.stream(list.spliterator(), false)
                .filter(t -> (t.getDelYn() == null || !t.getDelYn().equals("Y")))
                .sorted((t1, t2) -> t1.getLoc1().compareTo(t2.getLoc1()))
                .collect(Collectors.toList());

        return  locations;
    }*/

    /**
     * 삭제
     *
     * @param id
     * @return
     */
    @RequestMapping("/delete-location")
    public java.lang.String deleteCode(@RequestParam(value="no") Integer id) {
        tbLocationService.deleteTbLocation2(id);
        return "ok";
    }

    @RequestMapping("/check-code")
    public java.lang.String checkDups(@RequestParam(value="loc1", defaultValue = "") java.lang.String loc1,
                                      @RequestParam(value="loc2", defaultValue = "") java.lang.String loc2,
                                      @RequestParam(value="loc3", defaultValue = "") java.lang.String loc3) {
        java.lang.String isPass = "true";
        List<TbLocation> locations = null;

        if(StringUtils.isNotEmpty(loc1) && StringUtils.isNotEmpty(loc2) && StringUtils.isNotEmpty(loc3)) {
            locations = tbLocationRepository.checkDups(loc1, loc2, loc3);
        }

        if(locations != null && locations.size() > 0) {
            isPass = "false";
        }

        return isPass;
    }

    /**
     * 상위코드, 중위코드 중복 여부 확인 : 복잡하니 쿼리로 변경
     *
     * @param spec
     * @return
     */
    /*@RequestMapping("/check-code")
    public String checkMgEmail(@And({@Spec(path = "loc1", spec = Equal.class), @Spec(path = "loc2", spec = Equal.class), @Spec(path = "loc3", spec = Equal.class)
            *//*,@Spec(path = "delYn", spec = notequ.class)*//*}) Specification<TbLocation> spec) {

        String isPass = "true";

//        ((Conjunction)spec).innerSpecs()

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String cdGp = request.getParameter("cdGp");
        String cdIt = request.getParameter("cdIt");

        log.debug("cdGp = {}, cdIt = {}", cdGp, cdIt);

//        상위코드및 하위코드를 모두 입력했을때만 검사
        if(StringUtils.isNotEmpty(cdGp) && StringUtils.isNotEmpty(cdIt)) {

            List<TbLocation> list = tbLocationRepository.findAll(spec);

            if (list != null && list.size() > 0) {

                //        삭제여부가 Y인 데이터는 제외, id순으로 정렬
                List<TbLocation> locations = StreamSupport.stream(list.spliterator(), false)
                        .filter(t -> (t.getDelYn() == null || !t.getDelYn().equals("Y")))
                        .collect(Collectors.toList());

                if (locations != null && locations.size() > 0) {
                    isPass = "false";
                }
            }
        }

        return isPass;
    }*/
}
