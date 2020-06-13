package checkers;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;
import java.util.HashMap;

public class ImageRepository {
    private final HashMap<Pawn, Image> pawnDispatch;
    public static ImageRepository INSTANCE = null;

    public static ImageRepository getInstance() {
        return getInstance("White", "Brown");
    }

    public static ImageRepository getInstance(String Player1Color, String Player2Color) {
        if (INSTANCE != null)
            return INSTANCE;
        else {
            INSTANCE = new ImageRepository(Player1Color, Player2Color);
            return INSTANCE;
        }
    }

    private ImageRepository(String Player1Color, String Player2Color) {
        String whiteNormalPath = "/PawnPictures/" + Player1Color + "/Normal.png";
        String whiteQueenPath = "/PawnPictures/" + Player1Color + "/Queen.png";
        String brownNormalPath = "/PawnPictures/" + Player2Color + "/Normal.png";
        String brownQueenPath = "/PawnPictures/" + Player2Color + "/Queen.png";
        String EmptyPath = "/PawnPictures/EmptyImage.png";


        this.pawnDispatch = new HashMap<Pawn, Image>();
        pawnDispatch.put(Pawn.WHITENORMAL, getImageFromPath(whiteNormalPath));
        pawnDispatch.put(Pawn.WHITEQUIEEN, getImageFromPath(whiteQueenPath));
        pawnDispatch.put(Pawn.BROWNNORMAL, getImageFromPath(brownNormalPath));
        pawnDispatch.put(Pawn.BROWNQUIEEN, getImageFromPath(brownQueenPath));
        pawnDispatch.put(Pawn.EMPTY, getImageFromPath(EmptyPath));
    }

    private Image getImageFromPath(String imagePath) {
        Image image = null;
        try {
            image = ImageIO.read(getClass().getResourceAsStream(imagePath));
        } catch (IOException e) {
            System.err.println("Blad odczytu obrazka" + imagePath);
            e.printStackTrace();
        }
        return image;
    }

    public Image getPawnImage(Pawn pawn) {
        return this.pawnDispatch.get(pawn);
    }

}
