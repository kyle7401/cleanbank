package cleanbank.domain;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QBillResult is a Querydsl query type for BillResult
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QBillResult extends EntityPathBase<BillResult> {

    private static final long serialVersionUID = 405386469L;

    public static final QBillResult billResult = new QBillResult("billResult");

    public final StringPath cardauthcode = createString("cardauthcode");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath mbCd = createString("mbCd");

    public final StringPath moid = createString("moid");

    public final StringPath pgauthdate = createString("pgauthdate");

    public final StringPath pgauthtime = createString("pgauthtime");

    public final StringPath price = createString("price");

    public final DateTimePath<java.util.Date> regDt = createDateTime("regDt", java.util.Date.class);

    public final StringPath resultmsg = createString("resultmsg");

    public final StringPath tid = createString("tid");

    public QBillResult(String variable) {
        super(BillResult.class, forVariable(variable));
    }

    public QBillResult(Path<? extends BillResult> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBillResult(PathMetadata<?> metadata) {
        super(BillResult.class, metadata);
    }

}

