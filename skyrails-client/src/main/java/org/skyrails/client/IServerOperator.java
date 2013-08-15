package org.skyrails.client;

import java.io.IOException;

/**
 * Basic interface that is an entry point for all operator implementations providing high level API for running commands
 * on remote Skyrails server instance.
 *
 * @author activey
 * @date 27.06.13 16:34
 */
public interface IServerOperator {

    /**
     * An entry method for all operator implementations. Once Skyrails client instance is set through setClient method,
     * operator can provide higher level API that will wrap {@link SkyrailsClient} executeCommand invocations.
     *
     * @throws Exception Operator can throw an exception if something went wrong.
     */
    public void doOnServer() throws Exception;

    /**
     * Method is executed by Skyrails client code to provide reference to itself.
     *
     * @param client Reference to Skyrails client instance.
     */
    public void setClient(SkyrailsClient client);
}
