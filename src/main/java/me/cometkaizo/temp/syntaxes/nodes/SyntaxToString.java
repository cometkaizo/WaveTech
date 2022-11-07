package me.cometkaizo.temp.syntaxes.nodes;

import me.cometkaizo.util.ConsoleColors;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class SyntaxToString {

    @NotNull
    private final SyntaxNode rootNode;
    private final List<SyntaxNode> route;

    public SyntaxToString(Syntax syntax) {
        this.rootNode = syntax.getRootOrBuild();
        this.route = syntax.route;
    }

    public String represent() {
        StringBuilder representation = new StringBuilder();
        represent(rootNode, representation, "", "", "");
        return representation.toString();
    }

    public void represent(@NotNull SyntaxNode node,
                          @NotNull StringBuilder representation,
                          @NotNull String prefix,
                          @NotNull String subPrefix,
                          @NotNull String suffix) {

        if (!(node instanceof EmptySyntaxNode)) {
            representation.append(prefix);
            boolean isSuccessfulNode = route.contains(node);
            if (isSuccessfulNode)
                representation.append(ConsoleColors.GREEN);

            representation.append(node.representData());

            if (isSuccessfulNode)
                representation.append(ConsoleColors.RESET);
            representation.append(" ");
        }

        if (node.merges) {
            representation.append(suffix);
            return;
        }

        SyntaxNode mergeDestination = node.splits? findMergeDestination(node, 0) : null;
        //LogUtils.debug("node {} splits? {}, mergeDestination: {}", node, node.splits, mergeDestination);

        List<SyntaxNode> subNodes = node.subNodes;

        if (subNodes.size() == 1) {

            //LogUtils.debug("node: {}, subNodes: {}", node, node.subNodes);
            representNext(representation, subPrefix, subNodes);

        } else {

            //LogUtils.debug("node: {}, subNodes: {}", node, node.subNodes);
            representAllNext(representation, subPrefix, subNodes);

            if (mergeDestination != null) {
                representation.append("\n");
                represent(mergeDestination, representation, subPrefix, subPrefix, "");
            }

        }

        representation.append(suffix);
    }

    private void representAllNext(StringBuilder representation, String subPrefix, List<SyntaxNode> subNodes) {
        Optional<SyntaxNode> emptyNode = subNodes.stream().filter(node -> node instanceof EmptySyntaxNode).findAny();
        boolean isOptional = emptyNode.isPresent();
        String optionalColor = isOptional && subNodes.stream().noneMatch(route::contains)? ConsoleColors.GREEN : "";

        boolean isFirst = true;
        for (SyntaxNode subNode : subNodes) {
            if (subNode instanceof EmptySyntaxNode) continue;
            if (isFirst) {
                represent(subNode, representation,
                        "\n" + subPrefix + "then " +
                                (isOptional ? optionalColor + "optionally " +
                                        (optionalColor.equals("")? "" : ConsoleColors.RESET)
                                        : ""),
                        subPrefix + "    ", "");
                isFirst = false;
            } else {
                boolean b = !subNode.merges && subNode.hasSubNodes();
                represent(subNode, representation, (b ? "\n" : "") + "or ", subPrefix + "    ", "");
            }
        }
    }

    private void representNext(@NotNull StringBuilder representation,
                               @NotNull String subPrefix,
                               @NotNull List<SyntaxNode> subNodes) {
        SyntaxNode nextNode = subNodes.stream().findAny().orElseThrow();
        represent(nextNode, representation, "\n" + subPrefix + "then ", subPrefix, "\n");
    }

    private SyntaxNode findMergeDestination(@NotNull SyntaxNode node, int splittingNodeCount) {
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
