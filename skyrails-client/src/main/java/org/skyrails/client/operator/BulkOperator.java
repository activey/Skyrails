package org.skyrails.client.operator;

import org.skyrails.client.IServerHandle;
import org.skyrails.client.SkyrailsClient;
import org.skyrails.client.handle.BulkServerHandle;

import java.io.IOException;

/**
 * User: activey
 * Date: 27.06.13
 * Time: 16:48
 */
public abstract class BulkOperator extends AbstractServerOperator {

    private final BulkServerHandle handle;

    public BulkOperator() {
        this.handle = new BulkServerHandle();
    }

    @Override
    protected void doPostProcess() throws Exception {
        client.sendMessage(handle.getCommands());
    }

    @Override
    public IServerHandle buildServerHandle() {
        return handle;
    }

    public final void commit() throws Exception {
        client.sendMessage(handle.getCommands());
        handle.reset();
    }

}
