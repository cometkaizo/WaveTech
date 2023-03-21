package me.cometkaizo.wavetech.analyzer.structures;

import me.cometkaizo.wavetech.analyzer.ClassResourcePool;
import me.cometkaizo.wavetech.analyzer.diagnostics.*;
import me.cometkaizo.wavetech.parser.structures.ClassStructure;
import me.cometkaizo.wavetech.parser.structures.FieldDeclaration;
import me.cometkaizo.wavetech.parser.structures.Identifier;
import me.cometkaizo.wavetech.parser.structures.VariableAccessor;

import java.util.List;

public class FieldDeclarationAnalyzer implements ClassContextAnalyzer {
    boolean analyzed = false;
    private final FieldDeclaration fieldDeclaration;

    public FieldDeclarationAnalyzer(FieldDeclaration fieldDeclaration) {
        this.fieldDeclaration = fieldDeclaration;
    }


    @Override
    public void analyze(List<Diagnostic> problems, ClassResourcePool resources) {
        if (analyzed) return;
        analyzed = true;
        throwIfInvalidState();

        fieldDeclaration.typeAccessor.getAnalyzer().analyze(problems, resources);
        fieldDeclaration.name.getAnalyzer().analyze(problems);

        var expectedType = (ClassStructure) resources.getResourceOrAnalyze(fieldDeclaration.typeAccessor, problems);
        if (fieldDeclaration.initializer != null && expectedType != null) {
            fieldDeclaration.initializer.getAnalyzer().analyze(problems, resources);
            ClassStructure actualType = fieldDeclaration.initializer.value.type;

            if (actualType != null && !expectedType.equals(actualType))
                problems.add(new IncompatibleTypesDiagnostic(actualType, expectedType, fieldDeclaration.initializer.value));
        }

        checkUsedVariables(problems, resources);

    }

    private void throwIfInvalidState() {
        if (fieldDeclaration.name == null) throw new IllegalStateException("Name is null");
        if (fieldDeclaration.typeAccessor == null) throw new IllegalStateException("Type is null");
        if (fieldDeclaration.visibility == null) throw new IllegalStateException("Visibility is null");
    }


    private void checkUsedVariables(List<Diagnostic> problems, ClassResourcePool resources) {
        if (fieldDeclaration.initializer == null) return;

        for (var resourceAccessor : fieldDeclaration.initializer.value.getUsedResources()) {
            if (!resources.hasResourceOrAnalyze(resourceAccessor, problems)) continue;

            if (resourceAccessor instanceof VariableAccessor variableAccessor) {
                checkSelfReference(problems, variableAccessor.variableName);
                checkForwardReference(problems, resources, variableAccessor.variableName);
                checkReferenceFromStaticContext(problems, resources, variableAccessor.variableName);
            }
        }
    }

    private void checkSelfReference(List<Diagnostic> problems, Identifier variableName) {
        if (variableName.equals(fieldDeclaration.name))
            problems.add(new IllegalSelfReferenceDiagnostic(variableName));
    }

    private void checkReferenceFromStaticContext(List<Diagnostic> problems, ClassResourcePool resources, Identifier variableName) {
        var referencedField = resources.getField(variableName, problems);
        if (fieldDeclaration.isStatic() && !referencedField.isStatic())
            problems.add(new ReferenceFromStaticContextDiagnostic(variableName, fieldDeclaration));
    }

    private void checkForwardReference(List<Diagnostic> problems, ClassResourcePool resources, Identifier variableName) {
        var referencedField = resources.getField(variableName, problems);
        if (fieldDeclaration != referencedField &&
                !isFieldDeclaredBefore(resources, referencedField, fieldDeclaration)) {
            problems.add(new IllegalForwardReferenceDiagnostic(variableName, fieldDeclaration));
        }
    }

    private boolean isFieldDeclaredBefore(ClassResourcePool resources, FieldDeclaration field1, FieldDeclaration field2) {
        int field1Index = resources.fields.indexOf(field1);
        int field2Index = resources.fields.indexOf(field2);
        return field1Index < field2Index;
    }

}
