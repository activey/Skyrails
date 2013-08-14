package org.skyrails.client.neo4j;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.event.TransactionData;
import org.neo4j.graphdb.event.TransactionEventHandler;
import org.skyrails.client.IServerHandle;
import org.skyrails.client.SkyrailsClient;
import org.skyrails.client.operator.BulkOperator;
import org.skyrails.client.operator.DirectOperator;

/**
 * User: activey
 * Date: 30.06.13
 * Time: 18:17
 */
public class SkyrailsTransactionMonitor implements TransactionEventHandler {

    private final SkyrailsClient skyrails;

    public SkyrailsTransactionMonitor() {
        this.skyrails = new SkyrailsClient("localhost", 9999);
        try {
            skyrails.connect();
            skyrails.doOnServer(new DirectOperator() {
                @Override
                public void doOnServer(IServerHandle serverHandle) {
                    serverHandle.clearGraph();
                    serverHandle.createNode("0", "ROOT", "textures/computer.gif");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object beforeCommit(TransactionData data) throws Exception {
        return null;
    }

    @Override
    public void afterCommit(final TransactionData data, Object state) {
        try {
            final Iterable<Node> createdNodes = data.createdNodes();
            skyrails.doOnServer(new BulkOperator() {
                @Override
                public void doOnServer(IServerHandle serverHandle) {
                    for (Node node : createdNodes) {
                        String nodeName = (String) node.getProperty("name", node.getId() + "");
                        serverHandle.createNode(node.getId() + "", nodeName, "textures/computer.gif");
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            final Iterable<Relationship> relationships = data.createdRelationships();
            skyrails.doOnServer(new BulkOperator() {
                @Override
                public void doOnServer(IServerHandle serverHandle) {
                    for (final Relationship relation : relationships) {
                        String from = "node_" + relation.getStartNode().getId();
                        String to = "node_" + relation.getEndNode().getId();
                        serverHandle.createEdge(from, to, relation.getType().name());
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void afterRollback(TransactionData data, Object state) {

    }
}
