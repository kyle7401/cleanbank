package cleanbank.Service;

import cleanbank.domain.TbOrder;
import cleanbank.domain.TbOrderItems;
import cleanbank.gcm.exception.GcmMulticastLimitExceededException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

/**
 * Created by hyoseop on 2015-11-09.
 */
public interface ITbOrderItemsService {
    Iterable<TbOrderItems> listAllTbOrderItemss();

    TbOrderItems getTbOrderItemsById(Long id);

    TbOrderItems saveTbOrderItems(TbOrderItems TbOrderItems);

    void deleteTbOrderItems(Long id);

    //    이하 추가
    void deleteTbOrderItems2(Long id);

//    주문상태 변경
//    @Transactional
    int changeStatus(ArrayList<Long> items, String status);

//    품목 리스트 그리드 삭제 처리
    int deleteGrid(ArrayList<Long> items);

//    공장비고 저장
    int saveFMemo(final Long no, final String memo);

//    품목 리스트의 가격 수량을 계산하여 주문정보를 Update후 결과 리턴
    TbOrder saveAmountCnt(final Long no) throws NoSuchMethodException, GcmMulticastLimitExceededException, IllegalAccessException, IOException, InvocationTargetException;

    int changeStatus(final long orCd, String status);

//    List<?> getBranchCds();
}
