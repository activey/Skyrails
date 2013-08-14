package org.skyrails.client.handle;

import org.skyrails.client.SkyrailsClient;

import java.io.IOException;

/**
 * User: activey
 * Date: 27.06.13
 * Time: 16:42
 */
public class DirectServerHandle extends AbstractServerHandle {

    private final SkyrailsClient client;

    public DirectServerHandle(SkyrailsClient client) {
        this.client = client;
    }

    public final String createNode(String nodeId) {
        if (!client.isConnected()) {
            return null;
        }
        try {
            client.sendMessage(buildCreateNode(nodeId));
            return nodeId;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String createNode(String node, String label, String texture) {
        if (!client.isConnected()) {
            return null;
        }
        try {
            client.sendMessage(buildCreateNode(node, label, texture));
            return node;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String createNode(String nodeId, String texture) {
        if (!client.isConnected()) {
            return null;
        }
        try {
            client.sendMessage(buildCreateNode(nodeId, texture));
            return nodeId;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void createEdge(String nodeFrom, String nodeTo, String relationType) {
        try {
            client.sendMessage(buildCreateEdge(nodeFrom, nodeTo, relationType));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createEdge(String nodeFrom, String nodeTo, String relationType, boolean visible) {
        //TODO
    }

    @Override
    public void clearGraph() {
        try {
            client.sendMessage(buildCleargraph());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
