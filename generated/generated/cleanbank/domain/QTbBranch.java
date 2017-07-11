package cleanbank.domain;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QTbBranch is a Querydsl query type for TbBranch
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QTbBranch extends EntityPathBase<TbBranch> {

    private static final long serialVersionUID = 1651464145L;

    public static final QTbBranch tbBranch = new QTbBranch("tbBranch");

    public final StringPath bnBarCd = createString("bnBarCd");

    public final NumberPath<Integer> bnCd = createNumber("bnCd", Integer.class);

    public final StringPath bnCeo = createString("bnCeo");

    public final TimePath<java.util.Date> bnCloseTm = createTime("bnCloseTm", java.util.Date.class);

    public final NumberPath<Integer> bnDeliTm = createNumber("bnDeliTm", Integer.class);

    public final StringPath bnFax = createString("bnFax");

    public final StringPath bnFri = createString("bnFri");

    public final NumberPath<Integer> bnFriTo = createNumber("bnFriTo", Integer.class);

    public final StringPath bnMon = createString("bnMon");

    public final NumberPath<Integer> bnMonTo = createNumber("bnMonTo", Integer.class);

    public final StringPath bnNm = createString("bnNm");

    public final TimePath<java.util.Date> bnOpenTm = createTime("bnOpenTm", java.util.Date.class);

    public final NumberPath<Integer> bnPer = createNumber("bnPer", Integer.class);

    public final StringPath bnSat = createString("bnSat");

    public final NumberPath<Integer> bnSatTo = createNumber("bnSatTo", Integer.class);

    public final StringPath bnSun = createString("bnSun");

    public final NumberPath<Integer> bnSunTo = createNumber("bnSunTo", Integer.class);

    public final StringPath bnTel = createString("bnTel");

    public final StringPath bnThu = createString("bnThu");

    public final NumberPath<Integer> bnThuTo = createNumber("bnThuTo", Integer.class);

    public final NumberPath<Integer> bnTransTm = createNumber("bnTransTm", Integer.class);

    public final StringPath bnTue = createString("bnTue");

    public final NumberPath<Integer> bnTueTo = createNumber("bnTueTo", Integer.class);

    public final StringPath bnWed = createString("bnWed");

    public final NumberPath<Integer> bnWedTo = createNumber("bnWedTo", Integer.class);

    public final StringPath delYn = createString("delYn");

    public final StringPath evtNm = createString("evtNm");

    public final NumberPath<Integer> ptCd = createNumber("ptCd", Integer.class);

    public final DateTimePath<java.util.Date> regiDt = createDateTime("regiDt", java.util.Date.class);

    public final StringPath user = createString("user");

    public QTbBranch(String variable) {
        super(TbBranch.class, forVariable(variable));
    }

    public QTbBranch(Path<? extends TbBranch> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTbBranch(PathMetadata<?> metadata) {
        super(TbBranch.class, metadata);
    }

}

