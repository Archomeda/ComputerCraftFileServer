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

import java.io.File;

import org.apache.ftpserver.ftplet.Authentication;
import org.apache.ftpserver.ftplet.AuthenticationFailedException;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.User;
import org.apache.ftpserver.ftplet.UserManager;
import org.apache.ftpserver.usermanager.AnonymousAuthentication;
import org.apache.ftpserver.usermanager.UsernamePasswordAuthentication;

/**
 * A ComputerCraft FTP user manager.
 * 
 * @author Archomeda
 */
public class FtpUserManager implements UserManager {
    private final long maxFileSize;
    private final long maxStorageSize;

    /**
     * Initializes a new FTP user manager.
     */
    public FtpUserManager() {
        this.maxFileSize = 0;
        this.maxStorageSize = 0;
    }

    /**
     * Initializes a new FTP user manager.
     * 
     * @param maxFileSize
     *            The maximum size of a individual file.
     * @param maxStorageSize
     *            The maximum size of the total storage.
     */
    public FtpUserManager(long maxFileSize, long maxStorageSize) {
        this.maxFileSize = maxFileSize;
        this.maxStorageSize = maxStorageSize;
    }

    @Override
    public User authenticate(Authentication authentication) throws AuthenticationFailedException {
        if (authentication instanceof AnonymousAuthentication)
            throw new AuthenticationFailedException("Anonymous logins are not permitted");
        else if (authentication instanceof UsernamePasswordAuthentication) {
            UsernamePasswordAuthentication upAuth = (UsernamePasswordAuthentication) authentication;

            String username = upAuth.getUsername();
            User user = null;
            try {
                user = getUserByName(username);
            } catch (FtpException e) {
                e.printStackTrace();
            }

            if (user == null)
                throw new AuthenticationFailedException(
                        "Invalid username (use <WORLD>.<ID> as username if <ID> is not working)");

            String password = upAuth.getPassword();
            if (user.getPassword().equals(password))
                return user;

            throw new AuthenticationFailedException("Incorrect password");
        }

        throw new AuthenticationFailedException("This authentication type is not supported");
    }

    @Override
    public void delete(String username) throws FtpException {
    }

    @Override
    public boolean doesExist(String username) throws FtpException {
        return doesExist(ComputerUser.parseUsername(username));
    }

    public boolean doesExist(ComputerUser user) throws FtpException {
        // User exist if the home directory is found
        File homeDir = new File(user.getWorldName() + "/computer/" + user.getComputerId());
        return homeDir.exists() && homeDir.isDirectory();
    }

    @Override
    public String getAdminName() throws FtpException {
        return null;
    }

    @Override
    public String[] getAllUserNames() throws FtpException {
        return null;
    }

    @Override
    public User getUserByName(String username) throws FtpException {
        return getUserByName(ComputerUser.parseUsername(username));
    }

    public User getUserByName(ComputerUser user) throws FtpException {
        if (doesExist(user))
            return new FtpUser(user, maxFileSize, maxStorageSize);
        else
            return null;
    }

    @Override
    public boolean isAdmin(String username) throws FtpException {
        return false;
    }

    @Override
    public void save(User user) throws FtpException {
    }
}
