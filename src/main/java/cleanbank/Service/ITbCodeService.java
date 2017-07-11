package cleanbank.Service;

import cleanbank.domain.TbCode;

import java.util.List;

/**
 * Created by hyoseop on 2015-11-09.
 */
public interface ITbCodeService {
    Iterable<TbCode> listAllTbCodes();

    TbCode getTbCodeById(Integer id);

    TbCode saveTbCode(TbCode TbCode);

    void deleteTbCode(Integer id);

    //    이하 추가

//    상위코드 검색
    List<TbCode> getcdGps();

//    하위코드 검색 : 주문상태
    List<TbCode> getCdIts01();

//    하위코드 검색 : 클레임상태
    List<TbCode> getCdIts02();

//    하위코드 검색 : 환불상태
    List<TbCode> getCdIts03();

//    하위코드 검색 : 사고상태
    List<TbCode> getCdIts04();

//    하위코드 검색 : 결제상태
    List<TbCode> getCdIts05();

//    하위코드 검색 : 서비스 상태
    List<TbCode> getCdIts06();

//    하위코드 검색 : 담당업무
    List<TbCode> getCdIts07();

//    하위코드 검색 : 코디등급
    List<TbCode> getCdIts08();

//    하위코드 검색 : 운전능력
    List<TbCode> getCdIts09();

//    void deleteTbCode2(Integer id);

//    List<TbCode> findByEpEmail(String epEmail);
//    List<TbCode> findByBnCd(Integer bnCd);
}
