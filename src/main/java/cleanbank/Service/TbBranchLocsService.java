package cleanbank.Service;

import cleanbank.domain.TbBranchLocs;
import cleanbank.repositories.TbBranchLocsRepository;
import cleanbank.viewmodel.CodeValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by hyoseop on 2015-11-09.
 */
@Service
public class TbBranchLocsService implements ITbBranchLocsService {
    private TbBranchLocsRepository tbBranchLocsRepository;
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public void setTbBranchLocsRepository(TbBranchLocsRepository tbBranchLocsRepository) {
        this.tbBranchLocsRepository = tbBranchLocsRepository;
    }

    @Override
    public Iterable<TbBranchLocs> listAllTbBranchLocss() {
        return tbBranchLocsRepository.findAll();
    }

    @Override
    public TbBranchLocs getTbBranchLocsById(Integer id) {
        return tbBranchLocsRepository.findOne(id);
    }

    @Override
    public TbBranchLocs saveTbBranchLocs(TbBranchLocs TbBranchLocs) {
        return tbBranchLocsRepository.save(TbBranchLocs);
    }

    @Override
    public void deleteTbBranchLocs(Integer id) {
        tbBranchLocsRepository.delete(id);
    }

//    이하 추가

    @Override
    public void deleteTbBranchLocs2(Integer id) {
        TbBranchLocs product = tbBranchLocsRepository.findOne(id);
        product.setDelYn("Y");
        tbBranchLocsRepository.save(product);
    }

    @Override
    public List<CodeValue> getTbBranchLocsByBnCd(final Integer bnCd) {
        return tbBranchLocsRepository.getTbBranchLocsByBnCd(bnCd);
    }

    @Override
    public TbBranchLocs getTbBranchLocsByAddr1(final String mbAddr1) {
        TbBranchLocs tbBranchLocs = null;

        if(StringUtils.isEmpty(mbAddr1)) return tbBranchLocs;
        String[] split = mbAddr1.split(" ");
        String strLastAddr1 = split[split.length - 1];
        log.debug("주문 주소 = {}", strLastAddr1);

        List<TbBranchLocs> tbBranchLocList = tbBranchLocsRepository.getTbBranchLocsByBlNm(strLastAddr1);

        if(tbBranchLocList != null && tbBranchLocList.size() > 0) {
            tbBranchLocs = tbBranchLocList.get(0);
            log.debug("주문 지점 = {}", tbBranchLocs.getBnCd());
        }

        return tbBranchLocs;
    }

}
