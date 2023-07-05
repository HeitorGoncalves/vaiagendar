package vaiagendar.model;

import javax.validation.constraints.NotEmpty;
// import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class PerfilDTO {
    
    // @NotNull(message = "O código é obrigatório")
    private Long id;

    @NotEmpty(message = "O nome é obrigatório")
    private String nome;

    @NotEmpty(message = "O username é obrigatório")
    private String username;

    @NotEmpty(message = "O password é obrigatório")
    private String password;
    
}
