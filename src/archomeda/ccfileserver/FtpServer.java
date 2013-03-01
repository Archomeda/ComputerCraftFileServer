/*******************************************************************************
 * Copyright (c) 2013 Archomeda.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Archomeda - initial API and implementation
 ******************************************************************************/
package archomeda.ccfileserver;

import org.apache.ftpserver.ConnectionConfigFactory;
import org.apache.ftpserver.DataConnectionConfigurationFactory;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.listener.ListenerFactory;

/**
 * A ComputerCraft FTP server.
 * 
 * @author Archomeda
 */
public class FtpServer {
    private org.apache.ftpserver.FtpServer ftpServer;

    private final String bindAddress;
    private final int bindPort;
    private final String passiveAddress;
    private final String passivePorts;
    private final int maxLogins;
    private final long maxFileSize;
    private final long maxStorageSize;

    /**
     * Creates a new FTP server with given arguments.
     * 
     * @param bindAddress
     *            The local IP address to bind the server to.
     * @param bindPort
     *            The local port to bind the server to.
     * @param passiveAddress
     *            The passive IP address to use.
     * @param passivePorts
     *            The passive ports to use.
     * @param maxLogins
     *            The maximum number of simultaneous logged in users.
     * @param maxFileSize
     *            The maximum file size in bytes.
     * @param maxStorageSize
     *            The maximum storage size in bytes.
     */
    public FtpServer(String bindAddress, int bindPort, String passiveAddress, String passivePorts, int maxLogins,
            long maxFileSize, long maxStorageSize) {
        // TODO: Refactor constructor

        this.bindAddress = bindAddress;
        this.bindPort = bindPort;
        this.passiveAddress = passiveAddress;
        this.passivePorts = passivePorts;
        this.maxLogins = maxLogins;
        this.maxFileSize = maxFileSize;
        this.maxStorageSize = maxStorageSize;
        initServer();
    }

    /**
     * Initializes the FTP server.
     */
    protected void initServer() {
        FtpServerFactory serverFactory = new FtpServerFactory();

        ListenerFactory lFactory = new ListenerFactory();
        lFactory.setServerAddress(bindAddress);
        lFactory.setPort(bindPort);

        DataConnectionConfigurationFactory dccFactory = new DataConnectionConfigurationFactory();
        dccFactory.setPassiveExternalAddress(passiveAddress);
        dccFactory.setPassivePorts(passivePorts);
        lFactory.setDataConnectionConfiguration(dccFactory.createDataConnectionConfiguration());

        ConnectionConfigFactory ccFactory = new ConnectionConfigFactory();
        ccFactory.setAnonymousLoginEnabled(false);
        ccFactory.setMaxLogins(maxLogins);
        serverFactory.setConnectionConfig(ccFactory.createConnectionConfig());

        serverFactory.addListener("default", lFactory.createListener());
        serverFactory.setUserManager(new FtpUserManager(maxFileSize, maxStorageSize));

        ftpServer = serverFactory.createServer();
    }

    /**
     * Starts the FTP server.
     * 
     * @throws FtpException
     *             Thrown when an FTP error occures.
     */
    public void startServer() throws FtpException {
        ftpServer.start();
    }

    /**
     * Stops the FTP server.
     */
    public void stopServer() {
        ftpServer.stop();
    }
}
