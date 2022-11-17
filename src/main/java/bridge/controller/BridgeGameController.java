package bridge.controller;

import bridge.BridgeNumberGenerator;
import bridge.BridgeRandomNumberGenerator;
import bridge.BridgeGame;
import bridge.BridgeMaker;
import bridge.BridgeUpDownNumber;
import bridge.exception.BridgeGameValidator;
import bridge.view.InputView;
import bridge.view.OutputView;

import java.util.List;
import java.util.stream.IntStream;

public class BridgeGameController {

    private static final InputView inputView = new InputView();
    private static final OutputView outputView = new OutputView();

    private int bridgeSize;
    List<String> bridge;
    private static BridgeNumberGenerator bridgeNumberGenerator = new BridgeRandomNumberGenerator();
    private BridgeMaker bridgeMaker = new BridgeMaker(bridgeNumberGenerator);
    private BridgeGame bridgeGame = new BridgeGame();
    private BridgeUpDownNumber bridgeUpDownNumber = new BridgeUpDownNumber();

    public static BridgeGameController create(){
        return new BridgeGameController();
    }

    public void start(){
        bridgeSize=0;
        startGame();
        run();
    }

    private void run(){
        outputView.initMap();
        bridge = bridgeMaker.makeBridge(bridgeSize);
        boolean go = true;
        for (int index = 0;index<bridgeSize && go;index++) {
            go = crossBridge(index);
        }
        retryOrEnd(go);
    }

    private boolean crossBridge(int index){
        String moving = inputView.readMoving();
        if(bridgeGame.move(moving,index,bridge)){
            outputView.printMap("O", bridgeUpDownNumber.upOrDown(bridge.get(index)));
            return true;
        }
        outputView.printMap("X", (bridgeUpDownNumber.upOrDown(bridge.get(index))+1)%2);
        return false;
    }

    private void retryOrEnd(boolean go){
        if (!go){
            restartGame();
        }
        if (go) {
            outputView.printResult("성공");
        }
    }

    private void startGame(){
        outputView.printStart();
        createBridgeSize();
    }

    private void createBridgeSize(){
        try{
            bridgeSize = inputView.readBridgeSize();
            BridgeGameValidator.isValidGameNumber(bridgeSize);
        }catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
            createBridgeSize();
        }
    }

    private void restartGame(){
        boolean restart = bridgeGame.retry(inputView.readGameCommand());
        if(restart){
            run();
        }
        outputView.printResult("실패");
    }

}
