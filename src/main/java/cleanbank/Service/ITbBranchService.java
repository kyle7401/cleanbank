package cleanbank.Service;

import cleanbank.domain.TbBranch;

import java.util.List;

/**
 * Created by hyoseop on 2015-11-09.
 */
public interface ITbBranchService {
    Iterable<TbBranch> listAllTbBranchs();

    TbBranch getTbBranchById(Integer id);

    TbBranch saveTbBranch(TbBranch TbBranch);

    void deleteTbBranch(Integer id);

    //    이하 추가
    String getBnBarCd(final Integer id);

    String setBnBarCd(final Integer id);

    String setBnBarCd(final Integer id, String itTac);

    void deleteTbBranch2(Integer id);

    List<?> getBranchCds();
}
