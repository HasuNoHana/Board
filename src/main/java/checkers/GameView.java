package checkers;

import javax.swing.*;
import java.awt.*;

public class GameView extends JFrame {
    private final CheckersController controller;
    private final JPanel gamePanel;
    public PicturePanel[][] fieldsPanels;

    public GameView() {
        super("Checkers");
        this.setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        controller = new CheckersController(new GameModel(), this);

        gamePanel = new JPanel();
        int BOARDSIZE = controller.getBoardSideSize();
        gamePanel.setLayout(new GridLayout(BOARDSIZE,BOARDSIZE));
        this.fieldsPanels = new PicturePanel[BOARDSIZE][BOARDSIZE];
        for(int i=0; i<BOARDSIZE;i++){
            for(int j=0; j<BOARDSIZE;j++){
                PicturePanel field = new PicturePanel(i,j);
                gamePanel.add(field);
                fieldsPanels[i][j] = field;
            }
        }
        controller.play();
        add(gamePanel, BorderLayout.CENTER);
        setVisible(true);
    }

    public void updateView(GameModel model) {
        int BOARDSIZE = controller.getBoardSideSize();

        for (int i = 0; i < BOARDSIZE; i++) {
            for (int j = 0; j < BOARDSIZE; j++) {
                PicturePanel field = new PicturePanel(i,j);
                fieldsPanels[i][j].setField(model.getField(i,j));
            }
        }
        gamePanel.repaint();
    }

    public PicturePanel getPicturePanel(int i,int j){
        return this.fieldsPanels[i][j];
    }
}