package cleanbank.Service;

import cleanbank.domain.TbPartMember;

/**
 * Created by hyoseop on 2015-11-09.
 */
public interface ITbPartMemberService {
    Iterable<TbPartMember> listAllTbPartMembers();

    TbPartMember getTbPartMemberById(Integer id);

    TbPartMember saveTbPartMember(TbPartMember TbPartMember);

    void deleteTbPartMember(Integer id);

    //    이하 추가
    void deleteTbPartMember2(Integer id);

    TbPartMember findByPmEmail(String pmEmail);

/*    List<TbPartMember> getPdLvl1s();

    List<TbPartMember> getPdLvl2s();*/

//    List<String> getcdGps();

//    List<TbPartMember> findByEpEmail(String epEmail);
//    List<TbPartMember> findByBnCd(Integer bnCd);
}
