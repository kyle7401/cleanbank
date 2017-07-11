package cleanbank.controllers.rest;

import cleanbank.Service.ITbBranchLocsService;
import cleanbank.domain.TbBranchLocs;
import cleanbank.domain.notEqual;
import cleanbank.repositories.TbBranchLocsRepository;
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

import java.util.ArrayList;

/**
 * Created by hyoseop on 2015-11-16.
 */
@RestController
@RequestMapping("/branchlocs")
public class BranchLocsRestController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private ITbBranchLocsService tbBranchLocsService;

    @Autowired
    public void setITbBranchLocsService(ITbBranchLocsService tbBranchLocsService) {
        this.tbBranchLocsService = tbBranchLocsService;
    }

    @Autowired
    private TbBranchLocsRepository tbBranchLocsRepository;

    //    ----------------------------------------------------------------------------------------------------------------

    @RequestMapping(value = "/list")
    public Iterable<TbBranchLocs> filterList(
            @And({@Spec(path = "bnCd", spec = Equal.class)}) Specification<TbBranchLocs> spec1,
            @Or({@Spec(path="delYn", spec = notEqual.class, constVal = "Y"),
                    @Spec(path="delYn", spec = IsNull.class)}) Specification<TbBranchLocs> spec2,
            @PageableDefault(sort = {"blCd", "blNm"}, size = 1000) Pageable pageable) {

        Iterable<TbBranchLocs> list = null;

        if(spec1 == null) {
            list = new ArrayList<TbBranchLocs>();
        } else  {
            Specification<TbBranchLocs> spec = Specifications.where(spec1).and(spec2);
//            Iterable<TbBranchLocs> list = tbBranchLocsRepository.findAll(spec, pageable);
            list = tbBranchLocsRepository.findAll(spec, pageable);
        }

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
    /*@RequestMapping("/check-code")
    public String checkDups(@RequestParam(value="pdLvl1", defaultValue = "") String pdLvl1,
                                      @RequestParam(value="pdLvl2", defaultValue = "") String pdLvl2,
                                      @RequestParam(value="pdLvl3", defaultValue = "") String pdLvl3) {
        String isPass = "true";
        List<TbBranchLocs> branchlocss = null;

        if(StringUtils.isNotEmpty(pdLvl1) && StringUtils.isNotEmpty(pdLvl2) && StringUtils.isNotEmpty(pdLvl3)) {
            branchlocss = tbBranchLocsRepository.checkDups(pdLvl1, pdLvl2, pdLvl3);
        }

        if(branchlocss != null && branchlocss.size() > 0) {
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
    @RequestMapping("/delete-branchlocs")
    public String deleteCode(@RequestParam(value="no") Integer id) {
        tbBranchLocsService.deleteTbBranchLocs2(id);
        return "ok";
    }

}
