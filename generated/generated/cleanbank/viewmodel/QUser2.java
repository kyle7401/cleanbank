package cleanbank.viewmodel;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QUser2 is a Querydsl query type for User2
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QUser2 extends EntityPathBase<User2> {

    private static final long serialVersionUID = -10321164L;

    public static final QUser2 user2 = new QUser2("user2");

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath password = createString("password");

    public final StringPath username = createString("username");

    public final NumberPath<Long> version = createNumber("version", Long.class);

    public QUser2(String variable) {
        super(User2.class, forVariable(variable));
    }

    public QUser2(Path<? extends User2> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser2(PathMetadata<?> metadata) {
        super(User2.class, metadata);
    }

}

