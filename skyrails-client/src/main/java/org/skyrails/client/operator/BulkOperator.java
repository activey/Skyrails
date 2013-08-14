package org.skyrails.client.operator;

import org.skyrails.client.IServerHandle;
import org.skyrails.client.NotConnectedException;
import org.skyrails.client.handle.BulkServerHandle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * User: activey Date: 27.06.13 Time: 16:48
 */
public abstract class BulkOperator extends AbstractServerOperator {

    private final static Logger log = LoggerFactory.getLogger(BulkOperator.class);
    private final BulkServerHandle handle;
    private final int packetSize;

    public BulkOperator() {
        this(0);
    }

    /**
     * TODO
     *
     * @param packetSize When set to 0, packaging mechanism will be disabled.
     */
    public BulkOperator(int packetSize) {
        this.packetSize = packetSize;
        this.handle = new BulkServerHandle(packetSize) {

            @Override
            public void packetReady(String packetContents) {
                try {
                    client.executeCommand(packetContents);
                } catch (NotConnectedException e) {
                    log.error("", e);
                } catch (IOException e) {
                    log.error("", e);
                }
            }
        };
    }

    @Override
    protected void doPostProcess() throws Exception {
        // sending remaining messages in packet or whole buffer when packet size was set to 0
        client.executeCommand(handle.getCommands());
    }

    @Override
    public IServerHandle buildServerHandle() {
        return handle;
    }
}
