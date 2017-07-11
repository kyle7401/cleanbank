package cleanbank.controllers.accept;

import cleanbank.Service.*;
import cleanbank.domain.TbOrder;
import cleanbank.domain.TbOrderItems;
import cleanbank.domain.TbPicture;
import cleanbank.domain.notEqual;
import cleanbank.gcm.exception.GcmMulticastLimitExceededException;
import cleanbank.repositories.TbOrderItemsRepository;
import cleanbank.repositories.TbOrderRepository;
import cleanbank.repositories.TbPictureRepository;
import cleanbank.utils.SynapseCM;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.IsNull;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Or;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.internal.SessionImpl;
import org.hibernate.jpa.criteria.predicate.CompoundPredicate;
import org.hibernate.loader.OuterJoinLoader;
import org.hibernate.loader.criteria.CriteriaLoader;
import org.hibernate.persister.entity.OuterJoinLoadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by hyoseop on 2015-11-16.
 */
@RestController
@RequestMapping("/order")
public class OrderRestController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Inject
    private PropertyEditorRegistrar customPropertyEditorRegistrar2;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        this.customPropertyEditorRegistrar2.registerCustomEditors(binder);
    }

/*    private ITbOrderService tbOrderService;

    @Autowired
    public void setITbOrderService(TbOrderService tbOrderService) {
        this.tbOrderService = tbOrderService;
    }*/

    @Autowired
    private ITbOrderService tbOrderService;

    @Autowired
    private TbOrderRepository tbOrderRepository;

    @Autowired
    private TbOrderItemsRepository tbOrderItemsRepository;

//    @Autowired
/*    private TbOrderItemsService tbOrderItemsService;

    @Autowired
    public void setITbOrderItemsService(TbOrderItemsService tbOrderItemsService) {
        this.tbOrderItemsService = tbOrderItemsService;
    }*/

//    TODO : Service에 @Transactional 을 추가하였더니 위의 방식으로는 Injection of autowired dependencies failed 에러가 발생하면 Tomcat이 실행되지 않는다 ㅠ.ㅜ
//    근본적인 원인을 알아야 한다....
    @Autowired
    ITbOrderItemsService tbOrderItemsService;

    private TbPictureService tbPictureService;

    @Autowired
    public void setITbPictureService(TbPictureService tbPictureService) {
        this.tbPictureService = tbPictureService;
    }

    @Autowired
    private TbPictureRepository tbPictureRepository;

    @Autowired
    private IOrderListService orderListService;

    //    config.properties : 품목 리스트의 품목 정보 이미지 업로드 폴더및 http base url
    @Value("${ord_items_img_dir}")
    String ord_items_img_dir;

    @Value("${ord_items_img_url}")
    String ord_items_img_url;

    //    ----------------------------------------------------------------------------------------------------------------

    @RequestMapping(value = "/saveFMemo", method = RequestMethod.POST)
    public String saveFMemo(
            @RequestParam(value = "no") Long no,
            @RequestParam(value = "memo") String memo,
            HttpServletRequest request
    ) {

//        공장비고 저장
        Integer iResult = tbOrderItemsService.saveFMemo(no, memo);

        return "OK";
    }

    /**
     * 주문정보의 접수완료, 입고완료 처리
     *
     * @param no
     * @param status
     * @param request
     * @return
     */
    @RequestMapping(value = "/changeSt2", method = RequestMethod.POST)
    public String changeSt2(
            @RequestParam(value = "no") Long no,
            @RequestParam(value = "status") String status,
            HttpServletRequest request
    ) throws IOException, GcmMulticastLimitExceededException {

//        주문상태를 "입고완료" 또는 접수완료 로 변경
        Integer iResult = tbOrderService.changeSt2(no, status);

        return "OK";
    }

    /**
     * 접수 목록
     * @return
     */
/*    @RequestMapping(value = "/list")
    public Iterable<TbOrder> filterList(
            @And({@Spec(path = "bnCd", spec = Like.class)}) Specification<TbOrder> spec1,
            @Or({@Spec(path="delYn", spec = notEqual.class, constVal = "Y"),
                    @Spec(path="delYn", spec = IsNull.class)}) Specification<TbOrder> spec2,
            @PageableDefault(sort = {"bnCd", "orCd"}, size = 1000) Pageable pageable) {


        Specification<TbOrder> spec = Specifications.where(spec1).and(spec2);
        Iterable<TbOrder> list = tbOrderRepository.findAll(spec, pageable);
        return list;
    }*/

/*    @RequestMapping(value = "/saveOrderItems", method = RequestMethod.POST)
    public TbOrderItems saveOrderItems(@Valid TbOrderItems orderitem, BindingResult bindingResult
            ,@RequestParam(value="imgfile1", required=false) MultipartFile file1*/

//    접수완료 처리
    @RequestMapping(value = "/changeSt", method = RequestMethod.POST)
    public String changeSt(
//            @ModelAttribute(value = "items0") ArrayList<String> items,
            @RequestParam(value="items") ArrayList<Long> items,
//            @RequestParam(value="items", required=false) String[] items3,
            HttpServletRequest request
    ) {

        /*Enumeration em = request.getParameterNames();
        while(em.hasMoreElements()){
            String parameterName = (String)em.nextElement();
            String parameterValue = request.getParameter(parameterName);
            String[] parameterValues = request.getParameterValues(parameterName);
            if(parameterValues != null){
                for(int i=0; i<parameterValues.length; i++){
                    System.out.println("array_" + parameterName + "=" + parameterValues[i]);
                }
            } else {
                System.out.println(parameterName + "=" + parameterValue);
            }
        }*/

//        주문상태를 "접수완료" 로 변경
        if(items != null && items.size() > 0) {
            Integer iResult = tbOrderItemsService.changeStatus(items, "0104");
        }

        return "OK";
    }

    /**
     * 접수처리 검색
     *
     * @param bncd
     * @param from
     * @param to
     * @param keyword
     * @param condition
     * @param status
     * @return
     */
    @RequestMapping(value = "/list")
    public List<?> getList(
            @RequestParam(value="bncd", required=false) final String bncd,
            @RequestParam(value="from", required=false) final String from,
            @RequestParam(value="to", required=false) final String to,
            @RequestParam(value="keyword", required=false) final String keyword,
            @RequestParam(value="condition", required=false) final String condition,
            @RequestParam(value="status", required=false) final String status
    ) {

        log.debug("검색조건 = 지점: {}, 주문From: {}, 주문To: {}, 검색어: {}, 검색구분: {}, 주문상태: {}",
                bncd, from, to, keyword, condition, status);

//        List<?> list = tbOrderService.getOrderList(bncd, from, to, keyword, condition, status);
        List<?> list = orderListService.getTotalList5(bncd, from, to, keyword, condition, status, null, null);
        return list;
    }

    @RequestMapping(value = "/listPicture")
    public Iterable<TbPicture> filterPicture(
            @And({@Spec(path = "orCd", spec = Equal.class), @Spec(path = "itCd", spec = Equal.class)}) Specification<TbPicture> spec1,
            @Or({@Spec(path="delYn", spec = notEqual.class, constVal = "Y"),
                    @Spec(path="delYn", spec = IsNull.class)}) Specification<TbPicture> spec2,
            @PageableDefault(sort = {"ptCd"}, size = 1000) Pageable pageable) {

        /*List<TbPicture> list0 = tbPictureRepository.getPictureList((long) 1, (long) 1);
        TbPicture p = tbPictureRepository.getPictureInfo((long) 1, (long) 1, (long) 1);*/

        Specification<TbPicture> spec = Specifications.where(spec1).and(spec2);
        Iterable<TbPicture> list = tbPictureRepository.findAll(spec, pageable);
        return list;
    }

    @RequestMapping(value = "/saveOrderItems", method = RequestMethod.POST)
    public TbOrderItems saveOrderItems(@Valid TbOrderItems orderitem, BindingResult bindingResult){

        if (bindingResult.hasErrors()) {
            log.error("\n\n\n############### 저장시 bindingResult.hasErrors()\n{}", bindingResult.toString());
//            return "error";
        }

        tbOrderItemsService.saveTbOrderItems(orderitem);

//        return "ok";
        return  orderitem;
    }

    /**
     * 품목 리스트의 품목 정보 저장
     *
     * ,
     *
     * @param orderitem
     * @param bindingResult
     * @return
     */
//    public String saveOrderItems(@Valid TbOrderItems orderitem, BindingResult bindingResult
    @RequestMapping(value = "/saveOrderItems_OLD", method = RequestMethod.POST)
    public TbOrderItems saveOrderItems_OLD(@Valid TbOrderItems orderitem, BindingResult bindingResult
            ,@RequestParam(value="imgfile1", required=false) MultipartFile file1
            ,@RequestParam(value="imgfile2", required=false) MultipartFile file2
            ,@RequestParam(value="imgfile3", required=false) MultipartFile file3
            ,@RequestParam(value="imgfile4", required=false) MultipartFile file4
            ,@RequestParam(value="imgfile5", required=false) MultipartFile file5){

        if (bindingResult.hasErrors()) {
            log.error("\n\n\n############### 저장시 bindingResult.hasErrors()\n{}", bindingResult.toString());
//            return "error";
        }

        tbOrderItemsService.saveTbOrderItems(orderitem);

//        첨부 이미지 처리
        for(int i=1; i<=5; i++) {

            MultipartFile file = null;

            switch (i) {
                case 1:
                    file = file1;
                    break;

                case 2:
                    file = file2;
                    break;

                case 3:
                    file = file3;
                    break;

                case 4:
                    file = file4;
                    break;

                case 5:
                    file = file5;
                    break;
            }

            if (file != null && !file.isEmpty()) {
                String strFileName = "ord_items_img" + orderitem.getId() +"_"+ i;
                String strUploadNm = SynapseCM.imgUpload(file, ord_items_img_dir, strFileName);

                if (StringUtils.isNotEmpty(strUploadNm)) {
//                    사진코드(PT_CD) 컬럼에 이미지 일련번호(1 ~ 5)를 넣어 추후 수정시 사용
//                employee.setEpImg(ord_items_img_url + strUploadNm);
//                    tbOrderItemsService.saveTbOrderItems(orderitem);

                    TbPicture pic = tbPictureRepository.getPictureInfo(orderitem.getOrCd(), orderitem.getId(), (long) i);

                    if(pic == null) {
                        pic = new TbPicture();

                        pic.setOrCd(orderitem.getOrCd());
                        pic.setItCd(orderitem.getId());
                        pic.setPtCd((long) i);

                        pic.setPtImgFile(ord_items_img_url + strUploadNm);

                        pic.setRegiDt(new Date());
                        pic.setUser(SynapseCM.getAuthenticatedUserID());

                        tbPictureService.saveTbPicture(pic);
                    }


                } else {
                    log.error("이미지 업로드 실패!!!");
                }
            }
        }

//        return "ok";
        return  orderitem;
    }

    /**
     * 삭제
     *
     * @param id
     * @return
     */
    @RequestMapping("/delete-items")
    public String deleteItems(@RequestParam(value="no") Long id) {
        tbOrderItemsService.deleteTbOrderItems2(id);
        return "ok";
    }

    /**
     * 품목 리스트
     *
     * @param spec1
     * @param spec2
     * @param pageable
     * @return
     */
    @RequestMapping(value = "/items/list")
    public Iterable<TbOrderItems> itemsList(
            @And({@Spec(path = "orCd", spec = Equal.class)}) Specification<TbOrderItems> spec1,
            @Or({@Spec(path="delYn", spec = notEqual.class, constVal = "Y"),
                    @Spec(path="delYn", spec = IsNull.class)}) Specification<TbOrderItems> spec2,
            @PageableDefault(sort = {"pdLvl1", "pdLvl3", "pdLvl3"}, size = 1000) Pageable pageable) {

//        주문번호 조건이 있을때만 검색한다
        Iterable<TbOrderItems> list = null;

        if (spec1 != null) {
            Specification<TbOrderItems> spec = Specifications.where(spec1).and(spec2);
            list = tbOrderItemsRepository.findAll(spec, pageable);
        } else {
            list = new ArrayList<TbOrderItems>();
        }

        return list;
    }

    /**
     * 삭제
     *
     * @param id
     * @return
     */
    @RequestMapping("/delete-order")
    public String deleteCode(@RequestParam(value="no") Long id) {
        tbOrderService.deleteTbOrder2(id);
        return "ok";
    }

/*    @RequestMapping(value = "/loglist")
    public Iterable<TbOrder> getLogList(
            @RequestParam(value="bncd", required=false) final String bncd,
            @RequestParam(value="from", required=false) final String from,
            @RequestParam(value="to", required=false) final String to,
            @RequestParam(value="keyword", required=false) final String keyword,
            @RequestParam(value="condition", required=false) final String condition,
            @RequestParam(value="status", required=false) final String status,
            @PageableDefault(sort = {"bnCd", "orCd"}, size = 1000) Pageable pageable
    ) {

        log.debug("검색조건 = 지점: {}, 주문From: {}, 주문To: {}, 검색어: {}, 검색구분: {}, 주문상태: {}",
                bncd, from, to, keyword, condition, status);

        Iterable<TbOrder> list = tbOrderService.getLogList2(bncd, from, to, keyword, condition, status, pageable);
        return list;
    }*/

    /**
     * 접수처리 검색
     *
     * @param bncd
     * @param from
     * @param to
     * @param keyword
     * @param condition
     * @param status
     * @return
     */
    @RequestMapping(value = "/loglist")
    public List<?> getLogList(
            @RequestParam(value="bncd", required=false) final String bncd,
            @RequestParam(value="from", required=false) final String from,
            @RequestParam(value="to", required=false) final String to,
            @RequestParam(value="keyword", required=false) final String keyword,
            @RequestParam(value="condition", required=false) final String condition,
            @RequestParam(value="status", required=false) final String status
    ) {

        log.debug("검색조건 = 지점: {}, 주문From: {}, 주문To: {}, 검색어: {}, 검색구분: {}, 주문상태: {}",
                bncd, from, to, keyword, condition, status);

        List<?> list = tbOrderService.getLogList(bncd, from, to, keyword, condition, status);
        return list;
    }

    @Autowired
    private EntityManager em;

    //    https://adrianhummel.wordpress.com/2010/07/02/composed-specifications-using-jpa-2-0/
    @RequestMapping(value = "/list-test")
    public List<?> ListTest(
            @And({@Spec(path = "bnCd", spec = Like.class)}) Specification<TbOrder> spec1,
            @Or({@Spec(path="delYn", spec = notEqual.class, constVal = "Y"),
                    @Spec(path="delYn", spec = IsNull.class)}) Specification<TbOrder> spec2,
            @PageableDefault(sort = {"bnCd", "orCd"}, size = 1000) Pageable pageable) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<TbOrder> q = cb.createQuery(TbOrder.class);
        Root<TbOrder> order = q.from(TbOrder.class);

        Specification<TbOrder> spec = Specifications.where(spec1).and(spec2);
//        q.where(spec.toPredicate(cb, q, order));
        q.where(spec.toPredicate(order, q, cb));

        List<?> list = em.createQuery(q).getResultList();
//        List<?> list = tbOrderService.getOrderList();
        return list;
    }

    @RequestMapping(value = "/list-test3")
    public List<?> ListTest3(
            @And({@Spec(path = "bnCd", spec = Like.class)}) Specification<TbOrder> spec1,
            @Or({@Spec(path="delYn", spec = notEqual.class, constVal = "Y"),
                    @Spec(path="delYn", spec = IsNull.class)}) Specification<TbOrder> spec2,
            @PageableDefault(sort = {"bnCd", "orCd"}, size = 1000) Pageable pageable) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<TbOrder> q = cb.createQuery(TbOrder.class);
        Root<TbOrder> order = q.from(TbOrder.class);

        Specification<TbOrder> spec = Specifications.where(spec1).and(spec2);
//        q.where(spec.toPredicate(cb, q, order));
        q.where(spec.toPredicate(order, q, cb));

//        Set<ParameterExpression<?>> parameterExpressions = q.getParameters();
//        Predicate pred1 = q.getRestriction();
        Predicate pred = spec.toPredicate(order, q, cb);

        // TODO : Predicate 값을 꺼내는 방법을 모르겠다!
//        List<Expression<Boolean>> expressions = ((CompoundPredicate)pred1).expressions;
        List<Expression<Boolean>> expressions = ((CompoundPredicate)pred).getExpressions();
        log.debug("검색조건 = {}", expressions.size());

//        String toSql = this.toSql(q);

        Query selectQuery = em.createQuery(q);
//        Set<Parameter<?>> parameters = selectQuery.getParameters();

//        http://antoniogoncalves.org/2012/05/24/how-to-get-the-jpqlsql-string-from-a-criteriaquery-in-jpa/
//        selectQuery.unwrap(Query.class).q

        List<?> list = selectQuery.getResultList();
//        List<?> list = em.createQuery(q).getResultList();
//        List<?> list = tbOrderService.getOrderList();
        return list;
    }

    //    품목 리스트 그리드 삭제 처리
    @RequestMapping(value = "/deleteGrid", method = RequestMethod.POST)
    public String deleteGrid(
//            @ModelAttribute(value = "items0") ArrayList<String> items,
            @RequestParam(value="items") ArrayList<Long> items,
//            @RequestParam(value="items", required=false) String[] items3,
            HttpServletRequest request
    ) {

        /*Enumeration em = request.getParameterNames();
        while(em.hasMoreElements()){
            String parameterName = (String)em.nextElement();
            String parameterValue = request.getParameter(parameterName);
            String[] parameterValues = request.getParameterValues(parameterName);
            if(parameterValues != null){
                for(int i=0; i<parameterValues.length; i++){
                    System.out.println("array_" + parameterName + "=" + parameterValues[i]);
                }
            } else {
                System.out.println(parameterName + "=" + parameterValue);
            }
        }*/

        if(items != null && items.size() > 0) {
            Integer iResult = tbOrderItemsService.deleteGrid(items);
        }

        return "OK";
    }

    @RequestMapping(value = "/saveAmountCnt")
    public TbOrder saveAmountCnt(@RequestParam(value="no", required=false) final Long no) throws InvocationTargetException, NoSuchMethodException, GcmMulticastLimitExceededException, IOException, IllegalAccessException {
        TbOrder tbOrder = tbOrderItemsService.saveAmountCnt(no);
        return tbOrder;
    }

    //    https://tilmanrossmy.wordpress.com/2010/05/28/hibernate-criteria-to-native-sql/
    private String toSql(Criteria criteria)
    {
        try{
            CriteriaImpl c = (CriteriaImpl) criteria;
            SessionImpl s = (SessionImpl)c.getSession();
            SessionFactoryImplementor factory =
                    (SessionFactoryImplementor)s.getSessionFactory();
            String[] implementors = factory.getImplementors( c.getEntityOrClassName() );
            CriteriaLoader loader =
                    new CriteriaLoader((OuterJoinLoadable)factory.getEntityPersister(implementors[0]),
                            factory, c, implementors[0], s.getLoadQueryInfluencers());
            Field f = OuterJoinLoader.class.getDeclaredField("sql");
            f.setAccessible(true);
            return (String) f.get(loader);
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
    }

/*    @RequestMapping(value = "/list-test0")
    public List<TbOrderVO> ListTest0(
            @And({@Spec(path = "bnCd", spec = Like.class)}) Specification<TbOrder> spec1,
            @Or({@Spec(path="delYn", spec = notEqual.class, constVal = "Y"),
                    @Spec(path="delYn", spec = IsNull.class)}) Specification<TbOrder> spec2,
            @PageableDefault(sort = {"bnCd", "orCd"}, size = 1000) Pageable pageable) {

//        Specification<TbOrder> spec = Specifications.where(spec1).and(spec2);
//        Iterable<TbOrder> list = tbOrderRepository.findAll(spec, pageable);

        List<TbOrderVO> list = tbOrderService.getOrderList();
        return list;
    }*/

    /**
     * 중복확인
     *
     * @param pdLvl1
     * @param pdLvl2
     * @param pdLvl3
     * @return
     */
    /*@RequestMapping("/check-code")
    public String checkDups(@RequestParam(value="pdLvl1", defaultValue = "") String pdLvl1,
                                      @RequestParam(value="pdLvl2", defaultValue = "") String pdLvl2,
                                      @RequestParam(value="pdLvl3", defaultValue = "") String pdLvl3) {
        String isPass = "true";
        List<TbOrder> orders = null;

        if(StringUtils.isNotEmpty(pdLvl1) && StringUtils.isNotEmpty(pdLvl2) && StringUtils.isNotEmpty(pdLvl3)) {
            orders = tbOrderLocsRepository.checkDups(pdLvl1, pdLvl2, pdLvl3);
        }

        if(orders != null && orders.size() > 0) {
            isPass = "false";
        }

        return isPass;
    }*/

}