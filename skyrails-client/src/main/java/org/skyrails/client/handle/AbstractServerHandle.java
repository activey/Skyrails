package org.skyrails.client.handle;

import org.skyrails.client.IServerHandle;

import java.io.IOException;
import java.text.MessageFormat;

/**
 * User: activey
 * Date: 27.06.13
 * Time: 16:42
 */
public abstract class AbstractServerHandle implements IServerHandle {

    protected String buildCreateNode(String node, String texture) {
        return MessageFormat.format("node: node_{0} ::> (itemtexture name \"{1}\");", node, texture);
    }

    protected String buildCreateNode(String node, String label, String texture) {
        return MessageFormat.format("node: node_{0} ::> (nodelabel \"{1}\"; itemtexture name \"{2}\");", node, label, texture);
    }

    protected String buildCreateNode(String node) {
        return MessageFormat.format("node: {0};", node);
    }

    protected String buildCreateEdge(String nodeFrom, String nodeTo, String relationType) {
        return MessageFormat.format("{0}--{1}->{2};", nodeFrom, relationType, nodeTo);
    }

    protected String buildCleargraph() {
        return "cleargraph;";
    }

}
