package cleanbank.Service;

import cleanbank.domain.TbMember;
import cleanbank.domain.TbPromotion;
import cleanbank.domain.TbPromotionUse;
import cleanbank.repositories.TbMemberRepository;
import cleanbank.repositories.TbPromotionRepository;
import cleanbank.repositories.TbPromotionUseRepository;
import cleanbank.utils.SynapseCM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

@Service
public class TbPromotionService implements ITbPromotionService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private TbPromotionRepository tbPromotionRepository;

    /*@Autowired
    public void setTbPromotionRepository(TbPromotionRepository tbPromotionRepository) {
        this.tbPromotionRepository = tbPromotionRepository;
    }*/

/*    @PersistenceContext
    private EntityManager entityManager;*/

    /*@Autowired
    private ITbMemberService tbMemberService;*/

    @Autowired
    private TbMemberRepository tbMemberRepository;

    @Autowired
    private TbPromotionUseRepository tbPromotionUseRepository;

    @Autowired
    private PushMobileService pushMobileService;

//    --------------------------------------------------------------------------------------------------------

    @Override
    public Iterable<TbPromotion> listAllTbPromotions() {
        return tbPromotionRepository.findAll();
    }

    @Transactional
    @Override
    public TbPromotion saveTbPromotion(TbPromotion promotion) {


        if("new".equals(promotion.getMode())) { // 신규
            promotion.setRegiDt(new Date());
            promotion.setUser(SynapseCM.getAuthenticatedUserID());
        } else { // 수정
//
        }

//        return tbPromotionRepository.save(promotion);
        promotion = tbPromotionRepository.save(promotion);

        if("new".equals(promotion.getMode())) { // 신규
//            "신규" 일때만 저장시  쿠폰 번호가 없을때 : "푸시발송여부"에 따라 푸시 메세지 전송및 모든 회원에게 쿠폰 발급
            if(StringUtils.isEmpty(promotion.getPoCoup())) {
                List<TbMember> members = tbMemberRepository.listAllMembers();

                for (TbMember tbMember : members) {
//                    쿠폰 저장
                    TbPromotionUse tbPromotionUse = new TbPromotionUse();
                    tbPromotionUse.setMbCd(tbMember.getMbCd());
                    tbPromotionUse.setDelYn("N");
                    tbPromotionUse.setEvtNm("신규");
                    tbPromotionUse.setRegiDt(new Date());
                    tbPromotionUse.setUser("모바일");

                    tbPromotionUse.setTbPromotion(promotion);

                    tbPromotionUse = tbPromotionUseRepository.save(tbPromotionUse);
                }

                //        신규 저장이고 푸시발송에 체크 되어 있으면 전송
                if("Y".equals(promotion.getPoPushMsg())) {
                    try {
                        pushMobileService.sendPromotion(promotion);
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error("프로모션 푸시발송 에러 {}", e);
                    }
                }
            }
        }

        return promotion;
    }

    @Override
    public TbPromotion getTbPromotionById(Long id) {
        return tbPromotionRepository.findOne(id);
    }

    @Override
    public void deleteTbPromotion(Long id) {
        tbPromotionRepository.delete(id);
    }

//    이하 추가

    @Override
    public void deleteTbPromotion2(Long id) {
        TbPromotion Promotion = tbPromotionRepository.findOne(id);
        Promotion.setDelYn("Y");
        tbPromotionRepository.save(Promotion);
    }

/*    @Override
    public List<?> getPromotionCds(Integer mbCd) {
        return tbPromotionRepository.getPromotionCds(mbCd);
    }*/

/*    @Override
    public List<?> getPoCoup(final Integer mbCd) {
        String query = "SELECT u FROM TbPromotionUse u INNER JOIN u.tbPromotion p";
        List<?> list = entityManager.createQuery(query).getResultList();

        return null;
    }*/

}
