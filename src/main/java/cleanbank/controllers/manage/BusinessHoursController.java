package cleanbank.controllers.manage;

import cleanbank.Service.TbBranchService;
import cleanbank.domain.TbBranch;
import cleanbank.domain.TbSalesInfo;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hyoseop on 2015-11-19.
 */
@Controller
@RequestMapping("/manage/business_hours")
public class BusinessHoursController {
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
    //    ----------------------------------------------------------------------------------------------------------------

//    영업시간 테이블은 별도로 없고 지점 테이블 사용함

    /**
     * 목록
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model){
        return "manage/business_hours/business_hours_list";
    }

    /**
     * 신규입력
     * @param model
     * @return
     */
    /*@RequestMapping("/new")
    public String newLoc(Model model){
        TbBranch branch = new TbBranch();

        branch.setMode("new");

        model.addAttribute("branch", branch);
        return "manage/business_hours/business_hours_form";
    }*/

    /**
     * 저장
     * @param branch
     * @return
     */
    @RequestMapping(value = "/saveBhours", method = RequestMethod.POST)
    public String saveBranch(@Valid TbBranch branch, BindingResult bindingResult) throws ParseException {
        if (bindingResult.hasErrors()) {
            log.error("\n\n\n############### 저장시 bindingResult.hasErrors()\n{}", bindingResult.toString());
//            return "error";
        }

        TbBranch tmpBranch = this.getBranchHourInfo(branch.getBnCd());

        if(tmpBranch != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");

//            영업시작시간
            /*String strTm1 = branch.getBnOpenTm1() +":"+ branch.getBnOpenTm2();
            Date date1 = formatter.parse(strTm1);*/
            Date date1 = formatter.parse(branch.getbnOpenTm0());
            tmpBranch.setBnOpenTm(date1);

//            영업종료시간
            /*String strTm2 = branch.getBnCloseTm1() +":"+ branch.getBnCloseTm2();
            Date date2 = formatter.parse(strTm2);*/
            Date date2 = formatter.parse(branch.getbnCloseTm0());
            tmpBranch.setBnCloseTm(date2);

//            수거, 배송시간 간격
            tmpBranch.setBnTransTm(branch.getBnTransTm());
            tmpBranch.setBnDeliTm(branch.getBnDeliTm());

//            영업요일
            tmpBranch.setBnMon(branch.getBnMon());
            tmpBranch.setBnTue(branch.getBnTue());
            tmpBranch.setBnWed(branch.getBnWed());
            tmpBranch.setBnThu(branch.getBnThu());
            tmpBranch.setBnFri(branch.getBnFri());
            tmpBranch.setBnSat(branch.getBnSat());
            tmpBranch.setBnSun(branch.getBnSun());

//            수거TO
            tmpBranch.setBnMonTo(branch.getBnMonTo());
            tmpBranch.setBnTueTo(branch.getBnTueTo());
            tmpBranch.setBnWedTo(branch.getBnWedTo());
            tmpBranch.setBnThuTo(branch.getBnThuTo());
            tmpBranch.setBnFriTo(branch.getBnFriTo());
            tmpBranch.setBnSatTo(branch.getBnSatTo());
            tmpBranch.setBnSunTo(branch.getBnSunTo());

            tbBranchService.saveTbBranch(tmpBranch);
        }

//        tbBranchService.saveTbBranch(branch);
//        return "redirect:/manage/business_hours/" + branch.getBnCd();
        return "redirect:/manage/business_hours/list";
    }

    @RequestMapping(value = "/initBHours/{no}")
    public String initBHours(@PathVariable  Integer no) throws ParseException {

//        기존 지점 정보에서 영업시간 정보 필드만 null로 초기화후 저장
        TbBranch tmpBranch = this.getBranchHourInfo(no);

        if(tmpBranch != null) {
            tbBranchService.saveTbBranch(tmpBranch);
        }

//        return "redirect:/manage/business_hours/" + tmpBranch.getBnCd();
        return "redirect:/manage/business_hours/list";
    }

    /**
     * 영업시간 정보 수정및 삭제 처리를 위해 해당 컬럼값을 null로 update후 리턴
     * @param id
     * @return
     */
    private TbBranch getBranchHourInfo(Integer id) {
        TbBranch branch = tbBranchService.getTbBranchById(id);

        branch.setBnOpenTm(null);
        branch.setBnCloseTm(null);
        branch.setBnTransTm(null);
        branch.setBnDeliTm(null);

        branch.setBnMon(null);
        branch.setBnTue(null);
        branch.setBnWed(null);
        branch.setBnThu(null);
        branch.setBnFri(null);
        branch.setBnSat(null);
        branch.setBnSun(null);

        return  branch;
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
//        비영업일 정보
        TbSalesInfo salesinfo = new TbSalesInfo();

        salesinfo.setMode("new");

        model.addAttribute("salesinfo", salesinfo);

//        지사정보
        TbBranch branch = tbBranchService.getTbBranchById(id);
        branch.setMode("edit");

        model.addAttribute("branch", branch);
        return "manage/business_hours/business_hours_form";
    }

}
