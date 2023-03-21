package me.cometkaizo.wavetech.syntaxes;

import me.cometkaizo.util.PeekingIterator;
import me.cometkaizo.wavetech.lexer.tokens.Token;

import java.util.Objects;

public class AnchorSyntaxNode extends SyntaxNode {

    protected final int anchor;

    protected AnchorSyntaxNode(AnchorSyntaxNodeBuilder builder) {
        super(builder);
        this.anchor = builder.anchor;
    }

    @Override
    protected boolean matchAndStore(PeekingIterator<Token> tokens, SyntaxStructure structure, SyntaxMatcher matcher) {
        return anchorIsCursor(tokens);
    }

    private boolean anchorIsCursor(PeekingIterator<Token> candidates) {
        if (Math.abs(anchor) > candidates.size()) return false;
        // positive anchors will go forwards from 0
        // negative anchor will go backwards from the end to allow for anchoring to the end
        // i.e. anchor = -1 will result in candidates.size() - 1
        return ((anchor + candidates.size() + 1) % (candidates.size() + 1)) == candidates.cursor();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnchorSyntaxNode that = (AnchorSyntaxNode) o;
        return anchor == that.anchor;
    }

    @Override
    public String toPrettyString() {
        String anchorPos = (anchor == 0 ? "Start" : (anchor == -1 ? "End" : String.valueOf(anchor)));
        return "ANCHOR: " + anchorPos;
    }

    @Override
    public int hashCode() {
        return Objects.hash(anchor);
    }
}
