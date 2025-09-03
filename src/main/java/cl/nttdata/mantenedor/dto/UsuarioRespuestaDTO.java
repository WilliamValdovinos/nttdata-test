package cl.nttdata.mantenedor.dto;

import java.util.Date;

import cl.nttdata.mantenedor.entity.UsuarioEntity;
import lombok.Data;

@Data
public class UsuarioRespuestaDTO {
    private String id;
    private Date creado;
    private Date modificado;
    private Date ultimoLogin;
    private String token;
    private boolean activo;

    public UsuarioRespuestaDTO(UsuarioEntity usuario){
        this.id = usuario.getId();
        this.creado = usuario.getFechaCreacion();
        this.modificado = new Date();
        this.ultimoLogin = new Date();
        this.token = usuario.getToken();
        this.activo= true;
    }
    
}
