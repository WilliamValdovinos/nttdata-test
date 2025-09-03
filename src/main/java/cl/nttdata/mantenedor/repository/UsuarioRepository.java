package cl.nttdata.mantenedor.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.nttdata.mantenedor.entity.UsuarioEntity;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, String> {
    public Optional<UsuarioEntity> findByCorreo(String correo);
}
