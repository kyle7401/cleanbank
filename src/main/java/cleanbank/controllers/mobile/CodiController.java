package cleanbank.controllers.mobile;

import cleanbank.Service.*;
import cleanbank.domain.*;
import cleanbank.gcm.exception.GcmMulticastLimitExceededException;
import cleanbank.repositories.*;
import cleanbank.viewmodel.CodeValue;
import cleanbank.viewmodel.MoOrder2;
import cleanbank.viewmodel.MoOrder4;
import cleanbank.viewmodel.MoPromotion;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.IsNull;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Or;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.ImagingOpException;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.apache.commons.beanutils.PropertyUtils.copyProperties;

@RestController
@RequestMapping("/api/codi")
public class CodiController {

    @Inject
    private PropertyEditorRegistrar customPropertyEditorRegistrar2;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        this.customPropertyEditorRegistrar2.registerCustomEditors(binder);
    }

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private TbEmployeeRepository tbEmployeeRepository;

    @Autowired
    private CodiMobileService codiMobileService;

    @Autowired
    private TbOrderItemsRepository tbOrderItemsRepository;

    @Autowired
    private TbOrderRepository tbOrderRepository;

    @Autowired
    private TbPromotionUseRepository tbPromotionUseRepository;

    @Autowired
    private ITbPictureService tbPictureService;

    @Autowired
    private TbTraceInfoRepository tbTraceInfoRepository;

    @Autowired
    private TbProductRepository tbProductRepository;

    @Autowired
    private TbProductService tbProductService;

    @Autowired
    private ITbOrderItemsService tbOrderItemsService;

    @Autowired
    private TbCodeRepository tbCodeRepository;

    //    config.properties : 품목 리스트의 품목 정보 이미지 업로드 폴더및 http base url
    @Value("${ord_items_img_dir}")
    String ord_items_img_dir;

    @Value("${ord_items_img_url}")
    String ord_items_img_url;

    @Autowired
    private TbPictureRepository tbPictureRepository;

    @Autowired
    private PushMobileService pushMobileService;

    @Autowired
    private ITbAddressService tbAddressService;

    @Autowired
    private ITbOrderService tbOrderService;

    @Autowired
    private TbMemberBillinfoRepository tbMemberBillinfoRepository;

    @Autowired
    private ITbBranchService tbBranchService;

//    ==========================================================================================

    @RequestMapping(value = "/deleteBillinfo", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteBillinfo(@And({@Spec(path = "mbCd", spec = Equal.class), @Spec(path = "mbCardNo", spec = Equal.class)}) Specification<TbMemberBillinfo> spec,
                                         HttpServletRequest request) {

        if (spec == null) return new ResponseEntity<>("mbCd, mbCardNo 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);

        List<TbMemberBillinfo> list = StreamSupport.stream(tbMemberBillinfoRepository.findAll(spec).spliterator(), false).collect(Collectors.toList());

        if(list == null || list.size() <= 0) return new ResponseEntity<>("해당 빌링 정보가 존재하지 않습니다!", HttpStatus.NOT_FOUND);

        TbMemberBillinfo tbMemberBillinfo = list.get(0);
        tbMemberBillinfoRepository.delete(tbMemberBillinfo.getId());

        return new ResponseEntity<>("삭제 하였습니다.", HttpStatus.OK);
    }

    /**
     * 30. 지점별 택번호 검색
     * @param bnCd
     * @return
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws GcmMulticastLimitExceededException
     * @throws IOException
     * @throws IllegalAccessException
     */
    @RequestMapping(value = "/getBnTacNo")
    public ResponseEntity<?> getBnTacNo(@RequestParam(value="bnCd", required=false) final Integer bnCd) throws InvocationTargetException, NoSuchMethodException, GcmMulticastLimitExceededException, IOException, IllegalAccessException {
            if(StringUtils.isEmpty(bnCd)) return new ResponseEntity<>("bnCd(지점코드) 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);

        String bncd = null;

        try {
            bncd = tbBranchService.getBnBarCd(bnCd);
        } catch (Exception ex) {}

        if(StringUtils.isEmpty(bncd)) return new ResponseEntity<>("해당 지점의 택번호 정보가 존재하지 않습니다!", HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(bncd, HttpStatus.OK);
    }

    /**
     * 29: 주문수량및 금액 업데이트
     * @param orCd
     * @return
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws GcmMulticastLimitExceededException
     * @throws IOException
     * @throws IllegalAccessException
     */
    @RequestMapping(value = "/saveAmountCnt")
    public ResponseEntity<?> saveAmountCnt(@RequestParam(value="orCd", required=false) final Long orCd) throws InvocationTargetException, NoSuchMethodException, GcmMulticastLimitExceededException, IOException, IllegalAccessException {
        if(StringUtils.isEmpty(orCd)) return new ResponseEntity<>("orCd(주문코드) 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);

        TbOrder tbOrder = tbOrderItemsService.saveAmountCnt(orCd);
        if(tbOrder == null) return new ResponseEntity<>("해당 주문정보가 존재하지 않습니다!", HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(tbOrder, HttpStatus.OK);
    }

    @RequestMapping(value = "/getBillinfo", method = RequestMethod.GET)
    public ResponseEntity<?> getBillinfo(@And({@Spec(path = "mbCd", spec = Equal.class)}) Specification<TbMemberBillinfo> spec,
                                         @PageableDefault(sort = {"mbCardNm"}, size = 1000) Pageable pageable,
                                         HttpServletRequest request) {

        if (spec == null) return new ResponseEntity<>("mbCd 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);

        List<TbMemberBillinfo> list = StreamSupport.stream(tbMemberBillinfoRepository.findAll(spec, pageable).spliterator(), false).collect(Collectors.toList());

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @RequestMapping(value = "/modifyTransport", method = RequestMethod.PATCH)
    public ResponseEntity<?> modifyTransport(@RequestBody MoOrder4 moOrder4) throws NoSuchMethodException, GcmMulticastLimitExceededException, IllegalAccessException, IOException, InvocationTargetException {
        if(StringUtils.isEmpty(moOrder4.getOrCd())) return new ResponseEntity<>("orCd(주문코드) 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);

        Long orCd = moOrder4.getOrCd();

        TbOrder tmpOrder = tbOrderRepository.findOne(orCd);
        if(tmpOrder == null) return new ResponseEntity<>("해당 주문정보가 존재하지 않습니다!", HttpStatus.NOT_FOUND);

        TbOrder tbOrder = new TbOrder();
        copyProperties(tbOrder, tmpOrder); // 검색한 자료를 직접 사용하면 실제 저장시 현재 db 레코드와 새로 업데이트한 내용 비교가 안된다.

//        수거요청 주소
        if(!StringUtils.isEmpty(moOrder4.getReqAddr1()) && !StringUtils.isEmpty(moOrder4.getReqLat()) && !StringUtils.isEmpty(moOrder4.getReqLng())) {
            TbAddress newAddress = new TbAddress();
            newAddress.setMbCd(tbOrder.getMbCd());
            newAddress.setMbAddr1(moOrder4.getReqAddr1());
            newAddress.setMbAddr2(moOrder4.getReqAddr2());
            newAddress.setMbLat(moOrder4.getReqLat());
            newAddress.setMbLng(moOrder4.getReqLng());

            //        주소가 중복 확인후 추가
            TbAddress tbAddress = tbAddressService.addAddress(newAddress);
            tbOrder.setOrReqAddr(tbAddress.getId().toString());
        }

//        수거요청일시
        if(!StringUtils.isEmpty(moOrder4.getOrReqDt())) {
            tbOrder.setOrReqDt(moOrder4.getOrReqDt());
        }

//        배송주소
        if(!StringUtils.isEmpty(moOrder4.getDeliAddr1()) && !StringUtils.isEmpty(moOrder4.getDeliLat()) && !StringUtils.isEmpty(moOrder4.getDeliLng())) {
            TbAddress newAddress = new TbAddress();
            newAddress.setMbCd(tbOrder.getMbCd());
            newAddress.setMbAddr1(moOrder4.getDeliAddr1());
            newAddress.setMbAddr2(moOrder4.getDeliAddr2());
            newAddress.setMbLat(moOrder4.getDeliLat());
            newAddress.setMbLng(moOrder4.getDeliLng());

            //        주소가 중복 확인후 추가
            TbAddress tbAddress = tbAddressService.addAddress(newAddress);
            tbOrder.setOrDeliAddr(tbAddress.getId().toString());
        }

//        배송요청일시
        /*if(!StringUtils.isEmpty(moOrder4.getOrDeliVisitDt())) {
            tbOrder.setOrDeliVisitDt(moOrder4.getOrDeliVisitDt());
        }*/

//        배송요청일시 : 컬럼명을 잘못 주었다
        if(!StringUtils.isEmpty(moOrder4.getOrDeliVisitDt())) {
            tbOrder.setOrDeliFuVisitDt(moOrder4.getOrDeliVisitDt());
        }

//        주문상태 : 3월10일 주문상태에 따라 "수거일시", "배송일시"를 현재 일시로 저장
        if(!StringUtils.isEmpty(moOrder4.getOrStatus())) {
            tbOrder.setOrStatus(moOrder4.getOrStatus());

//          수거완료 : 수거일시
            if("0103".equals(moOrder4.getOrStatus())) {
                tbOrder.setOrVisitDt(new Date());
            }

//          배송완료 : 배송일시
            if("0110".equals(moOrder4.getOrStatus())) {
                tbOrder.setOrDeliVisitDt(new Date());
            }
        }

        tbOrderService.saveTbOrder(tbOrder);

        return new ResponseEntity<>(tbOrder, HttpStatus.OK);
    }

    /**
     * 27.주문비고 저장
     * @param orCd
     * @param orMemo
     * @return
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     */
    @RequestMapping(value = "/setOrMemo", method = RequestMethod.PATCH)
    public ResponseEntity<?> setOrMemo(@RequestParam(value="orCd", required=false) final Long orCd,
                                     @RequestParam(value="orMemo", required=false) final String orMemo) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        if(StringUtils.isEmpty(orCd)) return new ResponseEntity<>("orCd(주문코드) 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);
        if(StringUtils.isEmpty(orMemo)) return new ResponseEntity<>("orMemo(주문비고) 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);

//        db 검색
        TbOrder tbOrder = tbOrderRepository.findOne(orCd);
        if(tbOrder == null) return new ResponseEntity<>("해당 주문정보를 찾을수 없습니다!", HttpStatus.NOT_FOUND);

        if(!StringUtils.isEmpty(orMemo)) tbOrder.setOrMemo(orMemo);
        tbOrderRepository.save(tbOrder);

        return new ResponseEntity<>(tbOrder, HttpStatus.OK);
    }

    /**
     * 26.품목이미지 삭제 : FileController와 중복
     * @return
     */
    /*@RequestMapping(value = "deleteFile/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable("id") TbPicture image) {*/
    @RequestMapping(value = "deleteFile", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@RequestParam(value="id", required=false) final Integer id) {
        if(StringUtils.isEmpty(id)) return new ResponseEntity<>("id(이미지 id) 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);

        TbPicture image = tbPictureRepository.findOne(id);
        if(image == null) return new ResponseEntity<>("해당 이미지 정보를 찾을수 없습니다!", HttpStatus.NOT_FOUND);

        File imageFile = new File(ord_items_img_dir + image.getNewFilename());
        imageFile.delete();
        File thumbnailFile = new File(ord_items_img_dir + image.getThumbnailFilename());
        thumbnailFile.delete();

        tbPictureService.deleteTbPicture2(image.getId());

        return new ResponseEntity<>("삭제하였습니다!", HttpStatus.NO_CONTENT);
    }

    /**
     * 25.주문상태코드 : 주문상태코드 정보를 가져온다.(예:주문접수/수거중/…)
     * @param orCd
     * @return
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     */
    @RequestMapping(value = "/getOrderStatus", method = RequestMethod.GET)
    public ResponseEntity<?> getOrderStatus(@RequestParam(value="orCd", required=false) final Long orCd) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        if(StringUtils.isEmpty(orCd)) return new ResponseEntity<>("orCd(주문코드) 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);

//        db 검색
        TbOrder tbOrder = tbOrderRepository.findOne(orCd);
        if(tbOrder == null) return new ResponseEntity<>("해당 주문정보를 찾을수 없습니다!", HttpStatus.NOT_FOUND);

        String strOrStatus = tbOrder.getOrStatus();

        CodeValue codeValue = new CodeValue();
        codeValue.setCode(strOrStatus);

        if(!StringUtils.isEmpty(strOrStatus) && strOrStatus.length() >= 4) {
            TbCode tbCode = tbCodeRepository.getCdNm(strOrStatus.substring(0, 2), strOrStatus.substring(2, 4));
            if(!StringUtils.isEmpty(tbCode)) codeValue.setValue(tbCode.getCdNm());
        }

        return new ResponseEntity<>(codeValue, HttpStatus.OK);
    }

    /**
     * 23.수거지주소 저장 : 변경된 수거지 주소를 저장한다
     * @param orCd
     * @param id
     * @param mbAddr1
     * @param mbAddr2
     * @param mbLat
     * @param mbLng
     * @return
     * @throws IllegalStateException
     * @throws IOException
     * @throws ImagingOpException
     */
    @RequestMapping(value = "setDeliAddr", method = RequestMethod.PATCH)
    public ResponseEntity<?> setDeliAddr(@RequestParam(value="orCd", required=false) final Long orCd,
                                        @RequestParam(value="id", required=false) final Long id,
                                        @RequestParam(value="mbAddr1", required=false) final String mbAddr1,
                                        @RequestParam(value="mbAddr2", required=false) final String mbAddr2,
                                        @RequestParam(value="mbLat", required=false) final String mbLat,
                                        @RequestParam(value="mbLng", required=false) final String mbLng
    ) throws IllegalStateException, IOException, ImagingOpException {

        if(StringUtils.isEmpty(orCd)) return new ResponseEntity<>("orCd(주문코드) 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);
        if(StringUtils.isEmpty(id)) return new ResponseEntity<>("id(품목코드) 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);
        if(StringUtils.isEmpty(mbAddr1)) return new ResponseEntity<>("mbAddr1(주소) 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);
        if(StringUtils.isEmpty(mbLat)) return new ResponseEntity<>("mbLat(위도) 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);
        if(StringUtils.isEmpty(mbLng)) return new ResponseEntity<>("mbLng(경도) 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);

        TbOrder tbOrder = tbOrderRepository.findOne(orCd);
        if(tbOrder == null) return new ResponseEntity<>("해당 주문정보가 존재하지 않습니다!", HttpStatus.NOT_FOUND);

        TbOrderItems tbOrderItems = tbOrderItemsRepository.findOne(id);
        if(tbOrderItems == null) return new ResponseEntity<>("해당 품목정보가 존재하지 않습니다!", HttpStatus.NOT_FOUND);

        TbAddress newAddress = new TbAddress();
        newAddress.setMbCd(tbOrder.getMbCd());
        newAddress.setMbAddr1(mbAddr1);
        newAddress.setMbAddr2(mbAddr2);
        newAddress.setMbLat(mbLat);
        newAddress.setMbLng(mbLng);

        TbAddress tbAddress = codiMobileService.setDeliAddr(tbOrder, tbOrderItems, newAddress);

        return new ResponseEntity<>(tbAddress, HttpStatus.OK);
    }

    /**
     * 23.수거지주소 저장 : 변경된 수거지 주소를 저장한다
     * @param orCd
     * @param id
     * @param mbAddr1
     * @param mbAddr2
     * @param mbLat
     * @param mbLng
     * @return
     * @throws IllegalStateException
     * @throws IOException
     * @throws ImagingOpException
     */
    @RequestMapping(value = "setReqAddr", method = RequestMethod.PATCH)
    public ResponseEntity<?> setReqAddr(@RequestParam(value="orCd", required=false) final Long orCd,
                                        @RequestParam(value="id", required=false) final Long id,
                                        @RequestParam(value="mbAddr1", required=false) final String mbAddr1,
                                        @RequestParam(value="mbAddr2", required=false) final String mbAddr2,
                                        @RequestParam(value="mbLat", required=false) final String mbLat,
                                        @RequestParam(value="mbLng", required=false) final String mbLng
    ) throws IllegalStateException, IOException, ImagingOpException {

        if(StringUtils.isEmpty(orCd)) return new ResponseEntity<>("orCd(주문코드) 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);
        if(StringUtils.isEmpty(id)) return new ResponseEntity<>("id(품목코드) 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);
        if(StringUtils.isEmpty(mbAddr1)) return new ResponseEntity<>("mbAddr1(주소) 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);
        if(StringUtils.isEmpty(mbLat)) return new ResponseEntity<>("mbLat(위도) 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);
        if(StringUtils.isEmpty(mbLng)) return new ResponseEntity<>("mbLng(경도) 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);

        TbOrder tbOrder = tbOrderRepository.findOne(orCd);
        if(tbOrder == null) return new ResponseEntity<>("해당 주문정보가 존재하지 않습니다!", HttpStatus.NOT_FOUND);

        TbOrderItems tbOrderItems = tbOrderItemsRepository.findOne(id);
        if(tbOrderItems == null) return new ResponseEntity<>("해당 품목정보가 존재하지 않습니다!", HttpStatus.NOT_FOUND);

        TbAddress newAddress = new TbAddress();
        newAddress.setMbCd(tbOrder.getMbCd());
        newAddress.setMbAddr1(mbAddr1);
        newAddress.setMbAddr2(mbAddr2);
        newAddress.setMbLat(mbLat);
        newAddress.setMbLng(mbLng);

        TbAddress tbAddress = codiMobileService.setReqAddr(tbOrder, tbOrderItems, newAddress);

        return new ResponseEntity<>(tbAddress, HttpStatus.OK);
    }

    /**
     * 20.품목상위정보
     * @return
     */
    @RequestMapping("/getProductList1")
    public ResponseEntity<?> getProductList1() {

        List<TbProduct> pdLvl1s = tbProductService.getPdLvl1s();
        return new ResponseEntity<>(pdLvl1s, HttpStatus.OK);
    }

    /**
     * 21.품목중위정보
     * @param spec1
     * @param spec2
     * @param pageable
     * @param model
     * @return
     */
    @RequestMapping("/getProductList2")
    public ResponseEntity<?> getProductList2(
            @And({@Spec(path = "pdLvl1", spec = Equal.class),
                    @Spec(path = "pdLvl2", spec = notEqual.class, constVal = "00"),
                    @Spec(path = "pdLvl3", spec = Equal.class, constVal = "00")
            }) Specification<TbProduct> spec1,
            @Or({@Spec(path="delYn", spec = notEqual.class, constVal = "Y"),
                    @Spec(path="delYn", spec = IsNull.class)}) Specification<TbProduct> spec2,
            @PageableDefault(sort = {"pdLvl1", "pdSort", "pdLvl2", "pdLvl3"}, size = 1000) Pageable pageable, Model model) {


        Specification<TbProduct> spec = Specifications.where(spec1).and(spec2);
        Iterable<TbProduct> list = tbProductRepository.findAll(spec, pageable);

        List<TbProduct> itemList = StreamSupport.stream(list.spliterator(), false).collect(Collectors.toList());
        return new ResponseEntity<>(itemList, HttpStatus.OK);
    }

    /**
     * 22.품목하위정보
     * @param spec1
     * @param spec2
     * @param pageable
     * @param model
     * @return
     */
    @RequestMapping("/getProductList3")
    public ResponseEntity<?> getProductList3(
            @And({@Spec(path = "pdLvl1", spec = Equal.class),
                    @Spec(path = "pdLvl2", spec = Equal.class),
                    @Spec(path = "pdLvl3", spec = notEqual.class, constVal = "00")
            }) Specification<TbProduct> spec1,
            @Or({@Spec(path="delYn", spec = notEqual.class, constVal = "Y"),
                    @Spec(path="delYn", spec = IsNull.class)}) Specification<TbProduct> spec2,
            @PageableDefault(sort = {"pdLvl1", "pdLvl2", "pdSort", "pdLvl3"}, size = 1000) Pageable pageable, Model model) {


        Specification<TbProduct> spec = Specifications.where(spec1).and(spec2);
        Iterable<TbProduct> list = tbProductRepository.findAll(spec, pageable);

        List<TbProduct> itemList = StreamSupport.stream(list.spliterator(), false).collect(Collectors.toList());
        return new ResponseEntity<>(itemList, HttpStatus.OK);
    }

    @RequestMapping("/getProductList")
    public ResponseEntity<?> getProductList(
            @And({@Spec(path = "pdLvl1", spec = Equal.class),
                    @Spec(path = "pdLvl2", spec = Equal.class),
                    @Spec(path = "pdLvl3", spec = notEqual.class)
            }) Specification<TbProduct> spec1,
            @Or({@Spec(path="delYn", spec = notEqual.class, constVal = "Y"),
                    @Spec(path="delYn", spec = IsNull.class)}) Specification<TbProduct> spec2,
            @PageableDefault(sort = {"pdLvl1", "pdLvl2", "pdSort", "pdLvl3"}, size = 1000) Pageable pageable, Model model) {

        Specification<TbProduct> spec = Specifications.where(spec1).and(spec2);
        Iterable<TbProduct> list = tbProductRepository.findAll(spec, pageable);

        List<TbProduct> itemList = StreamSupport.stream(list.spliterator(), false).collect(Collectors.toList());
        return new ResponseEntity<>(itemList, HttpStatus.OK);
    }

    /**
     * 07.위치이동정보 : 3분간격으로 위치정보를 서버에 전송한다.
     * @param tbTraceInfo
     * @return
     * @throws IllegalStateException
     * @throws IOException
     * @throws ImagingOpException
     */
    @RequestMapping(value = "saveTraceInfo", method = RequestMethod.POST)
    public ResponseEntity<?> saveTraceInfo(@RequestBody TbTraceInfo tbTraceInfo) throws IllegalStateException, IOException, ImagingOpException {

        if(StringUtils.isEmpty(tbTraceInfo.getOrCd())) return new ResponseEntity<>("orCd(주문코드) 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);
        if(StringUtils.isEmpty(tbTraceInfo.getEpCd())) return new ResponseEntity<>("epCd(코디번호) 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);
        if(StringUtils.isEmpty(tbTraceInfo.getTiLati())) return new ResponseEntity<>("tiLati(위도) 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);
        if(StringUtils.isEmpty(tbTraceInfo.getTiLong())) return new ResponseEntity<>("tiLong(경도) 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);

        tbTraceInfo.setRegiDt(new Date());

        tbTraceInfoRepository.save(tbTraceInfo);

        return new ResponseEntity<>(tbTraceInfo, HttpStatus.OK);
    }

    /**
     * 06.스케쥴취소 : 스케쥴을 취소한다.
     * @param orCd
     * @param epCd
     * @return
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     */
    @RequestMapping(value = "/clearEpCd", method = RequestMethod.PATCH)
    public ResponseEntity<?> clearEpCd(@RequestParam(value="orCd", required=false) final Long orCd,
                                     @RequestParam(value="epCd", required=false) final Integer epCd) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        if(StringUtils.isEmpty(orCd)) return new ResponseEntity<>("orCd(주문코드) 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);
        if(StringUtils.isEmpty(epCd)) return new ResponseEntity<>("epCd(코디번호) 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);

//        db 검색
        TbOrder tbOrder = tbOrderRepository.findOne(orCd);
        if(tbOrder == null) return new ResponseEntity<>("해당 주문정보를 찾을수 없습니다!", HttpStatus.NOT_FOUND);

        if(!epCd.equals(tbOrder.getEpCd())) return new ResponseEntity<>("해당 주문건에 배정된 코디가 아닙니다!", HttpStatus.BAD_REQUEST);

        tbOrder.setEpCd(null);
        tbOrder.setEvtNm("스케쥴취소");
        tbOrderRepository.save(tbOrder);

        return new ResponseEntity<>(tbOrder, HttpStatus.OK);
    }

    /**
     * 05.스케쥴등록 : 스케쥴을 등록한다.
     * @param orCd
     * @param epCd
     * @return
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     */
    @RequestMapping(value = "/setEpCd", method = RequestMethod.PATCH)
    public ResponseEntity<?> setEpCd(@RequestParam(value="orCd", required=false) final Long orCd,
                                     @RequestParam(value="epCd", required=false) final Integer epCd,
                                     @RequestParam(value="orVisitPurpose", required=false) final String orVisitPurpose,
                                     @RequestParam(value="orMemo", required=false) final String orMemo) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        if(StringUtils.isEmpty(orCd)) return new ResponseEntity<>("orCd(주문코드) 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);
        if(StringUtils.isEmpty(epCd)) return new ResponseEntity<>("epCd(코디번호) 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);

//        db 검색
        TbOrder tbOrder = tbOrderRepository.findOne(orCd);
        if(tbOrder == null) return new ResponseEntity<>("해당 주문정보를 찾을수 없습니다!", HttpStatus.NOT_FOUND);

        if(!StringUtils.isEmpty(tbOrder.getEpCd())) return new ResponseEntity<>("해당 주문건은 이미 수거 코디가 배정 되었습니다!", HttpStatus.BAD_REQUEST);

        tbOrder.setEpCd(epCd);
        tbOrder.setEvtNm("스케쥴등록");

        if(!StringUtils.isEmpty(orVisitPurpose)) tbOrder.setOrVisitPurpose(orVisitPurpose);
        if(!StringUtils.isEmpty(orMemo)) tbOrder.setOrMemo(orMemo);

//        3월11일 배송코디 등록
        tbOrder.setOrDeliEpCd(epCd);

        tbOrderRepository.save(tbOrder);

//        푸시 메세지
        try {
            pushMobileService.sendCodiAssign(tbOrder);
        } catch (Exception e) {
            log.error("코디 배정 푸시 메세지 에러 {}", e);
            e.printStackTrace();
        }

        return new ResponseEntity<>(tbOrder, HttpStatus.OK);
    }

    /**
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/getItemDetail", method = RequestMethod.GET)
    public ResponseEntity<?> getItemDetail(@RequestParam(value="id", required=false) final Long id) {

        if(StringUtils.isEmpty(id)) return new ResponseEntity<>("id(품목id) 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);

//        db 검색
        TbOrderItems tbOrderItems = codiMobileService.getItemDetail(id);
        if(tbOrderItems == null) return new ResponseEntity<>("해당 품목정보를 찾을수 없습니다!", HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(tbOrderItems, HttpStatus.OK);
    }

    /**
     * 10.품목상세정보 등록
     * @param newOrderItems
     * @return
     * @throws IllegalStateException
     * @throws IOException
     * @throws ImagingOpException
     */
    @RequestMapping(value = "/saveOrdItems", method = RequestMethod.POST)
    public ResponseEntity<?> saveOrdItems(/*@RequestParam(value = "files[]", required = false) MultipartFile[] files,*/
                                          @ModelAttribute("tbOrderItems") TbOrderItems newOrderItems) throws IllegalStateException, IOException, ImagingOpException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, GcmMulticastLimitExceededException {

        TbOrder tbOrder = tbOrderRepository.findOne(newOrderItems.getOrCd());
        if(tbOrder == null) return new ResponseEntity<>("해당 주문정보가 존재하지 않습니다!", HttpStatus.NOT_FOUND);

//        필수컬럼 체크
        if(StringUtils.isEmpty(newOrderItems.getItCnt())) newOrderItems.setItCnt(0);
        if(StringUtils.isEmpty(newOrderItems.getItPrice())) return new ResponseEntity<>("가격을 입력해 주세요!", HttpStatus.BAD_REQUEST);
        if(StringUtils.isEmpty(newOrderItems.getItReqAddr())) newOrderItems.setItReqAddr("");
        if(StringUtils.isEmpty(newOrderItems.getOrCd())) return new ResponseEntity<>("주문코드를 입력해 주세요!", HttpStatus.BAD_REQUEST);
        if(StringUtils.isEmpty(newOrderItems.getPdLvl1())) return new ResponseEntity<>("품목상위코드를 입력해 주세요!", HttpStatus.BAD_REQUEST);
        if(StringUtils.isEmpty(newOrderItems.getPdLvl2())) return new ResponseEntity<>("품목중위코드를 입력해 주세요!", HttpStatus.BAD_REQUEST);
        if(StringUtils.isEmpty(newOrderItems.getPdLvl3())) return new ResponseEntity<>("품목하위코드를 입력해 주세요!", HttpStatus.BAD_REQUEST);
        if(StringUtils.isEmpty(newOrderItems.getItReqDt())) return new ResponseEntity<>("수거요청일을 입력해 주세요!", HttpStatus.BAD_REQUEST);
        if(StringUtils.isEmpty(newOrderItems.getItTac())) return new ResponseEntity<>("택번호를 입력해 주세요!", HttpStatus.BAD_REQUEST);

        log.debug("업로드 시작됨");

        MultipartFile[] files = newOrderItems.getFiles();

        if(files == null || files.length <= 0) {
            log.error("첨부파일 없음");
        } else {
            log.error("첨부파일 갯수 = {}", files.length);
        }

        newOrderItems.setUser("모바일");
//        newOrderItems.setRegiDt(new Date());
//        tbOrderItems = tbOrderItemsRepository.save(tbOrderItems);

//        품목 상/중/하 코드	pdAllLvl

//        수거지주소	itReqAddr, 수거위도	itReqLat, 수거경도	itReqLng

//        배송지주소	itDeliAddr, 배송위도	itDeliLat, 배송경도	itDeliLng

        // 그냥 저장하면 앱에서 사용하지 않는 컬럼은 null로 업데이트 된다
//        tbOrderItemsRepository.save(newOrderItems);

        TbOrderItems tbOrderItems = null;
        if(!StringUtils.isEmpty(newOrderItems.getId()) && newOrderItems.getId() > 0) tbOrderItems = tbOrderItemsService.getTbOrderItemsById(newOrderItems.getId());

        if(tbOrderItems == null) { // 신규
//            신규일 경우 택번호 중복 체크
            Object[] objTacCnt = tbOrderItemsRepository.getTacCnt(newOrderItems.getItTac());
            if(!"0".equals(objTacCnt[0].toString())) {
                String bncd = tbBranchService.getBnBarCd(tbOrder.getBnCd());
                return new ResponseEntity<>("이미 존재하는 택번호 입니다!\n"+ bncd, HttpStatus.CONFLICT);
            }

            tbOrderItems = new TbOrderItems();
            copyProperties(tbOrderItems, newOrderItems);
            tbOrderItems.setUser("모바일");
            tbOrderItems.setMode("new");
        } else { // 수정
            tbOrderItems.setOrCd(newOrderItems.getOrCd());
            tbOrderItems.setItTac(newOrderItems.getItTac());
            tbOrderItems.setPdLvl1(newOrderItems.getPdLvl1());
            tbOrderItems.setPdLvl2(newOrderItems.getPdLvl2());
            tbOrderItems.setPdLvl3(newOrderItems.getPdLvl3());
            tbOrderItems.setItPrice(newOrderItems.getItPrice());
            tbOrderItems.setItStatus(newOrderItems.getItStatus());
            tbOrderItems.setItMemo(newOrderItems.getItMemo());
            tbOrderItems.setItClMemo(newOrderItems.getItClMemo());
            tbOrderItems.setItReqDt(newOrderItems.getItReqDt());
//            tbOrderItems.setItReqAddr(newOrderItems.getItReqAddr());
            tbOrderItems.setItDeliFuVisitDt(newOrderItems.getItDeliFuVisitDt());
//            tbOrderItems.setItDeliAddr(newOrderItems.getItDeliAddr());
            tbOrderItems.setMode("edit");

//            tbOrderItems.setItReqAddr(""); // not null 에러 방지용
        }

        tbOrderItems = tbOrderItemsService.saveTbOrderItems(tbOrderItems);

        //        수거지 주소
        if(!StringUtils.isEmpty(newOrderItems.getItReqLat()) && !StringUtils.isEmpty(newOrderItems.getItReqLng()) && !StringUtils.isEmpty(newOrderItems.getItReqAddr1())) {
            TbAddress newAddress = new TbAddress();
            newAddress.setMbCd(tbOrder.getMbCd());
            newAddress.setMbAddr1(newOrderItems.getItReqAddr1());
            newAddress.setMbAddr2(newOrderItems.getItReqAddr2());
            newAddress.setMbLat(newOrderItems.getItReqLat());
            newAddress.setMbLng(newOrderItems.getItReqLng());

            TbAddress tbAddress = codiMobileService.setReqAddr(tbOrder, tbOrderItems, newAddress);
        }

//        배송지 주소
        if(!StringUtils.isEmpty(newOrderItems.getItDeliLat()) && !StringUtils.isEmpty(newOrderItems.getItDeliLng()) && !StringUtils.isEmpty(newOrderItems.getItDeliAddr1())) {
            TbAddress deliAddress = new TbAddress();
            deliAddress.setMbCd(tbOrder.getMbCd());
            deliAddress.setMbAddr1(newOrderItems.getItDeliAddr1());
            deliAddress.setMbAddr2(newOrderItems.getItDeliAddr2());
            deliAddress.setMbLat(newOrderItems.getItDeliLat());
            deliAddress.setMbLng(newOrderItems.getItDeliLng());

            TbAddress tbAddress = codiMobileService.setDeliAddr(tbOrder, tbOrderItems, deliAddress);
        }

//        사진 처리
        if(files != null && files.length > 0) {
            Map<String, Object> file_list = tbPictureService.uploadImages(files, tbOrderItems.getId(), tbOrderItems.getOrCd());
        }

//        금액/품목수 업데이트
        tbOrderItemsService.saveAmountCnt(tbOrder.getOrCd());

        return new ResponseEntity<>(tbOrderItems, HttpStatus.OK);
//        return new ResponseEntity<>(tbOrderItems, HttpStatus.OK);
    }

    /**
     * 13.주문쿠폰정보 : 주문 시에 이용한 쿠폰정보를 가져온다.
     * @param spec1
     * @param spec2
     * @param request
     * @return
     */
    @RequestMapping(value = "/getOrdPoCoupInf", method = RequestMethod.GET)
    public ResponseEntity<?> getPoCoupInf(@And({@Spec(path = "mbCd", spec = Equal.class), @Spec(path = "orCd", spec = Equal.class)}) Specification<TbPromotionUse> spec1,
                                          @Or({@Spec(path = "delYn", spec = notEqual.class, constVal = "Y"),
                                                  @Spec(path = "delYn", spec = IsNull.class)}) Specification<TbPromotionUse> spec2,
                                          HttpServletRequest request) {

        if (spec1 == null) return new ResponseEntity<>("mbCd(고객코드), orCd(주문코드) 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);

        String mbCd = request.getParameter("mbCd");
        String orCd = request.getParameter("orCd");
        if(StringUtils.isEmpty(mbCd)) return new ResponseEntity<>("mbCd(고객코드) 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);
        if(StringUtils.isEmpty(orCd)) return new ResponseEntity<>("orCd(주문코드) 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);

        Specification<TbPromotionUse> spec = Specifications.where(spec1).and(spec2);
        TbPromotionUse tbPromotionUse = tbPromotionUseRepository.findOne(spec);

        if(tbPromotionUse == null) return new ResponseEntity<>("해당 고객/주문 코드에 해당하는 쿠폰사용 정보가 존재하지 않습니다!", HttpStatus.NOT_FOUND);

        TbPromotion tbPromotion = tbPromotionUse.getTbPromotion();
        MoPromotion moPromotion = new MoPromotion();

        moPromotion.setId(tbPromotionUse.getId());
        moPromotion.setPoNm(tbPromotion.getPoNm());
        moPromotion.setPuUse(tbPromotionUse.getPuUse());
        moPromotion.setPoStartDt(tbPromotion.getPoStartDt());
        moPromotion.setPoFinishDt(tbPromotion.getPoFinishDt());
        moPromotion.setPoDubYn(tbPromotion.getPoDubYn());
        moPromotion.setPoImg(tbPromotion.getPoImg());

        moPromotion.setMbCd(Integer.parseInt(mbCd));
        moPromotion.setPoCoup(tbPromotion.getPoCoup());
        moPromotion.setPoCd(tbPromotion.getPoCd());
        moPromotion.setUseYn(tbPromotionUse.getUseYn());

        moPromotion.setPoGoldPrice(tbPromotion.getPoGoldPrice());
        moPromotion.setPoSilverPrice(tbPromotion.getPoSilverPrice());
        moPromotion.setPoGreenPrice(tbPromotion.getPoGreenPrice());

        moPromotion.setPoGoldPer(tbPromotion.getPoGoldPer());
        moPromotion.setPoSilverPer(tbPromotion.getPoSilverPer());
        moPromotion.setPoGreenPer(tbPromotion.getPoGreenPer());

        moPromotion.setPoLimitAmount(tbPromotion.getPoLimitAmount());

        if(!StringUtils.isEmpty(tbPromotionUse.getOrCd())) moPromotion.setOrCd(tbPromotionUse.getOrCd());
        if(!StringUtils.isEmpty(tbPromotionUse.getPuUse())) moPromotion.setPuUse(tbPromotionUse.getPuUse());
        if(!StringUtils.isEmpty(tbPromotionUse.getPuUseDt())) moPromotion.setPuUseDt(tbPromotionUse.getPuUseDt());

        return new ResponseEntity<>(moPromotion, HttpStatus.OK);
    }

    /**
     * 12.결제여부전송 : 결제여부를 전송한다.
     * @param tbOrder
     * @return
     */
    @RequestMapping(value = "/setOrCharge", method = RequestMethod.POST)
    public ResponseEntity<?> setOrCharge(@RequestBody TbOrder tbOrder){
//        필수 컬럼 체크
        if(StringUtils.isEmpty(tbOrder.getOrCd())) return new ResponseEntity<>("orCd(주문코드) 값을 입력해 주세요!", HttpStatus.BAD_REQUEST);
        if(StringUtils.isEmpty(tbOrder.getOrChargeType())) return new ResponseEntity<>("orChargeType(결제여부) 값을 입력해 주세요!", HttpStatus.BAD_REQUEST);

        TbOrder saveTbOrder = tbOrderRepository.findOne(tbOrder.getOrCd());

        if(saveTbOrder == null)  return new ResponseEntity<>("해당 주문 정보를 찾을수 없습니다!", HttpStatus.NOT_FOUND);

        saveTbOrder.setOrChargeType(tbOrder.getOrChargeType());
        saveTbOrder.setOrChargeMemo(tbOrder.getOrChargeMemo());
        tbOrderRepository.save(saveTbOrder);

        return new ResponseEntity<>(saveTbOrder, HttpStatus.OK);
    }

    /**
     * 10.품목삭제 : 품목내역을 삭제한다.
     * @param spec1
     * @param spec2
     * @return
     */
    @RequestMapping(value = "/deleteOrderItems", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteOrderItems(
            @And({
                    @Spec(path = "orCd", spec = Equal.class),
                    @Spec(path = "id", spec = Equal.class)
            }) Specification<TbOrderItems> spec1,
            @Or({@Spec(path="delYn", spec = notEqual.class, constVal = "Y"),
                    @Spec(path="delYn", spec = IsNull.class)}) Specification<TbOrderItems> spec2) throws InvocationTargetException, NoSuchMethodException, GcmMulticastLimitExceededException, IOException, IllegalAccessException {

        if (spec1 == null) {
            return new ResponseEntity<>("orCd및 id 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);
        }

        //        누락된 파라메터 확인
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String orCd = request.getParameter("orCd");
        String id = request.getParameter("id");
//        String mbPath = request.getParameter("mbPath");

        if(StringUtils.isEmpty(orCd)) return new ResponseEntity<>("orCd 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);
        if(StringUtils.isEmpty(id)) return new ResponseEntity<>("id 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);

        Specification<TbOrderItems> spec = Specifications.where(spec1).and(spec2);

        TbOrderItems tbOrderItems = tbOrderItemsRepository.findOne(spec);

        if (tbOrderItems == null) {
            return new ResponseEntity<>("해당 주문 데이터를 찾을수 없습니다!", HttpStatus.NOT_FOUND);
        }

//        삭제여부를 Y로
        tbOrderItems.setDelYn("Y");
        tbOrderItems.setEvtNm("삭제");
        tbOrderItemsRepository.save(tbOrderItems);

        //        금액/품목수 업데이트
        tbOrderItemsService.saveAmountCnt(tbOrderItems.getOrCd());

        return new ResponseEntity<>("삭제하였습니다!", HttpStatus.NO_CONTENT);
    }

    /**
     * 18.내정보 : 코디의 암호를 변경한다.
     * @param tbEmployee
     * @return
     */
    @RequestMapping(value = "/changePwd", method = RequestMethod.PATCH)
    public ResponseEntity<?> changePwd(@RequestBody TbEmployee tbEmployee){
//        필수 컬럼 체크
        if(StringUtils.isEmpty(tbEmployee.getEpCd())) return new ResponseEntity<>("epCd(코디번호) 값을 입력해 주세요!", HttpStatus.BAD_REQUEST);
        if(StringUtils.isEmpty(tbEmployee.getCurPass())) return new ResponseEntity<>("curPass(현재패스워드) 값을 입력해 주세요!", HttpStatus.BAD_REQUEST);
        if(StringUtils.isEmpty(tbEmployee.getNewPass())) return new ResponseEntity<>("newPass(변경패스워드) 값을 입력해 주세요!", HttpStatus.BAD_REQUEST);

        TbEmployee tbNewEmployee = tbEmployeeRepository.findOne(tbEmployee.getEpCd());

        if(tbNewEmployee == null)  return new ResponseEntity<>("해당 코디 정보를 찾을수 없습니다!", HttpStatus.NOT_FOUND);

        String encPwd = tbNewEmployee.getEpPass();
        boolean equal = cleanbank.utils.PasswordCrypto.getInstance().equal(tbEmployee.getCurPass(), encPwd);

        if(!equal)  return new ResponseEntity<>("현재 비밀번호가 일치하지 않습니다!", HttpStatus.BAD_REQUEST);

        String newEncPwd = cleanbank.utils.PasswordCrypto.getInstance().encrypt(tbEmployee.getNewPass());
        tbNewEmployee.setEpPass(newEncPwd);
        tbEmployeeRepository.save(tbNewEmployee);

        return new ResponseEntity<>(tbNewEmployee, HttpStatus.OK);
    }

    /**
     * 4.주문상세정보 : 주문상세정보를 가져온다.
     * @param orCd
     * @return
     */
    @RequestMapping(value = "/getOrderDetail", method = RequestMethod.GET)
    public ResponseEntity<?> getOrderDetail(@RequestParam(value="orCd", required=false) final Long orCd) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        if(StringUtils.isEmpty(orCd)) return new ResponseEntity<>("orCd(주문코드) 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);

//        db 검색
        MoOrder2 moOrder2 = codiMobileService.getOrderDetail(orCd);
        if(moOrder2 == null) return new ResponseEntity<>("해당 주문정보를 찾을수 없습니다!", HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(moOrder2, HttpStatus.OK);
    }

    /**
     * 03.지점전체 : 코디가 소속되어 있는 지점의 스케쥴정보를 가져온다.
     * 수거코디가 지정되지 않은 경우만
     * @param bnCd
     * @return
     */
    @RequestMapping(value = "/getBnSchedule", method = RequestMethod.GET)
    public ResponseEntity<?> getBnSchedule(@RequestParam(value="bnCd", required=false) final Integer bnCd) {

        if(StringUtils.isEmpty(bnCd)) return new ResponseEntity<>("bnCd(지점코드) 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);

//        db 검색
        List<?> list = codiMobileService.getBnSchedule(bnCd);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    /**
     * 02.나의 스케쥴	: 코디에게 할당 및 선택한 스케쥴내역을 가져온다.
     * @param epCd
     * @return
     */
    @RequestMapping(value = "/getMySchedule", method = RequestMethod.GET)
    public ResponseEntity<?> getMySchedule(@RequestParam(value="epCd", required=false) final Integer epCd) {

        if(StringUtils.isEmpty(epCd)) return new ResponseEntity<>("epCd(코디번호) 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);

//        db 검색
        List<?> list = codiMobileService.getMySchedule(epCd);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    /**
     * 로그인 : CMS에서 등록된 코디 아이디 및 암호를 이용하여 로그인
     * 코디배정 참고 : http://baekmin.synapsetech.co.kr:8080/take/assign/list
     * @param spec1
     * @param spec2
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ResponseEntity<?> login(
            @And({
                    @Spec(path = "epEmail", spec = Equal.class),
                    @Spec(path = "epPass", spec = Equal.class)
            }) Specification<TbEmployee> spec1,
            @Or({
                    @Spec(path="delYn", spec = notEqual.class, constVal = "Y"),
                    @Spec(path="delYn", spec = IsNull.class)
            }) Specification<TbEmployee> spec2
    ) {

        TbEmployee tbEmployee = new TbEmployee();

//        검색조건을 모두 입력하지 않았을 경우
        if(spec1 == null) return new ResponseEntity<>("epEmail, epPass 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);

//        누락된 검색조건 확인
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String epEmail = request.getParameter("epEmail");
        String epPass = request.getParameter("epPass");
        String epDeviceToken = request.getParameter("epDeviceToken");

        if(StringUtils.isEmpty(epEmail)) return new ResponseEntity<>("epEmail 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);
        if(StringUtils.isEmpty(epPass)) return new ResponseEntity<>("epPass 파라메터를 입력해 주세요!", HttpStatus.BAD_REQUEST);

//        db 검색
//        tbEmployee = tbEmployeeRepository.findByEpEmailAndEpPass(epEmail, encPwd);
        tbEmployee = tbEmployeeRepository.findByEpEmail(epEmail);

        if(tbEmployee == null)  return new ResponseEntity<>("해당 코디 정보를 찾을수 없습니다!", HttpStatus.NOT_FOUND);

        /*List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER");
        org.springframework.security.core.userdetails.User usr = new User(tbEmployee.getEpEmail(), epPass, authorities);
        log.debug("로그인 유저 = {}", usr);*/

//        String encPwd = cleanbank.utils.PasswordCrypto.getInstance().encrypt(epPass);
        String encPwd = tbEmployee.getEpPass();
        boolean equal = cleanbank.utils.PasswordCrypto.getInstance().equal(epPass, encPwd);
//        log.debug("비밀번호 일치 {} = {}", epPass, equal);

        if(!equal)  return new ResponseEntity<>("비밀번호가 일치하지 않습니다!", HttpStatus.NOT_FOUND);

        tbEmployee.setEpDeviceToken(epDeviceToken);
        tbEmployee = tbEmployeeRepository.save(tbEmployee);

        return new ResponseEntity<>(tbEmployee, HttpStatus.OK);
    }

}