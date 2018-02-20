package model;


public enum Shape {

    Square(new int[][]{{1,0},{0,0},{0,1},{1,1}}),
    SShape(new int[][]{{-1,-1},{0,0},{0,-1},{1,0}}),
    JShape(new int[][]{{0,1},{0,0},{0,-1},{-1,-1}}),
    ZShape(new int[][]{{-1,0},{0,0},{0,-1},{1,-1}}),
    TShape(new int[][]{{-1,0},{0,0},{1,0},{0,1}}),
    LShape(new int[][]{{-1,-1},{0,0},{0,-1},{0,1}}),
    IShape(new int[][]{{-1,0},{0,0},{0,1},{0,2}});


    private Shape(int [][] shape){
        this.shape = shape;
        pos = new int[4][2];
        reset();
    }

    public void reset(){
        for(int i = 0; i< pos.length; i++)
           pos[i] = shape[i].clone();
    }

    public final int[][] pos, shape;


}
