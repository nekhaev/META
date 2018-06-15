package ru.sbrf.bh.bfs.ufs.type.poet.audit;

import com.squareup.javapoet.TypeSpec;
import ru.sbrf.bh.bfs.generator.type.TypePoet;

import javax.lang.model.element.Modifier;

/**
 * Created by sbt-barsukov-sv on 15.06.2018.
 */
public class BfsAuditEventPoet extends TypePoet<String>{

    @Override
    protected TypeSpec createType(String param) {
        return TypeSpec.classBuilder(param)
                .addModifiers(Modifier.PUBLIC)
//TODO доделать
                .build();
    }

}
