package cleanbank.controllers.manage;

import cleanbank.Service.TbJserviceService;
import cleanbank.domain.TbJservice;
import cleanbank.utils.SynapseCM;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import javax.validation.Valid;

/**
 * Created by hyoseop on 2015-11-16.
 */
@Controller
@RequestMapping("/manage/jservice")
public class JserviceController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Inject
    private PropertyEditorRegistrar customPropertyEditorRegistrar;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        this.customPropertyEditorRegistrar.registerCustomEditors(binder);
    }

//    ----------------------------------------------------------------------------------------------------------------
    private TbJserviceService tbJserviceService;

    @Autowired
    public void setTbJserviceService(TbJserviceService tbJserviceService) {
        this.tbJserviceService = tbJserviceService;
    }

//    ----------------------------------------------------------------------------------------------------------------

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model){
        //        상위코드 목록
       /* final List<TbJservice> cdGps = tbJserviceService.getcdGps();
        model.addAttribute("cdGps", cdGps);*/

        //model.addAttribute("managers", tbManagerService.listAllTbManagers());
        return "manage/jservice/jservice_list";
    }

    /**
     * 신규입력
     * @param model
     * @return
     */
    @RequestMapping("/new")
    public String newmanager(Model model){
        TbJservice jservice = new TbJservice();

        jservice.setMode("new");

        model.addAttribute("jservice", jservice);
        return "manage/jservice/jservice_form";
    }

    //    상품 관리 이미지 업로드 폴더및 http base url
    @Value("${jservice_img_dir}")
    String jservice_img_dir;

    @Value("${jservice_img_url}")
    String jservice_img_url;

    /**
     * 저장
     * @param jservice
     * @return
     */
    @RequestMapping(value = "/saveJservice", method = RequestMethod.POST)
//    public String saveManager(@ModelAttribute("regfrm") TbJservice jservice, BindingResult bindingResult){
//    public String saveManager(TbJservice jservice, BindingResult bindingResult){
    public String saveJservice(@Valid TbJservice jservice, BindingResult bindingResult, @RequestParam("imgfile") MultipartFile file){
        if (bindingResult.hasErrors()) {
            log.error("\n\n\n############### 저장시 bindingResult.hasErrors()\n{}", bindingResult.toString());
//            return "error";
        }

        tbJserviceService.saveTbJservice(jservice);

        if (!file.isEmpty()) {
            String strFileName = "jservice_img"+ jservice.getId();
            String strUploadNm = SynapseCM.imgUpload(file, jservice_img_dir, strFileName);

            if(StringUtils.isNotEmpty(strUploadNm)) {
                jservice.setJsImg(jservice_img_url + strUploadNm);
                tbJserviceService.saveTbJservice(jservice);
            } else {
                log.error("이미지 업로드 실패!!!");
            }
        }

//        return "redirect:/manage/jservice/" + jservice.getId();
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
    public String showManager(@PathVariable Long id, Model model){
        TbJservice jservice = tbJserviceService.getTbJserviceById(id);
        jservice.setMode("edit");

        model.addAttribute("jservice", jservice);
        return "manage/jservice/jservice_form";
    }
}