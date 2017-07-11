package cleanbank.controllers.manage;

import cleanbank.Service.TbFaqService;
import cleanbank.domain.TbFaq;
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

/**
 * Created by hyoseop on 2015-11-16.
 */
@Controller
@RequestMapping("/manage/faq")
public class FaqController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Inject
    private PropertyEditorRegistrar customPropertyEditorRegistrar;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        this.customPropertyEditorRegistrar.registerCustomEditors(binder);
    }

//    ----------------------------------------------------------------------------------------------------------------
    private TbFaqService tbFaqService;

    @Autowired
    public void setTbFaqService(TbFaqService tbFaqService) {
        this.tbFaqService = tbFaqService;
    }

//    ----------------------------------------------------------------------------------------------------------------

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model){
        //        상위코드 목록
       /* final List<TbFaq> cdGps = tbFaqService.getcdGps();
        model.addAttribute("cdGps", cdGps);*/

        //model.addAttribute("managers", tbManagerService.listAllTbManagers());
        return "manage/faq/faq_list";
    }

    /**
     * 신규입력
     * @param model
     * @return
     */
    @RequestMapping("/new")
    public String newmanager(Model model){
        TbFaq faq = new TbFaq();

        faq.setMode("new");

        model.addAttribute("faq", faq);
        return "manage/faq/faq_form";
    }

    /**
     * 저장
     * @param faq
     * @return
     */
    @RequestMapping(value = "/saveFaq", method = RequestMethod.POST)
//    public String saveManager(@ModelAttribute("regfrm") TbFaq faq, BindingResult bindingResult){
//    public String saveManager(TbFaq faq, BindingResult bindingResult){
    public String saveFaq(@Valid TbFaq faq, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            log.error("\n\n\n############### 저장시 bindingResult.hasErrors()\n{}", bindingResult.toString());
//            return "error";
        }
        tbFaqService.saveTbFaq(faq);
//        return "redirect:/manage/faq/" + faq.getId();
        return "fancyclose";
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
        TbFaq faq = tbFaqService.getTbFaqById(id);
        faq.setMode("edit");

        model.addAttribute("faq", faq);
        return "manage/faq/faq_form";
    }
}