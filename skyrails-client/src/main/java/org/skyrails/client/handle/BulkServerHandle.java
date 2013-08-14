package org.skyrails.client.handle;

/**
 * User: activey Date: 27.06.13 Time: 16:42
 */
public abstract class BulkServerHandle extends AbstractServerHandle {

    private final int packetSize;
    private int currentPacketSize = 0;
    private StringBuffer commandsBuffer = new StringBuffer();

    public BulkServerHandle(int packetSize) {
        this.packetSize = packetSize;
    }

    @Override
    public void clearGraph() {
        commandsBuffer.append(buildCleargraph());
        afterCommandProcessed();
    }

    public final String createNode(String nodeId) {
        commandsBuffer.append(buildCreateNode(nodeId));
        afterCommandProcessed();
        return nodeId;
    }

    @Override
    public String createNode(String node, String label, String texture) {
        commandsBuffer.append(buildCreateNode(node, label, texture));
        afterCommandProcessed();
        return node;
    }

    public String createNode(String node, String texture) {
        commandsBuffer.append(buildCreateNode(node, texture));
        afterCommandProcessed();
        return node;
    }

    @Override
    public void createEdge(String nodeFrom, String nodeTo, String relationType) {
        commandsBuffer.append(buildCreateEdge(nodeFrom, nodeTo, relationType));
        afterCommandProcessed();
    }

    @Override
    public void createEdge(String nodeFrom, String nodeTo, String relationType, boolean visible) {

        afterCommandProcessed();
    }

    private void afterCommandProcessed() {
        if (packetSize == 0) {
            return;
        }
        this.currentPacketSize++;
        if (this.currentPacketSize + 1 > this.packetSize) {
            packetReady(commandsBuffer.toString());

            this.currentPacketSize = 0;
            this.commandsBuffer = new StringBuffer();
        }
    }

    public abstract void packetReady(String packetContents);

    public String getCommands() {
        return this.commandsBuffer.toString();
    }
}
