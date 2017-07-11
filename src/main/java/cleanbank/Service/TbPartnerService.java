package cleanbank.Service;

import cleanbank.domain.QTbPartMember;
import cleanbank.domain.QTbPartner;
import cleanbank.domain.TbPartner;
import cleanbank.repositories.TbPartnerRepository;
import cleanbank.viewmodel.PartnerSearch;
import com.mysema.query.BooleanBuilder;
import com.mysema.query.jpa.JPASubQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Created by hyoseop on 2015-11-09.
 */
@Service
public class TbPartnerService implements ITbPartnerService {
    private TbPartnerRepository tbPartnerRepository;

    @Autowired
    public void setTbPartnerRepository(TbPartnerRepository tbPartnerRepository) {
        this.tbPartnerRepository = tbPartnerRepository;
    }

    @Override
    public Iterable<TbPartner> listAllTbPartners() {
        return tbPartnerRepository.findAll();
    }

    @Override
    public TbPartner getTbPartnerById(Integer id) {
        return tbPartnerRepository.findOne(id);
    }

    @Override
    public TbPartner saveTbPartner(TbPartner TbPartner) {
        return tbPartnerRepository.save(TbPartner);
    }

    @Override
    public void deleteTbPartner(Integer id) {
        tbPartnerRepository.delete(id);
    }

//    이하 추가

    @Autowired
    private EntityManager entityManager;

    @Override
    public Iterable<TbPartner> List(final PartnerSearch partnerSearch, final Pageable pageable) {
        QTbPartner tbPartner = QTbPartner.tbPartner;
        QTbPartMember tbPartMember = QTbPartMember.tbPartMember;

        BooleanBuilder builder = new BooleanBuilder();

//        공장명
        if(org.springframework.util.StringUtils.hasText(partnerSearch.getPtNm())) {
            builder.and(tbPartner.ptNm.contains(partnerSearch.getPtNm()));
        }

//        전화번호
        if(org.springframework.util.StringUtils.hasText(partnerSearch.getPtTel())) {
            builder.and(tbPartner.ptTel.contains(partnerSearch.getPtTel()));
        }

//        담당자 정보
        if (org.springframework.util.StringUtils.hasText(partnerSearch.getPmKeyWord())) {
            builder.and(tbPartner.ptCd.in(
                    new JPASubQuery().from(tbPartMember)
                            .where(tbPartMember.pmNm.contains(partnerSearch.getPmKeyWord())
                                    .or(tbPartMember.pmEmail.contains(partnerSearch.getPmKeyWord()))
                                    .or(tbPartMember.pmTel.contains(partnerSearch.getPmKeyWord()))
                            )
                            .list(tbPartMember.ptCd)
            ));
        }

/*        JPAQuery query = new JPAQuery(entityManager);

        Iterable<TbPartner> list = query.from(tbPartner)
                .where(builder)
                .orderBy(tbPartner.ptNm.asc())
                .list(tbPartner);*/

        Iterable<TbPartner> list = tbPartnerRepository.findAll(builder, pageable);

        return list;
    }

    @Override
    public void deleteTbPartner2(Integer id) {
        TbPartner partner = tbPartnerRepository.findOne(id);
        partner.setDelYn("Y");
        tbPartnerRepository.save(partner);
    }

    @Override
    public List<Object> getPartnerCds() {
        return tbPartnerRepository.getPartnerCds();
    }

    @Override
    public Iterable<TbPartner> List2(final PartnerSearch partnerSearch, final Pageable pageable) {
        QTbPartner tbPartner = QTbPartner.tbPartner;
        QTbPartMember tbPartMember = QTbPartMember.tbPartMember;

        BooleanBuilder builder = new BooleanBuilder();

//        공장명
        if(org.springframework.util.StringUtils.hasText(partnerSearch.getPtNm())) {
            builder.and(tbPartner.ptNm.contains(partnerSearch.getPtNm()));
        }

//        전화번호
        if(org.springframework.util.StringUtils.hasText(partnerSearch.getPtTel())) {
            builder.and(tbPartner.ptTel.contains(partnerSearch.getPtTel()));
        }

//        담당자 정보
        if (org.springframework.util.StringUtils.hasText(partnerSearch.getPmKeyWord())) {
            builder.and(tbPartner.ptCd.in(
                    new JPASubQuery().from(tbPartMember)
                            .where(tbPartMember.pmNm.contains(partnerSearch.getPmKeyWord())
                                    .or(tbPartMember.pmEmail.contains(partnerSearch.getPmKeyWord()))
                                    .or(tbPartMember.pmTel.contains(partnerSearch.getPmKeyWord()))
                            )
                            .list(tbPartMember.ptCd)
            ));
        }

        Iterable<TbPartner> list = tbPartnerRepository.findAll(builder, pageable);

        return list;
    }

    /**
     * 상위코드 콤보
     *
     * @return
     */
/*    @Override
    public List<TbPartner> getPdLvl1s() {
        return tbPartnerRepository.getPdLvl1s();
    }*/

    /**
     * 중위코드 콤보
     *
     * @return
     */
/*    @Override
    public List<TbPartner> getPdLvl2s() {
        return tbPartnerRepository.getPdLvl2s();
    }*/

    /*@Override
    public List<String> getcdGps() {
        final List<String> cdGps = entityManager.createQuery("select distinct cdGp from TbPartner as t where t.delYn is null or t.delYn != 'Y' order by t.cdGp")
                .getResultList();

        return cdGps;
    }*/

//    http://stackoverflow.com/questions/32079084/how-to-find-distinct-rows-with-field-in-list-using-jpa-and-spring

/*    @Override
    public List<String> getcdGps() {
        return tbPartnerRepository.getcdGps();
    }*/

/*    @Override
    public List<TbPartner> findByEpEmail(String epEmail) {
        return tbPartnerRepository.findByEpEmail(epEmail);
    }

    @Override
    public List<TbPartner> findByBnCd(Integer bnCd) {
        return TbPartnerRepository.findByBnCd(bnCd);
    }*/
}
