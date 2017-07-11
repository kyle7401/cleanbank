package cleanbank.controllers.manage;

import cleanbank.Service.TbLocationService;
import cleanbank.domain.TbLocation;
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
@RequestMapping("/manage/location")
public class LocationController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Inject
    private PropertyEditorRegistrar customPropertyEditorRegistrar;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        this.customPropertyEditorRegistrar.registerCustomEditors(binder);
    }

    //    ----------------------------------------------------------------------------------------------------------------
    private TbLocationService tbLocationService;

    @Autowired
    public void setTbLocationService(TbLocationService tbLocationService) {
        this.tbLocationService = tbLocationService;
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
//        상위지역코드
        List<?> cmb_plCd1 = tbLocationService.getPlCd1s();
        model.addAttribute("cmb_plCd1", cmb_plCd1);

        return "manage/location/location_list";
    }

    /**
     * 신규입력
     * @param model
     * @return
     */
    @RequestMapping("/new")
    public String newLoc(Model model){
        TbLocation location = new TbLocation();

        location.setMode("new");

        model.addAttribute("location", location);
        return "manage/location/location_form";
    }

    /**
     * 저장
     * @param location
     * @return
     */
    @RequestMapping(value = "/saveLocation", method = RequestMethod.POST)
    public String saveLocation(@Valid TbLocation location, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            log.error("\n\n\n############### 저장시 bindingResult.hasErrors()\n{}", bindingResult.toString());
//            return "error";
        }

        if("new".equals(location.getMode())) { // 신규
            location.setRegiDt(new Date());
            location.setUser(SynapseCM.getAuthenticatedUserID());
        } else { // 수정
//
        }

        tbLocationService.saveTbLocation(location);
//        return "redirect:/manage/location/" + location.getId();
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
        TbLocation location = tbLocationService.getTbLocationById(id);
        location.setMode("edit");

        model.addAttribute("location", location);
        return "manage/location/location_form";
    }
}
