package org.skyrails.client;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.channels.AlreadyConnectedException;

/**
 * User: activey
 * Date: 27.06.13
 * Time: 15:28
 */
public class SkyrailsClient {

    private final String host;
    private final int port;
    private Socket socket;

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

    public final void connect() throws Exception {
        if (isConnected()) {
            throw new AlreadyConnectedException();
        }
        this.socket = new Socket(host, port);
        socket.setKeepAlive(true);
    }

    public final void disconnect() throws Exception {
        if (!isConnected()) {
            throw new NotConnectedException();
        }
        if (this.socket.isConnected()) {
            this.socket.close();
        }
        this.socket = null;
    }

    public void sendMessage(String message) throws Exception {
        if (!isConnected()) {
            throw new NotConnectedException();
        }
        System.out.print("Sending message: " + message);

        ByteArrayOutputStream temp = new ByteArrayOutputStream();
        DataOutputStream dataStream = new DataOutputStream(temp);
        message = "\"" + message + "\"";
        dataStream.writeInt(message.length());
        dataStream.write(message.getBytes());
        dataStream.writeByte(0);
        dataStream.writeByte(0);
        dataStream.writeByte(0);
        dataStream.writeByte(1);
        dataStream.write(59);

        DataOutputStream output = new DataOutputStream(socket.getOutputStream());
        //DataInputStream input = new DataInputStream(socket.getInputStream());
        output.write(temp.toByteArray());

        output.flush();
        //input.close();
    }


}
