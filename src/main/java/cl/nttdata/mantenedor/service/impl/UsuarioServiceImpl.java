package cl.nttdata.mantenedor.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import cl.nttdata.mantenedor.entity.UsuarioEntity;
import cl.nttdata.mantenedor.repository.UsuarioRepository;
import cl.nttdata.mantenedor.security.JwtUtil;
import cl.nttdata.mantenedor.service.UsuarioService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UsuarioServiceImpl implements UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final JwtUtil jwtUtil;

    private static final String ERROR_USUARIO_NO_ENCONTRADO = "Usuario no Encontrado";

    @Autowired
    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, JwtUtil jwtUtil) {
        this.usuarioRepository = usuarioRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public UsuarioEntity save(UsuarioEntity usuario) {
        usuario.setToken(jwtUtil.generateToken(usuario.getCorreo()));
        return usuarioRepository.save(usuario);
    }

    @Override
    public List<UsuarioEntity> findAll() {
        return usuarioRepository.findAll();
    }

    @Override
    public Optional<UsuarioEntity> findById(String id) {
        return usuarioRepository.findById(id);
    }

    @Override
    public Optional<UsuarioEntity> findByCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo);
    }

    @Override
    public UsuarioEntity update(String id, UsuarioEntity usuario) {
        Optional<UsuarioEntity> res = findById(id);
        if (!res.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ERROR_USUARIO_NO_ENCONTRADO);
        }
        UsuarioEntity usuarioModificado = res.get();
        usuarioModificado.setNombre(usuario.getNombre());
        usuarioModificado.setCorreo(usuario.getCorreo());
        usuarioModificado.setContrasena(usuario.getContrasena());
        usuarioModificado.setTelefonos(usuario.getTelefonos());
        usuarioModificado.setFechaModificacion(new Date());
        usuarioModificado.setActivo(usuario.isActivo());
        return usuarioRepository.save(usuarioModificado);
    }

    @Override
    public UsuarioEntity partialUpdate(String id, Map<String, Object> campos) {
        Optional<UsuarioEntity> res = this.findById(id);
        if (!res.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ERROR_USUARIO_NO_ENCONTRADO);
        }
        UsuarioEntity usuarioModificado = res.get();
        for (Map.Entry<String, Object> campo : campos.entrySet()) {
            switch (campo.getKey()) {
                case "nombre":
                    usuarioModificado.setNombre((String) campo.getValue());
                    break;
                case "correo":
                    usuarioModificado.setCorreo((String) campo.getValue());
                    break;
                case "contraseña":
                    usuarioModificado.setContrasena((String) campo.getValue());
                    break;
                default:
            }
        }
        return usuarioRepository.save(usuarioModificado);
    }

    @Override
    public boolean delete(String id) {
        if (!this.findById(id).isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ERROR_USUARIO_NO_ENCONTRADO);
        }
        usuarioRepository.deleteById(id);
        return true;
    }

    @Override
    public UsuarioEntity deactivate(String id) {
        Optional<UsuarioEntity> res = this.findById(id);
        if (!res.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ERROR_USUARIO_NO_ENCONTRADO);
        }
        res.get().setActivo(false);
        return usuarioRepository.save(res.get());
    }
}
