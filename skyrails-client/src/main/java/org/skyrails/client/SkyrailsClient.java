package org.skyrails.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Class provices basic operations for accessing and interacting with remote Skyrails server instance. First you have to
 * create {@link SkyrailsClient} instance by passing host and port parameters (Skyrails has to be started in server
 * mode!). When instance is ready, connection has to be established by calling {@see connect} method. Eg:
 * <pre>
 *     SkyrailsClient client = new SkyrailsClient("localhost", 9999);
 *     client.connect();
 * </pre>
 * <p/>
 * After connection is established (no exception appears), you can interact with Skyrails server instance both ways:
 *
 * <ul>
 *     <li>
 *         Using simple command invocation, eg: <br/>
 *          <pre>
 *              client.executeCommand("node1 -- has -> node2");
 *          </pre>
 *     </li>
 *     <li>
 *          or in more complex manner using one of available {@link IServerOperator} implementation that provides
 *          high-level Skyrails interaction API, eg:<br/>
 *          <pre>
 *              client.doOnServer(new DirectOperator() {

                    public void doOnServer({@link IServerHandle} serverHandle) {
                        serverHandle.createNode("node1", "First Node", "textures/computer.png");
                        serverHandle.createNode("node2", "Second Node", "textures/computer.png");
                        serverHandle.createEdge("node1", "node2", "LEADS_TO");
                    }
                });
 *          </pre>
 *     </li>
 * </ul>
 *
 * Unfortunately, Skyrails in server mode does not pride any feedback when executed command fails so no exception can be
 * thrown back when something goes wrong.
 *
 * @author activey
 * @date 27.06.13 15:28
 */
public class SkyrailsClient {

    private final static Logger log = LoggerFactory.getLogger(SkyrailsClient.class);
    private final String host;
    private final int port;
    private Socket socket;


    /**
     * Default constructor.
     *
     * @param host Skyrails server host name.
     * @param port Server port number.
     */
    public SkyrailsClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * Method provides an interface for running commands on remote Skyrails instance using high level API.
     * Once {@see IServerOperator} instance is given, all Skyrails commands are wrapped by methods provided by
     * {@see IServerHandle} instance to make it play nicely using clean API.
     *
     * @param operator Skyrails server operator instance.
     * @throws Exception Exception is thrown when something goes wrong during operator usage.
     * @see IServerOperator
     * @see org.skyrails.client.operator.DirectOperator
     * @see org.skyrails.client.operator.BulkOperator
     */
    public void doOnServer(IServerOperator operator) throws Exception {
        operator.setClient(this);
        operator.doOnServer();
    }

    /**
     * Method verifies if remote client connection is already established.
     *
     * @return Value indicating if client connection is already established.
     */
    public final boolean isConnected() {
        return this.socket != null && this.socket.isConnected();
    }

    /**
     * Method establishes client connection with Skyrails server instance.
     *
     * @throws IOException Exception is thrown when some IO operation fails.
     * @throws AlreadyConnectedException Exception is thrown when connection is already established.
     */
    public final void connect() throws IOException, AlreadyConnectedException {
        if (isConnected()) {
            throw new AlreadyConnectedException();
        }
        this.socket = new Socket(host, port);
        socket.setKeepAlive(true);
    }

    /**
     * Disconnects client socket from remote Skyrails instance.
     *
     * @throws NotConnectedException Exception is thrown when client is not yet connected.
     * @throws IOException           Exception is thrown when some IO operation fails.
     */
    public final void disconnect() throws NotConnectedException, IOException {
        if (!isConnected()) {
            throw new NotConnectedException();
        }
        if (this.socket.isConnected()) {
            this.socket.close();
        }
        this.socket = null;
    }

    /**
     * Method provides basic interface for executing commands on remote Skyrails server instance (Low level access).
     *
     * @param command Command to execute, eg: node1 -- has -> node2
     * @throws NotConnectedException Exception is thrown when client is not yet connected.
     * @throws IOException           Exception is thrown when some IO operation failed.
     */
    public void executeCommand(String command) throws NotConnectedException, IOException {
        if (!isConnected()) {
            throw new NotConnectedException();
        }
        if (log.isTraceEnabled()) {
            log.trace(String.format("Executing command: %s", command));
        }

        /*
         * this code part is an conversion of python Client code developed by Felix Leder (eder@cs.uni-bonn.de)
         * from University of Bonn.
         */
        ByteArrayOutputStream temp = new ByteArrayOutputStream();
        DataOutputStream dataStream = new DataOutputStream(temp);
        command = "\"" + command + "\"";
        dataStream.writeInt(command.length());
        dataStream.write(command.getBytes());
        dataStream.writeByte(0);
        dataStream.writeByte(0);
        dataStream.writeByte(0);
        dataStream.writeByte(1);
        dataStream.write(59);

        DataOutputStream output = new DataOutputStream(socket.getOutputStream());
        output.write(temp.toByteArray());

        output.flush();
    }

}
