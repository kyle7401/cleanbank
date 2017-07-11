package cleanbank.controllers.manage;

import cleanbank.Service.TbBranchLocsService;
import cleanbank.Service.TbBranchService;
import cleanbank.domain.TbBranch;
import cleanbank.domain.TbBranchLocs;
import cleanbank.domain.notEqual;
import cleanbank.repositories.TbBranchRepository;
import cleanbank.utils.SynapseCM;
import net.kaczmarzyk.spring.data.jpa.domain.IsNull;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

/**
 * Created by hyoseop on 2015-11-16.
 */
@RestController
@RequestMapping("/branch")
public class BranchRestController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private TbBranchService tbBranchService;

    @Autowired
    public void setITbBranchService(TbBranchService tbBranchService) {
        this.tbBranchService = tbBranchService;
    }

    @Autowired
    private TbBranchRepository tbBranchRepository;

    private TbBranchLocsService tbBranchLocsService;

    @Autowired
    public void setITbBranchLocsService(TbBranchLocsService tbBranchLocsService) {
        this.tbBranchLocsService = tbBranchLocsService;
    }

    //    ----------------------------------------------------------------------------------------------------------------

    @RequestMapping("/getBnBarCd")
    public String getBnBarCd(@RequestParam(value="id") Integer id){
        return tbBranchService.getBnBarCd(id);
    }

    @RequestMapping(value = "/saveLocation", method = RequestMethod.POST)
    public String saveSalesInfo(@Valid TbBranchLocs blInfo, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            log.error("\n\n\n############### 저장시 bindingResult.hasErrors()\n{}", bindingResult.toString());
//            return "error";
        }

//        상/중/하 지역코드를 6자리로 합침
        blInfo.setBlCd(blInfo.getLoc1() + blInfo.getLoc2() + blInfo.getLoc3());

        if("new".equals(blInfo.getMode())) { // 신규
            blInfo.setRegiDt(new Date());
            blInfo.setUser(SynapseCM.getAuthenticatedUserID());
        } else { // 수정
//
        }

        tbBranchLocsService.saveTbBranchLocs(blInfo);
        return "ok";
    }

    @RequestMapping(value = "/list")
    public Iterable<TbBranch> filterList(
            @And({@Spec(path = "bnNm", spec = Like.class)}) Specification<TbBranch> spec1,
            @Or({@Spec(path="delYn", spec = notEqual.class, constVal = "Y"),
                    @Spec(path="delYn", spec = IsNull.class)}) Specification<TbBranch> spec2,
            @PageableDefault(sort = {"bnNm", "bnCeo"}, size = 1000) Pageable pageable) {


        Specification<TbBranch> spec = Specifications.where(spec1).and(spec2);
        Iterable<TbBranch> list = tbBranchRepository.findAll(spec, pageable);
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
        List<TbBranch> branchs = null;

        if(StringUtils.isNotEmpty(pdLvl1) && StringUtils.isNotEmpty(pdLvl2) && StringUtils.isNotEmpty(pdLvl3)) {
            branchs = tbBranchLocsRepository.checkDups(pdLvl1, pdLvl2, pdLvl3);
        }

        if(branchs != null && branchs.size() > 0) {
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
    @RequestMapping("/delete-branch")
    public String deleteCode(@RequestParam(value="no") Integer id) {
        tbBranchService.deleteTbBranch2(id);
        return "ok";
    }

}
