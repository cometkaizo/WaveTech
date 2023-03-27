package me.cometkaizo.wavetech.syntaxes;

import me.cometkaizo.util.NullableOptional;

public class LoopSyntaxNodeBuilder extends CustomSyntaxNodeBuilder<Void> {
    public LoopSyntaxNodeBuilder(int min, int max, Syntax<?> nodeSyntax) {
        super((tokens, parent, matcher) -> {
            if (!tokens.hasNext()) return failure();
            int startCursor = tokens.cursor();

            for (int iterationCount = 0; max < 0 || iterationCount != max; iterationCount++) {
                int previousCursor = tokens.cursor();

                var result = matcher.tryMatch(nodeSyntax, parent, null);

                if (result == null) {
                    if (iterationCount < min) {
                        tokens.jumpTo(startCursor);
                        return failure();
                    } else {
                        tokens.jumpTo(previousCursor);
                        return success();
                    }
                }

            }

            tokens.jumpTo(startCursor);
            return failure();
        });
    }

    private static NullableOptional<Void> success() {
        return NullableOptional.of(null);
    }

    private static NullableOptional<Void> failure() {
        return NullableOptional.empty();
    }
}
