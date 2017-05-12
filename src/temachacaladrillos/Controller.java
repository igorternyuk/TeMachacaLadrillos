package temachacaladrillos;

public class Controller {
    private final Model model;
    public Controller(Model model) {
        this.model = model;
    }
    public void movePaddleLeft(){
        this.model.movePaddleLeft();
    }
    
    public void movePaddleRight(){
        this.model.movePaddleRight();
    }
    
    public void startNewGame(){
        this.model.prepareNewGame();
    }
    
    public void pauseGame(){
        this.model.togglePause();
    }
}
