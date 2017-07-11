package cleanbank.domain;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QTbOrder_test is a Querydsl query type for TbOrder_test
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QTbOrder_test extends EntityPathBase<TbOrder_test> {

    private static final long serialVersionUID = 1157847442L;

    public static final QTbOrder_test tbOrder_test = new QTbOrder_test("tbOrder_test");

    public final NumberPath<Integer> bnCd = createNumber("bnCd", Integer.class);

    public final StringPath delYn = createString("delYn");

    public final NumberPath<Integer> epCd = createNumber("epCd", Integer.class);

    public final StringPath evtNm = createString("evtNm");

    public final NumberPath<Integer> mbCd = createNumber("mbCd", Integer.class);

    public final StringPath orAccident = createString("orAccident");

    public final NumberPath<Long> orCd = createNumber("orCd", Long.class);

    public final StringPath orChannel = createString("orChannel");

    public final NumberPath<Integer> orCharge = createNumber("orCharge", Integer.class);

    public final StringPath orChargeMemo = createString("orChargeMemo");

    public final StringPath orChargeType = createString("orChargeType");

    public final StringPath orClaim = createString("orClaim");

    public final DateTimePath<java.util.Date> orClaimDt = createDateTime("orClaimDt", java.util.Date.class);

    public final StringPath orDeliAddr = createString("orDeliAddr");

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

    public final NumberPath<Integer> orPrice = createNumber("orPrice", Integer.class);

    public final StringPath orRefund = createString("orRefund");

    public final StringPath orReqAddr = createString("orReqAddr");

    public final DateTimePath<java.util.Date> orReqDt = createDateTime("orReqDt", java.util.Date.class);

    public final StringPath orReqMemo = createString("orReqMemo");

    public final DateTimePath<java.util.Date> orVisitDt = createDateTime("orVisitDt", java.util.Date.class);

    public final DateTimePath<java.util.Date> regiDt = createDateTime("regiDt", java.util.Date.class);

    public final StringPath user = createString("user");

    public QTbOrder_test(String variable) {
        super(TbOrder_test.class, forVariable(variable));
    }

    public QTbOrder_test(Path<? extends TbOrder_test> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTbOrder_test(PathMetadata<?> metadata) {
        super(TbOrder_test.class, metadata);
    }

}

