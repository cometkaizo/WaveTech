package me.cometkaizo.wavetech.parser.syntaxes.nodes;

import java.util.Set;

public class SyntaxToString {
    private final SyntaxNode rootNode;

    public SyntaxToString(Syntax syntax) {
        this.rootNode = syntax.getRoot();
    }

    public String represent() {
        StringBuilder representation = new StringBuilder();
        represent(rootNode, representation, "", "", "");
        return representation.toString();
    }

    public void represent(SyntaxNode node, StringBuilder representation, String prefix, String subPrefix, String suffix) {
        if (!(node instanceof EmptySyntaxNode)) {
            representation.append(prefix);
            representation.append(node.representData());
            representation.append(" ");
        }

        if (node.merges) {
            representation.append(suffix);
            return;
        }

        SyntaxNode mergeDestination = node.splits? findMergeDestination(node, 0) : null;

        Set<SyntaxNode> subNodes = node.subNodes;

        if (subNodes.size() == 1) {

            SyntaxNode nextNode = subNodes.stream().findAny().orElseThrow();
            represent(nextNode, representation, "\n" + subPrefix + "then ", subPrefix, "\n");

        } else {

            boolean isOptional = subNodes.stream().anyMatch(o -> o instanceof EmptySyntaxNode);

            boolean isFirst = true;
            for (SyntaxNode subNode : subNodes) {
                if (subNode instanceof EmptySyntaxNode) continue;
                if (isFirst) {
                    represent(subNode, representation,
                            "\n" + subPrefix + "then " + (isOptional ? "optionally " : ""),
                            subPrefix + "    ", "");
                    isFirst = false;
                } else {
                    represent(subNode, representation, "or ", subPrefix + "    ", "");
                }
            }

            if (mergeDestination != null) {
                representation.append("\n");
                represent(mergeDestination, representation, subPrefix, subPrefix, "");
            }

        }

        representation.append(suffix);
    }

    private SyntaxNode findMergeDestination(SyntaxNode node, int splittingNodeCount) {
        SyntaxNode firstSubNode = node.subNodes.stream().findFirst().orElseThrow();
        if (splittingNodeCount == 1 && node.merges)
            return firstSubNode;
        if (node.splits)
            return findMergeDestination(firstSubNode, splittingNodeCount + 1);
        else if (node.merges)
            return findMergeDestination(firstSubNode, splittingNodeCount - 1);
        return null;
    }

}
