package snake.config;

public final class AppConfig {
    private AppConfig() {
    }

    public static final String APP_TITLE = "Snake";

    public static final int BOARD_WIDTH = 10;
    public static final int BOARD_HEIGHT = 10;
    public static final int FOOD_COUNT = 3;
    public static final int TARGET_LENGTH = 20;

    public static final long TICK_MILLIS = 200;
}
