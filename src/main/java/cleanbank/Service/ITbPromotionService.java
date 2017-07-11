package cleanbank.Service;

import cleanbank.domain.TbPromotion;

/**
 * Created by hyoseop on 2015-11-09.
 */
public interface ITbPromotionService {
    Iterable<TbPromotion> listAllTbPromotions();

    TbPromotion getTbPromotionById(Long id);

//    @Transactional
    TbPromotion saveTbPromotion(TbPromotion TbPromotion);

    void deleteTbPromotion(Long id);

    //    이하 추가
    void deleteTbPromotion2(Long id);

//    List<?> getPromotionCds(Integer mbCd);

//    List<?> getPoCoup(final Integer mbCd);
}
