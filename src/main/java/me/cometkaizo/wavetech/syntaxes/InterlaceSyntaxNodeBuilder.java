package me.cometkaizo.wavetech.syntaxes;

import me.cometkaizo.util.NullableOptional;

public class InterlaceSyntaxNodeBuilder extends CustomSyntaxNodeBuilder<Void> {
    public InterlaceSyntaxNodeBuilder(int min, int max, Syntax<?> nodeSyntax, Syntax<?> extraSyntax) {
        super((tokens, parent, matcher) -> {
            if (!tokens.hasNext()) return failure();
            int startCursor = tokens.cursor();

            var firstResult = matcher.tryMatch(nodeSyntax, parent, null);
            if (firstResult == null && min > 0) {
                tokens.jumpTo(startCursor);
                return failure();
            }

            for (int iterationCount = 1; max < 0 || iterationCount != max; iterationCount ++) {
                // match extra syntax
                // if success, continue
                // if failure and iteration count is less than min, fail
                // if failure and iteration count is more than min, success
                int previousCursor = tokens.cursor();

                var result = matcher.tryMatch(extraSyntax, parent, null);

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
