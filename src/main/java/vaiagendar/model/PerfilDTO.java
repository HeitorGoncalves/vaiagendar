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

    private String trabalho;

    private String link;

    private String sobre;

    private String logo;
}
