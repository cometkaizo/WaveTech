package me.cometkaizo.commands.nodes;

import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A CommandNode represents a single action in a command (e.g. getting user input, translating user input, executing code, etc).
 * Specific functionality is specified by subclasses.
 *
 * @see Command
 * @see RootCommandNode
 * @see LiteralCommandNode
 * @see ArgumentCommandNode
 * @see SoftCommandNode
 * @see ConditionalCommandNode
 */
abstract class CommandNode {

    protected final int level;
    protected final List<CommandNode> subNodes;
    protected final List<Runnable> tasks;
    protected Command context;

    protected CommandNode(CommandNodeBuilder builder) {
        this.subNodes = buildSubNodes(builder);
        this.tasks = List.of(builder.tasks.toArray(Runnable[]::new));
        this.level = builder.level;
    }
    private static List<CommandNode> buildSubNodes(CommandNodeBuilder builder) {
        List<CommandNode> result = builder.subNodes.stream()
                .map(CommandNodeBuilder::build)
                .collect(Collectors.toCollection(ArrayList::new));
        return List.of(result.toArray(CommandNode[]::new));
    }


    /**
     * Executes this node's tasks, and potentially one of its sub-nodes depending on arguments provided by {@code context}.
     * If no sub-nodes can be executed or there are insufficient arguments, a CommandSyntaxException is thrown.
     * If multiple sub-nodes can be executed, it will execute the first one that accepts the next argument.
     * @param context the context to run this command in
     * @throws CommandSyntaxException If there are insufficient arguments, or an argument could not be parsed by any sub-nodes.
     */
    protected final void execute(Command context) throws CommandSyntaxException {
        this.context = context;

        executeFunctionality();

        for (Runnable task : tasks)
            task.run();

        if (hasSubNodes())
            executeSubNodes();
    }

    protected abstract void executeFunctionality();

    private void executeSubNodes() throws CommandSyntaxException {
        if (!hasSubNodes()) return;

        if (hasNextArg())
            executeSubNodesWithNextArg();
        else {
            if (requiresNextArg()) {
                String formattedArgs = Arrays.stream(context.args)
                        .map(Objects::toString)
                        .collect(Collectors.joining(" "));

                throw new CommandSyntaxException(
                        "Unexpected end of arguments: \n\t" +
                                formattedArgs + "\n" +
                                " ".repeat(formattedArgs.length()) + "^\n" +
                                "subNodes: \n" +
                                subNodes.stream()
                                        .map(Objects::toString)
                                        .collect(Collectors.joining("\n"))
                );
            } else executeNoArgSubNode();
        }
    }

    private void executeSubNodesWithNextArg() throws CommandSyntaxException {

        for (CommandNode subNode : subNodes) {
            if (subNode.accepts(nextArg())) {
                subNode.execute(context);
                return;
            }
        }

        throw new CommandSyntaxException("Unexpected argument '" + nextArg() + "'; candidate sub-nodes: \n" + subNodes);
    }

    private void executeNoArgSubNode() {
        CommandNode noArgSubNode = getNoArgSubNode();

        if (noArgSubNode == null) throw new NoSuchElementException();

        noArgSubNode.execute(context);
    }


    protected abstract boolean accepts(String arg);

    private boolean requiresNextArg() {
        return subNodes.stream().allMatch(CommandNode::acceptsArguments);
    }

    @Nullable
    private CommandNode getNoArgSubNode() {
        return subNodes.stream()
                .filter(node -> !node.acceptsArguments())
                .findFirst().orElse(null);
    }

    public final boolean acceptsArguments() {
        return !(this instanceof SoftCommandNode);
    }

    private String nextArg() {
        return context.args[level + 1];
    }

    private boolean hasNextArg() {
        return context.args.length - 1 > level;
    }

    public boolean hasSubNodes() {
        return subNodes.size() > 0;
    }

}
