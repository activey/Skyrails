package org.skyrails.client;

import java.io.IOException;

/**
 * User: activey
 * Date: 27.06.13
 * Time: 16:34
 */
public interface IServerOperator {

    public void doOnServer() throws Exception;

    public void setClient(SkyrailsClient client);
}
