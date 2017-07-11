package cleanbank.controllers.manage;

import cleanbank.Service.TbPartnerService;
import cleanbank.domain.TbPartner;
import cleanbank.repositories.TbPartnerRepository;
import cleanbank.viewmodel.PartnerSearch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by hyoseop on 2015-11-16.
 */
@RestController
@RequestMapping("/partner")
public class PartnerRestController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private TbPartnerService tbPartnerService;

    @Autowired
    public void setITbPartnerService(TbPartnerService tbPartnerService) {
        this.tbPartnerService = tbPartnerService;
    }

    @Autowired
    private TbPartnerRepository tbPartnerRepository;

    //    ----------------------------------------------------------------------------------------------------------------

    @RequestMapping(value = "/list")
    public Iterable<TbPartner> filterList(final PartnerSearch partnerSearch, @PageableDefault(sort = {"ptNm"}, size = 1000) Pageable pageable) {
        Iterable<TbPartner> List = tbPartnerService.List(partnerSearch, pageable);
        return List;
    }

/*    @RequestMapping(value = "/list")
    public Iterable<TbPartner> filterList(
            @And({@Spec(path = "ptNm", spec = Like.class), @Spec(path = "ptTel", spec = Like.class)}) Specification<TbPartner> spec1,
            @Or({@Spec(path="delYn", spec = notEqual.class, constVal = "Y"),
                    @Spec(path="delYn", spec = IsNull.class)}) Specification<TbPartner> spec2,
            @PageableDefault(sort = {"ptNm", "ptCd"}, size = 1000) Pageable pageable) {

        Specification<TbPartner> spec = Specifications.where(spec1).and(spec2);
        Iterable<TbPartner> list = tbPartnerRepository.findAll(spec, pageable);
        return list;
    }*/

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
        List<TbPartner> partners = null;

        if(StringUtils.isNotEmpty(pdLvl1) && StringUtils.isNotEmpty(pdLvl2) && StringUtils.isNotEmpty(pdLvl3)) {
            partners = tbPartnerLocsRepository.checkDups(pdLvl1, pdLvl2, pdLvl3);
        }

        if(partners != null && partners.size() > 0) {
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
    @RequestMapping("/delete-partner")
    public String deleteCode(@RequestParam(value="no") Integer id) {
        tbPartnerService.deleteTbPartner2(id);
        return "ok";
    }

}
