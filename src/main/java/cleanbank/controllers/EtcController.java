package cleanbank.controllers;

import cleanbank.Service.TbBranchLocsService;
import cleanbank.Service.TbPartMemberService;
import cleanbank.domain.*;
import cleanbank.repositories.TbLocationRepository;
import cleanbank.repositories.TbProductRepository;
import cleanbank.viewmodel.CodeValue;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.IsNull;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Or;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by hyoseop on 2015-11-24.
 */
@Controller
public class EtcController {

    @Autowired
    private TbProductRepository tbProductRepository;

    @Autowired
    private TbLocationRepository tbLocationRepository;

    private TbPartMemberService tbPartMemberService;

    @Autowired
    public void setTbPartMemberService(TbPartMemberService tbPartMemberService) {
        this.tbPartMemberService = tbPartMemberService;
    }

    @Autowired
    private TbBranchLocsService tbBranchLocsService;

//    ---------------------------------------------------------------------------------------------------

    @RequestMapping("/etc/getTbBranchLocsByBnCd")
    public String getTbBranchLocsByBnCd(@RequestParam(value="bnCd") Integer bnCd, Model model) {
        List<CodeValue> itemList = tbBranchLocsService.getTbBranchLocsByBnCd(bnCd);
        model.addAttribute("itemList", itemList);

        return "fragments/BranchLocationCombo :: resultsList";
    }

/*    @RequestMapping("/etc/ProductName")
    public String ProductName(
            @And({@Spec(path = "pdLvl1", spec = Equal.class),
                    @Spec(path = "pdLvl2", spec = notEqual.class),
                    @Spec(path = "pdLvl3", spec = Equal.class)
            }) Specification<TbProduct> spec1,
            @Or({@Spec(path="delYn", spec = notEqual.class, constVal = "Y"),
                    @Spec(path="delYn", spec = IsNull.class)}) Specification<TbProduct> spec2,
            @PageableDefault(sort = {"pdLvl1", "pdLvl2", "pdSort", "pdLvl3"}, size = 1000) Pageable pageable, Model model) {


        Specification<TbProduct> spec = Specifications.where(spec1).and(spec2);
        Iterable<TbProduct> list = tbProductRepository.findAll(spec, pageable);

        List<TbProduct> itemList = StreamSupport.stream(list.spliterator(), false).collect(Collectors.toList());

        String strName = null;

        if(itemList)

        return strName;
    }*/

    @RequestMapping("/etc/PartMemberForm")
    public String PartMemberForm(@RequestParam(value="no") Integer id, @RequestParam(value="cd", required=false) Integer ptCd, Model model) {

//        담당자 정보
        TbPartMember partmember = null;

        if(id == null || id <= 0) { // 신규
            partmember = new TbPartMember();
            partmember.setPtCd(ptCd);
            partmember.setMode("new");
        } else { // 수정
            partmember = tbPartMemberService.getTbPartMemberById(id);
        }

        model.addAttribute("partmember", partmember);

        return "fragments/PartMemberForm :: resultsList";
//        return "fragments/myView :: #cmbitem";
    }

    @RequestMapping("/etc/LocationCombo2")
    public String LocationCombo2(
            @And({@Spec(path = "loc1", spec = Equal.class),
                    @Spec(path = "loc2", spec = notEqual.class),
                    @Spec(path = "loc3", spec = Equal.class)
            }) Specification<TbLocation> spec1,
            @Or({@Spec(path="delYn", spec = notEqual.class, constVal = "Y"),
                    @Spec(path="delYn", spec = IsNull.class)}) Specification<TbLocation> spec2,
            @PageableDefault(sort = {"loc1", "loc2", "sortOrder", "loc3"}, size = 1000) Pageable pageable, Model model) {


        Specification<TbLocation> spec = Specifications.where(spec1).and(spec2);
        Iterable<TbLocation> list = tbLocationRepository.findAll(spec, pageable);

        List<TbLocation> itemList = StreamSupport.stream(list.spliterator(), false).collect(Collectors.toList());
        model.addAttribute("itemList", itemList);

        return "fragments/LocationCombo2 :: resultsList";
//        return "fragments/myView :: #cmbitem";
    }

    @RequestMapping("/etc/LocationCombo3")
    public String LocationCombo3(
            @And({@Spec(path = "loc1", spec = Equal.class),
                    @Spec(path = "loc2", spec = Equal.class),
                    @Spec(path = "loc3", spec = notEqual.class)
            }) Specification<TbLocation> spec1,
            @Or({@Spec(path="delYn", spec = notEqual.class, constVal = "Y"),
                    @Spec(path="delYn", spec = IsNull.class)}) Specification<TbLocation> spec2,
            @PageableDefault(sort = {"loc1", "loc2", "sortOrder", "loc3"}, size = 1000) Pageable pageable, Model model) {


        Specification<TbLocation> spec = Specifications.where(spec1).and(spec2);
        Iterable<TbLocation> list = tbLocationRepository.findAll(spec, pageable);

        List<TbLocation> itemList = StreamSupport.stream(list.spliterator(), false).collect(Collectors.toList());
        model.addAttribute("itemList", itemList);

        return "fragments/LocationCombo3 :: resultsList";
//        return "fragments/myView :: #cmbitem";
    }

    /**
     * 품목명 중위코드 콤보박스 Ajax 처리
     *
     * http://www.thymeleaf.org/doc/tutorials/2.1/thymeleafspring.html#ajax-fragments
     * http://xpadro.blogspot.in/2014/02/thymeleaf-integration-with-spring-part-2.html
     * https://unpocodejava.wordpress.com/2014/04/10/ajax-y-thymeleaf-fragments/
     *
     * @param spec1
     * @param spec2
     * @param pageable
     * @param model
     * @return
     */
    @RequestMapping("/etc/ProductCombo2")
    public String comboList2(
            @And({@Spec(path = "pdLvl1", spec = Equal.class),
                    @Spec(path = "pdLvl2", spec = notEqual.class),
                    @Spec(path = "pdLvl3", spec = Equal.class)
            }) Specification<TbProduct> spec1,
            @Or({@Spec(path="delYn", spec = notEqual.class, constVal = "Y"),
                    @Spec(path="delYn", spec = IsNull.class)}) Specification<TbProduct> spec2,
            @PageableDefault(sort = {"pdLvl1", "pdLvl2", "pdSort", "pdLvl3"}, size = 1000) Pageable pageable, Model model) {


        Specification<TbProduct> spec = Specifications.where(spec1).and(spec2);
        Iterable<TbProduct> list = tbProductRepository.findAll(spec, pageable);

        List<TbProduct> itemList = StreamSupport.stream(list.spliterator(), false).collect(Collectors.toList());
        model.addAttribute("itemList", itemList);

        return "fragments/ProductCombo2 :: resultsList";
//        return "fragments/myView :: #cmbitem";
    }

    @RequestMapping("/etc/ProductCombo3")
    public String comboList3(
            @And({@Spec(path = "pdLvl1", spec = Equal.class),
                    @Spec(path = "pdLvl2", spec = Equal.class),
                    @Spec(path = "pdLvl3", spec = notEqual.class)
            }) Specification<TbProduct> spec1,
            @Or({@Spec(path="delYn", spec = notEqual.class, constVal = "Y"),
                    @Spec(path="delYn", spec = IsNull.class)}) Specification<TbProduct> spec2,
            @PageableDefault(sort = {"pdLvl1", "pdLvl2", "pdSort", "pdLvl3"}, size = 1000) Pageable pageable, Model model) {


        Specification<TbProduct> spec = Specifications.where(spec1).and(spec2);
        Iterable<TbProduct> list = tbProductRepository.findAll(spec, pageable);

        List<TbProduct> itemList = StreamSupport.stream(list.spliterator(), false).collect(Collectors.toList());
        model.addAttribute("itemList", itemList);

        return "fragments/ProductCombo3 :: resultsList";
//        return "fragments/myView :: #cmbitem";
    }

    /**
     * 회원찾기 팝업
     * @param model
     * @return
     */
    @RequestMapping(value = "/popup/Member", method = RequestMethod.GET)
    public String list(Model model) {

        return "popup/member_popup";
    }
}
