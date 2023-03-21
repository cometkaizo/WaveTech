package me.cometkaizo.wavetech.analyzer.structures;

import me.cometkaizo.util.CollectionUtils;
import me.cometkaizo.wavetech.analyzer.ClassResourcePool;
import me.cometkaizo.wavetech.analyzer.diagnostics.Diagnostic;
import me.cometkaizo.wavetech.analyzer.diagnostics.DuplicateFieldDiagnostic;
import me.cometkaizo.wavetech.parser.BuiltInClasses;
import me.cometkaizo.wavetech.parser.structures.ClassStructure;
import me.cometkaizo.wavetech.parser.structures.FieldDeclaration;
import me.cometkaizo.wavetech.parser.structures.Identifier;

import java.util.List;

public class ClassAnalyzer implements ResourceStructureAnalyzer {
    boolean analyzed = false;
    private final ClassStructure classStructure;

    public ClassAnalyzer(ClassStructure classStructure) {
        this.classStructure = classStructure;
    }


    private void throwIfInvalidState() {
        if (classStructure.visibility == null) throw new IllegalStateException("Visibility is null");
        if (classStructure.name == null) throw new IllegalStateException("Name is null");
    }

    @Override
    public void analyze(List<Diagnostic> problems) {
        if (analyzed) return;
        throwIfInvalidState();

        classStructure.name.getAnalyzer().analyze(problems);

        checkDuplicateFields(problems);

        ClassResourcePool resources = new ClassResourcePool(
                BuiltInClasses.BUILT_IN_CLASSES,
                classStructure.fields,
                classStructure.methods
        );


        for (var field : classStructure.declaredFields) {
            field.getAnalyzer().analyze(problems, resources);
        }
        for (var method : classStructure.declaredMethods) {
            method.getAnalyzer().analyze(problems, resources);
        }

        analyzed = true;
    }

    public boolean hasField(Identifier identifier) {
        for (var field : classStructure.declaredFields) {
            if (field.name.equals(identifier)) return true;
        }
        return false;
    }

    public FieldDeclaration getField(Identifier identifier) {
        return CollectionUtils.getFirst(classStructure.declaredFields, field -> field.name.equals(identifier)).orElseThrow();
    }

    private void checkDuplicateFields(List<Diagnostic> problems) {
        var declaredFields = classStructure.declaredFields;
        var allFields = classStructure.fields;

        for (var declaredField : declaredFields) {
            for (var otherField : allFields) {
                if (declaredField == otherField) continue;
                if (declaredField.name.equals(otherField.name))
                    problems.add(new DuplicateFieldDiagnostic(declaredField, otherField, classStructure));
            }
        }
    }

}
