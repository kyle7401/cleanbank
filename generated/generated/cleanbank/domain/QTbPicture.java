package cleanbank.domain;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QTbPicture is a Querydsl query type for TbPicture
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QTbPicture extends EntityPathBase<TbPicture> {

    private static final long serialVersionUID = -1059688273L;

    public static final QTbPicture tbPicture = new QTbPicture("tbPicture");

    public final StringPath contentType = createString("contentType");

    public final DateTimePath<java.util.Date> dateCreated = createDateTime("dateCreated", java.util.Date.class);

    public final StringPath delYn = createString("delYn");

    public final StringPath evtNm = createString("evtNm");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<Long> itCd = createNumber("itCd", Long.class);

    public final DateTimePath<java.util.Date> lastUpdated = createDateTime("lastUpdated", java.util.Date.class);

    public final StringPath name = createString("name");

    public final StringPath newFilename = createString("newFilename");

    public final NumberPath<Long> orCd = createNumber("orCd", Long.class);

    public final NumberPath<Long> ptCd = createNumber("ptCd", Long.class);

    public final StringPath ptImgFile = createString("ptImgFile");

    public final DateTimePath<java.util.Date> regiDt = createDateTime("regiDt", java.util.Date.class);

    public final NumberPath<Long> size = createNumber("size", Long.class);

    public final StringPath thumbnailFilename = createString("thumbnailFilename");

    public final NumberPath<Long> thumbnailSize = createNumber("thumbnailSize", Long.class);

    public final StringPath user = createString("user");

    public QTbPicture(String variable) {
        super(TbPicture.class, forVariable(variable));
    }

    public QTbPicture(Path<? extends TbPicture> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTbPicture(PathMetadata<?> metadata) {
        super(TbPicture.class, metadata);
    }

}

