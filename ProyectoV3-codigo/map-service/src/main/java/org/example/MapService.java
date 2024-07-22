package org.example;

public class MapService {

    private char[][] grid; // mapa del juego

    public MapService(){
        grid = new char[5][5]; // inicializamos el mapa
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 5; j++){
                grid[i][j] = ' '; // llenamos el mapa con espacios en blanco
            }
        }
    }

    public void placeTower(TowerService tower, int x, int y) {
        grid[x][y] = tower.getSymbol(); // colocamos la torre en el mapa
    }

    @Override
    public String toString(){ // imprimimos el mapa
        StringBuilder sb = new StringBuilder();
        for(char[] row : grid){
            for(char cell : row){
                sb.append("[").append(cell).append("]");
            }
            sb.append("\n");
        }
        return sb.toString();
    }


}
