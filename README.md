# Proyecto V3

Alumno: De la Cruz Valdiviezo, Pedro Luis David

# Descripción del proyecto: V3

El juego "Tower Defense" es un videojuego de consola donde el jugador debe defender su base de
oleadas de enemigos colocando torres en lugares estratégicos del mapa. El proyecto incluirá el uso de mocks, stubs y fakes para pruebas unitarias y de integración utilizando Mockito y pruebas de mutación.

# Estructura del proyecto

### Clases principales

1. **Microservicio de Juego (GameService):** Maneja la lógica general del juego.
2. **Microservicio de Mapa (MapService):** Gestiona la representación y manipulación del mapa del juego.
3. **Microservicio de Enemigos (EnemyService):** Gestiona la creación y el comportamiento de los enemigos.
4. **Microservicio de Torres (TowerService):** Gestiona la creación y el comportamiento de las torres.
5. **Microservicio de Oleadas (WaveService):** Maneja la lógica de las oleadas de enemigos.
6. **Microservicio de Jugador (PlayerService):** Representa al jugador y sus estadísticas.

### Entrada y salida

**Entrada**:

- Comandos del usuario para colocar torres, iniciar oleadas, etc.
- Datos iniciales del mapa y configuración de juego.

**Salida**:

- Estado del juego después de cada comando.
- Puntuación y estado de salud de la base.

## Detalles del proyecto

# Paso 1: Preparación del entorno

En las Actividades dados por el profesor de configuración de Docker y instalación de minikube ya se realizaron y estan listas para usar, esto se puede corroborar en el siguiente enlace: https://github.com/Defiant7426/CC3S2/tree/master/Actividad-InstalacionDocker

# Paso 2: Desarrollar los microservicios

En este caso, cada clase se comporta como un módulo independiente en microservicios. Por ello, creamos módulos independientes de la siguiente manera:

Eliminamos la carpeta src que se creo por defecto en el proyecto

![Untitled](Proyecto%20V3%2050b8a27667ee4a44847d1a3c03a2a6b6/Untitled.png)

Ahora creamos la carpeta game-service de manera que ahi se encuentra el Servicio de Game:

![Untitled](Proyecto%20V3%2050b8a27667ee4a44847d1a3c03a2a6b6/Untitled%201.png)

## Creando los módulos

En este modulo creamos el archivo [GameService.java](http://GameService.java) y escribimos este código:

```java
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

```

Luego en nuestro build.gradle de este modulo agregamos las dependencias de los demas modulos que crearemos mas adelante así:

```java
plugins {
    id 'java'
}

group = 'org.example'
version = '1.0-SNAPSHOT'

dependencies {
    implementation project(':map-service')
    implementation project(':player-service')
    implementation project(':wave-service')
    implementation project(':tower-service')
    implementation project(':log-service')
}
```

<aside>
💡 No es necesario importar otras dependencias como JUnit o Mockito ya que eso se puede encargar el [gradle.build](http://gradle.build) padre.

</aside>

Igualmente creamos los demás módulos, por lo que la estructura de nuestro proyecto quedaría así:

![Untitled](Proyecto%20V3%2050b8a27667ee4a44847d1a3c03a2a6b6/Untitled%202.png)

Ahora en cada modulo creamos las clases correspondientes y agregamos las dependencias si son necesarias. Las clases de cada modulo estarían de la siguiente manera:

```java
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
```

```java
package org.example;

public class PlayerService {
    
    private int score;
    private int baseHealth;
    
    public PlayerService(){
        this.score = 0; // iniciamos con score 0
        this.baseHealth = 100; // iniciamos con vida 100
    }
    
    public int getScore() { // obtenemos el score actual
        return score;
    }
    
    public int getBaseHealth() { // obtenemos la vida base actual
        return baseHealth;
    }
    
}
```

```java
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
```

# Paso 3: Configurar Docker

## Crear un Dockerfile para cada microservicio

Crearemos un Dockerfile para cada microservicio. Tomaremos como ejemplo el Dockerfile para el componente game-service y lo explicaremos detalladamente.

### game-service

```bash
# Etapa de construcción
FROM gradle:7.3.3-jdk17 AS builder
WORKDIR /home/gradle/src

COPY --chown=gradle:gradle build.gradle /home/gradle/src/

RUN gradle build --no-daemon --refresh-dependencies

COPY --chown=gradle:gradle . /home/gradle/src

RUN gradle build --no-daemon

# Etapa de ejecución
FROM openjdk:17
EXPOSE 8080
WORKDIR /app
COPY --from=builder /home/gradle/src/build/libs/game-service-1.0-SNAPSHOT.jar /app/game-service.jar
CMD ["java", "-jar", "game-service.jar"]
```

Explicación:

**Etapa de Construcción:**

Esta etapa se encarga de compilar y construir la aplicación:

```bash
FROM gradle:7.3.3-jdk17 AS builder
```

Utilizamos una imagen de gradle con el JDK 17. Luego con `AS builder` le da un nombre a esta etapa para referirse a ella en la segunda etapa.

```bash
WORKDIR /home/gradle/src
```

Establece el directorio de trabajo dentro del contenedor en `/home/gradle/src`.

```bash
COPY --chown=gradle:gradle build.gradle /home/gradle/src/
```

Copiamos el archivo `build.gradle` desde nuestra maquina local al contenedor. `--chown=gradle:gradle` aseguramos que es de nuestra propiedad y grupo gradle dentro del contenedor.

```bash
RUN gradle build --no-daemon --refresh-dependencies
```

Ejecuta el comando `gradle build` para construir el proyecto y descargar todas las dependencias necesarias. `--no-daemon` evita que Gradle corra en modo daemon y `--refresh-dependencies` fuerza la actualización de las dependencias.

```bash
COPY --chown=gradle:gradle . /home/gradle/src
```

Copia todos los archivos del proyecto al directorio de trabajo dentro del contenedor.

```bash
RUN gradle build --no-daemon
```

Vuelve a ejecutar el comando `gradle build` para compilar el proyecto con todo el código fuente copiado.

**Etapa de ejecución:**

Esta etapa se encarga de preparar el entorno para ejecutar la aplicación construida:

```bash
FROM openjdk:17
```

Utiliza una imagen oficial de OpenJDK versión 17 para ejecutar la aplicación.

```bash
EXPOSE 8080
```

Indica que el contenedor escuchará en el puerto 8080.

```bash
WORKDIR /app
```

Establece el directorio de trabajo /app

```bash
COPY --from=builder /home/gradle/src/build/libs/game-service-1.0-SNAPSHOT.jar /app/game-service.jar
```

Copia el archivo JAR construido (`game-service-1.0-SNAPSHOT.jar)` desde la etapa de construcción (`builder`) al directorio de trabajo del contenedor en la etapa de ejecución.

```bash
CMD ["java", "-jar", "game-service.jar"]
```

Define el comando que se ejecutará cuando el contenedor se inicie, en este caso, para correr el archivo JAR con `java -jar`.

Igualmente construimos el dockerfile para los otros servicios:

### map-service

```bash
# Etapa de construcción
FROM gradle:7.3.3-jdk17 AS builder
WORKDIR /home/gradle/src

COPY --chown=gradle:gradle build.gradle /home/gradle/src/

RUN gradle build --no-daemon --refresh-dependencies

COPY --chown=gradle:gradle . /home/gradle/src

RUN gradle build --no-daemon

# Etapa de ejecución
FROM openjdk:17
EXPOSE 8080
WORKDIR /app
COPY --from=builder /home/gradle/src/build/libs/map-service-1.0-SNAPSHOT.jar /app/map-service.jar
CMD ["java", "-jar", "map-service.jar"]
```

### player-service

```bash
# Etapa de construcción
FROM gradle:7.3.3-jdk17 AS builder
WORKDIR /home/gradle/src

COPY --chown=gradle:gradle build.gradle /home/gradle/src/

RUN gradle build --no-daemon --refresh-dependencies

COPY --chown=gradle:gradle . /home/gradle/src

RUN gradle build --no-daemon

# Etapa de ejecución
FROM openjdk:17
EXPOSE 8080
WORKDIR /app
COPY --from=builder /home/gradle/src/build/libs/player-service-1.0-SNAPSHOT.jar /app/player-service.jar
CMD ["java", "-jar", "player-service.jar"]
```

### tower-service

```bash
# Etapa de construcción
FROM gradle:7.3.3-jdk17 AS builder
WORKDIR /home/gradle/src

COPY --chown=gradle:gradle build.gradle /home/gradle/src/

RUN gradle build --no-daemon --refresh-dependencies

COPY --chown=gradle:gradle . /home/gradle/src

RUN gradle build --no-daemon

# Etapa de ejecución
FROM openjdk:17
EXPOSE 8080
WORKDIR /app
COPY --from=builder /home/gradle/src/build/libs/tower-service-1.0-SNAPSHOT.jar /app/tower-service.jar
CMD ["java", "-jar", "tower-service.jar"]
```

### wave-service

```bash
# Etapa de construcción
FROM gradle:7.3.3-jdk17 AS builder
WORKDIR /home/gradle/src

COPY --chown=gradle:gradle build.gradle /home/gradle/src/

RUN gradle build --no-daemon --refresh-dependencies

COPY --chown=gradle:gradle . /home/gradle/src

RUN gradle build --no-daemon

# Etapa de ejecución
FROM openjdk:17
EXPOSE 8080
WORKDIR /app
COPY --from=builder /home/gradle/src/build/libs/wave-service-1.0-SNAPSHOT.jar /app/wave-service.jar
CMD ["java", "-jar", "wave-service.jar"]

```

## Construir las imágenes de Docker

Contruimos las imagenes usando nuestro IDE, la salida son las siguientes:

![Untitled](Proyecto%20V3%2050b8a27667ee4a44847d1a3c03a2a6b6/Untitled%203.png)

![Untitled](Proyecto%20V3%2050b8a27667ee4a44847d1a3c03a2a6b6/Untitled%204.png)

![Untitled](Proyecto%20V3%2050b8a27667ee4a44847d1a3c03a2a6b6/Untitled%205.png)

![Untitled](Proyecto%20V3%2050b8a27667ee4a44847d1a3c03a2a6b6/Untitled%206.png)

![Untitled](Proyecto%20V3%2050b8a27667ee4a44847d1a3c03a2a6b6/Untitled%207.png)

# Paso 4: Configurar el docker compose

### Creamos el docker-compose.yml

```bash
version: '3'
services:
  game:
    build: ./game-service
    ports:
      - "8080:8080"
    networks:
      - game-network
    volumes:
        - game-data:/app/data
    depends_on:
      - map
      - player
      - tower
      - wave

  map:
    build: ./map-service
    networks:
      - game-network
    volumes:
      - game-data:/app/data

  player:
    build: ./player-service
    networks:
      - game-network
    volumes:
      - game-data:/app/data

  tower:
    build: ./tower-service
    networks:
      - game-network
    volumes:
        - game-data:/app/data

  wave:
    build: ./wave-service
    networks:
      - game-network
    volumes:
        - game-data:/app/data

networks:
  game-network:
    driver: bridge

volumes:
  game-data:
    driver: local
```

Este archivo define los servicios y la red necesaria para que los microservicios se comuniquen entre sí. Cada servicio se construye desde su respectivo directorio y se conecta a la red `game-network`. Los volúmenes se utilizan para persistir los datos.

### Levantar los servicios definidos en docker-compose

Intentamos levantar los servicios con el comando:

```bash
docker-compose up
```

La salida fue la siguiente:

![Untitled](Proyecto%20V3%2050b8a27667ee4a44847d1a3c03a2a6b6/Untitled%208.png)

![Untitled](Proyecto%20V3%2050b8a27667ee4a44847d1a3c03a2a6b6/Untitled%209.png)

Tratamos de solucionarlo, pero lamentablemente no tuvimos éxito.

# Paso 5: Desplegar en kubernetes

Creamos los archivos de deployment.yaml y service.yaml para cada microservicio:

### Crear Archivo de despliegue

**game-deployment**

```java
apiVersion: apps/v1
kind: Deployment
metadata:
  name: game-deployment
spec:
  replicas: 1
  selector:
    matchLabels:
      app: game-service
  template:
    metadata:
      labels:
        app: game-service
    spec:
      containers:
        - name: game-service
          image: game-service
          ports:
            - containerPort: 8080
```

**map-deployment**

```java
apiVersion: apps/v1
kind: Deployment
metadata:
  name: map-deployment
spec:
  replicas: 1
  selector:
    matchLabels:
      app: map-service
  template:
    metadata:
      labels:
        app: map-service
    spec:
      containers:
        - name: map-service
          image: map-service
          ports:
            - containerPort: 8080
```

**player-deployment**

```java
apiVersion: apps/v1
kind: Deployment
metadata:
  name: player-deployment
spec:
  replicas: 1
  selector:
    matchLabels:
      app: player-service
  template:
    metadata:
      labels:
        app: player-service
    spec:
      containers:
        - name: player-service
          image: player-service
          ports:
            - containerPort: 8080
```

**tower-deployment**

```java
apiVersion: apps/v1
kind: Deployment
metadata:
  name: tower-deployment
spec:
  replicas: 1
  selector:
    matchLabels:
      app: tower-service
  template:
    metadata:
      labels:
        app: tower-service
    spec:
      containers:
        - name: tower-service
          image: tower-service
          ports:
            - containerPort: 8080
```

### Crear archivo de servicio

**game-service**

```java
apiVersion: v1
kind: Service
metadata:
  name: game-service
spec:
  selector:
    app: game-service
  ports:
    - protocol: TCP
      port: 80
      targetPort: 8080
  type: LoadBalancer
```

**map-service**

```java
apiVersion: v1
kind: Service
metadata:
  name: map-service
spec:
  selector:
    app: map-service
  ports:
    - protocol: TCP
      port: 80
      targetPort: 8080
  type: LoadBalancer
```

**player-service**

```java
apiVersion: v1
kind: Service
metadata:
  name: player-service
spec:
  selector:
    app: player-service
  ports:
    - protocol: TCP
      port: 80
      targetPort: 8080
  type: LoadBalancer
```

**tower-service**

```java
apiVersion: v1
kind: Service
metadata:
  name: tower-service
spec:
  selector:
    app: tower-service
  ports:
    - protocol: TCP
      port: 80
      targetPort: 8080
  type: LoadBalancer
```

**wave-service**

```java
apiVersion: v1
kind: Service
metadata:
  name: wave-service
spec:
  selector:
    app: wave-service
  ports:
    - protocol: TCP
      port: 80
      targetPort: 8080
  type: LoadBalancer
```

# Paso 6: Verificar despliegue

Usamos comandos kubectl para verificar que los pods y servicios estén funcionando
correctamente 

```java
kubectl apply -f game-deployment.yaml
kubectl apply -f map-deployment.yaml
kubectl apply -f player-deployment.yaml
kubectl apply -f tower-deployment.yaml
kubectl apply -f wave-deployment.yaml
```

![Untitled](Proyecto%20V3%2050b8a27667ee4a44847d1a3c03a2a6b6/Untitled%2010.png)

```java
kubectl apply -f game-service.yaml
kubectl apply -f map-service.yaml
kubectl apply -f player-service.yaml
kubectl apply -f tower-service.yaml
kubectl apply -f wave-service.yaml
```

![Untitled](Proyecto%20V3%2050b8a27667ee4a44847d1a3c03a2a6b6/Untitled%2011.png)

## Verificar el estado del despliegue

```java
kubectl get pods
kubectl get services
```

![Untitled](Proyecto%20V3%2050b8a27667ee4a44847d1a3c03a2a6b6/Untitled%2012.png)

![Untitled](Proyecto%20V3%2050b8a27667ee4a44847d1a3c03a2a6b6/Untitled%2013.png)

# Paso 7: Implementación de pruebas

Usar Mockito para pruebas unitarias y de integración. Lamentablemente, no pudimos usar Pitest junto con Mockito, por lo cual decidimos solo usar Mockito.

## Agregando Mockito como dependencia:

Para agregar mockito en todos nuestros componentes basta con cambiar nuestro `build.gradle` padre de la siguiente manera:

```bash
plugins {
    id 'java'
}

group = 'org.example'
version = '1.0-SNAPSHOT'

repositories {
    mavenCentral()
}

subprojects {
    apply plugin: 'java'

    repositories {
        mavenCentral()
    }

    dependencies {
        // Use JUnit Jupiter API for unit testing with JUnit 5
        testImplementation 'org.junit.jupiter:junit-jupiter-api:5.8.2'
        testRuntimeOnly 'org.junit.jupiter:junit-jupiter-engine:5.8.2'

        // For backward compatibility with JUnit 4 tests
        testImplementation 'junit:junit:4.13.2'
        testImplementation 'org.junit.vintage:junit-vintage-engine:5.8.2'
        testImplementation 'org.junit.jupiter:junit-jupiter-params:5.8.2'

        testImplementation 'org.assertj:assertj-core:3.23.1'
        testImplementation platform('org.junit:junit-bom:5.10.0')
        testImplementation 'org.junit.jupiter:junit-jupiter'

        implementation platform('org.assertj:assertj-bom:3.25.3')
        implementation 'org.assertj:assertj-core'

        implementation platform('com.google.code.gson:gson:2.10.1')
        implementation 'com.google.code.gson:gson'

        implementation platform('com.fasterxml.jackson:jackson-bom:2.17.0')
        implementation 'com.fasterxml.jackson.core:jackson-databind'

        implementation platform('org.junit:junit-bom:5.11.0-M1')
        implementation 'org.junit.jupiter:junit-jupiter'
        implementation 'org.junit.platform:junit-platform-launcher'
        implementation 'org.junit.vintage:junit-vintage-engine'

        implementation platform('org.mockito:mockito-bom:5.11.0')
        implementation 'org.mockito:mockito-core'
        implementation 'org.mockito:mockito-junit-jupiter'

        implementation platform('com.squareup.retrofit2:retrofit-bom:2.11.0')
        implementation 'com.squareup.retrofit2:retrofit'
        implementation 'com.squareup.retrofit2:converter-gson'

        // https://mvnrepository.com/artifact/com.sun.mail/jakarta.mail
        implementation group: 'com.sun.mail', name: 'jakarta.mail', version: '2.0.1'
    }

    test {
        useJUnitPlatform()
    }
}
```

### Clase de prueba con Mockito

Escribimos el archivo GameServiceGame:

```java
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
```

Corremos el test:

![Untitled](Proyecto%20V3%2050b8a27667ee4a44847d1a3c03a2a6b6/Untitled%2014.png)