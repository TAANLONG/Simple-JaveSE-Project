import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Random;

/**
 * 五子棋游戏主类
 * @author Long Tan tanlong@csu.edu.cn
 * @version 1.0
 *
 */

public class GoBangGame {
    ChessBoard board;
    private static final int WIN = 5;

    public GoBangGame(ChessBoard board) {
        this.board = board;
    }

    public GoBangGame() {
        board = new ChessBoard ( );
    }

    /**
     * 开始游戏
     * （1）初始化棋盘
     * (2) 从键盘持续获得用户的输入
     * (3) 判断当前用户的输入是否胜利
     * (4) 获得电脑玩家的输入(随机落子) 并判断是否胜利
     * (5) 任何一方获得胜利则立即结束游戏，由用户选择是否重开。
     * (6) 如果棋盘已满 仍未分出胜负，则平局。
     *
     * @throws Exception
     */
    public void start() throws Exception {
        BufferedReader br = null;
        String inputStr = null;
        board.initBoard ( );
        board.printBoard ( );
        boolean over = false;
        br = new BufferedReader (new InputStreamReader (System.in));
        System.out.println ("开始游戏！");
        System.out.print ("请输入您的落子位置:");
        while ((inputStr = br.readLine ( )) != null) {
            if (!isValid (inputStr)) {
                System.out.print ("请以(x,y)的格式输入坐标：");
                continue;
            }
            String[] positions = inputStr.split (",");
            int x = Integer.parseInt (positions[0]) - 1;
            int y = Integer.parseInt (positions[1]) - 1;
            String chess = null;
            String playchess = Chess.BLACK.getChess ( );
            String computerChess = Chess.WHITE.getChess ( );
            String equal = "EQUAL";
            boolean fu = board.isFull ( );
            if (!board.isFull ( )) {
                board.setChess_board (x, y, playchess);
                if (isWin (x, y, playchess)) {
                    over = true;
                    chess = playchess;
                } else {
                    if (!board.isFull ( )) {
                        int[] cPositions = computerPlay ( );
                        String cPlay = Chess.WHITE.getChess ( );
                        board.setChess_board (cPositions[0], cPositions[1], cPlay);
                        if (isWin (cPositions[0], cPositions[1], computerChess)) {
                            over = true;
                            chess = computerChess;
                        }
                        board.printBoard ( );
                    } else {
                        over = true;
                        chess = equal;
                    }
                }
            } else {
                over = true;
                chess = equal;
            }
            if (isOver (over, chess)) {
                if (isReplay ( )) {
                    board.initBoard ( );
                    board.printBoard ( );
                    over = false;
                }else {
                    System.out.println ("退出游戏！");
                    System.exit (0);
                }
            }


        }

    }

    /**
     * 判断用户的数入是否合法
     *
     * @param inputStr 用户的输入字符串
     * @return
     */
    public boolean isValid(String inputStr) {
        //棋盘已满 结束游戏 平局！
        if (board.isFull ( )) {
            return true;
        }

        String[][] chess_board = board.getChess_board ( );
        String[] positions;
        int x;
        int y;

        try {
            positions = inputStr.split (",");
            x = Integer.parseInt (positions[0]) - 1;
            y = Integer.parseInt (positions[1]) - 1;
        } catch (Exception e) {
            board.printBoard ( );
            System.out.print ("输入的坐标格式有误！");
            return false;
        }
        if (positions.length != 2) {
            board.printBoard ( );
            System.out.println ("输入的坐标格式有误！positions.length = " + positions.length);
            return false;
        }

        if (x < 0 || y < 0 || x > ChessBoard.BOARD_SIZE - 1 || y > ChessBoard.BOARD_SIZE - 1) {
            board.printBoard ( );
            System.out.println ("输入的坐标超过了棋盘范围: (x,y) = " + x + "," + y + " 要求：0< x < " + (ChessBoard.BOARD_SIZE - 1) +
                    ", 0 < y < " + (ChessBoard.BOARD_SIZE - 1));
            return false;
        }

        if (chess_board[x][y] != "+") {
            board.printBoard ( );
            System.out.print ("该位置已有棋子！");
            return false;
        }


        return true;
    }

    public int[] computerPlay() {
        Random random = new Random ( );
        int x;
        int y;
        String[][] chess_board = board.getChess_board ( );
        int[] pos = new int[2];
        int count = 0;
        while (true) {
            x = random.nextInt (ChessBoard.BOARD_SIZE);
            y = random.nextInt (ChessBoard.BOARD_SIZE);
            count++;
            if (chess_board[x][y] == "+") {
                pos[0] = x;
                pos[1] = y;
                break;
            }
            //避免陷入死循环
            if (count >= ChessBoard.BOARD_SIZE * ChessBoard.BOARD_SIZE) {
                pos[0] = ChessBoard.BOARD_SIZE;
                pos[1] = ChessBoard.BOARD_SIZE;
            }
        }
        return pos;
    }

    public boolean isReplay() throws Exception {
        BufferedReader br = new BufferedReader (new InputStreamReader (System.in));
        String continueGame = br.readLine ( );
        if (continueGame.equals ("y")) {
            return true;
        }
        return false;
    }

    public boolean isOver(boolean over, String chess) {
        if (over == true) {
            if (chess.equals (Chess.BLACK.getChess ( ))) {
                board.printBoard ();
                System.out.print ("恭喜您获得胜利！是否继续游戏?(y/n):");
            } else if (chess.equals (Chess.WHITE.getChess ( ))) {
                board.printBoard ();
                System.out.print ("很遗憾，您输了！是否继续游戏?(y/n):");

            } else if (chess.equals ("EQUAL")) {
                board.printBoard ();
                System.out.println ("平局！是否继续游戏?(y/n):");
            }
            return true;

        }
        return false;
    }

    /**
     * 判断当前操作是否胜利
     * 通过判断水平方向、竖直方向、主对角线、副对角线四条直线上是否存在连续的五个相同点
     *
     * @param posX      水平方向坐标
     * @param posY      竖直方向坐标
     * @param playChess 棋子类型 ：X/O
     * @return
     */
    public boolean isWin(int posX, int posY, String playChess) {
        String[][] chess_board = board.getChess_board ( );

        int wincount = 0;

        int startX = (posX - WIN + 1) > 0 ? (posX - WIN + 1) : 0;
        int endX = (posX + WIN - 1) < (ChessBoard.BOARD_SIZE - 1) ? (posX + WIN - 1) : (ChessBoard.BOARD_SIZE - 1);
        int startY = (posY - WIN + 1) > 0 ? (posY - WIN + 1) : 0;
        int endY = (posY + WIN - 1) < (ChessBoard.BOARD_SIZE - 1) ? (posY + WIN - 1) : (ChessBoard.BOARD_SIZE - 1);
        try{


        //从左往右搜索
        for (int i = startX; i < endX; i++) {
            if (chess_board[i][posY] == playChess && chess_board[i + 1][posY] == playChess) {
                wincount++;
            } else if (wincount < WIN - 1) {
                wincount = 0;
            } else {
                break;
            }
        }
        //左上到右下
        int j = endY;
        for (int i = startX; i < endX; i++) {
            if (chess_board[i][j] == playChess && chess_board[i + 1][j - 1] == playChess) {
                wincount++;
            } else if (wincount < WIN - 1) {
                wincount = 0;
            } else {
                break;
            }
            if (j >0){
                j--;
            }else {
                break;
            }

        }
        //从左下到右上
        j = startY;
        for (int i = startX; i < endX; i++) {
            if (chess_board[i][j] == playChess && chess_board[i + 1][j + 1] == playChess) {
                wincount++;
            } else if (wincount < WIN - 1) {
                wincount = 0;
            } else {
                break;
            }
            if (j < ChessBoard.BOARD_SIZE){
                j++;
            }else {
                break;
            }
        }
        //从下到上

        for (int i = startY; i < endY; i++) {
            if (chess_board[posX][i] == playChess && chess_board[posX][i + 1] == playChess) {
                wincount++;
            } else if (wincount < WIN - 1){
                wincount = 0;
            }else {
                break;
            }

        }
        }catch (Exception e){

        }


       return  wincount >= WIN -1?true:false;

    }

    public static void main(String[] args) {
        GoBangGame go = new GoBangGame ( );
        try {
            go.start ( );
        } catch (Exception e) {
            e.printStackTrace ( );
        }

    }


}
