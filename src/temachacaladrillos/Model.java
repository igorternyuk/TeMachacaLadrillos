package temachacaladrillos;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static java.lang.Integer.min;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.Timer;

public class Model implements ActionListener{
    ArrayList<ModelListener> views = new ArrayList<>();
    Random random = new Random();
    GameState gameState = GameState.INITIAL;
    Ball ball = new Ball(Constants.BALL_X, Constants.BALL_Y, Constants.BALL_DX, Constants.BALL_DY, Constants.BALL_DIAMETER);
    Paddle paddle = new Paddle(Constants.PADDLE_X, Constants.PADDLE_Y, Constants.PADDLE_WIDTH, Constants.PADDLE_HEIGHT);
    ArrayList<Brick> bricks = new ArrayList<>();
    Timer timer;
    int score = 0;
    
    public Model() {
        this.timer = new Timer(Constants.TIMER_DELAY, this);
        createBricks();
    }

    private final void createBricks() {
        for (int i = 0; i < Constants.BRICKS_ROW_COUNT; ++i) {
            for (int j = 0; j < Constants.BRICKS_COLUMN_COUNT; ++j) {
                bricks.add(new Brick(50 + j * Constants.BRICK_WIDTH, Constants.FIELD_TOP_BOUND + 35 + i * Constants.BRICK_HEIGHT,
                        Constants.BRICK_WIDTH, Constants.BRICK_HEIGHT));
            }
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        //Timer event
        if (gameState == GameState.PLAY) {
            if (ball.bottom() >= Constants.FIELD_BOTTOM_BOUND) {
                gameState = GameState.DEFEAT;
            } else if(isWin()) {
                gameState = GameState.VICTORY;
            } else {
                if (detectCollision(ball, paddle)) {
                    //Обработка коллизии мячика с ракеткой
                    ball.reflectFromHorizontalWall();
                    /*if(ball.left() + ball.right() <= paddle.left() + paddle.right()){
                        //Отражаем мячик влево
                        ball.pushTopLeft();
                    } else {
                        ball.pushTopRight();
                    }*/
                }
                for (Brick b : bricks) {
                    if (b.isAlive()) {
                        //Обработка коллизии мячика с кирпичем
                        if (detectCollision(b, ball)) {
                            /* double minOverlapX{std::min(fabs(moveable.right() - fixed.left()), fabs(moveable.left() - fixed.right()))};
                               double minOverlapY{std::min(fabs(moveable.bottom() - fixed.top()), fabs(moveable.top() - fixed.bottom()))};
                            */
                            int horizontalOverlap = min(Math.abs(ball.right() - b.left()), Math.abs(ball.left() - b.right()));
                            int verticalOverlap = min(Math.abs(ball.bottom() - b.top()), Math.abs(ball.top() - b.bottom()));
                            if(horizontalOverlap >= verticalOverlap){
                                ball.reflectFromHorizontalWall();
                            } else {
                                ball.reflectFromVerticalWall();
                            }
                            b.hit();
                            if (!b.isAlive()) {
                                score += b.scoreIncrement();
                            }
                        }
                    }
                }
            }
            ball.move();
            this.notifyAllListeners();
        }
    }
    
    public void togglePause(){
        switch (gameState) {
            case INITIAL:                
                timer.start();
                gameState = GameState.PLAY;
                break;
            case PLAY:
                gameState = GameState.PAUSE;
                break;
            case PAUSE:
                gameState = GameState.PLAY;
                break;
            default:
                break;
        }
        this.notifyAllListeners();
    }
    
    public void movePaddleLeft(){
        paddle.moveLeft();
    }
    
    public void movePaddleRight(){
        paddle.moveRight();
    }
        
    public void prepareNewGame(){
        score = 0;
        gameState = GameState.PLAY;
        bricks.clear();
        createBricks();
        ball.reset();
        paddle.reset();
        timer.start();        
    }
    
    public void addListener(ModelListener listener){
        views.add(listener);
    }
    
    public void removeListener(ModelListener listener){
        views.remove(listener);
    }
    
    public void notifyAllListeners(){
        for(ModelListener l : views){
            l.updateView();
        }
    }
    
    public int paddleX(){
        return paddle.left();
    }
    
    public int paddleY(){
        return paddle.top();
    }
    
    public int ballX(){
        return ball.left();
    }
    
    public int ballY(){
        return ball.top();
    }
    
    public int getScore() {
        return score;
    }
    
    public int numBricks(){
        return bricks.size();
    }
    
    public boolean isBrickAlive(int index){
        return bricks.get(index).isAlive();
    }
    public int getBrickX(int index){
        return bricks.get(index).left();
    }
    
    public int getBrickY(int index){
        return bricks.get(index).top();
    }
    
    public Color getBrickColor(int index){
        return bricks.get(index).getColor();
    }
    
    public String getInfoString(){
        String result = null;
        switch(gameState){
            case PAUSE :
                result = Constants.PAUSE_TEXT;
                break;
            case INITIAL :
                result = "Press SPACE";
                break;
            case PLAY :
                result = "";
                break;
            case VICTORY :
                result = Constants.WIN_MESSAGE;
                break;
            case DEFEAT :
                result = Constants.LOSS_MESSAGE;
                 break;
        }
        return result;
    }
    private enum GameState {
        INITIAL,
        PLAY,
        PAUSE,
        VICTORY,
        DEFEAT
    }
    private enum BrickType {
        RED(25, 5),
        YELLOW(50, 10),
        GREEN(75, 15),
        CYAN(100, 20);
        private final int strenth;
        private final int score;

        public int getStrenth() {
            return strenth;
        }

        public int getScore() {
            return score;
        }

        private BrickType(int strenth, int score) {
            this.strenth = strenth;
            this.score = score;
        }
    }
    
    private interface Colliding{
        public int left();
        public int top();
        public int right();
        public int bottom();
        public int width();
        public int height();
    }
    private abstract class RectEntity implements Colliding {
        protected final Rectangle rect;

        public RectEntity(int x, int y, int width, int height) {
            this.rect = new Rectangle(x, y, width, height);
        }
        
        @Override
        public int left(){
            return rect.x;
        }
        @Override
        public int top(){
            return rect.y;
        }
        @Override
        public int right(){
            return rect.x + rect.width;
        }
        @Override
        public int bottom(){
            return rect.y + rect.height;
        }
        @Override
        public int width(){
            return rect.width;
        }
        @Override
        public int height(){
            return rect.height;
        }
    }
    
    private class Paddle extends RectEntity{
        private final int VELOCITY = 20;
        public Paddle(int x, int y, int width, int height) {
            super(x, y, width, height);
        }
        
        public void reset(){
            rect.setLocation(Constants.PADDLE_X, Constants.PADDLE_Y);
        }
        public void moveLeft(){
            if(this.left() > Constants.FIELD_LEFT_BOUND + VELOCITY){
                rect.setLocation(rect.x - VELOCITY, rect.y);
            }
        }
        
        public void moveRight(){
            if(this.right() < Constants.FIELD_RIGHT_BOUND - VELOCITY){
                rect.setLocation(rect.x + VELOCITY, rect.y);
            }
        }
    }
    
    private class Brick extends RectEntity {
        private final BrickType type;
        private final int HIT_DAMAGE = 25;
        private int health;
        private final Color color;
        public Brick(int x, int y, int width, int height){
            super(x, y, width, height);
            int randNum = random.nextInt(1000);
            if(randNum < 250){
                type = BrickType.RED;
                color = Color.RED;
            } else if(randNum < 500){
                type = BrickType.YELLOW;
                color = Color.YELLOW;
            } else if(randNum < 750){
                type = BrickType.GREEN;
                color = Color.GREEN;
            } else {
                type = BrickType.CYAN;
                color = Color.CYAN.darker();
            }
            health = type.getStrenth();
        }

        public Color getColor() {
            return color;
        }
        
        public void hit(){
           health -= HIT_DAMAGE;
        }
        
        public int scoreIncrement(){
            return type.getScore();
        }
        
        public boolean isAlive(){
            return health > 0;
        }
    }

    private class Ball implements Colliding{
        private int x, y, dx, dy;
        private final int diameter;

        public Ball(int x, int y, int dx, int dy, int diameter) {
            this.x = x;
            this.y = y;
            this.dx = dx;
            this.dy = dy;
            this.diameter = diameter;
        }

        @Override
        public int left() {
            return this.x;
        }

        @Override
        public int top() {
            return this.y;
        }

        @Override
        public int right() {
            return this.x + this.diameter;
        }

        @Override
        public int bottom() {
            return this.y + this.diameter;
        }

        @Override
        public int width() {
            return this.diameter;
        }

        @Override
        public int height() {
            return this.diameter;
        }
        
        public void reset(){
            x = Constants.BALL_X;
            y = Constants.BALL_Y;
            dx = Constants.BALL_DX;
            dy = Constants.BALL_DY;
        }
        
        public void move(){
            bounceBalls();
            x += dx;
            y += dy;
        }
        
        public void reflectFromVerticalWall(){
            dx = -dx;
        }
        
        public void reflectFromHorizontalWall(){
            dy = -dy;
        }
        
        public void pushTopLeft(){
            dx = -Math.abs(dx);
            dy = -Math.abs(dy);
        }
        
        public void pushTopRight(){
            dx = +Math.abs(dx);
            dy = -Math.abs(dy);
        }
        private void bounceBalls(){
           if(this.left() <= Constants.FIELD_LEFT_BOUND || this.right() >= Constants.FIELD_RIGHT_BOUND){
                this.reflectFromVerticalWall();
           }
           if(this.top() <= Constants.FIELD_TOP_BOUND){
               this.reflectFromHorizontalWall();
           }
        }

    }
    
    private boolean detectCollision(Colliding obj1, Colliding obj2){
        return !(obj1.right() < obj2.left() || obj1.left() > obj2.right() ||
                 obj1.bottom() < obj2.top() || obj1.top() > obj2.bottom());
    }
    
    private boolean isWin(){
        boolean result = true;
        for(Brick b : bricks){
            if(b.isAlive()){
                result = false;
                break;
            }
        }
        return result;
    }

}
