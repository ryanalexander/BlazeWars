/*
 *
 * *
 *  *
 *  * © Stelch Games 2019, distribution is strictly prohibited
 *  *
 *  * Changes to this file must be documented on push.
 *  * Unauthorised changes to this file are prohibited.
 *  *
 *  * @author Ryan Wood
 *  * @since 14/7/2019
 *
 */

package com.stelch.games2.BlazeWars;

import com.stelch.games2.BlazeWars.Utils.Game.BlockHandler;
import com.stelch.games2.BlazeWars.Utils.Game.start;
import com.stelch.games2.BlazeWars.Utils.TeamManager;
import com.stelch.games2.BlazeWars.varables.gameState;
import com.stelch.games2.BlazeWars.varables.gameType;
import com.stelch.games2.core.API;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

public class Game {

    // Game Title
    private String title;

    // Game Type
    private gameType gameType;

    // TeamManager instance
    private TeamManager teamManager;

    // Minimum players for game to start (Naturally)
    private int min_players;

    // Max players the game will allow
    private int max_players;

    // Handler for game
    private Plugin handler;

    // Block Handler
    private BlockHandler blockHandler;

    // Default map for game
    private World map;

    // Current gameState
    private gameState gameState;

    // Should the game autostart
    private boolean AutoStart;

    /**
     *
     * @param title What will the game be called?
     * @param gameType Game type from gameType enum
     * @param min_players Minimum players for game to start (Naturally)
     * @param max_players Max players the game will allow
     * @param handler Handler for game
     * @param map Default map for game
     * @since 14/07/2019
     *
     */
    public Game(String title, gameType gameType, int min_players, int max_players, Plugin handler, World map){
        this.title=title;
        this.gameType=gameType;
        this.min_players=min_players;
        this.max_players=max_players;
        this.handler=handler;
        this.map=map;
        this.teamManager=new TeamManager();
        this.blockHandler=new BlockHandler();
        this.gameState= gameState.LOBBY;
        this.AutoStart =true;
        this.apiPush();
    }

    /**
     * @return are to minimum requirements met for game to start
     * @since 14/07/2019
     */
    public boolean canStart() {
        boolean start=true;
        if(Bukkit.getOnlinePlayers().size()<this.min_players){start=false;}
        if(!this.AutoStart){start=false;}
        return start;
    }

    /**
     *
     * @return Game type from gameType enum
     * @since 14/07/2019
     */
    public com.stelch.games2.BlazeWars.varables.gameType getGameType() {
        return gameType;
    }

    /**
     *
     * @return Max players the game will allow
     * @since 14/07/2019
     */
    public int getMax_players() {
        return max_players;
    }

    /**
     *
     * @return Minimum players for game to start (Naturally)
     * @since 14/07/2019
     */
    public int getMin_players() {
        return min_players;
    }

    /**
     *
     * @return Handler for game
     * @since 14/07/2019
     */
    public Plugin getHandler() {
        return handler;
    }

    /**
     *
     * @return What will the game be called?
     * @since 14/07/2019
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @param gameType Game type from gameType enum
     * @since 14/07/2019
     */
    public void setGameType(com.stelch.games2.BlazeWars.varables.gameType gameType) {
        this.gameType = gameType;
    }

    /**
     *
     * @param max_players Max players the game will allow
     * @since 14/07/2019
     */
    public void setMax_players(int max_players) {
        this.max_players = max_players;
    }

    /**
     *
     * @param min_players Minimum players for game to start (Naturally)
     * @since 14/07/2019
     */
    public void setMin_players(int min_players) {
        this.min_players = min_players;
    }

    /**
     *
     * @param map Set current map for game
     * @since 14/07/2019
     */
    public void setMap(World map) {
        this.map = map;
    }

    /**
     *
     * @param title What will the game be called?
     * @since 14/07/2019
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     *
     * @return Default map for game
     * @since 14/07/2019
     */
    public World getMap() {
        return map;
    }

    /**
     *
     * @return BlockHandler instance
     * @since 14/07/2019
     */
    public BlockHandler getBlockHandler() {
        return this.blockHandler;
    }

    /**
     *
     * @return fetch current GameState
     * @since 14/07/2019
     */
    public gameState getGameState() {
        return this.gameState;
    }

    /**
     *
     * @param gameState from gameState enum, which gameState shall be set
     * @see gameState for options
     * @since 14/07/2019
     */
    public void setGameState(gameState gameState) {
        this.apiPush();
        this.gameState = gameState;
    }

    /**
     *
     * @return will the game autostart when requirements are met
     * @since 14/07/2019
     */
    public boolean isAutoStart() {
        return AutoStart;
    }

    /**
     *
     * @param auto_start should the game autostart when requirements are met
     * @since 14/07/2019
     */
    public void setAutoStart(boolean auto_start) {
        this.AutoStart = auto_start;
    }

    /**
     *
     * @return Game instance of TeamManager
     */
    public TeamManager getTeamManager() {
        return teamManager;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return super.toString();
    }

    // Main game start function
    public void start() {new start(this);}

    private void apiPush() {
        API.setGame(this.title);
        API.setState(this.gameState.name());
    }


}
