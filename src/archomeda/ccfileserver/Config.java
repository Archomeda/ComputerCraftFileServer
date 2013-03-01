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

import net.minecraftforge.common.Configuration;

/**
 * A class that handles and contains all configurations of ComputerCraftFileServer.
 * 
 * @author Archomeda
 */
public class Config {
    public final static String CATEGORY_FTP = "ftp";
    public final static String CATEGORY_USER = "user";

    private final static String comment_ftpWanIp = "The external IP address of the server. If empty, it will be automatically detected.";
    private final static String comment_ftpLanIp = "The local IP address of the server to bind. If empty, it will be automatically detected.";
    private final static String comment_ftpPort = "The FTP port number.";
    private final static String comment_ftpPassivePortRange = "The FTP passive port range. Needs to be properly forwarded in the router settings (even if auto_portforwarding_enabled is set to true).";
    private final static String comment_ftpMaxLogins = "The maximum number of simultaneous logged in users.";
    private final static String comment_userMaxFileSize = "The maximum size in bytes of each individual file that gets uploaded through FTP.";
    private final static String comment_userMaxStorageSize = "The maximum size in bytes of each computer storage.";
    private final static String comment_autoPortForwardingEnabled = "Enable auto UPnP port forwarding of the FTP port (works only if the mod MinecraftUPnP is installed as well).";
    private final static String comment_defaultWorld = "The default world that the mod should look for computers.";

    public static String ftpWanIp;
    public static String ftpLanIp;
    public static int ftpPort;
    public static String ftpPassivePortRange;
    public static int ftpPassivePortRangeStart;
    public static int ftpPassivePortrangeEnd;
    public static int ftpMaxLogins;
    public static long userMaxFileSize;
    public static long userMaxStorageSize;
    public static boolean autoPortForwardingEnabled;
    public static String defaultWorld;

    /**
     * Loads the configuration of the mod.
     * 
     * @param config
     *            The configuration to load.
     */
    public static void load(Configuration config) {
        try {
            config.load();

            ftpWanIp = config.get(CATEGORY_FTP, "ftp_wanip", "", comment_ftpWanIp).value;
            ftpLanIp = config.get(CATEGORY_FTP, "ftp_lanip", "", comment_ftpLanIp).value;
            ftpPort = config.get(CATEGORY_FTP, "ftp_port", 2221, comment_ftpPort).getInt();
            ftpPassivePortRange = config.get(CATEGORY_FTP, "ftp_passive_portrange", "50000-51000",
                    comment_ftpPassivePortRange).value;
            ftpMaxLogins = config.get(CATEGORY_FTP, "ftp_max_logins", 10, comment_ftpMaxLogins).getInt();

            userMaxFileSize = config.get(CATEGORY_USER, "max_filesize", 1024 * 1024, comment_userMaxFileSize).getInt();
            userMaxStorageSize = config.get(CATEGORY_USER, "max_storagesize", 10 * 1024 * 1024,
                    comment_userMaxStorageSize).getInt();

            autoPortForwardingEnabled = config.get(Configuration.CATEGORY_GENERAL, "auto_portforwarding_enabled", true,
                    comment_autoPortForwardingEnabled).getBoolean(true);
            defaultWorld = config.get(Configuration.CATEGORY_GENERAL, "default_world", "world", comment_defaultWorld).value;

            // FTP passive port range parsing
            String[] ftp_passive_portrange_splitted = ftpPassivePortRange.split("-");
            if (ftp_passive_portrange_splitted.length == 2) {
                ftpPassivePortRangeStart = Integer.parseInt(ftp_passive_portrange_splitted[0].trim());
                ftpPassivePortrangeEnd = Integer.parseInt(ftp_passive_portrange_splitted[1].trim());
            } else {
                ftpPassivePortRangeStart = 50000;
                ftpPassivePortrangeEnd = 51000;
            }
        } catch (Exception e) {
            ComputerCraftFileServer.log
                    .severe("ComputerCraftFileServer has encountered a problem while loading its configuration");
        } finally {
            config.save();
        }
    }
}
