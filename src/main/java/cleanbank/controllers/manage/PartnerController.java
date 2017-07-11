package cleanbank.controllers.manage;

import cleanbank.Service.TbPartnerService;
import cleanbank.domain.TbPartner;
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

/**
 * Created by hyoseop on 2015-11-19.
 */
@Controller
@RequestMapping("/manage/partner")
public class PartnerController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Inject
    private PropertyEditorRegistrar customPropertyEditorRegistrar;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        this.customPropertyEditorRegistrar.registerCustomEditors(binder);
    }

    //    ----------------------------------------------------------------------------------------------------------------
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
        return "manage/partner/partner_list";
    }

    /**
     * 신규입력
     * @param model
     * @return
     */
    @RequestMapping("/new")
    public String newLoc(Model model){

//        담당자 정보
        /*TbPartMember partmember = new TbPartMember();
        model.addAttribute("partmember", partmember);*/

//        공장정보
        TbPartner partner = new TbPartner();
        partner.setMode("new");
        model.addAttribute("partner", partner);

        return "manage/partner/partner_form";
    }

    /**
     * 수정
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/{id}")
    public String showsavePartner(@PathVariable Integer id, Model model){
//        담당자 정보
        /*TbPartMember partmember = new TbPartMember();
        model.addAttribute("partmember", partmember);*/

//        공장정보
        TbPartner partner = tbPartnerService.getTbPartnerById(id);
        partner.setMode("edit");
        model.addAttribute("partner", partner);

        return "manage/partner/partner_form";
    }

    /**
     * 저장
     * @param partner
     * @return
     */
    @RequestMapping(value = "/savePartner", method = RequestMethod.POST)
    public String savePartner(@Valid TbPartner partner, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            log.error("\n\n\n############### 저장시 bindingResult.hasErrors()\n{}", bindingResult.toString());
//            return "error";
        }

        if("new".equals(partner.getMode())) { // 신규
            partner.setRegiDt(new Date());
            partner.setUser(SynapseCM.getAuthenticatedUserID());
        } else { // 수정
//
        }

        tbPartnerService.saveTbPartner(partner);
//        return "redirect:/manage/partner/" + partner.getPtCd();
        return "redirect:/manage/partner/list";
    }
}
