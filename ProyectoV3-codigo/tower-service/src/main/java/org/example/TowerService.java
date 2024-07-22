package org.example;

public class TowerService {

    private char symbol; //simbolo de la torre

    public TowerService(char symbol){
        this.symbol = symbol; // asignamos el simbolo de la torre
    }

    public char getSymbol() {
        return symbol; // debolvemos el simbolo de la torre
    }

}