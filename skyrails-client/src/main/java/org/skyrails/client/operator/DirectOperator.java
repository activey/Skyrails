package org.skyrails.client.operator;

import org.skyrails.client.IServerHandle;
import org.skyrails.client.handle.DirectServerHandle;

/**
 * User: activey
 * Date: 27.06.13
 * Time: 16:48
 */
public abstract class DirectOperator extends AbstractServerOperator {

    @Override
    public IServerHandle buildServerHandle() {
        return new DirectServerHandle(client);
    }
}
