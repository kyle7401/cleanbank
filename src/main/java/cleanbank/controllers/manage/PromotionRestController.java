package cleanbank.controllers.manage;

import cleanbank.Service.ITbPromotionService;
import cleanbank.Service.TbPromoLocationService;
import cleanbank.domain.TbPromoLocation;
import cleanbank.domain.TbPromotion;
import cleanbank.domain.notEqual;
import cleanbank.repositories.TbPromotionRepository;
import cleanbank.utils.SynapseCM;
import net.kaczmarzyk.spring.data.jpa.domain.IsNull;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
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

import javax.validation.Valid;
import java.util.Date;
import java.util.Random;

/**
 * Created by hyoseop on 2015-11-16.
 */
@RestController
@RequestMapping("/promotion")
public class PromotionRestController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ITbPromotionService tbPromotionService;

/*    @Autowired
    public void setTbPromotionService(TbPromotionService tbPromotionService) {
        this.tbPromotionService = tbPromotionService;
    }*/

    @Autowired
    private TbPromotionRepository tbPromotionRepository;

    private TbPromoLocationService tbPromoLocationService;

    @Autowired
    public void setTbPromoLocationService(TbPromoLocationService tbPromoLocationService) {
        this.tbPromoLocationService = tbPromoLocationService;
    }

    //    ----------------------------------------------------------------------------------------------------------------

    /**
     * 구폰번호 생성
     * TODO : 기존에 생성된 쿠폰번호와 중복되는지 확인 필요
     *
     * @return
     */
    @RequestMapping(value = "/createCouponNum")
    public String createCouponNum() {

        //실행시 ???개 쿠폰 생성
//        int couponSize = 20;
        int iLen = 10;
        String couponnum = null;

        final char[] possibleCharacters =
                {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'A', 'B', 'C', 'D', 'E', 'F',
                        'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
                        'W', 'X', 'Y', 'Z'};

        final int possibleCharacterCount = possibleCharacters.length;
//        String[] arr = new String[couponSize];
        Random rnd = new Random();
        int currentIndex = 0;
        int i = 0;

//        while (currentIndex < couponSize) {
            StringBuffer buf = new StringBuffer(16);
            //i는 8자리의 랜덤값을 의미
//            for (i = 8; i > 0; i--) {
            for (i = iLen; i > 0; i--) {
                buf.append(possibleCharacters[rnd.nextInt(possibleCharacterCount)]);
            }
//            String couponnum = buf.toString();
            couponnum = buf.toString();
//            System.out.println("couponnum==>" + couponnum);
            log.debug("couponnum==> {}", couponnum);
//            arr[currentIndex] = couponnum;
            currentIndex++;
//        }

        return couponnum;
    }

    @RequestMapping(value = "/savePromoLocation", method = RequestMethod.POST)
    public String savePromoLocation(@Valid TbPromoLocation PromoLocation, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            log.error("\n\n\n############### 저장시 bindingResult.hasErrors()\n{}", bindingResult.toString());
//            return "error";
        }

        if("new".equals(PromoLocation.getMode())) { // 신규
            PromoLocation.setRegiDt(new Date());
            PromoLocation.setUser(SynapseCM.getAuthenticatedUserID());
        } else { // 수정
//
        }

        tbPromoLocationService.saveTbPromoLocation(PromoLocation);
        return "ok";
    }

    @RequestMapping(value = "/list")
    public Iterable<TbPromotion> filterList(
            @And({@Spec(path = "xxx", spec = Like.class)}) Specification<TbPromotion> spec1,
            @Or({@Spec(path="delYn", spec = notEqual.class, constVal = "Y"),
                    @Spec(path="delYn", spec = IsNull.class)}) Specification<TbPromotion> spec2,
            @PageableDefault(sort = {"poNm"}, size = 1000) Pageable pageable) {

        Specification<TbPromotion> spec = Specifications.where(spec1).and(spec2);
        Iterable<TbPromotion> list = tbPromotionRepository.findAll(spec, pageable);
        return list;
    }

    /**
     * 삭제
     *
     * @param id
     * @return
     */
    @RequestMapping("/delete-promotion")
    public String deleteCode(@RequestParam(value="no") Long id) {
        tbPromotionService.deleteTbPromotion2(id);
        return "ok";
    }

}
