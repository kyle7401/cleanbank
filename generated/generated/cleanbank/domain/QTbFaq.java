package cleanbank.domain;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QTbFaq is a Querydsl query type for TbFaq
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QTbFaq extends EntityPathBase<TbFaq> {

    private static final long serialVersionUID = -1048633529L;

    public static final QTbFaq tbFaq = new QTbFaq("tbFaq");

    public final StringPath delYn = createString("delYn");

    public final StringPath evtNm = createString("evtNm");

    public final StringPath faMemo = createString("faMemo");

    public final StringPath faTitle = createString("faTitle");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final DateTimePath<java.util.Date> regiDt = createDateTime("regiDt", java.util.Date.class);

    public final StringPath user = createString("user");

    public QTbFaq(String variable) {
        super(TbFaq.class, forVariable(variable));
    }

    public QTbFaq(Path<? extends TbFaq> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTbFaq(PathMetadata<?> metadata) {
        super(TbFaq.class, metadata);
    }

}

