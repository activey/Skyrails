package org.skyrails.client.neo4j;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.event.TransactionData;
import org.neo4j.graphdb.event.TransactionEventHandler;
import org.skyrails.client.IServerHandle;
import org.skyrails.client.SkyrailsClient;
import org.skyrails.client.operator.BulkOperator;
import org.skyrails.client.operator.DirectOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class is an implementation of {@link org.neo4j.graphdb.event.TransactionEventHandler} that translates all node/edge
 * operations into Skyrails server calls. Using this Event Handler implementation you can monitor your Neo4j instance in
 * almost realtime.
 *
 * @author activey
 * @date 30.06.13 18:17
 */
public class SkyrailsTransactionMonitor implements TransactionEventHandler {

    private final static Logger log = LoggerFactory.getLogger(SkyrailsTransactionMonitor.class);
    private final SkyrailsClient skyrails;

    public SkyrailsTransactionMonitor() {
        // creating Skyrails client instance
        this.skyrails = new SkyrailsClient(getHost(), getPort());
        try {
            skyrails.connect();
            skyrails.doOnServer(new DirectOperator() {
                @Override
                public void doOnServer(IServerHandle serverHandle) {
                    // clearing graph just after monitor instance is created
                    serverHandle.clearGraph();

                    // creating Neo4j root node
                    serverHandle.createNode("0", "ROOT", SkyrailsTransactionMonitor.this.getNodeTexture());
                }
            });
        } catch (Exception e) {
            log.error("", e);
        }
    }

    @Override
    public Object beforeCommit(TransactionData data) throws Exception {
        return null;
    }

    @Override
    public void afterCommit(final TransactionData data, Object state) {
        try {
            // iterating through all created nodes
            final Iterable<Node> createdNodes = data.createdNodes();
            // using Bulk operator not to override Skyrails ;)
            skyrails.doOnServer(new BulkOperator() {
                @Override
                public void doOnServer(IServerHandle serverHandle) {
                    for (Node node : createdNodes) {
                        // if property is not available, node id will be used as it's label
                        String nodeName = (String) node.getProperty(getLabelProperty(), node.getId() + "");
                        // creating node in Skyrails
                        serverHandle.createNode(node.getId() + "", nodeName, SkyrailsTransactionMonitor.this
                                .getNodeTexture());
                    }
                }
            });
        } catch (Exception e) {
            log.error("", e);
        }

        try {
            // iterating through all edges created within given transaction
            final Iterable<Relationship> relationships = data.createdRelationships();
            skyrails.doOnServer(new BulkOperator() {
                @Override
                public void doOnServer(IServerHandle serverHandle) {
                    for (final Relationship relation : relationships) {
                        String from = "node_" + relation.getStartNode().getId();
                        String to = "node_" + relation.getEndNode().getId();
                        // creating edge between nodes in Skyrails
                        serverHandle.createEdge(from, to, relation.getType().name());
                    }
                }
            });
        } catch (Exception e) {
            log.error("", e);
        }

    }

    /**
     * Method returns Neo4j node property name that will be used as Skyrails node label. You can override this value by
     * any other value in your custom implementation.
     *
     * @return Property name from Neo4j node that will be used as Skyrails node label.
     */
    protected String getLabelProperty() {
        return "name";
    }

    /**
     * Method returns default port for connecting with Skyrails instance. You can change it to any value in your custom
     * implementation.
     *
     * @return  Default port that Skyrails server is listening on.
     */
    protected int getPort() {
        return 9999;
    }

    /**
     * Method returns default host name for connecting with Skyrails instance. You can change it to any value in your
     * custom implementation.
     *
     * @return  Default host name of remote Skyrails server.
     */
    protected String getHost() {
        return "localhost";
    }

    /**
     * Method returns texture location for Skyrails nodes. You can change it to any value in your custom
     * implementation.
     *
     * @return Default texture location for nodes created in Skyrails instance.
     */
    protected String getNodeTexture() {
        return "textures/computer.gif";
    }


    @Override
    public void afterRollback(TransactionData data, Object state) {

    }
}
