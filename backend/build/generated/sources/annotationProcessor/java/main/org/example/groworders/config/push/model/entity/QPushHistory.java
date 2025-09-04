package org.example.groworders.config.push.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPushHistory is a Querydsl query type for PushHistory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPushHistory extends EntityPathBase<PushHistory> {

    private static final long serialVersionUID = -1665695260L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPushHistory pushHistory = new QPushHistory("pushHistory");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath message = createString("message");

    public final BooleanPath read = createBoolean("read");

    public final StringPath title = createString("title");

    public final EnumPath<PushType> type = createEnum("type", PushType.class);

    public final org.example.groworders.domain.users.model.entity.QUser user;

    public QPushHistory(String variable) {
        this(PushHistory.class, forVariable(variable), INITS);
    }

    public QPushHistory(Path<? extends PushHistory> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPushHistory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPushHistory(PathMetadata metadata, PathInits inits) {
        this(PushHistory.class, metadata, inits);
    }

    public QPushHistory(Class<? extends PushHistory> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new org.example.groworders.domain.users.model.entity.QUser(forProperty("user")) : null;
    }

}

