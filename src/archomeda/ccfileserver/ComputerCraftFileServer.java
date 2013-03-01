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

import java.util.logging.Logger;

import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.Mod.ServerStopping;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.network.NetworkMod;

/**
 * The main class for ComputerCraftFileServer. Handles all mod logic.
 * 
 * @author Archomeda
 */
@Mod(modid = "ComputerCraftFileServer", name = "ComputerCraftFileServer", version = "@MOD_VERSION@", dependencies = "after:MinecraftUPnP@[1.0,)")
@NetworkMod(clientSideRequired = false, serverSideRequired = false)
public class ComputerCraftFileServer {
    @Instance("ComputerCraftFileServer")
    public static ComputerCraftFileServer instance;

    @SidedProxy(clientSide = "archomeda.ccfileserver.ClientProxy", serverSide = "archomeda.ccfileserver.Proxy")
    public static Proxy proxy;

    public static Logger log = Logger.getLogger("ComputerCraftFileServer");
    public static boolean modMinecraftUpnpAvailable = false;

    @PreInit
    public void preInit(FMLPreInitializationEvent event) {
        log.setParent(FMLLog.getLogger());

        // Less log spam from Apache FTP server (changed from INFO (default) to WARN)
        org.apache.log4j.Logger.getLogger("org.apache.mina").setLevel(org.apache.log4j.Level.WARN);
        org.apache.log4j.Logger.getLogger("org.apache.ftpserver").setLevel(org.apache.log4j.Level.WARN);

        Config.load(new Configuration(event.getSuggestedConfigurationFile()));
    }

    @Init
    public void init(FMLInitializationEvent event) {
        if (Loader.isModLoaded("MinecraftUPnP")) {
            modMinecraftUpnpAvailable = true;
            log.info("MinecraftUPnP is available, UPnP features enabled");
        } else
            log.warning("MinecraftUPnP is not available, UPnP features disabled");
        
        proxy.initFtp();
    }

    @PostInit
    public void postInit(FMLPostInitializationEvent event) {
        proxy.startFtp();
    }

    @ServerStopping
    public void serverStopping(FMLServerStoppingEvent event) {
        proxy.stopFtp();
    }

}
