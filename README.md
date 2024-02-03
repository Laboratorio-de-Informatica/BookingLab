# BookingLab

El proyecto "Reservas de salones para el Laboratorio de Informática" tiene como objetivo simplificar y optimizar la creación de reservas para el laboratorio de informática.

## Comenzando 🚀

Las siguientes instrucciones le permitirán obtener una copia del proyecto en funcionamiento en su máquina local para fines de desarrollo y prueba.

### Requisitos 📋

* [Git](https://git-scm.com/) - Control de versiones
* [Maven](https://maven.apache.org/) - Manejador de dependencias
* [Java](https://www.oracle.com/java/technologies/downloads/#java17) - Lenguaje de programación
* [Docker](https://www.docker.com/) - Contenedor de aplicaciones

> [!IMPORTANT]
> Es necesario tener instalado Git, Maven, Docker y Java 17 para poder ejecutar el proyecto con efectos de desarrollo.

### Instalación 🔧

Realice los siguientes pasos para clonar el proyecto en su máquina local.

```bash
git clone https://github.com/Laboratorio-de-Informatica/BookingLab.git
cd BookingLab/

```

## Ejecutando la aplicación ⚙️

Para ejecutar la aplicación, ejecute el siguiente comando para crear un contenedor con MySQL debe asegurarse que docker esté instalado y en ejecución:

```bash
docker run -p 3306:3306 --name some-mysql -e MYSQL_ROOT_PASSWORD=my-secret-pw -d mysql:latest

```

> [!NOTE]  
> El anterior comando creará un contenedor con MySQL en el puerto 3306, el nombre del contenedor será `some-mysql` y la contraseña del usuario root será `my-secret-pw`.

```bash
mvn clean spring-boot:run

```

El anterior comando compilará y ejecutará la aplicación, espere a que la aplicación se inicie y muestre un mensaje similar al siguiente:

Diríjase a su navegador de preferencia y vaya a la siguiente dirección: [http://localhost/login.xhtml](http://localhost/login.xhtml) para ver la aplicación en funcionamiento.

## Descripción de la aplicación 📖

...

## Versionado 📌

![BookingLab](https://img.shields.io/badge/BookingLab-v1.0.0-blue)

## Dependencias 🛠️

Se está utilizando el framework de Spring Boot para el desarrollo de la aplicación. Las dependencias utilizadas son:

* ![Java Version](https://img.shields.io/badge/Java-v17.0.9-orange)

    La versión de Java indicada es la versión utilizada para el desarrollo de la aplicación.

* ![Maven Version](https://img.shields.io/badge/Maven-v3.9.5-lightgrey)

    Maven es la herramienta utilizada para la construcción del proyecto. La versión de Maven indicada es la versión utilizada para el desarrollo de la aplicación.

* ![Spring Boot Version](https://img.shields.io/badge/SpringBoot-v3.2.2-green)

    Spring Boot es el framework utilizado para el desarrollo de la aplicación. La versión de Spring Boot indicada es la versión utilizada para el desarrollo de la aplicación.

* ![Spring Security Cypto Version](https://img.shields.io/badge/SpringBootSecurity-v6.2.0-blue)

    Spring Security Crypto es la dependencia utilizada para la encriptación de contraseñas. La versión de Spring Security Crypto indicada es la versión utilizada para el desarrollo de la aplicación.

* ![MySQL Conector Version](https://img.shields.io/badge/MySQL-v8.2.0-purple)

    MySQL Conector es la dependencia utilizada para la conexión con la base de datos MySQL. La versión de MySQL Conector indicada es la versión utilizada para el desarrollo de la aplicación.

* ![Join Faces Version](https://img.shields.io/badge/JoinFaces-v5.2.2-red)

    Join Faces es la dependencia utilizada para la integración de PrimeFaces con Spring Boot. La versión de Join Faces indicada es la versión utilizada para el desarrollo de la aplicación.

* ![Prime Faces Version](https://img.shields.io/badge/PrimeFaces-v13.0.4-yellow)

    Prime Faces es la dependencia utilizada para la interfaz gráfica de la aplicación. La versión de Prime Faces indicada es la versión utilizada para el desarrollo de la aplicación.

* ![Embedded Tomcat Version](https://img.shields.io/badge/EmbeddedTomcat-v10.1.18-brown)

    Embedded Tomcat es la dependencia utilizada para el servidor web embebido de la aplicación. La versión de Embedded Tomcat indicada es la versión utilizada para el desarrollo de la aplicación.

* ![Lombok Version](https://img.shields.io/badge/Lombok-v1.18.30-lightblue)

    Lombok es la dependencia utilizada para la generación de getters, setters, constructores y otros métodos. La versión de Lombok indicada es la versión utilizada para el desarrollo de la aplicación.

> [!TIP]
> Si se desea utilizar una versión diferente de las dependencias indicadas, se debe cambiar la versión en el archivo `pom.xml` que se encuentra en la raíz del proyecto. Luego de esto se debe compilar el proyecto de nuevo con Maven.

## Autores ✒️

Equipo de monitores del laboratorio de informática de la Universidad Escuela Colombiana de Ingeniería Julio Garavito 2024-1.

* **Daniel Santanilla** - [ELS4NTA](https://github.com/ELS4NTA)
* **Andres Oñate** - [AndresOnate](https://github.com/AndresOnate)
* **David Valecia** - [DavidVal6](https://github.com/DavidVal6)
* **Angie Mojica** - [An6ie02](https://github.com/An6ie02)

## Licencia 📄

[![License: CC BY-SA 4.0](https://licensebuttons.net/l/by-sa/4.0/88x31.png)](https://creativecommons.org/licenses/by-sa/4.0/)

Este proyecto está bajo la licencia de Creative Commons Reconocimiento-CompartirIgual 4.0 Internacional (CC BY-SA 4.0) para más detalles consulte el archivo [LICENSE](LICENSE)
