package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class GameServiceTest {

    @Mock
    private MapService mockMapService; // simulamos el servicio de mapa

    @Mock
    private PlayerService mockPlayerService; // simulamos el servicio de jugador

    @InjectMocks
    private GameService gameService; // inyectamos el servicio de juego

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // inicializamos los mocks
    }

    @Test
    void testPlaceTower() {
        TowerService mockTower = mock(TowerService.class); // simulamos la torre
        gameService.placeTower(mockTower, 2, 2); // insertamos la torre en la posición 2, 2
        verify(mockMapService).placeTower(mockTower, 2, 2); // verificamos que se insertó la torre en la posición 2, 2
    }

}
