package cleanbank.controllers.rest;

import cleanbank.Service.TbSalesInfoService;
import cleanbank.domain.TbSalesInfo;
import cleanbank.domain.notEqual;
import cleanbank.repositories.TbSalesInfoRepository;
import cleanbank.utils.SynapseCM;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.IsNull;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Or;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.util.Date;

/**
 * Created by hyoseop on 2015-11-16.
 */
@RestController
@RequestMapping("/salesinfo")
public class SalesInfoRestController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private TbSalesInfoService tbSalesInfoService;

    @Autowired
    public void setITbSalesInfoService(TbSalesInfoService tbSalesInfoService) {
        this.tbSalesInfoService = tbSalesInfoService;
    }

    @Autowired
    private TbSalesInfoRepository tbSalesInfoRepository;

    @Inject
    private PropertyEditorRegistrar customPropertyEditorRegistrar;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        this.customPropertyEditorRegistrar.registerCustomEditors(binder);
    }

    //    ----------------------------------------------------------------------------------------------------------------

    @RequestMapping(value = "/saveSalesInfo", method = RequestMethod.POST)
    public String saveSalesInfo(@Valid TbSalesInfo salesinfo, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            log.error("\n\n\n############### 저장시 bindingResult.hasErrors()\n{}", bindingResult.toString());
//            return "error";
        }

        if("new".equals(salesinfo.getMode())) { // 신규
            salesinfo.setRegiDt(new Date());
            salesinfo.setUser(SynapseCM.getAuthenticatedUserID());
        } else { // 수정
//
        }

        tbSalesInfoService.saveTbSalesInfo(salesinfo);
        return "ok";
    }

    @RequestMapping(value = "/list")
    public Iterable<TbSalesInfo> filterList(
            @And({@Spec(path = "bnCd", spec = Equal.class)}) Specification<TbSalesInfo> spec1,
            @Or({@Spec(path="delYn", spec = notEqual.class, constVal = "Y"),
                    @Spec(path="delYn", spec = IsNull.class)}) Specification<TbSalesInfo> spec2,
            @PageableDefault(sort = {"saDate"}, size = 1000) Pageable pageable) {


        Specification<TbSalesInfo> spec = Specifications.where(spec1).and(spec2);
        Iterable<TbSalesInfo> list = tbSalesInfoRepository.findAll(spec, pageable);
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
        List<TbSalesInfo> salesinfos = null;

        if(StringUtils.isNotEmpty(pdLvl1) && StringUtils.isNotEmpty(pdLvl2) && StringUtils.isNotEmpty(pdLvl3)) {
            salesinfos = tbSalesInfoLocsRepository.checkDups(pdLvl1, pdLvl2, pdLvl3);
        }

        if(salesinfos != null && salesinfos.size() > 0) {
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
    @RequestMapping("/delete-salesinfo")
    public String deleteCode(@RequestParam(value="no") Integer id) {
        tbSalesInfoService.deleteTbSalesInfo2(id);
        return "ok";
    }

}
