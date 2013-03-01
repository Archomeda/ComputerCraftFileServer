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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;

import org.apache.ftpserver.ftplet.FtpException;

import archomeda.upnp.Upnp;

/**
 * The main entry point for both Minecraft servers and clients.
 * 
 * @author Archomeda
 */
public class Proxy {
    private FtpServer ftpServer;

    public Proxy() {
    }

    /**
     * Initializes the FTP server.
     */
    public void initFtp() {
        String bindAddress = Config.ftpLanIp;
        int bindPort = Config.ftpPort;
        String passiveAddress = Config.ftpWanIp;
        String passivePorts = Config.ftpPassivePortRange;
        int maxLogins = Config.ftpMaxLogins;
        long maxFileSize = Config.userMaxFileSize;
        long maxStorageSize = Config.userMaxStorageSize;

        if (ComputerCraftFileServer.modMinecraftUpnpAvailable) {
            bindAddress = Upnp.getLanIp();
            if (passiveAddress == null || passiveAddress.isEmpty())
                passiveAddress = Upnp.getWanIp();
        } else {
            try {
                bindAddress = InetAddress.getLocalHost().getHostAddress();

                URLConnection urlConnection = new URL("http://checkip.amazonaws.com/").openConnection();
                urlConnection.setRequestProperty("User-Agent",
                        "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:19.0) Gecko/20100101 Firefox/19.0");

                BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                passiveAddress = reader.readLine();

                reader.close();
            } catch (IOException e) {
                ComputerCraftFileServer.log
                        .warning("ComputerCraftFileServer has encountered a problem while resolving the LAN and/or external IP address, using 0.0.0.0");
                e.printStackTrace();
            }
        }

        if (bindAddress == null || bindAddress.isEmpty())
            bindAddress = "0.0.0.0";

        ComputerCraftFileServer.log.info("Binding to " + bindAddress);
        ComputerCraftFileServer.log.info("Using external IP address " + passiveAddress + " with passive ports "
                + passivePorts);
        ComputerCraftFileServer.log.info("Max users: " + maxLogins);
        ComputerCraftFileServer.log.info("Max file/storage size: " + maxFileSize + "/" + maxStorageSize + " bytes");

        ftpServer = new FtpServer(bindAddress, bindPort, passiveAddress, passivePorts, maxLogins, maxFileSize,
                maxStorageSize);
    }

    /**
     * Starts the FTP server.
     */
    public void startFtp() {
        if (ComputerCraftFileServer.modMinecraftUpnpAvailable) {
            if (Config.autoPortForwardingEnabled) {
                ComputerCraftFileServer.log.info("Registering FTP port " + Config.ftpPort + " to MinecraftUPnP");
                Upnp.registerPort(Config.ftpPort);
            }
        }

        try {
            ftpServer.startServer();
            ComputerCraftFileServer.log.info("FTP server started");
        } catch (FtpException e) {
            ComputerCraftFileServer.log
                    .severe("ComputerCraftFileServer has encountered a problem while starting the FTP server");
            e.printStackTrace();
        }
    }

    /**
     * Stops the FTP server.
     */
    public void stopFtp() {
        ftpServer.stopServer();
        ComputerCraftFileServer.log.info("FTP server stopped");

        if (ComputerCraftFileServer.modMinecraftUpnpAvailable) {
            if (Config.autoPortForwardingEnabled) {
                ComputerCraftFileServer.log.info("Unregistering FTP port " + Config.ftpPort + " from MinecraftUPnP");
                Upnp.unregisterPort(Config.ftpPort);
            }
        }
    }
}
