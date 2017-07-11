package cleanbank.controllers.manage;

import cleanbank.Service.TbAddressService;
import cleanbank.Service.TbBranchService;
import cleanbank.Service.TbCodeService;
import cleanbank.Service.TbMemberService;
import cleanbank.domain.TbAddress;
import cleanbank.domain.TbMember;
import cleanbank.domain.notEqual;
import cleanbank.repositories.TbAddressRepository;
import cleanbank.utils.ExcelView;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.View;

import javax.inject.Inject;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by hyoseop on 2015-11-19.
 */
//@PreAuthorize("hasRole('ADMIN') AND hasRole('DBA')")
//@Secured("ROLE_ADMIN")
@Controller
@RequestMapping("/manage/member")
public class MemberController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Inject
    private PropertyEditorRegistrar customPropertyEditorRegistrar;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        this.customPropertyEditorRegistrar.registerCustomEditors(binder);
    }

    //    ----------------------------------------------------------------------------------------------------------------
    private TbMemberService tbMemberService;

    @Autowired
    public void setTbMemberService(TbMemberService tbMemberService) {
        this.tbMemberService = tbMemberService;
    }

    private TbBranchService tbBranchService;

    @Autowired
    public void setTbBranchService(TbBranchService tbBranchService) {
        this.tbBranchService = tbBranchService;
    }

/*    private TbAddressService tbAddressService;

    @Autowired
    public void setTbAddressService(TbAddressService tbAddressService) {
        this.tbAddressService = tbAddressService;
    }*/

    private TbCodeService tbCodeService;

    @Autowired
    public void setTbCodeService(TbCodeService tbCodeService) {
        this.tbCodeService = tbCodeService;
    }

    @Autowired
    private TbAddressRepository tbAddressRepository;

    @Autowired
    private TbAddressService tbAddressService;

    //    ----------------------------------------------------------------------------------------------------------------

    /**
     * 목록
     *
     * @param model
     * @return
     */
//    @AuthorizeRequest("hasRole('ROLE_ADMIN')")
//    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model) {

//        지점콤보
        List<?> branch_cds = tbBranchService.getBranchCds();
        model.addAttribute("branch_cds", branch_cds);

//        서비스 상태 콤보
        List<?> cmb_service = tbCodeService.getCdIts06();
        model.addAttribute("cmb_service", cmb_service);

//        고객현황
        Object statistic = tbMemberService.getStatistic();
        model.addAttribute("statistic", statistic);

        return "manage/member/member_list";
    }

    /**
     * 회원에 따른 주소 Combo를 ajax로 처리
     *
     * @param spec1
     * @param spec2
     * @param pageable
     * @return
     */
    @RequestMapping(value = "/mbrAddressCmb")
    public String filterAddress(
            @And({@Spec(path = "mbCd", spec = Equal.class)}) Specification<TbAddress> spec1,
            @Or({@Spec(path="delYn", spec = notEqual.class, constVal = "Y"),
                    @Spec(path="delYn", spec = IsNull.class)}) Specification<TbAddress> spec2,
            @PageableDefault(sort = {"mbAddr1", "id"}, size = 1000) Pageable pageable,
            Model model) {

        Specification<TbAddress> spec = Specifications.where(spec1).and(spec2);

//        회원id 검색조건이 있을때만 검색한다
        Iterable<TbAddress> list = null;

        if (spec1 != null) {
            list = tbAddressRepository.findAll(spec, pageable);
        } else {
            list = new ArrayList<TbAddress>();
        }

        model.addAttribute("itemList", list);
        return "fragments/mbrAddressCmb :: resultsList";
    }

    /**
     * 다음 우편번호및 지동 연동하여 주소 -> 좌표 변환
     *
     * @param model
     * @return
     */
/*    @RequestMapping(value = "/address/{mbCd}", method = RequestMethod.GET)
    public String address(@PathVariable Integer mbCd, Model model) {

        TbMember member = tbMemberService.getTbMemberById(mbCd);
//        member.setMode("edit");
        model.addAttribute("member", member);
        model.addAttribute("mode", "edit");

        TbAddress address = new TbAddress();
        address.setId(0);
        model.addAttribute("address", address);

        return "manage/member/member_address";
    }*/

    /**
     * 다음 우편번호및 지동 연동하여 주소 -> 좌표 변환
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/address/{mbCd}", method = RequestMethod.GET)
    public String address(@PathVariable Integer mbCd, Model model) {
        return view_address(mbCd, "/manage/member", model);
    }

    @RequestMapping(value = "/address2/{mbCd}", method = RequestMethod.GET)
    public String address2(@PathVariable Integer mbCd, Model model) {
        return view_address(mbCd, "/accept/order/member", model);
    }

    private String view_address(final Integer mbCd, final String back_url, Model model) {
        // 뒤로가기시 url
        model.addAttribute("back_url", back_url +"/"+ mbCd);

        TbMember member = tbMemberService.getTbMemberById(mbCd);
//        member.setMode("edit");
        model.addAttribute("member", member);
        model.addAttribute("mode", "edit");

        TbAddress address = new TbAddress();
        address.setId(0);
        model.addAttribute("address", address);

        return "manage/member/member_address";
    }

    /**
     * 다음 우편번호및 지동 연동하여 주소 -> 좌표 변환
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/address/{mbCd}/{id}", method = RequestMethod.GET)
    public String address2(@PathVariable Integer mbCd, @PathVariable Integer id, Model model) {
        return view_address2(mbCd, id, "/manage/member", model);
    }

    @RequestMapping(value = "/address2/{mbCd}/{id}", method = RequestMethod.GET)
    public String address3(@PathVariable Integer mbCd, @PathVariable Integer id, Model model) {
        return view_address2(mbCd, id, "/accept/order/member", model);
    }

    private String view_address2(final Integer mbCd, Integer id, final String back_url, Model model) {
        // 뒤로가기시 url
        model.addAttribute("back_url", back_url +"/"+ mbCd);

        TbMember member = tbMemberService.getTbMemberById(mbCd);
//        member.setMode("edit");
        model.addAttribute("member", member);
        model.addAttribute("mode", "edit");

        TbAddress address = tbAddressRepository.findOne(id);
        model.addAttribute("address", address);

        return "manage/member/member_address";
    }

    /**
     * 신규입력
     * @param model
     * @return
     */
    @RequestMapping("/new")
    public String newLoc(Model model){

//        지점콤보
        List<?> branch_cds = tbBranchService.getBranchCds();
        model.addAttribute("branch_cds", branch_cds);

        //        서비스 상태 콤보
        List<?> cmb_service = tbCodeService.getCdIts06();
        model.addAttribute("cmb_service", cmb_service);

        TbMember member = new TbMember();
        member.setMbJoinDt(new Date());
        member.setMode("new");

        model.addAttribute("member", member);
        return "manage/member/member_form";
    }

    /**
     * 저장
     * @param member
     * @return
     */
    @RequestMapping(value = "/saveMember", method = RequestMethod.POST)
    public String saveMember(@Valid TbMember member, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            log.error("\n\n\n############### 저장시 bindingResult.hasErrors()\n{}", bindingResult.toString());
//            return "error";
        }

        tbMemberService.saveTbMember(member);
//        return "redirect:/manage/member/" + member.getMbCd();
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
//        지점콤보
        List<?> branch_cds = tbBranchService.getBranchCds();
        model.addAttribute("branch_cds", branch_cds);

        //        서비스 상태 콤보
        List<?> cmb_service = tbCodeService.getCdIts06();
        model.addAttribute("cmb_service", cmb_service);

        TbMember member = tbMemberService.getTbMemberById(id);
        member.setMode("edit");

        model.addAttribute("member", member);
        return "manage/member/member_form";
    }

    /**
     * TODO : 검색 조건및 결과 조정 필요
     * 엑셀 다운로드
     * @param model
     * @return
     */
    @RequestMapping(value = "/excel", method = RequestMethod.GET)
    public View excel(Model model,
                      @RequestParam(value = "bncd", required = false) final String bncd,
                    @RequestParam(value="from", required=false) final String from,
                    @RequestParam(value="to", required=false) final String to,
                      @RequestParam(value = "keyword", required = false) final String keyword,
                      @RequestParam(value = "condition", required = false) final String condition,
                      @RequestParam(value = "status", required = false) final String status
    ) {
//        List<TbMember> employees = (List<TbMember>) tbMemberService.listAllMembers();
        List<?> list = tbMemberService.getList(bncd, from, to, keyword, condition, status);
        model.addAttribute("list", list);

        model.addAttribute("in_fname", "excel/member_template.xls"); // 템플릿 엑셀 파일명
        model.addAttribute("out_fname", "고객목록"); // 다운로드 받을 엑셀 파일명

        return new ExcelView();
    }

/*    @RequestMapping(value = "/saveAddress", method = RequestMethod.POST)
    public String saveAddress(@Valid TbAddress address, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            log.error("\n\n\n############### 저장시 bindingResult.hasErrors()\n{}", bindingResult.toString());
//            return "error";
        }

        if("new".equals(address.getMode())) { // 신규
            address.setRegiDt(new Date());
            address.setUser(SynapseCM.getAuthenticatedUserID());
        } else { // 수정
//
        }

        tbAddressService.saveTbAddress(address);
        return "redirect:/manage/member/" + address.getMbCd();
    }*/
}
