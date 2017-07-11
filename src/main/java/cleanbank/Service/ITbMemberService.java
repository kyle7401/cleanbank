package cleanbank.Service;

import cleanbank.domain.TbMember;
import cleanbank.gcm.exception.GcmMulticastLimitExceededException;
import cleanbank.viewmodel.MoPoint;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;

/**
 * Created by hyoseop on 2015-11-09.
 */
public interface ITbMemberService {
    Iterable<TbMember> listAllTbMembers();

    TbMember getTbMemberById(Integer id);

    TbMember saveTbMember(TbMember TbMember) throws IOException, GcmMulticastLimitExceededException;

    void deleteTbMember(Integer id);

    //    이하 추가
    void deleteTbMember2(Integer id);

    List<TbMember> listAllMembers();

//    List<?> getList(Specification<TbMember> spec);

//    그리드및 엑셀용 검색(조건추가)
    List<?> getList(
            final String bncd,
            final String from,
            final String to,
            final String keyword,
            final String condition,
            final String status
    );

    Object getStatistic();

    ResponseEntity<?> setMbrPoint(MoPoint moPoint);

    List<?> getPointList(Integer mbcd);

    ResponseEntity<?> setMbrPointInf(MoPoint moPoint);

    List<?> getUsePoCoup(Integer mbcd);
}
