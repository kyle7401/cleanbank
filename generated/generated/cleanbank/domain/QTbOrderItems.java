package cleanbank.domain;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QTbOrderItems is a Querydsl query type for TbOrderItems
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QTbOrderItems extends EntityPathBase<TbOrderItems> {

    private static final long serialVersionUID = 1137529793L;

    public static final QTbOrderItems tbOrderItems = new QTbOrderItems("tbOrderItems");

    public final StringPath delYn = createString("delYn");

    public final StringPath evtNm = createString("evtNm");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> itAddChg = createNumber("itAddChg", Integer.class);

    public final DateTimePath<java.util.Date> itBaekInDt = createDateTime("itBaekInDt", java.util.Date.class);

    public final StringPath itBeginTac = createString("itBeginTac");

    public final StringPath itBrand = createString("itBrand");

    public final NumberPath<Long> itCd = createNumber("itCd", Long.class);

    public final StringPath itClaim = createString("itClaim");

    public final DateTimePath<java.util.Date> itClaimDt = createDateTime("itClaimDt", java.util.Date.class);

    public final StringPath itClMemo = createString("itClMemo");

    public final NumberPath<Integer> itCnt = createNumber("itCnt", Integer.class);

    public final StringPath itColor = createString("itColor");

    public final StringPath itDeliAddr = createString("itDeliAddr");

    public final DateTimePath<java.util.Date> itDeliFuVisitDt = createDateTime("itDeliFuVisitDt", java.util.Date.class);

    public final DateTimePath<java.util.Date> itDeliVisitDt = createDateTime("itDeliVisitDt", java.util.Date.class);

    public final StringPath itFactoryMemo = createString("itFactoryMemo");

    public final DateTimePath<java.util.Date> itInDt = createDateTime("itInDt", java.util.Date.class);

    public final StringPath itMemo = createString("itMemo");

    public final DateTimePath<java.util.Date> itOutDt = createDateTime("itOutDt", java.util.Date.class);

    public final StringPath itPatten = createString("itPatten");

    public final NumberPath<Integer> itPrice = createNumber("itPrice", Integer.class);

    public final StringPath itRefund = createString("itRefund");

    public final DateTimePath<java.util.Date> itRegiDt = createDateTime("itRegiDt", java.util.Date.class);

    public final StringPath itRegiStaff = createString("itRegiStaff");

    public final StringPath itReqAddr = createString("itReqAddr");

    public final DateTimePath<java.util.Date> itReqDt = createDateTime("itReqDt", java.util.Date.class);

    public final StringPath itStatus = createString("itStatus");

    public final StringPath itTac = createString("itTac");

    public final DateTimePath<java.util.Date> itVisitDt = createDateTime("itVisitDt", java.util.Date.class);

    public final NumberPath<Long> orCd = createNumber("orCd", Long.class);

    public final StringPath pdLvl1 = createString("pdLvl1");

    public final StringPath pdLvl2 = createString("pdLvl2");

    public final StringPath pdLvl3 = createString("pdLvl3");

    public final DateTimePath<java.util.Date> regiDt = createDateTime("regiDt", java.util.Date.class);

    public final StringPath user = createString("user");

    public QTbOrderItems(String variable) {
        super(TbOrderItems.class, forVariable(variable));
    }

    public QTbOrderItems(Path<? extends TbOrderItems> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTbOrderItems(PathMetadata<?> metadata) {
        super(TbOrderItems.class, metadata);
    }

}

