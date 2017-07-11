package cleanbank.domain;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QTbOrder is a Querydsl query type for TbOrder
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QTbOrder extends EntityPathBase<TbOrder> {

    private static final long serialVersionUID = 1589302079L;

    public static final QTbOrder tbOrder = new QTbOrder("tbOrder");

    public final NumberPath<Integer> bnCd = createNumber("bnCd", Integer.class);

    public final StringPath delYn = createString("delYn");

    public final NumberPath<Integer> epCd = createNumber("epCd", Integer.class);

    public final StringPath evtNm = createString("evtNm");

    public final NumberPath<Integer> mbCd = createNumber("mbCd", Integer.class);

    public final DateTimePath<java.util.Date> orAcceptDt = createDateTime("orAcceptDt", java.util.Date.class);

    public final StringPath orAccident = createString("orAccident");

    public final NumberPath<Long> orCd = createNumber("orCd", Long.class);

    public final StringPath orChannel = createString("orChannel");

    public final NumberPath<Integer> orCharge = createNumber("orCharge", Integer.class);

    public final StringPath orChargeGubun = createString("orChargeGubun");

    public final StringPath orChargeMemo = createString("orChargeMemo");

    public final StringPath orChargeType = createString("orChargeType");

    public final StringPath orClaim = createString("orClaim");

    public final DateTimePath<java.util.Date> orClaimDt = createDateTime("orClaimDt", java.util.Date.class);

    public final NumberPath<Integer> orCnt = createNumber("orCnt", Integer.class);

    public final StringPath orDeliAddr = createString("orDeliAddr");

    public final NumberPath<Integer> orDeliEpCd = createNumber("orDeliEpCd", Integer.class);

    public final DateTimePath<java.util.Date> orDeliFuVisitDt = createDateTime("orDeliFuVisitDt", java.util.Date.class);

    public final NumberPath<Integer> orDelivery = createNumber("orDelivery", Integer.class);

    public final DateTimePath<java.util.Date> orDeliVisitDt = createDateTime("orDeliVisitDt", java.util.Date.class);

    public final NumberPath<Integer> orDiscount = createNumber("orDiscount", Integer.class);

    public final NumberPath<Integer> orExtMoney = createNumber("orExtMoney", Integer.class);

    public final NumberPath<Integer> orFeedbackEmp = createNumber("orFeedbackEmp", Integer.class);

    public final StringPath orFeedbackMemo = createString("orFeedbackMemo");

    public final NumberPath<Integer> orFeedbackSat = createNumber("orFeedbackSat", Integer.class);

    public final NumberPath<Integer> orFeedbackSvr = createNumber("orFeedbackSvr", Integer.class);

    public final StringPath orMemo = createString("orMemo");

    public final StringPath orNonMberYn = createString("orNonMberYn");

    public final DateTimePath<java.util.Date> orOrderDt = createDateTime("orOrderDt", java.util.Date.class);

    public final NumberPath<Integer> orPoint = createNumber("orPoint", Integer.class);

    public final NumberPath<Integer> orPrice = createNumber("orPrice", Integer.class);

    public final StringPath orRefund = createString("orRefund");

    public final StringPath orRegularPay = createString("orRegularPay");

    public final StringPath orReqAddr = createString("orReqAddr");

    public final DateTimePath<java.util.Date> orReqDt = createDateTime("orReqDt", java.util.Date.class);

    public final StringPath orReqMemo = createString("orReqMemo");

    public final StringPath orStatus = createString("orStatus");

    public final DateTimePath<java.util.Date> orVisitDt = createDateTime("orVisitDt", java.util.Date.class);

    public final StringPath orVisitPurpose = createString("orVisitPurpose");

    public final DateTimePath<java.util.Date> regiDt = createDateTime("regiDt", java.util.Date.class);

    public final StringPath user = createString("user");

    public QTbOrder(String variable) {
        super(TbOrder.class, forVariable(variable));
    }

    public QTbOrder(Path<? extends TbOrder> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTbOrder(PathMetadata<?> metadata) {
        super(TbOrder.class, metadata);
    }

}

