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
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.ftplet.AuthorizationRequest;
import org.apache.ftpserver.ftplet.User;
import org.apache.ftpserver.usermanager.impl.FileSizeRequest;
import org.apache.ftpserver.usermanager.impl.StorageSizeRequest;

/**
 * A ComputerCraft FTP user.
 * 
 * @author Archomeda
 */
public class FtpUser extends ComputerUser implements User {
    private final long maxFileSize;
    private final long maxStorageSize;

    /**
     * Initializes a new FTP user from a ComputerCraft user.
     * 
     * @param user
     *            The ComputerCraft user.
     */
    public FtpUser(ComputerUser user) {
        this(user.getWorldName(), user.getComputerId(), Config.userMaxFileSize, Config.userMaxStorageSize);
    }

    /**
     * Initializes a new FTP user from a ComputerCraft user.
     * 
     * @param user
     *            The ComputerCraft user.
     * @param maxFileSize
     *            The maximum size of a individual file.
     * @param maxStorageSize
     *            The maximum size of the total storage.
     */
    public FtpUser(ComputerUser user, long maxFileSize, long maxStorageSize) {
        this(user.getWorldName(), user.getComputerId(), maxFileSize, maxStorageSize);
    }

    /**
     * Initializes a new FTP user.
     * 
     * @param worldName
     *            The world name.
     * @param computerId
     *            The computer ID.
     */
    public FtpUser(String worldName, int computerId) {
        this(worldName, computerId, Config.userMaxFileSize, Config.userMaxStorageSize);
    }

    /**
     * Initializes a new FTP user.
     * 
     * @param worldName
     *            The world name.
     * @param computerId
     *            The computer ID.
     * @param maxFileSize
     *            The maximum size of a individual file.
     * @param maxStorageSize
     *            The maximum size of the total storage.
     */
    public FtpUser(String worldName, int computerId, long maxFileSize, long maxStorageSize) {
        super(worldName, computerId);
        this.maxFileSize = maxFileSize;
        this.maxStorageSize = maxStorageSize;
    }

    @Override
    public AuthorizationRequest authorize(AuthorizationRequest request) {
        if (request instanceof FileSizeRequest) {
            ((FileSizeRequest) request).setMaxFileSize(maxFileSize);
        } else if (request instanceof StorageSizeRequest) {
            ((StorageSizeRequest) request).setMaxStorageSize(maxStorageSize);
        }
        return request;
    }

    @Override
    public List<Authority> getAuthorities() {
        return null;
    }

    @Override
    public List<Authority> getAuthorities(Class<? extends Authority> clazz) {
        return null;
    }

    @Override
    public boolean getEnabled() {
        return true;
    }

    @Override
    public String getHomeDirectory() {
        return new File(getWorldName() + "/computer/" + getComputerId() + "/").getAbsolutePath();
    }

    @Override
    public int getMaxIdleTime() {
        return 0;
    }

    @Override
    public String getName() {
        return getWorldName() + "." + getComputerId();
    }

    @Override
    public String getPassword() {
        String password = null;
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(new File(getHomeDirectory(), "_ftppasswd")));
            password = reader.readLine();
        } catch (Exception e) {
            password = null;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }
        }

        return password;
    }

}
