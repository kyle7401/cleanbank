package cleanbank.controllers.manage;

import cleanbank.Service.*;
import cleanbank.domain.TbEmployee;
import cleanbank.utils.ExcelView;
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
import org.springframework.web.servlet.View;

import javax.inject.Inject;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * Created by hyoseop on 2015-11-09.
 */
@Controller
@RequestMapping("/manage/employee")
public class EmployeeController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private TbEmployeeService tbEmployeeService;

    @Autowired
    public void setTbEmployeeService(TbEmployeeService tbEmployeeService) {
        this.tbEmployeeService = tbEmployeeService;
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

    private TbCodeService tbCodeService;

    @Autowired
    public void setTbCodeService(TbCodeService tbCodeService) {
        this.tbCodeService = tbCodeService;
    }

    private TbLocationService tbLocationService;

    @Autowired
    public void setTbLocationService(TbLocationService tbLocationService) {
        this.tbLocationService = tbLocationService;
    }

//    ----------------------------------------------------------------------------------------------------------------

    /**
     * TODO : 검색 조건및 결과 조정 필요
     * 코디목록
     * @param model
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model){

//        model.addAttribute("employees", tbEmployeeService.listAllTbEmployees());

//        지점콤보
        List<?> branch_cds = tbBranchService.getBranchCds();
        model.addAttribute("branch_cds", branch_cds);

        //        하위코드 검색 : 담당업무
        List<?> cmb_epPart = tbCodeService.getCdIts07();
        model.addAttribute("cmb_epPart", cmb_epPart);

        return "manage/employee/employee_list";
    }

    /**
     * 신규입력
     * @param model
     * @return
     */
//    @RequestMapping("manage/employee/new")
    @RequestMapping("/new")
    public String newEmployee(Model model){

//        지점콤보
        List<?> branch_cds = tbBranchService.getBranchCds();
        model.addAttribute("branch_cds", branch_cds);

//        하위코드 검색 : 담당업무
        List<?> cmb_epPart = tbCodeService.getCdIts07();
        model.addAttribute("cmb_epPart", cmb_epPart);

//        하위코드 검색 : 코디등급
        List<?> cmb_epLevel = tbCodeService.getCdIts08();
        model.addAttribute("cmb_epLevel", cmb_epLevel);

//        하위코드 검색 : 운전능력
        List<?> cmb_epDrive = tbCodeService.getCdIts09();
        model.addAttribute("cmb_epDrive", cmb_epDrive);

//        담당지역
        List<?> cmb_location = tbLocationService.getLocationCds();
        model.addAttribute("cmb_location", cmb_location);

        TbEmployee employee = new TbEmployee();
        employee.setEpJoinDt(new Date());
        employee.setMode("new");

        model.addAttribute("employee", employee);
        return "manage/employee/employee_form";
    }

    /**
     *
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/{id}")
    public String showEmployee(@PathVariable Integer id, Model model){

//        지점콤보
        List<?> branch_cds = tbBranchService.getBranchCds();
        model.addAttribute("branch_cds", branch_cds);

//        하위코드 검색 : 담당업무
        List<?> cmb_epPart = tbCodeService.getCdIts07();
        model.addAttribute("cmb_epPart", cmb_epPart);

//        하위코드 검색 : 코디등급
        List<?> cmb_epLevel = tbCodeService.getCdIts08();
        model.addAttribute("cmb_epLevel", cmb_epLevel);

//        하위코드 검색 : 운전능력
        List<?> cmb_epDrive = tbCodeService.getCdIts09();
        model.addAttribute("cmb_epDrive", cmb_epDrive);

//        담당지역
        List<?> cmb_location = tbLocationService.getLocationCds();
        model.addAttribute("cmb_location", cmb_location);

        TbEmployee employee = tbEmployeeService.getTbEmployeeById(id);
        employee.setMode("edit");

        model.addAttribute("employee", employee);
        return "manage/employee/employee_form";
    }

    //    config.properties : 직원(코디) 이미지 업로드 폴더및 http base url
    @Value("${emp_img_dir}")
    String emp_img_dir;

    @Value("${emp_img_url}")
    String emp_img_url;

    /**
     * 저장
     * @param employee
     * @return
     */
    @RequestMapping(value = "/saveEmployee", method = RequestMethod.POST)
//    public String saveManager(@ModelAttribute("regfrm") TbManager manager, BindingResult bindingResult){
//    public String saveManager(TbManager manager, BindingResult bindingResult){
    public String saveEmployee(@Valid TbEmployee employee, BindingResult bindingResult, @RequestParam("imgfile") MultipartFile file){
        if (bindingResult.hasErrors()) {
            log.error("\n\n\n############### 저장시 bindingResult.hasErrors()\n{}", bindingResult.toString());
//            return "error";
        }

        if("new".equals(employee.getMode())) { // 신규
            String password = employee.getEpPass();
            String hashedPassword = cleanbank.utils.PasswordCrypto.getInstance().encrypt(password);
            employee.setEpPass(hashedPassword);

            employee.setRegiDt(new Date());
            employee.setUser(SynapseCM.getAuthenticatedUserID());
        } else { // 수정
//            비밀번호 변경
            String password = employee.getNewPass();

            if(StringUtils.isNotEmpty(password)) {
                String hashedPassword = cleanbank.utils.PasswordCrypto.getInstance().encrypt(password);
                employee.setEpPass(hashedPassword);
            }
        }

        tbEmployeeService.saveTbEmployee(employee);

        if (!file.isEmpty()) {
            String strFileName = "emp_img"+ employee.getEpCd();
            String strUploadNm = SynapseCM.imgUpload(file, emp_img_dir, strFileName);

            if(StringUtils.isNotEmpty(strUploadNm)) {
                employee.setEpImg(emp_img_url + strUploadNm);
                tbEmployeeService.saveTbEmployee(employee);
            } else {
                log.error("이미지 업로드 실패!!!");
            }
        }

//        return "redirect:/manage/employee/" + employee.getEpCd();
        return "fancyclose";
    }

    /**
     * 이미지 업로드 테스트
     * @param file
     * @return
     */
/*    @RequestMapping(value="/upload", method=RequestMethod.POST)
    public @ResponseBody
    String handleFileUpload(*//*@RequestParam("name") String name, *//*@RequestParam("file") MultipartFile file) {

        log.debug("config.properties name = {}", emp_img_dir);
        boolean isUpload = SynapseCM.imgUpload(file, emp_img_dir, "emp_img1");
        return  "";
    }*/

    /**
     * TODO : 검색 조건및 결과 조정 필요
     * 엑셀 다운로드
     * @param model
     * @return
     */
    @RequestMapping(value = "/excel", method = RequestMethod.GET)
    public View excel(Model model,
                      @RequestParam(value="bncd", required=false) final String bncd,
                      @RequestParam(value="keyword", required=false) final String keyword,
                      @RequestParam(value="condition", required=false) final String condition,
                      @RequestParam(value="epPart", required=false) final String epPart
    ){
//        List<?> employees = (List<?>) tbEmployeeService.listAllTbEmployees();
        List<?> list = tbEmployeeService.getList(bncd, keyword, condition, epPart);
        model.addAttribute("list", list);

        model.addAttribute("in_fname", "excel/codi_template.xls"); // 템플릿 엑셀 파일명
        model.addAttribute("out_fname", "코디목록"); // 다운로드 받을 엑셀 파일명

        return new ExcelView();
    }
}
