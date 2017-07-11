package cleanbank.Service;

import cleanbank.domain.TbPushHistory;

/**
 * Created by hyoseop on 2015-11-09.
 */
public interface ITbPushHistoryService {
    Iterable<TbPushHistory> listAllTbPushHistorys();

    TbPushHistory getTbPushHistoryById(Integer id);

    TbPushHistory saveTbPushHistory(TbPushHistory TbPushHistory);

    void deleteTbPushHistory(Integer id);

    //    이하 추가
    /*void deleteTbPushHistory2(Integer id);

    List<?> getPushHistoryCds(Integer mbCd);

    TbPushHistory saveMoPushHistory(MoOrder order);*/

    TbPushHistory addPushHistory(final int mbCd, final String pushTitle);
}
