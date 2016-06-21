package model;

import java.util.Observable;
import java.util.Observer;

import model.data.ClientGameDataObject;

import util.ResponseObject;
import util.SendObject;
import util.SendableAction;
import util.WordObject;

import controller.NetworkController;

public class GameModel extends Observable implements Observer {

	private NetworkController cc;

	public GameModel() {
		cc = NetworkController.getInstance("");
		cc.addObserver(this);
	}

	public void sendPassRequest(int gameID) {
		SendObject object = new SendObject(SendableAction.PASS, null);
		cc.send(object);
	}

	public void sendPlaceWordRequest(int gameID, WordObject w) {
		SendObject object = new SendObject(SendableAction.PLACE_WORD, new ClientGameDataObject(null, null, w, -1, gameID));
		cc.send(object);
	}

	public void sendQuitGameRequest(int gameID) {
		SendObject object = new SendObject(SendableAction.QUIT_GAME, gameID);
		cc.send(object);
	}

	public void sendSwapRequest(int gameID, int nrLetters) {
		SendObject object = new SendObject(SendableAction.SWAP, new ClientGameDataObject(null, null, null, nrLetters,gameID));
		cc.send(object);
	}

	@Override
	public void update(Observable arg0, Object o) {
		ResponseObject r = ((ResponseObject) o);
		if (r.getAction() == SendableAction.PASS
				|| r.getAction() == SendableAction.PLACE_WORD
				|| r.getAction() == SendableAction.QUIT_GAME
				|| r.getAction() == SendableAction.SEARCH_PLAYER
				|| r.getAction() == SendableAction.START_GAME
				|| r.getAction() == SendableAction.SWAP) {
			setChanged();
			notifyObservers(r.getObject());
		}
	}
}