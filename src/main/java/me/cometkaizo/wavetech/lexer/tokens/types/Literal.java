package me.cometkaizo.wavetech.lexer.tokens.types;

import me.cometkaizo.wavetech.lexer.tokens.TokenType;
import me.cometkaizo.wavetech.parser.BuiltInClasses;
import me.cometkaizo.wavetech.parser.structures.ClassStructure;

public enum Literal implements TokenType {

    INT(BuiltInClasses.INT),
    LONG(BuiltInClasses.LONG),
    SHORT(BuiltInClasses.SHORT),
    BYTE(BuiltInClasses.BYTE),
    FLOAT(BuiltInClasses.FLOAT),
    DOUBLE(BuiltInClasses.DOUBLE),
    BOOLEAN(BuiltInClasses.BOOLEAN),
    CHAR(BuiltInClasses.CHAR),
    STRING(BuiltInClasses.STRING),
    NULL(BuiltInClasses.OBJECT);

    public final ClassStructure type;

    Literal(ClassStructure type) {
        this.type = type;
    }

    public ClassStructure getClassType() {
        return type;
    }

    @Override
    public String symbol() {
        return null;
    }
}
