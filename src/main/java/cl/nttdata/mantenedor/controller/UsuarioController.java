package cl.nttdata.mantenedor.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import cl.nttdata.mantenedor.dto.UsuarioRespuestaDTO;
import cl.nttdata.mantenedor.entity.UsuarioEntity;
import cl.nttdata.mantenedor.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/usuarios")
@Tag(name = "Usuarios", description = "Operaciones para gestionar usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Value("${validacion.contrasena}")
    private String regexContrasena;

    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /* GET - Init */

    @GetMapping
    @Operation(summary = "Listar todos los Usuarios", description = "Permite listar todos los usuarios almacenados en la base de datos")
    public List<UsuarioEntity> getListado() {
        return usuarioService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Permite Obtener un Usuario mediante su ID", description = "Permite obtener un usuario único mediante su ID, en caso de no encontrarse retorna error 404")
    public UsuarioEntity getUsuario(@PathVariable String id) {
        log.info("getUsuario: {}", id);
        Optional<UsuarioEntity> res = usuarioService.findById(id);
        if (!res.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no Encontrado");
        }
        return res.get();
    }

    /* GET - End */

    /* POST - Init */
    @PostMapping
    @Operation(summary = "Crear un nuevo usuario", description = "Permite crear un usuario. No requiere autenticación.")
    public ResponseEntity<UsuarioRespuestaDTO> guardar(@RequestBody @Valid UsuarioEntity usuario) {
        log.info("guardar {}", usuario);
        if (usuarioService.findByCorreo(usuario.getCorreo()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El correo ya está registrado");
        }
        if (!usuario.getContrasena().matches(regexContrasena)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Contraseña Inválida");
        }
        UsuarioRespuestaDTO respuesta = new UsuarioRespuestaDTO(usuarioService.save(usuario));
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }
    /* POST - End */

    /* PUT - Init */
    @PutMapping("/{id}")
    @Operation(summary = "Modificación Completa de un Usuario", description = "Permite Modificar toda la información de un Usuario")
    public UsuarioEntity modificarUsuario(@PathVariable String id, @RequestBody @Valid UsuarioEntity usuario) {
        log.info("modificar {}", id);
        return usuarioService.update(id, usuario);
    }
    /* PUT - End */

    /* PATCH - Init */

    @PatchMapping("/{id}")
    @Operation(summary = "Modificación Parcial de un Usuario", description = "Permite Modificar parcialmente la información de un Usuario")
    public UsuarioEntity modificarParcial(@PathVariable String id, @RequestBody Map<String, Object> campos) {
        return usuarioService.partialUpdate(id, campos);
    }
    /* PATCH - End */

    /* DELETE - Init */
    @DeleteMapping("/{id}")
    @Operation(summary = "Elimina Físicamente un Usuario de la Base de Datos", description = "Permite Eliminar de forma física un Usuario de la Base de Datos (Esta operación NO tiene vuelta atrás)")
    public ResponseEntity<Map<String, String>> eliminar(@PathVariable String id) {
        usuarioService.delete(id);
        return ResponseEntity.ok(Map.of("mensaje", "Usuario eliminado correctamente"));
    }

    @DeleteMapping("/{id}/desactivar")
    @Operation(summary = "Elimina Lógicamente un Usuario", description = "Permite Desactivar de forma lógica un Usuario de la Base de Datos.")
    public UsuarioEntity desactivar(@PathVariable String id) {
        return usuarioService.deactivate(id);
    }

    /* DELETE - End */

}
