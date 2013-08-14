package org.skyrails.client.handle;

/**
 * User: activey
 * Date: 27.06.13
 * Time: 16:42
 */
public class BulkServerHandle extends AbstractServerHandle {

    @Override
    public void clearGraph() {
        commandsBuffer.append(buildCleargraph());
    }

    private StringBuffer commandsBuffer = new StringBuffer();

    public final String createNode(String nodeId) {
        commandsBuffer.append(buildCreateNode(nodeId));
        return nodeId;
    }

    @Override
    public String createNode(String node, String label, String texture) {
        commandsBuffer.append(buildCreateNode(node, label, texture));
        return node;
    }

    public String createNode(String node, String texture) {
        commandsBuffer.append(buildCreateNode(node, texture));
        return node;
    }

    @Override
    public void createEdge(String nodeFrom, String nodeTo, String relationType) {
        commandsBuffer.append(buildCreateEdge(nodeFrom, nodeTo, relationType));
    }

    @Override
    public void createEdge(String nodeFrom, String nodeTo, String relationType, boolean visible) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getCommands() {
        return commandsBuffer.toString();
    }

    public final void reset() {
        this.commandsBuffer = new StringBuffer();
    }
}
