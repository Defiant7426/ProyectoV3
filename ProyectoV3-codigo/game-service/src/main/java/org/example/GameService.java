package org.example;

import java.util.*;

public class GameService {

    private MapService mapService; // servicio de mapa
    private PlayerService playerService; // servicio de jugador
    private List<WaveService> waves; // lista de oleadas

    public GameService(){
        this.mapService = new MapService(); // se crea el servicio de mapa
        this.playerService = new PlayerService(); // se crea el servicio de jugador
        this.waves = new ArrayList<>(); // se crea la lista de oleadas
    }

    public void placeTower(TowerService tower, int x, int y){ // insertar torre en la posición x, y
        mapService.placeTower(tower, x, y);
    }

    public void startWave(){ // empezar oleada
        WaveService wave = new WaveService(); // se crea una oleada
        waves.add(wave); // se agrega a la lista de oleadas
        wave.start(); // se inicia
    }

    public void gameState(){ // estado del juego
        System.out.println(mapService); // se imprime el mapa
        System.out.println("Puntuación: " + playerService.getScore()); // se imprime la puntuación
        System.out.println("Vida de la base: " + playerService.getBaseHealth()); // se imprime la vida de la base
    }

    public static void main(String[] args) {

        System.out.println("\nBienvenidos a Tower Defense Game\n");

        GameService gs = new GameService();
        TowerService t1 = new TowerService('B');
        TowerService t2 = new TowerService('B');
        TowerService t3 = new TowerService('B');
        gs.placeTower(t1 ,0 ,0);
        gs.placeTower(t2, 1, 1);
        gs.placeTower(t3, 2, 2);
        gs.startWave();
        gs.gameState();
    }

}
