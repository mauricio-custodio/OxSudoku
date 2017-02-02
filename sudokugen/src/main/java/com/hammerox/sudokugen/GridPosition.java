package com.hammerox.sudokugen;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class GridPosition {


    public static int[] getPosition(int index) {
        int row = index / 9 + 1;
        int col = index % 9 + 1;
        return new int[] {row, col};
    }


    public static int getIndex(int row, int col) {
        return 9 * (row - 1) + col - 1;
    }

    public static Box getBox(int row, int col) {
        if (isBetween(row, 1, 3)) {
            return getTopBox(col);
        } else if (isBetween(row, 4, 6)) {
            return getCenterBox(col);
        } else if (isBetween(row, 7, 9)) {
            return getBottomBox(col);
        }
        throw new IndexOutOfBoundsException();
    }

    private static Box getTopBox(int col) {
        if (isBetween(col, 1, 3)) {
            return Box.TOP_LEFT;
        } else if (isBetween(col, 4, 6)) {
            return Box.TOP_CENTER;
        } else if (isBetween(col, 7, 9)) {
            return Box.TOP_RIGHT;
        }
        throw new IndexOutOfBoundsException();
    }

    private static Box getCenterBox(int col) {
        if (isBetween(col, 1, 3)) {
            return Box.CENTER_LEFT;
        } else if (isBetween(col, 4, 6)) {
            return Box.CENTER_CENTER;
        } else if (isBetween(col, 7, 9)) {
            return Box.CENTER_RIGHT;
        }
        throw new IndexOutOfBoundsException();
    }

    private static Box getBottomBox(int col) {
        if (isBetween(col, 1, 3)) {
            return Box.BOTTOM_LEFT;
        } else if (isBetween(col, 4, 6)) {
            return Box.BOTTOM_CENTER;
        } else if (isBetween(col, 7, 9)) {
            return Box.BOTTOM_RIGHT;
        }
        throw new IndexOutOfBoundsException();
    }

    private static boolean isBetween(int x, int lower, int upper) {
        return lower <= x && x <= upper;
    }

    // Not yet tested









    public static List<Integer> getRowColBoxIndexes(int row, int col, Boolean includeClickedPosition) {
        List<Integer> indexes = new ArrayList<>();
        // Box
        List<Integer> boxList = getBoxList(row, col, includeClickedPosition);
        indexes.addAll(boxList);
        // Row
        List<Integer> rowList = getRowIndexes(row, col, includeClickedPosition);
        indexes.addAll(rowList);
        // Column
        List<Integer> colList = getColIndexes(row, col, includeClickedPosition);
        indexes.addAll(colList);
        // Removing duplicates
        HashSet hashSet = new HashSet();
        hashSet.addAll(indexes);
        indexes.clear();
        indexes.addAll(hashSet);

        return indexes;
    }


    public static List<Integer> getRowColBoxIndexes(int index, Boolean includeClickedPosition) {
        int[] position = getPosition(index);
        int row = position[0];
        int col = position[1];
        return getRowColBoxIndexes(row, col, includeClickedPosition);
    }


    public static List<Integer> getBoxList(int row, int col, Boolean includeClickedPosition) {
        int clickedIndex = getIndex(row, col);
        int[] boxIndexes = getBox(row, col).index;
        List<Integer> indexes = new ArrayList<>();

        for (int i : boxIndexes) {
            Boolean isToAdd = (clickedIndex != i) || includeClickedPosition;
            if (isToAdd) indexes.add(i);
        }
        return indexes;
    }


    public static List<Integer> getRowIndexes(int row, int col, Boolean includeClickedPosition) {
        List<Integer> rowIndexes = new ArrayList<>();
        for (int r = 1; r <= 9; r++) {
            Boolean isToAdd = (r != row) || includeClickedPosition;
            if (isToAdd) {
                int i = getIndex(r, col);
                rowIndexes.add(i);
            }
        }
        return rowIndexes;
    }


    public static List<Integer> getColIndexes(int row, int col, Boolean includeClickedPosition) {
        List<Integer> colIndexes = new ArrayList<>();
        for (int c = 1; c <= 9; c++) {
            Boolean isToAdd = (c != col) || includeClickedPosition;
            if (isToAdd) {
                int i = getIndex(row, c);
                colIndexes.add(i);
            }
        }
        return colIndexes;
    }


    public static int getCellId(int row, int col) {
        String idString = "" + row + col;
        return Integer.valueOf(idString);
    }


    public static int getCellId(int index) {
        int[] position = getPosition(index);
        int row = position[0];
        int col = position[1];
        String idString = "" + row + col;
        return Integer.valueOf(idString);
    }


    public static int getPencilId(int row, int col, int number) {
        String idString = "1" + number + row + col;
        return Integer.valueOf(idString);
    }


    public static int getPencilId(int index, int number) {
        int[] position = getPosition(index);
        int row = position[0];
        int col = position[1];
        return getPencilId(row, col, number);
    }


    public static int getIdFromIndex(int index) {
        int[] position = getPosition(index);
        int row = position[0];
        int col = position[1];
        return getCellId(row, col);
    }


    public enum Box {

        TOP_LEFT(new int[]{ 0,  1,  2,  9, 10, 11, 18, 19, 20}),
        TOP_CENTER(new int[]{ 3,  4,  5, 12, 13, 14, 21, 22, 23}),
        TOP_RIGHT(new int[]{ 6,  7,  8, 15, 16, 17, 24, 25, 26}),
        CENTER_LEFT(new int[]{27, 28, 29, 36, 37, 38, 45, 46, 47}),
        CENTER_CENTER(new int[]{30, 31, 32, 39, 40, 41, 48, 49, 50}),
        CENTER_RIGHT(new int[]{33, 34, 35, 42, 43, 44, 51, 52, 53}),
        BOTTOM_LEFT(new int[]{54, 55, 56, 63, 64, 65, 72, 73, 74}),
        BOTTOM_CENTER(new int[]{57, 58, 59, 66, 67, 68, 75, 76, 77}),
        BOTTOM_RIGHT(new int[]{60, 61, 62, 69, 70, 71, 78, 79, 80});

        public final int[] index;

        Box(int[] index) {
            this.index = index;
        }
    }

}
