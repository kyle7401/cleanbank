package cleanbank.Service;

import cleanbank.domain.TbSalesInfo;
import cleanbank.repositories.TbSalesInfoRepository;
import org.hibernate.SQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by hyoseop on 2015-11-09.
 */
@Service
public class TbSalesInfoService implements ITbSalesInfoService {
    private TbSalesInfoRepository tbSalesInfoRepository;

    @Autowired
    public void setTbSalesInfoRepository(TbSalesInfoRepository tbSalesInfoRepository) {
        this.tbSalesInfoRepository = tbSalesInfoRepository;
    }

    @Override
    public Iterable<TbSalesInfo> listAllTbSalesInfos() {
        return tbSalesInfoRepository.findAll();
    }

    @Override
    public TbSalesInfo getTbSalesInfoById(Integer id) {
        return tbSalesInfoRepository.findOne(id);
    }

    @Override
    public TbSalesInfo saveTbSalesInfo(TbSalesInfo TbSalesInfo) {
        return tbSalesInfoRepository.save(TbSalesInfo);
    }

    @Override
    public void deleteTbSalesInfo(Integer id) {
        tbSalesInfoRepository.delete(id);
    }

    @Autowired
    private EntityManager entityManager;

//    이하 추가

    @Override
    public void deleteTbSalesInfo2(Integer id) {
        TbSalesInfo SalesInfo = tbSalesInfoRepository.findOne(id);
        SalesInfo.setDelYn("Y");
        tbSalesInfoRepository.save(SalesInfo);
    }

    //    수거 시간표 비영업일 확인용
    @Override
    public List<TbSalesInfo> getSaList(final int bnCd) {
        StringBuilder strQuery = new StringBuilder();

        strQuery.append(" SELECT * ");
        strQuery.append(" FROM                                                                                         ");
        strQuery.append("     TB_SALES_INFO                                                                                 ");
        strQuery.append(" WHERE BN_CD = ?1 ");
        strQuery.append("     AND DATE(SA_DATE) BETWEEN DATE(NOW()) AND DATE(DATE(DATE_ADD(NOW(), INTERVAL + 4 DAY)))    ");
        strQuery.append("     AND (DEL_YN IS NULL OR DEL_YN <> 'Y') ");
        strQuery.append(" ORDER BY SA_DATE                                                                           ");

        Query selectQuery = entityManager.createNativeQuery(strQuery.toString()).setParameter(1, bnCd);

        List<TbSalesInfo> list = selectQuery.unwrap(SQLQuery.class)
                .addEntity(TbSalesInfo.class)
                .list();

        return list;
    }
}
