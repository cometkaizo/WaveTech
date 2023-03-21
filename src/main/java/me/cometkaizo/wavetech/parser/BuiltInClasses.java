package me.cometkaizo.wavetech.parser;

import me.cometkaizo.wavetech.lexer.tokens.types.ModifierKeyword;
import me.cometkaizo.wavetech.lexer.tokens.types.VisibilityKeyword;
import me.cometkaizo.wavetech.parser.structures.ClassStructure;
import me.cometkaizo.wavetech.parser.structures.Identifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class BuiltInClasses {


    // built-in types
    public static final ClassStructure OBJECT = new ClassStructure();
    public static final ClassStructure INT = new ClassStructure();
    public static final ClassStructure LONG = new ClassStructure();
    public static final ClassStructure SHORT = new ClassStructure();
    public static final ClassStructure BYTE = new ClassStructure();
    public static final ClassStructure FLOAT = new ClassStructure();
    public static final ClassStructure DOUBLE = new ClassStructure();
    public static final ClassStructure BOOLEAN = new ClassStructure();
    public static final ClassStructure CHAR = new ClassStructure();
    public static final ClassStructure STRING = new ClassStructure();
    public static final ClassStructure VOID = new ClassStructure();
    static {
        OBJECT.name = new Identifier("Object");
        INT.name = new Identifier("Integer");
        LONG.name = new Identifier("Long");
        SHORT.name = new Identifier("Short");
        BYTE.name = new Identifier("Byte");
        FLOAT.name = new Identifier("Float");
        DOUBLE.name = new Identifier("Double");
        BOOLEAN.name = new Identifier("Boolean");
        CHAR.name = new Identifier("Char");
        STRING.name = new Identifier("String");
        VOID.name = new Identifier("Void");

        OBJECT.visibility = VisibilityKeyword.PUBLIC;
        INT.visibility = VisibilityKeyword.PUBLIC;
        LONG.visibility = VisibilityKeyword.PUBLIC;
        SHORT.visibility = VisibilityKeyword.PUBLIC;
        BYTE.visibility = VisibilityKeyword.PUBLIC;
        FLOAT.visibility = VisibilityKeyword.PUBLIC;
        DOUBLE.visibility = VisibilityKeyword.PUBLIC;
        BOOLEAN.visibility = VisibilityKeyword.PUBLIC;
        CHAR.visibility = VisibilityKeyword.PUBLIC;
        STRING.visibility = VisibilityKeyword.PUBLIC;
        VOID.visibility = VisibilityKeyword.PUBLIC;
        VOID.modifiers.add(ModifierKeyword.FINAL);
    }


    public static final List<ClassStructure> BUILT_IN_CLASSES = new ArrayList<>(Arrays.asList(
            OBJECT,
            INT,
            LONG,
            SHORT,
            BYTE,
            FLOAT,
            DOUBLE,
            BOOLEAN,
            CHAR,
            STRING,
            VOID
    ));



    private BuiltInClasses() {
        throw new AssertionError("No BuiltInClasses instances for you!");
    }
}
