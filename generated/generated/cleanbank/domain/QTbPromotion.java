package cleanbank.domain;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QTbPromotion is a Querydsl query type for TbPromotion
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QTbPromotion extends EntityPathBase<TbPromotion> {

    private static final long serialVersionUID = -93525996L;

    public static final QTbPromotion tbPromotion = new QTbPromotion("tbPromotion");

    public final StringPath delYn = createString("delYn");

    public final StringPath evtNm = createString("evtNm");

    public final NumberPath<Long> poCd = createNumber("poCd", Long.class);

    public final StringPath poCoup = createString("poCoup");

    public final NumberPath<Integer> poCoupPubCnt = createNumber("poCoupPubCnt", Integer.class);

    public final StringPath poDesc = createString("poDesc");

    public final NumberPath<Integer> poDiscountAmt = createNumber("poDiscountAmt", Integer.class);

    public final StringPath poDubYn = createString("poDubYn");

    public final DateTimePath<java.util.Date> poFinishDt = createDateTime("poFinishDt", java.util.Date.class);

    public final TimePath<java.util.Date> poFinishTm = createTime("poFinishTm", java.util.Date.class);

    public final StringPath poFri = createString("poFri");

    public final NumberPath<Integer> poGoldPer = createNumber("poGoldPer", Integer.class);

    public final NumberPath<Integer> poGoldPrice = createNumber("poGoldPrice", Integer.class);

    public final NumberPath<Integer> poGreenPer = createNumber("poGreenPer", Integer.class);

    public final NumberPath<Integer> poGreenPrice = createNumber("poGreenPrice", Integer.class);

    public final StringPath poImg = createString("poImg");

    public final NumberPath<Integer> poLimitAmount = createNumber("poLimitAmount", Integer.class);

    public final StringPath poMon = createString("poMon");

    public final StringPath poNm = createString("poNm");

    public final StringPath poPushMsg = createString("poPushMsg");

    public final StringPath poSat = createString("poSat");

    public final NumberPath<Integer> poSilverPer = createNumber("poSilverPer", Integer.class);

    public final NumberPath<Integer> poSilverPrice = createNumber("poSilverPrice", Integer.class);

    public final DateTimePath<java.util.Date> poStartDt = createDateTime("poStartDt", java.util.Date.class);

    public final TimePath<java.util.Date> poStartTm = createTime("poStartTm", java.util.Date.class);

    public final StringPath poSun = createString("poSun");

    public final StringPath poThu = createString("poThu");

    public final StringPath poTue = createString("poTue");

    public final StringPath poType = createString("poType");

    public final StringPath poWed = createString("poWed");

    public final DateTimePath<java.util.Date> regiDt = createDateTime("regiDt", java.util.Date.class);

    public final StringPath user = createString("user");

    public QTbPromotion(String variable) {
        super(TbPromotion.class, forVariable(variable));
    }

    public QTbPromotion(Path<? extends TbPromotion> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTbPromotion(PathMetadata<?> metadata) {
        super(TbPromotion.class, metadata);
    }

}

