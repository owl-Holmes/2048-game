package main.java.game2048.board;

import main.java.game2048.util.GameBoardUtils;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;

import static main.java.game2048.util.Preconditions.checkArgument;

/**
 * Represents the internal state and logic of a {@code 2048} game board.
 * Encapsulates the tile matrix, score tracking and logic, movement logic, merging rules, shifting rules,
 * and the tile generation.
 *
 * <p>This class provides all necessary methods to:
 * <ul>
 *   <li>Query and manipulate the current board state</li>
 *   <li>Execute all the directional shifts and merge operations</li>
 *   <li>Insert new random tiles when the playing shift occur or during the generation of a new board</li>
 *   <li>Determine if the game is failed</li>
 *   <li>Reset the board to its initial state for a new game</li>
 *   <li>Access and get the game information: score ... </li>
 * </ul>
 *
 *
 * @author owl
 */
public final class GameBoard implements GameBoardUtils {


    //*******************************************************************************************
    // Game State Fields and Constants
    //*******************************************************************************************

    /**
     * Representation of the board
     */
    private int[][] board =
            {
                    {0, 0 , 0, 0},
                    {0, 0 , 0, 0},
                    {0, 0 , 0, 0},
                    {0, 0 , 0, 0}
            };

    /**
     * Current score of the game
     */
    private int score = 0;

    /**
     * Represents the four cardinal directions in which tiles can be shifted
     */
    public enum SHIFT {UP, DOWN, RIGHT, LEFT}

    private static final Random RANDOM = new Random();


    //*******************************************************************************************
    // Constructors and Accessors
    //*******************************************************************************************

    /**
     * Initializes a new GameBoard with two random elements inserted
     */
    public GameBoard(){
        addTwoRandomElements();
    }

    /**
     * Returns the current score
     *
     * @return the accumulated score from all merges
     */
    public int getScore(){
        return score;
    }

    /**
     * Gives the tile value at a given position
     *
     * @param line the row index (0–3)
     * @param column the column index (0–3)
     * @return the integer value at the given position (0 if empty)
     * @throws IllegalArgumentException if coordinates are outside board bounds
     */
    public int getElement (int line, int column){
        checkArgument(line >= 0 && line < 4 && column >= 0 && column < 4);
        return board[line][column];
    }


    //*******************************************************************************************
    // Board State Inspection and Information
    //*******************************************************************************************

    /**
     * Checks whether a given cell is within bounds and contains no tile
     *
     * @param line row index
     * @param column column index
     * @return {@code true} if the cell is valid and empty; {@code false} otherwise
     */
    private boolean isEmpty(int line, int column){
        if(line < 0 || column < 0 || line > 3 || column > 3) return false;
        return board[line][column] == 0;
    }

    /**
     * Determines whether the board contains at least one empty cell
     *
     * @return {@code true} if space is available; {@code false} if full
     */
    private boolean hasSpace(){
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if(isEmpty(i,j)) return true;
            }
        }
        return false;
    }

    /**
     * Determines whether a merge is possible at the given position
     *
     * @param line row index
     * @param column column index
     * @param value the value to compare against
     * @return {@code true} if the tile matches {@code value}; {@code false} otherwise
     */
    private boolean canMerge(int line, int column, int value){
        if(line < 0 || column < 0 || line > 3 || column > 3) return false;
        return board[line][column] == value;
    }


    //*******************************************************************************************
    // Tile Insertion Logic
    //*******************************************************************************************

    /**
     * Inserts a number at a given linear position (0–15) if the cell is empty
     *
     * @param positionToAdd a linear index from 0 to 15
     * @return {@code true} if the number was inserted; {@code false} otherwise
     */
    private boolean addNewElement(int positionToAdd){
        if(board[getLinPos(positionToAdd)][getColPos(positionToAdd)] == 0){
            board[getLinPos(positionToAdd)][getColPos(positionToAdd)] = 2;
            return true;
        }
        return false;
    }

    /**
     * Adds two random tiles (with value 2) to the board
     * Each is inserted in a random available position
     * If no valid position is found, retries until two are placed
     */
    private void addTwoRandomElements() {
        for (int i = 0; i < 2; i++) {
            if (hasSpace()) {
                int randomNumber = RANDOM.nextInt(16);
                if (!addNewElement(randomNumber)) {
                    i--;
                }
            }
        }
    }


    //*******************************************************************************************
    // Board Movement and Merge Logic
    //*******************************************************************************************

    /**
     * Shifts all tiles in the specified direction and performs merges accordingly.
     * After the move, two new random tiles are inserted (if space allows it).
     *
     * @param direction the direction in which to shift all tiles
     * @throws IllegalArgumentException if the shift direction is null or unsupported
     */
    public void shift(SHIFT direction){

        switch (direction){
            case UP  -> shiftUP();
            case DOWN  -> shiftDOWN();
            case LEFT  -> shiftLEFT();
            case RIGHT  -> shiftRIGHT();
            default -> throw new IllegalArgumentException();
        }

        addTwoRandomElements();
    }

    /**
     * Executes an upward shift: tiles are moved up, and adjacent matches are merged.
     * Merges are performed top-to-bottom; score is incremented accordingly.
     */
    private void shiftUP(){
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                int line = i;
                int column = j;
                while (isEmpty(--line, column)){
                    board[line][column] = board[line + 1][column];
                    board[line + 1 ][column] = 0;
                }
                if(canMerge(line, column, board[line + 1][column])){
                    board[line][column] *= 2;
                    score += board[line][column];
                    board[line + 1][column] = 0;
                }
            }
        }
    }

    /**
     * Executes a downward shift: tiles are moved down, and adjacent matches are merged.
     * Merges are performed bottom-to-top; score is incremented accordingly.
     */
    private void shiftDOWN(){
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                int line = 3 - i;
                int column = j;
                while (isEmpty(++line, column)){
                    board[line][column] = board[line - 1][column];
                    board[line - 1  ][column] = 0;
                }
                if(canMerge(line, column, board[line - 1][column])){
                    board[line][column] *= 2;
                    score += board[line][column];
                    board[line - 1][column] = 0;
                }
            }
        }
    }

    /**
     * Executes a leftward shift: tiles are moved left, and adjacent matches are merged.
     * Merges are performed left-to-right; score is incremented accordingly.
     */
    private void shiftLEFT(){
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                int line = j;
                int column = i;
                while (isEmpty(line, --column)){
                    board[line][column] = board[line][column + 1];
                    board[line][column + 1] = 0;
                }
                if(canMerge(line, column, board[line][column + 1])){
                    board[line][column] *= 2;
                    score += board[line][column];
                    board[line][column + 1] = 0;
                }
            }
        }

    }

    /**
     * Executes a rightward shift: tiles are moved right, and adjacent matches are merged.
     * Merges are performed right-to-left; score is incremented accordingly.
     */
    private void shiftRIGHT(){
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                int line = j;
                int column = 3 - i;
                while (isEmpty(line, ++column)){
                    board[line][column] = board[line][column - 1];
                    board[line][column - 1] = 0;
                }
                if(canMerge(line, column, board[line][column - 1])){
                    board[line][column] *= 2;
                    score += board[line][column];
                    board[line][column - 1] = 0;
                }
            }
        }
    }


    //*******************************************************************************************
    // Game State Management
    //*******************************************************************************************

    /**
     * Clears the board, resets the score, and adds two initial random tiles.
     */
    public void reset(){
        score = 0;
        board =
                new int[][]{
                        {0, 0, 0, 0},
                        {0, 0, 0, 0},
                        {0, 0, 0, 0},
                        {0, 0, 0, 0}
                };

        addTwoRandomElements();
    }

    /**
     * Determines whether the game is lost (when no more moves are possible)
     *
     * @return {@code true} if no legal moves remain; {@code false} otherwise
     */
    public boolean failedGame(){
        if(!hasSpace()){
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    if(canMerge(i + 1,j, board[i][j])
                            || canMerge(i - 1, j, board[i][j])
                            || canMerge(i, j + 1, board[i][j])
                            || canMerge(i, j - 1, board[i][j])){
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }


    //*******************************************************************************************
    // Debugging and Representation
    //*******************************************************************************************

    /**
     * Returns a string representation of the board
     *
     * @return a formatted matrix and current score
     */
    @Override
    public String toString() {
        return Arrays.stream(board)
                .map(Arrays::toString)
                .collect(
                        Collectors.joining(
                                "\r\n", "", "\r\n -- And the score is: " + getScore() + "\r\n"
                        )
                );
    }
}