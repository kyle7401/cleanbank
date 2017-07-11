package cleanbank.repositories;

import cleanbank.domain.TbOrderItems;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

/**
 * Created by hyoseop on 2015-11-05.
 */
//public interface TbOrderRepository extends PagingAndSortingRepository<TbOrder, Integer>, JpaSpecificationExecutor<TbOrder> {
public interface TbOrderItemsRepository extends PagingAndSortingRepository<TbOrderItems, Long>, JpaSpecificationExecutor<TbOrderItems> {
/*    @Query("SELECT t.bnCd as code, t.bnNm as value FROM TbOrder as t where t.delYn is null or t.delYn != 'Y' ORDER BY t.bnNm")
    List<CodeValue> getBranchCds();*/

/*    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private EntityManager entityManager;

    @Modifying
    @Transactional
    public int changeStatus(ArrayList<Long> items, String status) {
        for(int i=0; i<items.size(); i++) {
            log.debug("접수완료 처리 ID = {}", items.get(i).toString());
        }

        Query uptQuery = entityManager.createQuery("update TbOrderItems set itStatus = ?1 where id in (?2)")
                .setParameter(1, status)
                .setParameter(2, items);

        Integer result = uptQuery.executeUpdate();

        return result;
    }*/

    @Query(value = "SELECT COUNT(DISTINCT it_status) AS cnt, COUNT(1) AS cnt2, max(it_status) as status" +
            " FROM \n" +
            "    TB_ORDER_ITEMS\n" +
            "WHERE \n" +
            "    or_cd = ?", nativeQuery = true)
    Object[][] getStatusCnt(@Param("orCd") long orCd);

    @Query(value = "SELECT COUNT(1) AS cnt" +
            " FROM \n" +
            "    TB_ORDER_ITEMS \n" +
            "WHERE IT_TAC = ? AND (DEL_YN IS NULL OR DEL_YN <> 'Y')", nativeQuery = true)
    Object[] getTacCnt(@Param("itTac") String itTac);
}
