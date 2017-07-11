package cleanbank.controllers.manage;

import cleanbank.Service.ITbPromotionService;
import cleanbank.Service.TbLocationService;
import cleanbank.domain.TbPromoLocation;
import cleanbank.domain.TbPromotion;
import cleanbank.repositories.TbPromotionUseRepository;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RequestMapping("/manage/promotion")
@Controller
public class PromotionController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Inject
    private PropertyEditorRegistrar customPropertyEditorRegistrar;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        this.customPropertyEditorRegistrar.registerCustomEditors(binder);
    }

    //    ----------------------------------------------------------------------------------------------------------------

//    서비스에 @Transactional 이 존재할 경우 @Autowired 주입에 실패할 경우가 있다, 그럴때는 Interface 클래스를 사용한다
    @Autowired
    private ITbPromotionService tbPromotionService;
//    private TbPromotionService tbPromotionService;

    @Autowired
    private TbLocationService tbLocationService;

    @Autowired
    private TbPromotionUseRepository tbPromotionUseRepository;

    //    ----------------------------------------------------------------------------------------------------------------
    /**
     * 목록
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model){
        return "manage/promotion/promotion_list";
    }

    /**
     * 신규입력
     * @param model
     * @return
     */
    @RequestMapping("/new")
    public String newLoc(Model model){
//        공장콤보
        /*List<?> partner_cds = tbPartnerService.getPartnerCds();
        model.addAttribute("partner_cds", partner_cds);*/

        //        상위지역코드
        List<?> cmb_plCd1 = tbLocationService.getPlCd1s();
        model.addAttribute("cmb_plCd1", cmb_plCd1);

//        중위지역코드 : 그리드용
        List<?> cmb_plCd2 = tbLocationService.getPlCd2s();
        model.addAttribute("cmb_plcd2", cmb_plCd2);

        TbPromoLocation promolocation = new TbPromoLocation();
        promolocation.setMode("new");
        model.addAttribute("promolocation", promolocation);

        TbPromotion promotion = new TbPromotion();
        promotion.setMode("new");
        model.addAttribute("promotion", promotion);

        return "manage/promotion/promotion_form";
    }

    /**
     * 수정
     *
     * @param model
     * @return
     */
    @RequestMapping("/{poCd}")
    public String showManager(@PathVariable("poCd") TbPromotion tbPromotion, Model model){

//        공장콤보
        /*List<?> partner_cds = tbPartnerService.getPartnerCds();
        model.addAttribute("partner_cds", partner_cds);*/

        //        상위지역코드
        List<?> cmb_plCd1 = tbLocationService.getPlCd1s();
        model.addAttribute("cmb_plCd1", cmb_plCd1);

//        중위지역코드 : 그리드용
        List<?> cmb_plCd2 = tbLocationService.getPlCd2s();
        model.addAttribute("cmb_plcd2", cmb_plCd2);

        TbPromoLocation promolocation = new TbPromoLocation();
        promolocation.setMode("new");
        promolocation.getId();
        model.addAttribute("promolocation", promolocation);

//        쿠폰번호가 있는 경우만 사용수량 표시
        if(!org.springframework.util.StringUtils.isEmpty(tbPromotion.getPoCoup())) {
            Integer cnt = tbPromotionUseRepository.getPoUseCnt(tbPromotion.getPoCd());
            tbPromotion.setPoCoupUseCnt(cnt);
        }

        /*        TbPromotion promotion = tbPromotionService.getTbPromotionById(id);
        promotion.setMode("edit");*/
        model.addAttribute("promotion", tbPromotion);

        return "manage/promotion/promotion_form";
    }

    //    config.properties : 프로모션) 이미지 업로드 폴더및 http base url
    @Value("${promotion_img_dir}")
    String promotion_img_dir;

    @Value("${promotion_img_url}")
    String promotion_img_url;

    /**
     * 저장
     * @param promotion
     * @return
     */
    @RequestMapping(value = "/savePromotion", method = RequestMethod.POST)
    public String savePromotion(@Valid TbPromotion promotion, BindingResult bindingResult, @RequestParam("imgfile") MultipartFile file) throws ParseException {
        if (bindingResult.hasErrors()) {
            log.error("\n\n\n############### 저장시 bindingResult.hasErrors()\n{}", bindingResult.toString());
//            return "error";
        }

        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");

//            프로모션 시작시간
/*        if(StringUtils.isNotEmpty(promotion.getPoStartTm1()) && StringUtils.isNotEmpty(promotion.getPoStartTm2())) {
            String strTm1 = promotion.getPoStartTm1() + ":" + promotion.getPoStartTm2();
            Date date1 = formatter.parse(strTm1);
            promotion.setPoStartTm(date1);
        }*/

//            프로모션 종료시간
        /*if(StringUtils.isNotEmpty(promotion.getpoFinishTm1()) && StringUtils.isNotEmpty(promotion.getpoFinishTm2())) {
            String strTm2 = promotion.getpoFinishTm1() + ":" + promotion.getpoFinishTm2();
            Date date2 = formatter.parse(strTm2);
            promotion.setPoFinishTm(date2);
        }*/

        Date date1 = null;
        if(!StringUtils.isEmpty(promotion.getPoStartTm0())) date1 = formatter.parse(promotion.getPoStartTm0());
        promotion.setPoStartTm(date1);

        Date date2 = null;
        if(!StringUtils.isEmpty(promotion.getPoFinishTm0())) date2 = formatter.parse(promotion.getPoFinishTm0());
        promotion.setPoFinishTm(date2);

        tbPromotionService.saveTbPromotion(promotion);

        if (!file.isEmpty()) {
            String strFileName = "promotion_img"+ promotion.getPoCd();
            String strUploadNm = SynapseCM.imgUpload(file, promotion_img_dir, strFileName);

            if(StringUtils.isNotEmpty(strUploadNm)) {
                promotion.setPoImg(promotion_img_url + strUploadNm);
                promotion.setMode("edit"); // 그냥 저장하면 쿠폰이 2번 발행 된다
                tbPromotionService.saveTbPromotion(promotion);
            } else {
                log.error("이미지 업로드 실패!!!");
            }
        }

//        return "redirect:/manage/promotion/" + promotion.getPoCd();
        return "redirect:/manage/promotion/list";
    }
}
