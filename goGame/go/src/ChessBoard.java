/**
 * 棋盘类
 * 提供棋盘的基本操作
 */
public class ChessBoard {
    private String[][] chess_board;
    public static final int BOARD_SIZE = 8;
    private int currentSize  ;
    public ChessBoard() {
        initBoard ( );
    }


    /**
     * 根据给定棋盘初始化
     * @param chess_board
     */
    public ChessBoard(String[][] chess_board) {
        this.chess_board = chess_board;
        for (int i=0;i< ChessBoard.BOARD_SIZE;i++){
            for (int j=0;j<ChessBoard.BOARD_SIZE;j++){
                if (chess_board[i][j] != "+"){
                    currentSize++;
                }
            }
        }

    }

    /**
     *  初始化棋盘
     */


    public void initBoard() {
        chess_board = new String[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                chess_board[i][j] = "+";
            }
        }
        currentSize =0;

    }

    /**
     * 判断棋盘是否已满
     * @return
     */
    public boolean isFull(){
        if (currentSize == ChessBoard.BOARD_SIZE*ChessBoard.BOARD_SIZE){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 将棋盘输出
     */
    public void printBoard() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                System.out.print (chess_board[i][j]);
            }
            System.out.print ("\n");
        }
    }
    /**
     * 设置棋盘，通过此方法，玩家在指定位置下棋。
     * @return
     */

    public void setChess_board(int x, int y, String chess) {
        chess_board[x][y] = chess;
        currentSize ++;

    }


    public String[][] getChess_board(){
        return chess_board;
    }

}
