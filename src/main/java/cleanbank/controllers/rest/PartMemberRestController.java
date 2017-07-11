package cleanbank.controllers.rest;

import cleanbank.Service.TbPartMemberService;
import cleanbank.domain.TbPartMember;
import cleanbank.domain.notEqual;
import cleanbank.repositories.TbPartMemberRepository;
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
import java.util.ArrayList;

/**
 * Created by hyoseop on 2015-11-16.
 */
@RestController
@RequestMapping("/partmember")
public class PartMemberRestController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Inject
    private PropertyEditorRegistrar customPropertyEditorRegistrar;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        this.customPropertyEditorRegistrar.registerCustomEditors(binder);
    }

    private TbPartMemberService tbPartMemberService;

    @Autowired
    public void setITbPartMemberService(TbPartMemberService tbPartMemberService) {
        this.tbPartMemberService = tbPartMemberService;
    }

    @Autowired
    private TbPartMemberRepository tbPartMemberRepository;

    //    ----------------------------------------------------------------------------------------------------------------

    @RequestMapping(value = "/savePartMember", method = RequestMethod.POST)
    public String saveSalesInfo(@Valid TbPartMember partmember, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            log.error("\n\n\n############### 저장시 bindingResult.hasErrors()\n{}", bindingResult.toString());
//            return "error";
        }

        tbPartMemberService.saveTbPartMember(partmember);
        return "ok";
    }

    @RequestMapping(value = "/list")
    public Iterable<TbPartMember> filterList(
            @And({@Spec(path = "ptCd", spec = Equal.class)}) Specification<TbPartMember> spec1,
            @Or({@Spec(path="delYn", spec = notEqual.class, constVal = "Y"),
                    @Spec(path="delYn", spec = IsNull.class)}) Specification<TbPartMember> spec2,
            @PageableDefault(sort = {"pmEmail", "pmNm"}, size = 1000) Pageable pageable) {

        Iterable<TbPartMember> list = null;

        if(spec1 == null) {
            list = new ArrayList<TbPartMember>();
        } else {
            Specification<TbPartMember> spec = Specifications.where(spec1).and(spec2);
            list = tbPartMemberRepository.findAll(spec, pageable);
        }

        return list;
    }

    /**
     * 삭제
     *
     * @param id
     * @return
     */
    @RequestMapping("/delete-partmember")
    public String deleteCode(@RequestParam(value="no") Integer id) {
        tbPartMemberService.deleteTbPartMember2(id);
        return "ok";
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
        List<TbPartMember> partmembers = null;

        if(StringUtils.isNotEmpty(pdLvl1) && StringUtils.isNotEmpty(pdLvl2) && StringUtils.isNotEmpty(pdLvl3)) {
            partmembers = tbPartMemberLocsRepository.checkDups(pdLvl1, pdLvl2, pdLvl3);
        }

        if(partmembers != null && partmembers.size() > 0) {
            isPass = "false";
        }

        return isPass;
    }*/

}
