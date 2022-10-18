package me.cometkaizo.wavetech.lexer.tokens.types;

import java.util.List;

public abstract class PropertyDeclaration {

    public final List<String> declaredObjects;


    protected PropertyDeclaration(String[] declaredObjects) {
        this.declaredObjects = List.of(declaredObjects);
    }
}
