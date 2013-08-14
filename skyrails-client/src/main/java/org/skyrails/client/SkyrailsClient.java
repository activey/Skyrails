package org.skyrails.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.channels.AlreadyConnectedException;

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
 *          or in more complex manner using one of available {@link IServerOperator} implementation, eg:<br/>
 *          <pre>
 *              [TODO]
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

    public void doOnServer(IServerOperator operator) throws Exception {
        operator.setClient(this);
        operator.doOnServer();
    }

    public final boolean isConnected() {
        return this.socket != null && this.socket.isConnected();
    }

    /**
     * Method .
     *
     * @throws IOException
     */
    public final void connect() throws IOException {
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
     * @throws IOException           Exception is thrown when some IO operation failed.
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
     * Method provides basic interface for executing commands on remote Skyrails server instance.
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
            log.trace(String.format("Sending message: %s", command));
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
