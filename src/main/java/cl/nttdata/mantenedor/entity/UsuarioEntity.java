package cl.nttdata.mantenedor.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.ToString;

@Entity
@Data
@ToString(exclude = "contrasena")
public class UsuarioEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El correo es obligatorio")
    @Pattern(regexp = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$", message = "El correo no cumple con el formato")
    private String correo;

    @JsonProperty("contraseña")
    @NotBlank(message = "La contraseña es obligatoria")
    private String contrasena;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "usuario_id")
    private List<TelefonoEntity> telefonos;

    private Date fechaCreacion;
    private Date fechaModificacion;
    private Date fechaUltimoLogin;
    private String token;
    private boolean activo;

    @PrePersist
    protected void onCreate() {
        fechaCreacion = fechaUltimoLogin = new Date();
    }
}
