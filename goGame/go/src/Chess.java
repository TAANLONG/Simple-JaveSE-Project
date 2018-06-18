public enum Chess {

    BLACK("O"),WHITE("X");
    private String chess ;
    private Chess(String chess){
        this.chess = chess;
    }
    public String getChess(){
        return this.chess;
    }

}
