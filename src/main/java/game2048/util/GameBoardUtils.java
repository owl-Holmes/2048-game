package main.java.game2048.util;

import static main.java.game2048.util.Preconditions.checkArgument;


/**
 * Utility interface providing helper methods to convert a linear index
 * (0–15) into 2D coordinates (row and column) on a 4×4 game board.
 * <p>
 * Intended to be mixed into classes that operate on a flat or matrix-based
 * board representation.
 *
 *
 * @author owl
 */
public interface GameBoardUtils {

    /**
     * Computes the column index (0–3) from a linear board index (0–15)
     *
     * @param posInBoard the linear index in the 4×4 board (0 ≤ index ≤ 15)
     * @return the column index corresponding to the given linear position
     * @throws IllegalArgumentException if the index is not in the range [0, 15]
     */
    default int getColPos(int posInBoard){
        checkArgument(posInBoard < 16 && posInBoard >= 0);
        return posInBoard % 4;
    }

    /**
     * Computes the row index (0–3) from a linear board index (0–15)
     *
     * @param posInBoard the linear index in the 4×4 board (0 ≤ index ≤ 15)
     * @return the row index corresponding to the given linear position
     * @throws IllegalArgumentException if the index is not in the range [0, 15]
     */
    default int getLinPos(int posInBoard){
        checkArgument(posInBoard < 16 && posInBoard >= 0);
        return posInBoard /4;
    }
}