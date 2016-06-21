package util;

import java.io.Serializable;

/**
 * enum that takes care of all
 * actions that possibly can be
 * sent to or from the server
 * 
 * @author Magnus
 */
public enum SendableAction implements Serializable{
	LOGIN,
	LOGOUT,
	SEARCH_PLAYER,
	SIGN_UP,
	GAME_DATA,
	MESSAGE,
	PLAYERS_ONLINE,
	PLACE_WORD,
	START_GAME,
	QUIT_GAME,
	PASS,
	SWAP,
	GET_DICTIONARIES,
	OPPONENT_DATA,
	MAIN_THREAD,
	INVITE_GAME
}