package cleanbank.domain;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QTbProduct is a Querydsl query type for TbProduct
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QTbProduct extends EntityPathBase<TbProduct> {

    private static final long serialVersionUID = -791420768L;

    public static final QTbProduct tbProduct = new QTbProduct("tbProduct");

    public final StringPath delYn = createString("delYn");

    public final StringPath evtNm = createString("evtNm");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath pdDesc = createString("pdDesc");

    public final StringPath pdLvl1 = createString("pdLvl1");

    public final StringPath pdLvl2 = createString("pdLvl2");

    public final StringPath pdLvl3 = createString("pdLvl3");

    public final StringPath pdNm = createString("pdNm");

    public final NumberPath<Integer> pdPer = createNumber("pdPer", Integer.class);

    public final NumberPath<Integer> pdPrice = createNumber("pdPrice", Integer.class);

    public final NumberPath<Integer> pdSort = createNumber("pdSort", Integer.class);

    public final DateTimePath<java.util.Date> regiDt = createDateTime("regiDt", java.util.Date.class);

    public final StringPath user = createString("user");

    public QTbProduct(String variable) {
        super(TbProduct.class, forVariable(variable));
    }

    public QTbProduct(Path<? extends TbProduct> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTbProduct(PathMetadata<?> metadata) {
        super(TbProduct.class, metadata);
    }

}

