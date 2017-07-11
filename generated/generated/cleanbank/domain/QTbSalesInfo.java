package cleanbank.domain;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QTbSalesInfo is a Querydsl query type for TbSalesInfo
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QTbSalesInfo extends EntityPathBase<TbSalesInfo> {

    private static final long serialVersionUID = 621913931L;

    public static final QTbSalesInfo tbSalesInfo = new QTbSalesInfo("tbSalesInfo");

    public final NumberPath<Integer> bnCd = createNumber("bnCd", Integer.class);

    public final StringPath delYn = createString("delYn");

    public final StringPath evtNm = createString("evtNm");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final DateTimePath<java.util.Date> regiDt = createDateTime("regiDt", java.util.Date.class);

    public final DateTimePath<java.util.Date> saDate = createDateTime("saDate", java.util.Date.class);

    public final StringPath saMemo = createString("saMemo");

    public final NumberPath<Integer> saNum = createNumber("saNum", Integer.class);

    public final StringPath user = createString("user");

    public QTbSalesInfo(String variable) {
        super(TbSalesInfo.class, forVariable(variable));
    }

    public QTbSalesInfo(Path<? extends TbSalesInfo> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTbSalesInfo(PathMetadata<?> metadata) {
        super(TbSalesInfo.class, metadata);
    }

}

