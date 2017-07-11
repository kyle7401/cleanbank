package cleanbank.controllers.manage;

import cleanbank.Service.TbCodeService;
import cleanbank.domain.TbCode;
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
 * Created by hyoseop on 2015-11-16.
 */
@Controller
@RequestMapping("/manage/code")
public class CodeController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Inject
    private PropertyEditorRegistrar customPropertyEditorRegistrar;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        this.customPropertyEditorRegistrar.registerCustomEditors(binder);
    }

//    ----------------------------------------------------------------------------------------------------------------
    private TbCodeService tbCodeService;

    @Autowired
    public void setTbCodeService(TbCodeService tbCodeService) {
        this.tbCodeService = tbCodeService;
    }

//    ----------------------------------------------------------------------------------------------------------------

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model){
        //        상위코드 목록
        final List<TbCode> cdGps = tbCodeService.getcdGps();
        model.addAttribute("cdGps", cdGps);

        //model.addAttribute("managers", tbManagerService.listAllTbManagers());
        return "manage/code/code_list";
    }

    /**
     * 신규입력
     * @param model
     * @return
     */
    @RequestMapping("/new")
    public String newmanager(Model model){
        TbCode code = new TbCode();

        code.setMode("new");

        model.addAttribute("code", code);
        return "manage/code/code_form";
    }

    /**
     * 저장
     * @param code
     * @return
     */
    @RequestMapping(value = "/saveCode", method = RequestMethod.POST)
//    public String saveManager(@ModelAttribute("regfrm") TbCode code, BindingResult bindingResult){
//    public String saveManager(TbCode code, BindingResult bindingResult){
    public String saveCode(@Valid TbCode code, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            log.error("\n\n\n############### 저장시 bindingResult.hasErrors()\n{}", bindingResult.toString());
//            return "error";
        }

        if("new".equals(code.getMode())) { // 신규
            code.setRegiDt(new Date());
            code.setUser(SynapseCM.getAuthenticatedUserID());
        } else { // 수정
//
        }

        tbCodeService.saveTbCode(code);
//        return "redirect:/manage/code/" + code.getId();
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
        TbCode code = tbCodeService.getTbCodeById(id);
        code.setMode("edit");

        model.addAttribute("code", code);
        return "manage/code/code_form";
    }
}