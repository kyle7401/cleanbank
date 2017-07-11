package cleanbank.domain;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QTbBranchLocs is a Querydsl query type for TbBranchLocs
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QTbBranchLocs extends EntityPathBase<TbBranchLocs> {

    private static final long serialVersionUID = 1754349732L;

    public static final QTbBranchLocs tbBranchLocs = new QTbBranchLocs("tbBranchLocs");

    public final StringPath blCd = createString("blCd");

    public final StringPath blMemo = createString("blMemo");

    public final StringPath blNm = createString("blNm");

    public final NumberPath<Integer> bnCd = createNumber("bnCd", Integer.class);

    public final StringPath delYn = createString("delYn");

    public final StringPath evtNm = createString("evtNm");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final DateTimePath<java.util.Date> regiDt = createDateTime("regiDt", java.util.Date.class);

    public final StringPath user = createString("user");

    public QTbBranchLocs(String variable) {
        super(TbBranchLocs.class, forVariable(variable));
    }

    public QTbBranchLocs(Path<? extends TbBranchLocs> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTbBranchLocs(PathMetadata<?> metadata) {
        super(TbBranchLocs.class, metadata);
    }

}

