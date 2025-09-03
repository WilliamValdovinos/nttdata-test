
# Mantenedor de Usuarios

Aplicación REST para gestionar usuarios, incluyendo operaciones CRUD completas y autenticación mediante JWT.
---

## Tabla de Contenidos

- [Instalación](#instalación)
- [Uso](#uso)
- [Endpoints API](#endpoints-api)
- [Documentación Swagger](#documentación-swagger)
- [Tecnologías](#tecnologías)
- [Diagramas](#diagramas)
- [Acerca de](#acerca-de)


---

## Instalación 

Clonar el repositorio y ejecutar la aplicación:

```bash
git clone https://github.com/WilliamValdovinos/nttdata-test.git
cd nttdata-test
mvn clean install
mvn spring-boot:run
```
La aplicación correrá en http://localhost:8080

Para modificar la expresión regular de la contraseña, se debe realizar en el valor de la llave "validacion.contrasena" del archivo application.properties

**Nota sobre la Base de Datos**: Se utiliza Presistencia, por lo cual no es necesario crear la base de datos manualmente, se creará al levantar la aplicación.

---
## Uso

Se expone endpoints REST para manejar usuarios. Se requiere un token JWT para todas las operaciones excepto crear un nuevo usuario.

## Endpoints API

|Método | Ruta                | Descripción                     | Requiere JWT|
|-------|---------------------|--------------------------------------------|--|
|POST   |/api/v1/usuarios    | Crear un nuevo Usuario                      |NO|
|GET	|/api/v1/usuarios	  |Listar todos los usuarios                   |SI|
|GET	|/api/v1/usuarios/{id}|	Obtener usuario por ID                     |SI|
|PUT	|/api/v1/usuarios/{id}|	Modificar toda la información de un usuario|SI|
|PATCH	|/api/v1/usuarios/{id}|	Actualización parcial de un usuario	       |SI|
|DELETE	|/api/v1/usuarios/{id}|	Eliminar un usuario físicamente de la BD   |SI|
|DELETE	|/api/v1/usuarios/{id}/desactivar|	Eliminar un usuario	lógicamente de la BD|SI|

**Nota**: El prefijo de cada Endpoint es http://localhost:8080/mantenedor-usuarios
(Esta URL es válida cuando la aplicación se ejecuta localmente en el puerto 8080).


## Documentación Swagger
Toda la API está documentada con Swagger/OpenAPI.  
Puedes visualizar los endpoints, probar las llamadas y ver los modelos de datos en la siguiente URL:

[http://localhost:8080/mantenedor-usuarios/swagger-ui/index.html](http://localhost:8080/mantenedor-usuarios/swagger-ui/index.html)

**Nota**: Esta URL es válida cuando la aplicación se ejecuta localmente en el puerto 8080.

## Tecnologías

* Java 17
* Spring Boot 3
* Hibernate / JPA
* Base de datos H2 (memoria)
* JWT para seguridad
* Lombok
* Spring Boot Validation
* Swagger / OpenAPI
* Maven
* Mockito

## Diagramas

Diagrama de Arquitectura

![Diagrama de Arquitectura](https://www.plantuml.com/plantuml/png/VPF1Rjim38RlUWgYf_XGzBx3K7TRjnR3PiJkQSY1bJb3g28w4frX6FPKUOIysEWaWx1CAM3O0Cd_a_G9las2KQQrD-B44zHnCpaXArD4w0ayGij-icw_2yO4rjBLE-ye2C7YFh9-Gmytq72IDORg2duPW6zFQSBe6EvTGByol_z6wz4xYmSQj6DqOO0xPZccz1dHlk0mhw7aoDvJFFOoCtTMMbSov5HBSUiidKTEvOVrwF_FB6dav8JZp_DOxfHMibDqaZEFF-H9t8clipqAFcFIqJjzpohVet3wrq-23fyzhUPIHvw-SyWrXPnvXElhjoKFy0O-TLq3IzfCb0GMomzj1tNpK9dIj5AAQXJpURyRd3tSVA21hMDJd5H4IPCIQcifCIXYFYui7fkwCgNHZCWUBWqMx5pSLxuAtSr72XGntcpHknuZ2BzGCAzRoRwprpHoIAIdQRz-gOovfT3F_ymV)

Diagrama de Clases
![Diagrama de Clases](https://www.plantuml.com/plantuml/png/dLJjRjim3Fplfz3F16jV80YCOkb-R6YHOaqUW97eZ9ii6XATBIZsxhDhsQdbfDcM7_aude_CyQIx8E2bBcscBOIWjg46J_p92SbHlMIgUTqgCcgXdiMJsqV4SRdpcAAQlKUUeauy17IGLWGj5kmuD8K72hBST43idqTQWVeRh3o29dODzHu4tvOUsL11-b9vQuLAVk0z9LNX7-ZIcK0B7RX1TymMmMM_i-XDFznrS-eIFQUeK7N2UjN1yMVq1z8OLJ-e00USrLrjCLxBN2L0VAIk30ZEoFHDRjI_2LJWXS1kfpeQoXrnMAX7g9OzkDvzHotv9JM35aSoyvEfaLAGCn-Fdqr2MbT2xC0kHxBvwA7LQzHcNUB-vk7Ky5MRIhOM_U3vlYaOyE_O_XL3nIvWX_GjMhJ6872_MVTJbZ6JFdB-Posfr1DuJM2lRkRAOj1IIGvyudhotzxeTHzvFYmsl1wIHE5yCqj11XF2R1vF-ldLyaxGOSz-Ofz68sanxFJBJzaAsTCcMnaGtBGdV1gI-2sI3s3936Hftoz7Te-oia1bc18xl1sAdA0lGACQ4b9n865_LFXBq9bcv0fEwDYEcoYQlvoBV5UBT-tytS1FetvxcoStJZR-6Hd9rDb8k-Kaj-4vbRYKx0wTgKlx1m00)

Diagrama de Packages
![Diagrama de Packages](https://www.plantuml.com/plantuml/png/ZP7TQiCm38Nl_HIc3v1dA7GRR3U3_h1hpL6Ac685MTuee-y-D_DImWgDxdJEuPCvNYS3jTop6o1ym17zAd0JpLemQ7g8XX5RqIP8D1Lcr9N_Te4X9Nz86PHaCrdkxEvn4ked1Rn0g8ZQGS0_qExNSxxCXA5-u4lQZpCxNFFF9J7QvmNt5GT9PAAdcq_OJlO25aOZErtxs2DZ9r4UHtrUKjMM51Ioph0lN_P4RAZN_M34TTlL_4PYHySglk5xtUyVR4t-VUaMqv0n6JpiNmjYZR7DFVy0)

Ejemplo de Diagrama de Secuencia:
![Diagrama de Secuencia](https://www.plantuml.com/plantuml/png/TTB1QWGX30RWkq-HOqjPfjjZ3sLXslEMxlG1mXYAO7M8KPYthqC53rbFekJ_YCPB4cJ9lzxWAf5Xzew2a6ahdDxWEsLa5-SOXAFtnF0AdzVR0XDkRYhdAVyNf8Dm1OLqn1YjTFf6NDnArVsfUulyqCX7HRRIvlLenxveYyb9vBrw2GjfJ9SQ7JxTwwyTV0HniXjBOr5tE7ZA4QblhC87sGPLePVdCynCDMhXgJUKjan9y7svDdmmeGi5U_o3Fm00)


## Acerca de
MIT License Copyright (c) 2025 William Valdovinos

Creado por William Valdovinos, como prueba técnica para Ntt Data
