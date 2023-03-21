package me.cometkaizo.wavetech.analyzer;

import me.cometkaizo.util.CollectionUtils;
import me.cometkaizo.wavetech.analyzer.diagnostics.Diagnostic;
import me.cometkaizo.wavetech.parser.structures.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ClassResourcePool implements ResourcePool {
    @NotNull
    public List<ClassStructure> classes;
    @NotNull
    public List<FieldDeclaration> fields;
    @NotNull
    public List<MethodStructure> methods;

    public ClassResourcePool(
            @NotNull List<ClassStructure> classes,
            @NotNull List<FieldDeclaration> fields,
            @NotNull List<MethodStructure> methods
    ) {
        this.classes = classes;
        this.fields = fields;
        this.methods = methods;
    }

    public ClassResourcePool(ClassResourcePool other) {
        this(new ArrayList<>(other.classes), new ArrayList<>(other.fields),
                new ArrayList<>(other.methods));
    }

    @Override
    public boolean hasResourceOrAnalyze(ResourceAccessor<?> resourceAccessor, List<Diagnostic> problems) {
        if (resourceAccessor instanceof VariableAccessor variableAccessor) {
            return hasField(variableAccessor.getSignature(), problems);
        } else if (resourceAccessor instanceof FieldAccessor fieldAccessor) {
            return hasField(fieldAccessor.getSignature(), problems);
        } else if (resourceAccessor instanceof MethodAccessor methodAccessor) {
            return hasMethod(methodAccessor.getSignature(), problems);
        } else if (resourceAccessor instanceof ClassAccessor classAccessor) {
            return hasClass(classAccessor.getSignature(), problems);
        }
        return false;
    }

    @Override
    public @Nullable Object getResourceOrAnalyze(ResourceAccessor<?> resourceAccessor, List<Diagnostic> problems) {
        if (resourceAccessor instanceof VariableAccessor variableAccessor) {
            return getField(variableAccessor.getSignature(), problems);
        } else if (resourceAccessor instanceof FieldAccessor fieldAccessor) {
            return getField(fieldAccessor.getSignature(), problems);
        } else if (resourceAccessor instanceof MethodAccessor methodAccessor) {
            return getMethod(methodAccessor.getSignature(), problems);
        } else if (resourceAccessor instanceof ClassAccessor classAccessor) {
            return getClass(classAccessor.getSignature(), problems);
        }
        return null;
    }

    @Override
    public ClassResourcePool copy() {
        return new ClassResourcePool(this);
    }

    public boolean hasField(Identifier identifier, List<Diagnostic> problems) {
        return CollectionUtils.anyMatch(fields, f -> {
            f.getAnalyzer().analyze(problems, this);
            return f.name != null && f.name.equals(identifier);
        });
    }

    public FieldDeclaration getField(Identifier identifier, List<Diagnostic> problems) {
        return CollectionUtils.getFirst(fields, f -> {
            f.getAnalyzer().analyze(problems, this);
            return f.name != null && f.name.equals(identifier);
        }).orElse(null);
    }

    public boolean hasMethod(MethodStructure.Signature signature, List<Diagnostic> problems) {
        return CollectionUtils.anyMatch(methods, m -> {
            m.getAnalyzer().analyze(problems, this);
            return m.getSignature() != null && m.getSignature().equals(signature);
        });
    }

    public MethodStructure getMethod(MethodStructure.Signature signature, List<Diagnostic> problems) {
        return CollectionUtils.getFirst(methods, m -> {
            m.getAnalyzer().analyze(problems, this);
            return m.getSignature() != null && m.getSignature().equals(signature);
        }).orElse(null);
    }

    public boolean hasClass(Identifier identifier, List<Diagnostic> problems) {
        return CollectionUtils.anyMatch(classes, c -> {
            c.getAnalyzer().analyze(problems);
            return c.name != null && c.name.equals(identifier);
        });
    }

    public ClassStructure getClass(Identifier identifier, List<Diagnostic> problems) {
        return CollectionUtils.getFirst(classes, c -> {
            c.getAnalyzer().analyze(problems);
            return c.name != null && c.name.equals(identifier);
        }).orElse(null);
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ClassResourcePool) obj;
        return Objects.equals(this.fields, that.fields) &&
                Objects.equals(this.methods, that.methods);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fields, methods);
    }

    @Override
    public String toString() {
        return "ClassResourcePool[" +
                "fields=" + fields + ", " +
                "methods=" + methods + ']';
    }


}
