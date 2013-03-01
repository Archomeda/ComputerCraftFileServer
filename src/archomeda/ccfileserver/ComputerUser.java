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

/**
 * A container that holds certain information about a ComputerCraft user.
 * 
 * @author Archomeda
 */
public class ComputerUser {
    private String worldName;
    private int computerId;

    /**
     * Initializes a new ComputerCraft user.
     * 
     * @param worldName
     *            The world name of where the computer is currently in.
     * @param computerId
     *            The id of the computer.
     */
    public ComputerUser(String worldName, int computerId) {
        if (worldName == null || worldName.isEmpty())
            worldName = Config.defaultWorld;

        this.worldName = worldName;
        this.computerId = computerId;
    }

    /**
     * Parses a ComputerCraft user from a generic username in the form of "[WORLD_NAME].<COMPUTER_ID>".
     * 
     * @param username
     *            The username to parse.
     * @return A new ComputerCraft user based on the given username.
     */
    public static ComputerUser parseUsername(String username) {
        String worldName = null;
        int computerId;

        if (username.contains(".")) {
            String[] usernameSplitted = username.split(".", 2);
            worldName = usernameSplitted[0];
            computerId = Integer.parseInt(usernameSplitted[1]);
        } else {
            computerId = Integer.parseInt(username);
        }

        return new ComputerUser(worldName, computerId);
    }

    /**
     * Gets the world name.
     * 
     * @return The world name.
     */
    public String getWorldName() {
        return worldName;
    }

    /**
     * Sets the world name.
     * 
     * @param worldName
     *            The world name.
     */
    public void setWorldName(String worldName) {
        this.worldName = worldName;
    }

    /**
     * Gets the computer ID.
     * 
     * @return The computer ID.
     */
    public int getComputerId() {
        return computerId;
    }

    /**
     * Sets the computer ID.
     * 
     * @param computerId
     *            The computer ID.
     */
    public void setComputerId(int computerId) {
        this.computerId = computerId;
    }
}
