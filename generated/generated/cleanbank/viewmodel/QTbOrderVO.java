package cleanbank.viewmodel;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QTbOrderVO is a Querydsl query type for TbOrderVO
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QTbOrderVO extends EntityPathBase<TbOrderVO> {

    private static final long serialVersionUID = -1330733338L;

    public static final QTbOrderVO tbOrderVO = new QTbOrderVO("tbOrderVO");

    public final NumberPath<Integer> bnCd = createNumber("bnCd", Integer.class);

    public final StringPath delYn = createString("delYn");

    public final NumberPath<Integer> epCd = createNumber("epCd", Integer.class);

    public final StringPath evtNm = createString("evtNm");

    public final NumberPath<Integer> mbCd = createNumber("mbCd", Integer.class);

    public final StringPath orAccident = createString("orAccident");

    public final NumberPath<Long> orCd = createNumber("orCd", Long.class);

    public final StringPath orChannel = createString("orChannel");

    public final NumberPath<Integer> orCharge = createNumber("orCharge", Integer.class);

    public final StringPath orChargeType = createString("orChargeType");

    public final StringPath orClaim = createString("orClaim");

    public final DateTimePath<java.util.Date> orClaimDt = createDateTime("orClaimDt", java.util.Date.class);

    public final NumberPath<Integer> orDelivery = createNumber("orDelivery", Integer.class);

    public final NumberPath<Integer> orDiscount = createNumber("orDiscount", Integer.class);

    public final NumberPath<Integer> orFeedbackEmp = createNumber("orFeedbackEmp", Integer.class);

    public final StringPath orFeedbackMemo = createString("orFeedbackMemo");

    public final NumberPath<Integer> orFeedbackSat = createNumber("orFeedbackSat", Integer.class);

    public final NumberPath<Integer> orFeedbackSvr = createNumber("orFeedbackSvr", Integer.class);

    public final StringPath orMemo = createString("orMemo");

    public final NumberPath<Integer> orPrice = createNumber("orPrice", Integer.class);

    public final StringPath orRefund = createString("orRefund");

    public final DateTimePath<java.util.Date> regiDt = createDateTime("regiDt", java.util.Date.class);

    public final StringPath user = createString("user");

    public QTbOrderVO(String variable) {
        super(TbOrderVO.class, forVariable(variable));
    }

    public QTbOrderVO(Path<? extends TbOrderVO> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTbOrderVO(PathMetadata<?> metadata) {
        super(TbOrderVO.class, metadata);
    }

}

