package cl.nttdata.mantenedor.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.ObjectMapper;

import cl.nttdata.mantenedor.TestSecurityConfig;
import cl.nttdata.mantenedor.entity.UsuarioEntity;
import cl.nttdata.mantenedor.service.UsuarioService;

@SpringBootTest
@TestPropertySource(properties = {
        "validacion.contrasena=^(?=.*[a-z])(?=.*[A-Z])(?=.*\\\\d)(?=.*[!@#$%^&*()_+\\\\-=\\\\[\\\\]{}|;:'\\\",.<>?/]).{8,}$"
})
@Import(TestSecurityConfig.class)
@AutoConfigureMockMvc
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UsuarioService usuarioService;

    @Test
    void listarUsuarios_deberiaRetornar200YLista() throws Exception {
        UsuarioEntity usuario1 = new UsuarioEntity();
        usuario1.setCorreo("juan@gmail.com");
        usuario1.setNombre("Usuario Juan");

        UsuarioEntity usuario2 = new UsuarioEntity();
        usuario2.setCorreo("maria@gmail.com");
        usuario2.setNombre("Usuario Maria");

        when(usuarioService.findAll()).thenReturn(List.of(usuario1, usuario2));

        mockMvc.perform(get("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].correo").value("juan@gmail.com"))
                .andExpect(jsonPath("$[1].correo").value("maria@gmail.com"));
    }

    @Test
    void obtenerUsuario_existente_deberiaRetornar200() throws Exception {
        UsuarioEntity usuario = new UsuarioEntity();
        String id = UUID.randomUUID().toString();
        usuario.setId(id);
        usuario.setCorreo("test@gmail.com");
        usuario.setNombre("Usuario Test");

        when(usuarioService.findById("123")).thenReturn(Optional.of(usuario));

        mockMvc.perform(get("/api/v1/usuarios/123")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.correo").value("test@gmail.com"));
    }

    @Test
    void obtenerUsuario_noExistente_deberiaRetornar404() throws Exception {
        when(usuarioService.findById("28b096b0-40c2-4d2f-8255-ab0041a5a26e")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/usuarios/28b096b0-40c2-4d2f-8255-ab0041a5a26e")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensaje").value("Usuario no Encontrado"));
    }

    @Test
    void crearUsuario_deberiaRetornar201YUsuarioConToken() throws Exception {
        UsuarioEntity request = new UsuarioEntity();
        request.setCorreo("william.valdovinos@gmail.com");
        request.setNombre("William Valdovinos");
        request.setContrasena("MiPass123!");

        UsuarioEntity response = new UsuarioEntity();
        response.setId(UUID.randomUUID().toString());
        response.setCorreo(request.getCorreo());
        response.setNombre(request.getNombre());
        response.setToken("TOKEN_FAKE");
        response.setFechaCreacion(new Date());

        when(usuarioService.save(any(UsuarioEntity.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").value("TOKEN_FAKE"));
    }

    @Test
    void crearUsuario_conPasswordInvalida_deberiaRetornar400() throws Exception {
        UsuarioEntity request = new UsuarioEntity();
        request.setCorreo("william.valdovinos@gmail.com");
        request.setNombre("William Valdovinos");
        request.setContrasena("123");

        when(usuarioService.save(any(UsuarioEntity.class)))
                .thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Contraseña Inválida"));

        mockMvc.perform(post("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("Contraseña Inválida"));
    }

    @Test
    void crearUsuario_sinCorreo_deberiaRetornar400() throws Exception {
        UsuarioEntity request = new UsuarioEntity();
        request.setNombre("William Valdovinos");
        request.setContrasena("MiPass123!");

        when(usuarioService.save(any(UsuarioEntity.class)))
                .thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "El correo es obligatorio"));

        mockMvc.perform(post("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("El correo es obligatorio"));
    }

    @Test
    void actualizarUsuario_valido_deberiaRetornar200() throws Exception {
        String id = "123";

        // request que se enviará al PUT
        UsuarioEntity request = new UsuarioEntity();
        request.setNombre("William Actualizado");
        request.setCorreo("william.valdovinos@gmail.com");
        request.setContrasena("NuevaPass123!");

        // usuario existente encontrado por el service
        UsuarioEntity existente = new UsuarioEntity();
        existente.setId(id);
        existente.setNombre("William");
        existente.setCorreo("william.old@gmail.com");
        existente.setContrasena("MiPass123!");

        // usuario actualizado devuelto por el service
        UsuarioEntity actualizado = new UsuarioEntity();
        actualizado.setId(id);
        actualizado.setNombre(request.getNombre());
        actualizado.setCorreo(request.getCorreo());
        actualizado.setContrasena(request.getContrasena());

        when(usuarioService.findById(id)).thenReturn(Optional.of(existente));
        when(usuarioService.update(eq(id), any(UsuarioEntity.class))).thenReturn(actualizado);

        mockMvc.perform(put("/api/v1/usuarios/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.nombre").value("William Actualizado"))
                .andExpect(jsonPath("$.correo").value("william.valdovinos@gmail.com"));
    }

    @Test
    void actualizarUsuario_noExistente_deberiaRetornar404() throws Exception {
        String id = "UUID-NO-EXISTE";

        UsuarioEntity request = new UsuarioEntity();
        request.setNombre("William");
        request.setCorreo("william.valdovinos@gmail.com");
        request.setContrasena("MiPass123!");

        when(usuarioService.update(eq(id), any(UsuarioEntity.class)))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no Encontrado"));

        mockMvc.perform(put("/api/v1/usuarios/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print()) // imprime status y body en consola
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensaje").value("Usuario no Encontrado"));
    }

    @Test
    void actualizarParcialUsuario_valido_deberiaRetornar200() throws Exception {
        String id = "123";

        Map<String, Object> cambios = Map.of("nombre", "William Parcial");

        UsuarioEntity existente = new UsuarioEntity();
        existente.setId(id);
        existente.setNombre("William");
        existente.setCorreo("william@gmail.com");
        existente.setContrasena("Pass123!");
        existente.setToken("TOKEN_FAKE");

        UsuarioEntity actualizado = new UsuarioEntity();
        actualizado.setId(id);
        actualizado.setNombre("William Parcial"); 
        actualizado.setCorreo("william@gmail.com");
        actualizado.setContrasena("Pass123!");
        actualizado.setToken("TOKEN_FAKE");

        when(usuarioService.partialUpdate(eq(id), anyMap())).thenReturn(actualizado);

        mockMvc.perform(patch("/api/v1/usuarios/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cambios)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.nombre").value("William Parcial"))
                .andExpect(jsonPath("$.correo").value("william@gmail.com"));
    }

    @Test
    void eliminarUsuario_valido_deberiaRetornar200() throws Exception {
        String id = "123";

        when(usuarioService.delete(id)).thenReturn(true);

        mockMvc.perform(delete("/api/v1/usuarios/{id}", id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("Usuario eliminado correctamente"));
    }



}
