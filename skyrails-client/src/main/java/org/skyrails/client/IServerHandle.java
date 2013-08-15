package org.skyrails.client;

import java.io.IOException;

/**
 * [TODO]
 *
 * User: activey
 * Date: 27.06.13
 * Time: 16:39
 */
public interface IServerHandle {

    public String createNode(String nodeId);

    public String createNode(String node, String label, String texture);

    public String createNode(String node, String texture);

    public void createEdge(String nodeFrom, String nodeTo, String relationType);

    public void createEdge(String nodeFrom, String nodeTo, String relationType, boolean visible);

    public void clearGraph();
}
