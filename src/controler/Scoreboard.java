package controler;


class Scoreboard {

    static final int MAX_LEVEL = 9;

    private int level;
    private int score;
    private int topScore;
    private boolean gameOver = true;
    private int lines;

    void reset() {
        setTopscore();
        lines = level = score =0;
        gameOver = false;
    }

    void setTopscore() {
        if(score > topScore)
            topScore = score;
    }

    void setGameOver(){
        gameOver = true;
    }

    boolean isGameOver() {
        return gameOver;
    }

    void addScore(int score) {
        score+= score;
    }

    void addLines(int line) {
        switch (line) {
            case 1:
                addScore(10);
                break;
            case 2:
                addScore(20);
                break;
            case 3:
                addScore(30);
                break;
            case 4:
                addScore(40);
                break;
                default:
                    return;
        }

        lines += line;
        if(lines > 10) {
            addLevel();
        }
    }

    void addLevel() {
        lines %= 10;
        if(level < MAX_LEVEL)
            level++;
    }

    int getSpeed() {
        switch (level) {
            case 0:
                return 700;
            case 1:
                return 600;
            case 2:
                return 500;
            case 3:
                return 400;
            case 4:
                return 300;
            case 5:
                return 200;
            case 6:
                return 100;
            default:
                return 100;


        }
    }

    int getLevel() {
        return level;
    }

    int getScore() {
        return score;
    }

    int getLines() {
        return lines;
    }
}
