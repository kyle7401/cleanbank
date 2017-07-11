package cleanbank.controllers.manage;

import cleanbank.Service.TbBranchService;
import cleanbank.Service.TbManagerService;
import cleanbank.domain.TbManager;
import cleanbank.utils.SynapseCM;
import org.apache.commons.lang3.StringUtils;
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
 * Created by hyoseop on 2015-11-09.
 */
@Controller
@RequestMapping("/manage/manager")
public class ManagerController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private TbManagerService tbManagerService;

    @Autowired
    public void setTbManagerService(TbManagerService tbManagerService) {
        this.tbManagerService = tbManagerService;
    }

    @Inject
    private PropertyEditorRegistrar customPropertyEditorRegistrar;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        this.customPropertyEditorRegistrar.registerCustomEditors(binder);
    }

    private TbBranchService tbBranchService;

    @Autowired
    public void setTbBranchService(TbBranchService tbBranchService) {
        this.tbBranchService = tbBranchService;
    }

//    http://jyukutyo.hatenablog.com/entry/20100512/1273742460
    /*@InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new DateTypeEditor());
        binder.registerCustomEditor(Timestamp.class, new DateTypeEditor());
    }*/

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model){
//        model.addAttribute("managers", tbManagerService.listAllTbManagers());

//        지점콤보
        List<?> branch_cds = tbBranchService.getBranchCds();
        model.addAttribute("branch_cds", branch_cds);

        return "manage/manager/manager_list";
    }

    /**
     * 신규입력
     * @param model
     * @return
     */
//    @RequestMapping("manage/manager/new")
    @RequestMapping("/new")
    public String newmanager(Model model){
//        지점콤보
        List<?> branch_cds = tbBranchService.getBranchCds();
        model.addAttribute("branch_cds", branch_cds);

        TbManager manager = new TbManager();
        manager.setMgDt(new Date());
        manager.setMode("new");

        model.addAttribute("manager", manager);
        return "manage/manager/manager_form";
    }

    /**
     * 저장
     * @param manager
     * @return
     */
    @RequestMapping(value = "/saveManager", method = RequestMethod.POST)
//    public String saveManager(@ModelAttribute("regfrm") TbManager manager, BindingResult bindingResult){
//    public String saveManager(TbManager manager, BindingResult bindingResult){
    public String saveManager(@Valid TbManager manager, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            log.error("\n\n\n############### 저장시 bindingResult.hasErrors()\n{}", bindingResult.toString());
//            return "manage/manager/manager_form";
//            return "error";
        }

        if("new".equals(manager.getMode())) { // 신규
            String password = manager.getMgPass();
            String hashedPassword = cleanbank.utils.PasswordCrypto.getInstance().encrypt(password);
            manager.setMgPass(hashedPassword);

            manager.setRegiDt(new Date());
            manager.setUser(SynapseCM.getAuthenticatedUserID());
        } else { // 수정
//            비밀번호 변경
            String password = manager.getNewPass();

            if(StringUtils.isNotEmpty(password)) {
                String hashedPassword = cleanbank.utils.PasswordCrypto.getInstance().encrypt(password);
                manager.setMgPass(hashedPassword);
            }
        }

        tbManagerService.saveTbManager(manager);
//        return "redirect:/manage/manager/" + manager.getId();
        return "fancyclose";
    }

    @RequestMapping("/{id}")
    public String showmanager(@PathVariable Integer id, Model model){
//        지점콤보
        List<?> branch_cds = tbBranchService.getBranchCds();
        model.addAttribute("branch_cds", branch_cds);

        TbManager manager = tbManagerService.getTbManagerById(id);
        manager.setMode("edit");

        model.addAttribute("manager", manager);
        return "manage/manager/manager_form";
    }

    /*
    @RequestMapping("manager/{id}")
    public String showmanager(@PathVariable Integer id, Model model){
        model.addAttribute("manager", tbManagerService.getTbManagerById(id));
        return "managershow";
    }

    @RequestMapping("manager/edit/{id}")
    public String edit(@PathVariable Integer id, Model model){
        model.addAttribute("manager", tbManagerService.getTbManagerById(id));
        return "managerform";
    }

    @RequestMapping("manage/manager/new")
    public String newmanager(Model model){
        model.addAttribute("manager", new TbManager());
        return "manage/manager/managerform";
    }

    @RequestMapping("manager/delete/{id}")
    public String delete(@PathVariable Integer id){
        tbManagerService.deleteTbManager(id);
        return "redirect:/managers";
    }*/
}
