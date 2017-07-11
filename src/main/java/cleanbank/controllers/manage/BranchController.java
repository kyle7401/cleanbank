package cleanbank.controllers.manage;

import cleanbank.Service.TbBranchService;
import cleanbank.Service.TbLocationService;
import cleanbank.Service.TbPartnerService;
import cleanbank.domain.TbBranch;
import cleanbank.domain.TbBranchLocs;
import cleanbank.utils.SynapseCM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.inject.Inject;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * Created by hyoseop on 2015-11-19.
 */
@Controller
@RequestMapping("/manage/branch")
public class BranchController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Inject
    private PropertyEditorRegistrar customPropertyEditorRegistrar;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        this.customPropertyEditorRegistrar.registerCustomEditors(binder);
    }

    //    ----------------------------------------------------------------------------------------------------------------
    private TbBranchService tbBranchService;

    @Autowired
    public void setTbBranchService(TbBranchService tbBranchService) {
        this.tbBranchService = tbBranchService;
    }

    private TbLocationService tbLocationService;

    @Autowired
    public void setTbLocationService(TbLocationService tbLocationService) {
        this.tbLocationService = tbLocationService;
    }

    private TbPartnerService tbPartnerService;

    @Autowired
    public void setTbPartnerService(TbPartnerService tbPartnerService) {
        this.tbPartnerService = tbPartnerService;
    }

    //    ----------------------------------------------------------------------------------------------------------------
    /**
     * 목록
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model){
        return "manage/branch/branch_list";
    }

    /**
     * 신규입력
     * @param model
     * @return
     */
    @RequestMapping("/new")
    public String newLoc(Model model){
//        공장콤보
        List<?> partner_cds = tbPartnerService.getPartnerCds();
        model.addAttribute("partner_cds", partner_cds);

//        상위지역코드
        List<?> cmb_plCd1 = tbLocationService.getPlCd1s();
        model.addAttribute("cmb_plCd1", cmb_plCd1);

//        중위지역코드 : 그리드용
        List<?> cmb_plCd2 = tbLocationService.getPlCd2s();
        model.addAttribute("cmb_plcd2", cmb_plCd2);

//        하단 담당지역 정보
        TbBranchLocs location = new TbBranchLocs();
        model.addAttribute("location", location);

        TbBranch branch = new TbBranch();

        branch.setMode("new");

        model.addAttribute("branch", branch);
        return "manage/branch/branch_form";
    }

    /**
     * 수정
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/{id}")
    public String showManager(@PathVariable Integer id, Model model){

//        공장콤보
        List<?> partner_cds = tbPartnerService.getPartnerCds();
        model.addAttribute("partner_cds", partner_cds);

//        상위지역코드
        List<?> cmb_plCd1 = tbLocationService.getPlCd1s();
        model.addAttribute("cmb_plcd1", cmb_plCd1);

//        중위지역코드 : 그리드용
        List<?> cmb_plCd2 = tbLocationService.getPlCd2s();
        model.addAttribute("cmb_plcd2", cmb_plCd2);

//        하단 담당지역 정보
        TbBranchLocs location = new TbBranchLocs();
        model.addAttribute("location", location);

        TbBranch branch = tbBranchService.getTbBranchById(id);
        branch.setMode("edit");

        model.addAttribute("branch", branch);
        return "manage/branch/branch_form";
    }

    /**
     * 저장
     * @param branch
     * @return
     */
    @RequestMapping(value = "/saveBranch", method = RequestMethod.POST)
    public String saveBranch(@Valid TbBranch branch, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            log.error("\n\n\n############### 저장시 bindingResult.hasErrors()\n{}", bindingResult.toString());
//            return "error";
        }

        if("new".equals(branch.getMode())) { // 신규
            branch.setRegiDt(new Date());
            branch.setUser(SynapseCM.getAuthenticatedUserID());
        } else { // 수정
//
        }

        tbBranchService.saveTbBranch(branch);
//        return "redirect:/manage/branch/" + branch.getBnCd();
        return "redirect:/manage/branch/list";
    }
}
