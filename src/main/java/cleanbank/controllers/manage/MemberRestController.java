package cleanbank.controllers.manage;

import cleanbank.Service.ITbMemberService;
import cleanbank.Service.TbAddressService;
import cleanbank.domain.TbAddress;
import cleanbank.domain.TbMember;
import cleanbank.domain.notEqual;
import cleanbank.repositories.TbAddressRepository;
import cleanbank.repositories.TbMemberRepository;
import cleanbank.utils.SynapseCM;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.IsNull;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Or;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by hyoseop on 2015-11-16.
 */
@RestController
@RequestMapping("/member")
public class MemberRestController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ITbMemberService tbMemberService;

    /*@Autowired
    public void setITbMemberService(TbMemberService tbMemberService) {
        this.tbMemberService = tbMemberService;
    }*/

    @Autowired
    private TbMemberRepository tbMemberRepository;

    @Autowired
    private TbAddressService tbAddressService;

    /*@Autowired
    public void setTbAddressService(TbAddressService tbAddressService) {
        this.tbAddressService = tbAddressService;
    }*/

    @Autowired
    private TbAddressRepository tbAddressRepository;
    //    ----------------------------------------------------------------------------------------------------------------

    @RequestMapping(value = "/list")
    public List<?> filterList(
            @RequestParam(value="bncd", required=false) final String bncd,
            @RequestParam(value="from", required=false) final String from,
            @RequestParam(value="to", required=false) final String to,
            @RequestParam(value="keyword", required=false) final String keyword,
            @RequestParam(value="condition", required=false) final String condition,
            @RequestParam(value="status", required=false) final String status
            ) {
        List<?> list = tbMemberService.getList(bncd, from, to, keyword, condition, status);
        return list;
    }

/*    @RequestMapping(value = "/list")
    public Iterable<TbMember> filterList(
            @And({@Spec(path = "bnNm", spec = Like.class)}) Specification<TbMember> spec1,
            @Or({@Spec(path="delYn", spec = notEqual.class, constVal = "Y"),
                    @Spec(path="delYn", spec = IsNull.class)}) Specification<TbMember> spec2,
            @PageableDefault(sort = {"mbCd", "mbNicNm"}, size = 1000) Pageable pageable) {

        Specification<TbMember> spec = Specifications.where(spec1).and(spec2);
        Iterable<TbMember> list = tbMemberRepository.findAll(spec, pageable);
        return list;
    }*/

    @Autowired
    private EntityManager em;

    @RequestMapping(value = "/listAddress")
    public Iterable<TbAddress> filterAddress(
            @And({@Spec(path = "mbCd", spec = Equal.class)}) Specification<TbAddress> spec1,
            @Or({@Spec(path="delYn", spec = notEqual.class, constVal = "Y"),
                    @Spec(path="delYn", spec = IsNull.class)}) Specification<TbAddress> spec2,
            @PageableDefault(sort = {"mbAddr1", "id"}, size = 1000) Pageable pageable) {

        Specification<TbAddress> spec = Specifications.where(spec1).and(spec2);

//        회원id 검색조건이 있을때만 검색한다
       /*
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<TbAddress> q = cb.createQuery(TbAddress.class);
        Root<TbAddress> address = q.from(TbAddress.class);

        Predicate pred = spec.toPredicate(address, q, cb);
        List<Expression<Boolean>> expressions = ((CompoundPredicate)pred).getExpressions();
        log.debug("검색조건 = {}", expressions.size());*/

        Iterable<TbAddress> list = null;

/*        if(expressions.size() >= 2) {
            q.where(spec.toPredicate(address, q, cb));

            list = em.createQuery(q).getResultList();
        }*/

        if (spec1 != null) {
            list = tbAddressRepository.findAll(spec, pageable);
        } else {
            list = new ArrayList<TbAddress>();
        }

//        Iterable<TbAddress> list = tbAddressRepository.findAll(spec, pageable);
        return list;
    }

//    주소 추가
    @RequestMapping(value = "/saveAddress", method = RequestMethod.POST)
    public String saveAddress(@Valid TbAddress address, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error("\n\n\n############### 저장시 bindingResult.hasErrors()\n{}", bindingResult.toString());
//            return "error";
        }

        if ("new".equals(address.getMode())) { // 신규
            address.setRegiDt(new Date());
            address.setUser(SynapseCM.getAuthenticatedUserID());
        } else { // 수정
//
        }

        tbAddressService.saveTbAddress(address);
//        return "redirect:/manage/member/" + address.getMbCd();
        return "ok";
    }

    /**
     * 삭제
     *
     * @param id
     * @return
     */
    @RequestMapping("/delete-member")
    public String deleteCode(@RequestParam(value="no") Integer id) {
        tbMemberService.deleteTbMember2(id);
        return "ok";
    }

//    public String checkEpEmail(@RequestParam(value="mbCd") Integer mbCd, @RequestParam(value="mbEmail") String mbEmail) {

    /**
     * 아이디 중복체크
     * @param spec1
     * @param spec2
     * @return
     */
    @RequestMapping("/check-email")
    public String checkMbEmail(
            @And({@Spec(path = "mbEmail", spec = Equal.class), @Spec(path = "mbCd", spec = notEqual.class)}) Specification<TbMember> spec1,
            @Or({@Spec(path="delYn", spec = notEqual.class, constVal = "Y"),
                    @Spec(path="delYn", spec = IsNull.class)}) Specification<TbMember> spec2) {

        String isPass = "true";

        if(spec1 != null) {
            Specification<TbMember> spec = Specifications.where(spec1).and(spec2);List<TbMember> list = tbMemberRepository.findAll(spec);

//            TODO : 버전에 따라서 직접 에러 메세지를 리턴하거나 false를 리턴한후 html에서 메세지를 표시해야 되는 2가지 경우가 있다
            if(list != null && list.size() > 0) {
//                isPass = "이미 사용중인 아이디 입니다";
                isPass = "false";
            }
        }

//        List<TbMember> list = tbMemberRepository.findByMbEmail(mbEmail);

//        본인 record 이외에 중복되는 내용이 있는지 확인
/*        if(list != null && list.size() > 0 && mbCd != null && mbCd > 0) {
            if(!list.getMbCd().equals(mbCd)) {
                isPass = "이미 사용중인 아이디 입니다!";
            }
        }*/

        return isPass;
    }

    @RequestMapping("/check-tel")
    public String checkMbTel(
            @And({@Spec(path = "mbTel", spec = Equal.class), @Spec(path = "mbCd", spec = notEqual.class)}) Specification<TbMember> spec1,
            @Or({@Spec(path="delYn", spec = notEqual.class, constVal = "Y"),
                    @Spec(path="delYn", spec = IsNull.class)}) Specification<TbMember> spec2) {

        String isPass = "true";

        if(spec1 != null) {
            Specification<TbMember> spec = Specifications.where(spec1).and(spec2);
            List<TbMember> list = tbMemberRepository.findAll(spec);

            if(list != null && list.size() > 0) {
//                isPass = "이미 사용중인 연락처 입니다";
                isPass = "false";
            }
        }

        return isPass;
    }

}
