package dataAccess;

import java.util.HashMap;

import model.GameData;

public class MemoryGameDAO {

  HashMap<String, GameData> mapGame = new HashMap<> ();

  public void clear() {
    mapGame.clear();
  }
}
