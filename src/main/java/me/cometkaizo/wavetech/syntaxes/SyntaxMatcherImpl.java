package me.cometkaizo.wavetech.syntaxes;

import me.cometkaizo.util.PeekingIterator;
import me.cometkaizo.wavetech.lexer.tokens.Position;
import me.cometkaizo.wavetech.lexer.tokens.Token;
import me.cometkaizo.wavetech.parser.diagnostics.NotASyntaxDiagnostic;
import me.cometkaizo.wavetech.parser.diagnostics.ParseDiagnostic;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class SyntaxMatcherImpl implements SyntaxMatcher {

    private final PeekingIterator<Token> tokens;
    private final List<ParseDiagnostic> problems = new ArrayList<>(1);
    private final Map<List<Token>, List<SyntaxNode>> problemNodes = new HashMap<>(1);
    private int lastDepth = 0;

    public SyntaxMatcherImpl(PeekingIterator<Token> tokens) {
        this.tokens = tokens;
    }

    // find path of nodes that matches. If one exists, advance tokens & return syntax object
    @Override
    @Nullable
    public <T extends SyntaxStructure>
    SyntaxStructure tryMatch(Syntax<T> syntax, SyntaxStructure parent, List<ParseDiagnostic> problems) {
        var root = syntax.getRootNode();
        var startPosition = getStartPosition();

        SyntaxStructure structure = getStructureOrThrow(syntax, parent);

        // if matched return structure
        if (tryMatchAndStore(root, structure)) {
            structure.setStartPosition(startPosition);
            structure.setEndPosition(getEndPosition());
            return structure;
        }

        // did not match - add problems and return null
        if (problems != null) problems.addAll(this.problems);
        return null;
    }

    @NotNull
    private Position getEndPosition() {
        return tokens.hasPrevious() ? tokens.peek(-1).end : tokens.current().end;
    }

    @NotNull
    private static <T extends SyntaxStructure> SyntaxStructure getStructureOrThrow(Syntax<T> syntax, SyntaxStructure parent) {
        SyntaxStructure structure = syntax.getStructure(parent);
        if (structure == null) structure = parent;
        if (structure == null) throw new IllegalArgumentException("Both the parent and structure of '" + syntax.getClass() + "' is null");
        return structure;
    }

    @NotNull
    private Position getStartPosition() {
        return tokens.size() > 0 ? tokens.current().start : new Position(0, 1, 1);
    }


    @Contract(mutates = "param2")
    private boolean tryMatchAndStore(SyntaxNode node, SyntaxStructure structure) {
        int startCursor = tokens.cursor();

        var matched = matches(node, structure);

        if (matched) {
            if (tokens.cursor() >= lastDepth) clearProblems();

            if (!node.hasSubNodes()) return true;
            for (Iterator<SyntaxNode> iterator = node.subNodes.iterator(); iterator.hasNext(); ) {
                SyntaxNode subNode = iterator.next();
                if (tryMatchAndStore(subNode, structure)) return true;
                if (!iterator.hasNext()) {
                    addDiagnostics();
                    problemNodes.clear();
                }
            }
        } else if (tokens.cursor() >= lastDepth) {
            addProblemNode(node, tokens.getListRange(startCursor));
        }

        tokens.jumpTo(startCursor);
        return false;
    }

    private void addProblemNode(SyntaxNode expectedNode, List<Token> actualTokens) {
        problemNodes.computeIfAbsent(actualTokens, t -> new ArrayList<>(1)).add(expectedNode);
        lastDepth = tokens.cursor();
    }

    private void clearProblems() {
        problems.clear();
        problemNodes.clear();
        lastDepth = tokens.cursor();
    }

    private void addDiagnostics() {
        for (var entry : problemNodes.entrySet()) {
            var actualTokens = entry.getKey();
            var expectedNodes = entry.getValue();
            problems.add(new NotASyntaxDiagnostic(expectedNodes, actualTokens));
        }
    }

    private boolean matches(SyntaxNode node, SyntaxStructure structure) {
        return node.matchAndStore(tokens, structure, this);
    }

}
