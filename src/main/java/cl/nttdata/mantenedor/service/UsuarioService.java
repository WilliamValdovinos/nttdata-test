package cl.nttdata.mantenedor.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import cl.nttdata.mantenedor.entity.UsuarioEntity;

public interface UsuarioService {
    public UsuarioEntity save(UsuarioEntity usuario);

    public List<UsuarioEntity> findAll();

    public Optional<UsuarioEntity> findById(String id);

    public Optional<UsuarioEntity> findByCorreo(String correo);

    public UsuarioEntity update(String id, UsuarioEntity usuario);

    public UsuarioEntity partialUpdate(String id, Map<String, Object> campos);

    public boolean delete(String id);

    public UsuarioEntity deactivate(String id);

}
