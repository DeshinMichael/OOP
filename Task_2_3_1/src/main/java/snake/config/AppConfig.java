package snake.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class AppConfig {

    private static AppConfig instance;

    public static AppConfig getInstance() {
        if (instance == null) {
            load("/config.json");
        }
        return instance;
    }

    public static void load(String path) {
        try (InputStream in = AppConfig.class.getResourceAsStream(path)) {
            if (in == null) {
                instance = new AppConfig();
                return;
            }
            ObjectMapper mapper = new ObjectMapper();
            instance = mapper.readValue(in, AppConfig.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load " + path, e);
        }
    }

    private String appTitle = "Snake";
    private int boardWidth = 100;
    private int boardHeight = 100;
    private int foodCount = 3;
    private int targetLength = 20;
    private long tickMillis = 200;
    private int botCount = 2;

    public AppConfig() {
    }

    public String getAppTitle() { return appTitle; }
    public void setAppTitle(String appTitle) { this.appTitle = appTitle; }

    public int getBoardWidth() { return boardWidth; }
    public void setBoardWidth(int boardWidth) { this.boardWidth = boardWidth; }

    public int getBoardHeight() { return boardHeight; }
    public void setBoardHeight(int boardHeight) { this.boardHeight = boardHeight; }

    public int getFoodCount() { return foodCount; }
    public void setFoodCount(int foodCount) { this.foodCount = foodCount; }

    public int getTargetLength() { return targetLength; }
    public void setTargetLength(int targetLength) { this.targetLength = targetLength; }

    public long getTickMillis() { return tickMillis; }
    public void setTickMillis(long tickMillis) { this.tickMillis = tickMillis; }

    public int getBotCount() { return botCount; }
    public void setBotCount(int botCount) { this.botCount = botCount; }
}
