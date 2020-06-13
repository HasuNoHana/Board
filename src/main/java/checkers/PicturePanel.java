package checkers;

import javax.swing.*;
import java.awt.*;

//implements MouseListener, MouseMotionListener
public class PicturePanel extends JPanel {
    private final int row;
    private final int col;
    private ImageRepository imageRepository;
    Color background;
    private Pawn pawn;
    private double pawnScale = 0.85;


    public PicturePanel(int row, int col) {
        imageRepository = ImageRepository.getInstance();
        this.row =row;
        this.col = col;
        //        addMouseListener(this);
//        addMouseMotionListener(this);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Image image = imageRepository.getPawnImage(pawn);
        int width = (int) ( this.getWidth() * this.pawnScale);
        int height = (int) (this.getHeight() * this.pawnScale);
        image = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        int x = (this.getWidth() - image.getWidth(null)) / 2;
        int y = (this.getHeight() - image.getHeight(null)) / 2;
        g.drawImage(image, x, y,this);
    }

    public void setField(Field field) {
        this.background = field.getColor();
        this.setBackground(background);
        this.pawn = field.getPawn();
    }

    public int getRow() {
        return this.row;
    }

    public int getCol() {
        return col;
    }

    public void click() {
        this.setBackground(Color.YELLOW);
        //TODO
    }

    public void unclick() {
        this.setBackground(background);
    }

    //
//
//    @Override
//    public void mouseClicked(MouseEvent e) {
////        System.out.println("mouseClicked");
//    }
//
//    @Override
//    public void mouseEntered(MouseEvent e) {
//        System.out.println("mouseEntered");
////        this.setBackground(new Color(0xAF2143));
//    }
//
//    @Override
//    public void mouseExited(MouseEvent e) {
////        System.out.println("mouseExited");
//        this.setBackground(background);
//    }
//
//    @Override
//    public void mousePressed(MouseEvent e) {
////        System.out.println("mousePressed");
//    }
//
//    @Override
//    public void mouseReleased(MouseEvent e) {
////        System.out.println("mouseReleased");
//    }
}