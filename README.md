# :hammer_and_wrench: Interactive board con web socket seguro

## Arquitecturas de Software (ARSW)

### :pushpin: Daniel Felipe Hernández Mancipe

<br/>

[![Deployed to Heroku](https://www.herokucdn.com/deploy/button.png)](https://nameless-coast-98812.herokuapp.com/)

<br/>

El proyecto es una implementación de un sistema de tablero interactivo que permite a múltiples usuarios dibujar en un espacio compartido mediante el uso de web sockets. Este sistema cumple los siguientes requerimientos:

- Dado a que el espacio es compartido, lo que un usuario dibuje aparece en el tablero de todos los otros usuarios.

- Cada vez que un usuario se conecta al tablero compartido, inicia con un color aleatorio.

- Cuando un usuario oprime el botón de borrar, el tablero se borra para todos los usuarios.

## Getting Started

### Prerequisites

- Java >= 11.x
- Maven >= 3.x
- Git >= 2.x
- JUnit 4.x

### Installing

Simplemente clone el repositorio:

```
git clone https://github.com/danielhndz/ARSW-interactive-board-secure-web-socket.git
```

Luego compile el proyecto con maven:

```
mvn compile
```

Si salió bien, debería tener un **BUILD SUCCESS** verde.

### Using

```
mvn exec:java -Dexec.mainClass="edu.escuelaing.arsw.labs.boardsecurews.AppStarter"
```

Puede conectarse a la aplicación desplegada en [Heroku](https://nameless-coast-98812.herokuapp.com/)

Aunque las imágenes muestran la dirección de la versión anterior del proyecto, el funcionamiento es el mismo y se puede comprobar con la nueva versión desplegada en Heroku. (Puede comparar los links para verificar cuál es la nueva versión y cuál la antigua)

Se conecta un primer usuario:

![](../media/1.png?raw=true)

Se conecta un segundo usuario:

![](../media/2.jpeg?raw=true)

El primer usuario dibuja algo, con un color inicial aleatorio:

![](../media/3.png?raw=true)

El segundo usuario lo ve:

![](../media/4.jpg?raw=true)

El segundo usuario dibuja algo, con un color inicial aleatorio (los números son parte de la edición de la imagen) :

![](../media/5.jpeg?raw=true)

El primer usuario ve lo dibujado por el segundo:

![](../media/6.png?raw=true)

Luego, el segundo usuario limpia el tablero:

![](../media/7.jpeg?raw=true)

Se limpia también para el primer usuario:

![](../media/8.png?raw=true)

## Built With

- [Maven](https://maven.apache.org/) - Dependency Management
- [Git](https://git-scm.com/) - Version Management
- [P5.js](https://p5js.org/reference/) - Visual arts library
- [JUnit4](https://junit.org/junit4/) - Unit testing framework for Java

## Design Metaphor

- Autor: Daniel Hernández
- Última modificación: 28/06/2022

### Class Diagram

Se sigue una arquitectura similar a la versión anterior, pero ahora con el uso de la tecnología web socket, con un `endpoint` en [BoardEndPoint](/src/main/java/edu/escuelaing/arsw/labs/boardws/endpoints/BoardEndPoint.java), quién es el controlador de casi todas las peticiones HTTP y del servicio del tablero. [Board](/src/main/java/edu/escuelaing/arsw/labs/boardws/model/Board.java) modela el tablero junto con [Point](/src/main/java/edu/escuelaing/arsw/labs/boardws/model/Point.java) y [Color](/src/main/java/edu/escuelaing/arsw/labs/boardws/model/Color.java).

![](../media/class_diagram1.png?raw=true)

![](../media/class_diagram2.png?raw=true)

## Authors

- **Daniel Hernández** - _Initial work_ - [danielhndz](https://github.com/danielhndz)

## License

This project is licensed under the GPLv3 License - see the [LICENSE.md](LICENSE.md) file for details

## Javadoc

Para generar Javadocs independientes para el proyecto en la carpeta `/target/site/apidocs` ejecute:

```
mvn javadoc:javadoc
```
