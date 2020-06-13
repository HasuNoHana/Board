package checkers;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

import static checkers.Pawn.WHITENORMAL;

public class GameModel {
    private boolean duringCombo;
    private Field FieldInCombo;

    public boolean isCapture() {
        return isCapture;
    }

    public void setCapture(boolean capture) {
        isCapture = capture;
    }

    private boolean isCapture = false;
    private Field oponent;

    public boolean isGameFinished() {
        return isGameFinished;
    }

    private boolean isGameFinished;
    private Field[][] board;
    private int playerWhitePoints = 0;
    private int playerBrownPoints = 0;
    private final int sideSize = 8;
    private Player currentPlayer = Player.WHITE;

    public int getSideSize() {
        return sideSize;
    }

    public void movePawn(Field oldField, Field newField) {
        newField.setPawn(oldField.getPawn());
        oldField.setPawn(Pawn.EMPTY);
    }

    public GameModel() {
        board = new Field[sideSize][];
        for (int i = 0; i < sideSize; i++) {
            board[i] = new Field[sideSize];
            for (int j = 0; j < sideSize; j++) {
                board[i][j] = createField(i, j);
            }
        }
    }

    private void setFieldColor(Field f, int i, int j) {
        if (i % 2 == 0)
            if (j % 2 == 1)
                f.setColor(Color.DARK_GRAY);
            else
                f.setColor(Color.WHITE);
        else if (j % 2 == 0)
            f.setColor(Color.DARK_GRAY);
        else
            f.setColor(Color.WHITE);
    }

    private void setFieldPawn(Field f, int i, int j) {
        if (f.getColor() == Color.DARK_GRAY) {
            if (i < 2)
                f.setPawn(Pawn.BROWNNORMAL);
            if (i > sideSize - 3)
                f.setPawn(WHITENORMAL);
        }
    }

    private Field createField(int i, int j) {
        Field f = new Field(i, j);
        setFieldColor(f, i, j);
        setFieldPawn(f, i, j);
        return f;
    }


    public Field getField(int i, int j) {
        return board[i][j];
    }

    public boolean isNormalMoveAvaliable(Field oldField, Field newField) {
//        System.out.println("OLdField col: "+oldField.getCol()+" row: "+oldField.getRow());
//        System.out.println("NewField col: "+newField.getCol()+" row: "+newField.getRow());
        return isFieldBlack(newField) &&
                isCurrentPlayerPawn(oldField) &&
                isFieldEmpty(newField) &&
                isMoveDiagonally(oldField, newField) &&
                isNumerOfJumpedFieldsValid(oldField, newField);
    }

    private boolean isFieldBlack(Field newField) {
        return newField.getColor() == Color.DARK_GRAY;
    }

    private boolean isCurrentPlayerPawn(Field oldField) {
        return (currentPlayer == Player.WHITE && (oldField.getPawn() == WHITENORMAL || oldField.getPawn() == Pawn.WHITEQUIEEN)) ||
                (currentPlayer == Player.BROWN && (oldField.getPawn() == Pawn.BROWNNORMAL || oldField.getPawn() == Pawn.BROWNQUIEEN));
    }

    private boolean isFieldEmpty(Field newField) {
        return newField.getPawn() == Pawn.EMPTY;
    }

    private boolean isMoveDiagonally(Field oldField, Field newField) {
        return (Math.abs(oldField.getRow() - newField.getRow()) == Math.abs(oldField.getCol() - newField.getCol()));
    }

    private boolean isNumerOfJumpedFieldsValid(Field oldField, Field newField) {
        if (oldField.isQueen()) {
            return isPathClear(oldField, newField);
        } else { // normal pawn
            return isMoveForward(oldField, newField) && (isDistanceOne(oldField, newField));
        }
    }

    private boolean isQueenCaptureVallid(Field oldField, Field newField) {
        if(oldField.equals(newField)) return false;
        int differenceRow = oldField.getRow() < newField.getRow() ? 1 : -1;
        int differenceCol = oldField.getCol() < newField.getCol() ? 1 : -1;
        int oponentRow = newField.getRow() - differenceRow;
        int oponentCol = newField.getCol() - differenceCol;
        if ( (oponentRow-differenceRow > sideSize-1) && (oponentCol-differenceCol > sideSize-1) &&(!isPathClear(oldField, board[oponentRow - differenceRow][oponentCol - differenceCol])))
            return false;
        if (isNotOponent(oponentRow, oponentCol))
            return false;
        this.isCapture = true;
        this.oponent = board[oponentRow][oponentCol];
        return true;
    }

    private boolean isPathClear(Field oldField, Field newField) {//BLAD
        if(oldField.equals(newField))
            return true;
        int differenceRow = oldField.getRow() < newField.getRow() ? 1 : -1;
        int differenceCol = oldField.getCol() < newField.getCol() ? 1 : -1;
        int currRow = oldField.getRow(), currCol = oldField.getCol();

        while (currCol != newField.getCol()) {
            currRow += differenceRow;
            currCol += differenceCol;
            if (board[currRow][currCol].isOccupied())
                return false;
        }
        return true;
    }

    private boolean isMoveForward(Field oldField, Field newField) {
        if (currentPlayer == Player.WHITE) {
            return (newField.getRow() < oldField.getRow());
        } else {
            return (newField.getRow() > oldField.getRow());
        }
    }

    private boolean isDistanceOne(Field oldField, Field newField) {
        return (Math.abs(oldField.getCol() - newField.getCol()) + Math.abs(oldField.getRow() - newField.getRow()) == 2);
    }

    private boolean isNormalCaptureVallid(Field oldField, Field newField) {
        int differenceRow = oldField.getRow() < newField.getRow() ? 1 : -1;
        int differenceCol = oldField.getCol() < newField.getCol() ? 1 : -1;
        int oponentRow = oldField.getRow() + differenceRow;
        int oponentCol = oldField.getCol() + differenceCol;

        if (isNotOponent(oponentRow, oponentCol))
            return false;
        if (oponentRow + differenceRow != newField.getRow() || oponentCol + differenceCol != newField.getCol())
            return false;
        this.isCapture = true;
        this.oponent = board[oponentRow][oponentCol];
        return true;
    }

    public void destroyOponentsPawn() {
        this.oponent.setPawn(Pawn.EMPTY);
        this.oponent.setOccupied(false);
        if (this.currentPlayer == Player.WHITE)
            this.playerWhitePoints += 1;
        else
            this.playerBrownPoints += 1;
    }

    private boolean isNotOponent(int oponentRow, int oponentCol) {
        if (currentPlayer == Player.WHITE) {
            if (board[oponentRow][oponentCol].getPawn() == Pawn.BROWNQUIEEN || board[oponentRow][oponentCol].getPawn() == Pawn.BROWNNORMAL)
                return false;
        } else {
            if (board[oponentRow][oponentCol].getPawn() == Pawn.WHITEQUIEEN || board[oponentRow][oponentCol].getPawn() == WHITENORMAL)
                return false;
        }
        return true;
    }

    public void changePlayer() {
        currentPlayer = currentPlayer == Player.WHITE ? Player.BROWN : Player.WHITE;
    }

    public void tryMakeQueen(Field newField) {
        if (isEndField(newField)) {
            createQueen(newField);
        }
    }

    private void createQueen(Field newField) {
        if (newField.isWhite())
            newField.setPawn(Pawn.WHITEQUIEEN);
        else if (newField.isBrown())
            newField.setPawn(Pawn.BROWNQUIEEN);
    }

    private boolean isEndField(Field newField) {
        return ((newField.isWhite() && newField.getRow() == 0) ||
                (newField.isBrown() && newField.getRow() == sideSize - 1));
    }

    public boolean canPawnMove(Field Field) {
        //TODO

        return false;
    }

    public boolean makeMove(Field oldField, Field newField) {
        Set<Field> a = getAvaliableCapturesMoves(oldField);

        if (a.contains(newField)) {
            makeCapture(oldField, newField);
            this.duringCombo = isDuringCombo(newField);
            if (this.duringCombo) {
                this.FieldInCombo = newField;
            } else {
                changePlayer();
            }

        } else if (getAvaliableNormalMoves(oldField).contains(newField)) {
            movePawn(oldField, newField);
            changePlayer();
        } else {
            return false;
        }
        tryMakeQueen(newField);
        return true;
    }

    private boolean isDuringCombo(Field newField) {
        return (getAvaliableCapturesMoves(newField).size() > 0);
    }

    private Set<Field> getAvaliableNormalMoves(Field oldField) {
        Set<Field> avaliableNormalMoves = new HashSet<Field>();

        if(this.duringCombo){
            return avaliableNormalMoves;
        }

        for (Field[] fields : board
        ) {
            for (Field field : fields
            ) {
                if (isNormalMoveAvaliable(oldField, field)) {
                    avaliableNormalMoves.add(field);
                }
            }
        }
        return avaliableNormalMoves;
    }

    public Set<Field> getAvaliableMoves(Field field){
        Set<Field> avaliableMoves = getAvaliableCapturesMoves(field);
        avaliableMoves.addAll(getAvaliableNormalMoves(field));
        return avaliableMoves;
    }

    private Set<Field> getAvaliableCapturesMoves(Field oldField) {
        Set<Field> avaliableCaptureMoves = new HashSet<Field>();
        if (this.duringCombo) {
            if (!oldField.equals(this.FieldInCombo)) {
                return avaliableCaptureMoves;
            }
        }
        for (Field[] fields : board
        ) {
            for (Field field : fields
            ) {
                if (isCaptureMoveAvliable(oldField, field)) {
                    avaliableCaptureMoves.add(field);
                }
            }
        }
        return avaliableCaptureMoves;
    }

    private boolean isCaptureMoveAvliable(Field oldField, Field newField) {
        return isFieldBlack(newField) &&
                isCurrentPlayerPawn(oldField) &&
                isFieldEmpty(newField) &&
                isMoveDiagonally(oldField, newField) &&
                isCaptureValid(oldField, newField);

    }

    private boolean isCaptureValid(Field oldField, Field newField) {
        if (oldField.isQueen()) {
            return isPathClear(oldField, newField) && isQueenCaptureVallid(oldField, newField);
        } else { // normal pawn
            return isNormalCaptureVallid(oldField, newField) && isMoveForward(oldField, newField);
            //if want capture backward
            //delete ismoveforward
        }
    }

    private void makeCapture(Field oldField, Field newField) {
        destroyOponentsPawn();
        movePawn(oldField, newField);
        setCapture(false);
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }
}
