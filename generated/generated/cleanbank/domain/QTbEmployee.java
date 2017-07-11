package cleanbank.domain;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QTbEmployee is a Querydsl query type for TbEmployee
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QTbEmployee extends EntityPathBase<TbEmployee> {

    private static final long serialVersionUID = -861976291L;

    public static final QTbEmployee tbEmployee = new QTbEmployee("tbEmployee");

    public final NumberPath<Integer> bnCd = createNumber("bnCd", Integer.class);

    public final StringPath delYn = createString("delYn");

    public final NumberPath<Integer> epCd = createNumber("epCd", Integer.class);

    public final StringPath epDeviceToken = createString("epDeviceToken");

    public final StringPath epDriveYn = createString("epDriveYn");

    public final StringPath epEmail = createString("epEmail");

    public final StringPath epImg = createString("epImg");

    public final StringPath epIntro = createString("epIntro");

    public final DateTimePath<java.util.Date> epJoinDt = createDateTime("epJoinDt", java.util.Date.class);

    public final StringPath epLevel = createString("epLevel");

    public final StringPath epLoc = createString("epLoc");

    public final StringPath epNm = createString("epNm");

    public final StringPath epPart = createString("epPart");

    public final StringPath epPass = createString("epPass");

    public final StringPath epSex = createString("epSex");

    public final StringPath epTel = createString("epTel");

    public final StringPath evtNm = createString("evtNm");

    public final DateTimePath<java.util.Date> regiDt = createDateTime("regiDt", java.util.Date.class);

    public final StringPath user = createString("user");

    public QTbEmployee(String variable) {
        super(TbEmployee.class, forVariable(variable));
    }

    public QTbEmployee(Path<? extends TbEmployee> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTbEmployee(PathMetadata<?> metadata) {
        super(TbEmployee.class, metadata);
    }

}

