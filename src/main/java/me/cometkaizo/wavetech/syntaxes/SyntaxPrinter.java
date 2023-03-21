package me.cometkaizo.wavetech.syntaxes;

import org.jetbrains.annotations.NotNull;

public class SyntaxPrinter {

    /*
     * root -> (a, (b -> (c, d) -> e)) -> f
     *
     * ROOT
     *   then A
     *   or B
     *     then C or D
     *   then E
     * then F
     *
     * If a node has sub-nodes A, B, and C, it will be displayed as:
     *
     * ...
     *   then A or B or C
     *
     * UNLESS any of them have sub-nodes themselves, in which case it will be displayed as:
     *
     * ...
     *   then A
     *     *sub-nodes*
     *   or B
     *     *sub-nodes*
     *   or C
     *     *sub-nodes*
     *
     * This is applied recursively.
     * Here is the tree of the class declaration:
     *
     * ROOT
     *   then (PUBLIC or //)
     *   then (
     *     SEALED
     *       then (ABSTRACT or //)
     *     or ABSTRACT or FINAL
     *   )
     *   then CLASS
     *   then symbol
     *
     */

    public String calculateString(Syntax<?> syntax) {
        var root = syntax.getRootNode();
        var buffer = new StringBuilder();

        appendString(root, buffer, 0, "");
        return buffer.toString();
    }

    private void appendString(SyntaxNode node, StringBuilder buffer, int depth, String prefix) {
        buffer
                .append(" ".repeat(depth * 2))
                .append(prefix)
                .append(node.toPrettyString())
                .append("\n");


        appendFirstSubNode(node, buffer, depth); // prepends "then"
        appendOtherSubNodes(node, buffer, depth); // prepends "or"

        appendMergeDestination(node, buffer, depth);

    }

    private void appendFirstSubNode(SyntaxNode node, StringBuilder buffer, int depth) {
        if (node.hasSubNodes() && !node.merges) {
            appendString(node.subNodes.get(0), buffer, depth + 1, "then ");
        }
    }

    private void appendOtherSubNodes(SyntaxNode node, StringBuilder buffer, int depth) {
        if (node.merges) return;
        for (int index = 1; index < node.subNodes.size(); index ++) {
            appendString(node.subNodes.get(index), buffer, depth + 1, "or ");
        }
    }

    private void appendMergeDestination(SyntaxNode node, StringBuilder buffer, int depth) {
        if (node.subNodes.size() < 2) return;
        var mergeDestination = findMergeDestination(node, 0);

        appendString(mergeDestination, buffer, depth, "and then ");
    }

    private SyntaxNode findMergeDestination(@NotNull SyntaxNode node, int splitCount) {
        var firstSubNode = node.subNodes.get(0);
        //System.out.println(node.name() + ", split count: " + splitCount + ", subNodes: " + Arrays.stream(node.subNodes()).map(Node::name).collect(Collectors.joining(" ")));

        if (splitCount == 1 && node.merges)
            return firstSubNode;

        if (node.subNodes.size() > 1) splitCount ++;
        else if (node.merges) splitCount --;

        return findMergeDestination(firstSubNode, splitCount);
    }

}
