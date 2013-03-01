# ComputerCraftFTPServer #
ComputerCraftFTPServer is a mod for Minecraft servers that adds the ability for clients to remotely access their ComputerCraft computers through FTP. It's inspired by and a continuation of [my own fork of ComputerCraftFTPd](https://github.com/Archomeda/ComputerCraftFTPd) for MC 1.4.7, only with more settings and features.

Please note that this readme is provided "as is" and may contain incorrect and/or incomplete information. If something is not correct, please file an issue explaining what's wrong or missing.

- [Features](#features)
- [Downloads](#downloads)
- [Setting up the build environment](#setting-up-the-build-environment)
- [Compiling](#compiling)
- [Third party dependencies](#third-party-dependencies)


## Features ##
All features currently available in ComputerCraftFTPServer are listed below. If you want a feature to be added, please submit an issue or fork this repo and do a pull request.

- Provide your players a way to easily manage their files on a ComputerCraft computer through FTP
- Supports multiple worlds as far as ComputerCraft supports it
- Automatically forward the main FTP port on your router (*not* the passive port because of limitations)
- Highly configurable (FTP ports, bind IP address, passive IP address, maximum number of simultaneous users, maximum file and storage sizes)


## Downloads ##
Currently, there are no downloads available yet. Look below if you want to compile it yourself.


## Setting up the build environment ##
*Note: In this section, [Eclipse](http://www.eclipse.org/) will be used as example.*

1. Clone or download the source of ComputerCraftFTPServer.
2. Import the downloaded source into Eclipse.
3. Download the source of [Minecraft Forge](http://files.minecraftforge.net/) build #497 (or the latest build), extract it somewhere (preferably somewhere near the source) and execute `install.bat` (Windows) or `install.sh` (Linux).
4. Compile it once (see below) in order to get all required dependencies.
5. Import the Minecraft project of MCP into Eclipse (can be found in `forge/mcp/eclipse/Minecraft`) and rename it to Forge.
6. Check if the path variable `MCP_LOC` is pointing to the correct directory (`forge/mcp`). If you have linked the Minecraft project, this variable should be set to `${PROJECT_LOC}/../..`, which means it will look two directories up from the project directory.
7. Refresh the projects in order to load and detect their contents properly.


## Compiling ##
*Note: Only tested with Forge #497, it is not guaranteed that it works with other builds of Forge.*

1. Must have followed all steps until step 4 of setting up the build environment (see above).
2. See that you get a recent version of [MinecraftUPnP](https://github.com/Archomeda/MinecraftUPnP) (go to the [dependencies](#dependencies) for more information).
3. Download [Apache Ant](http://ant.apache.org/) and install it somewhere where you can easily find it later.
4. Inside the `ComputerCraftFTPd` directory, create a file named `build.properties` with the following properties:
  - `mcp.home=<relative_path_to_mcp_dir>` (most common: `forge/mcp` or `../forge/mcp`)
  - `version.minecraft=<version>` (Minecraft version, current: 1.4.7)
  - `version=<version>` (ComputerCraftFTPServer version, latest: 1.0)
5. Run `ant build` and let it run for a while. Please note that an active internet connection is required when running this for the first time as it will download Maven first in order to resolve and download the dependencies. After that, it will automatically compile and obfuscate the code.
6. If successful, inside the `build` directory, you should see a new .zip file with your version numbers in its name.

## Dependencies ##
ComputerCraftFTPServer uses [MinecraftUPnP](https://github.com/Archomeda/MinecraftUPnP) to automatically detect the external IP address of the server and forward the main FTP port automatically (passive ports will not be forwarded automatically due to UPnP protocol limitations). This dependency is optional if you only use ComputerCraftFTPServer for your server. If you want to compile however, this dependency is required. Get a recent binary version of MinecraftUPnP (if available) or compile it yourself and place it in the `lib` folder inside the project directory.

### Third party ###
ComputerCraftFTPServer uses a slightly modified version of [Apache FtpServer](http://mina.apache.org/ftpserver-project/index.html) from the Apache MINA Project as its backend FTP server. This dependency will be downloaded automatically when compiling. You can download the source of the modified version of [ftpserver-core](https://dl.dropbox.com/u/678063/mvn-repository/org/apache/ftpserver/ftpserver-core/1.0.6-archomeda/ftpserver-core-1.0.6-archomeda-sources.jar), which is licensed under the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html).