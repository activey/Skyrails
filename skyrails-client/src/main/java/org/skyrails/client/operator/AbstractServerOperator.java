package org.skyrails.client.operator;

import org.skyrails.client.IServerHandle;
import org.skyrails.client.IServerOperator;
import org.skyrails.client.SkyrailsClient;

/**
 * User: activey
 * Date: 27.06.13
 * Time: 19:01
 */
public abstract class AbstractServerOperator implements IServerOperator {

    protected SkyrailsClient client;

    @Override
    public void doOnServer() throws Exception {
        doOnServer(buildServerHandle());

        doPostProcess();
    }

    @Override
    public final void setClient(SkyrailsClient client) {
        this.client = client;
    }

    public abstract void doOnServer(IServerHandle serverHandle);

    protected void doPostProcess() throws Exception {

    }

    protected abstract IServerHandle buildServerHandle();
}
