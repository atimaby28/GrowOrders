package org.example.groworders.config.push.model.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPushSub is a Querydsl query type for PushSub
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPushSub extends EntityPathBase<PushSub> {

    private static final long serialVersionUID = 368870288L;

    public static final QPushSub pushSub = new QPushSub("pushSub");

    public final StringPath auth = createString("auth");

    public final StringPath endpoint = createString("endpoint");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath p256dh = createString("p256dh");

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QPushSub(String variable) {
        super(PushSub.class, forVariable(variable));
    }

    public QPushSub(Path<? extends PushSub> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPushSub(PathMetadata metadata) {
        super(PushSub.class, metadata);
    }

}

