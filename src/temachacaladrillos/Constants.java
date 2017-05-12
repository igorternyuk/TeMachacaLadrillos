package temachacaladrillos;

public class Constants {
    public static final int WINDOW_WIDTH = 700, WINDOW_HEIGHT = 600;
    public static final int BRICK_WIDTH = 55, BRICK_HEIGHT = 30;
    public static final int BRICKS_ROW_COUNT = 5, BRICKS_COLUMN_COUNT = 11;
    public static final int BALL_DIAMETER = 20;
    public static final int BALL_X = 335, BALL_Y = 450, BALL_DX = -1, BALL_DY = -2;
    public static final int PADDLE_WIDTH = 140, PADDLE_HEIGHT = 10, PADDLE_X = (WINDOW_WIDTH - PADDLE_WIDTH) / 2, PADDLE_Y = 500;
    public static final int TIMER_DELAY = 10;
    public static final int FIELD_LEFT_BOUND = 5, FIELD_RIGHT_BOUND = WINDOW_WIDTH - 7;
    public static final int FIELD_TOP_BOUND = 50, FIELD_BOTTOM_BOUND = 510;
    public static final String WIN_MESSAGE = "YOU WON!!!";
    public static final String LOSS_MESSAGE = "YOU LOST!";
    public static final String PAUSE_TEXT = "GAME PAUSED";
}
