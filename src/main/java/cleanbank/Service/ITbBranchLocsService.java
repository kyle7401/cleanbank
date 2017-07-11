package cleanbank.Service;

import cleanbank.domain.TbBranchLocs;
import cleanbank.viewmodel.CodeValue;

import java.util.List;

/**
 * Created by hyoseop on 2015-11-09.
 */
public interface ITbBranchLocsService {
    Iterable<TbBranchLocs> listAllTbBranchLocss();

    TbBranchLocs getTbBranchLocsById(Integer id);

    TbBranchLocs saveTbBranchLocs(TbBranchLocs TbBranchLocs);

    void deleteTbBranchLocs(Integer id);

    //    이하 추가
    void deleteTbBranchLocs2(Integer id);

    List<CodeValue> getTbBranchLocsByBnCd(final Integer bnCd);

    TbBranchLocs getTbBranchLocsByAddr1(final String mbAddr1);
}
