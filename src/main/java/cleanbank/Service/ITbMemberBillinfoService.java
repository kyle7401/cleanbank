package cleanbank.Service;

import cleanbank.domain.TbMemberBillinfo;

/**
 * Created by hyoseop on 2015-11-09.
 */
public interface ITbMemberBillinfoService {
    Iterable<TbMemberBillinfo> listAllTbMemberBillinfos();

    TbMemberBillinfo getTbMemberBillinfoById(Integer id);

//    TbMemberBillinfo getTbMemberBillinfoByMbCd(Integer mbCd);

    TbMemberBillinfo saveTbMemberBillinfo(TbMemberBillinfo TbMemberBillinfo);

    void deleteTbMemberBillinfo(Integer id);

    //    이하 추가
//    void deleteTbMemberBillinfo2(Integer id);
}
