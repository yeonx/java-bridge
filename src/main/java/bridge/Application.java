package bridge;

import bridge.controller.BridgeGameController;

public class Application {
    public static void main(String[] args) {
        BridgeGameController bridgeGameController = BridgeGameController.create();
        bridgeGameController.start();
    }
}
