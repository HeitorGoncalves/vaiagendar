package vaiagendar.model;

import javax.validation.constraints.NotEmpty;
// import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class ServicosDTO {
    
    // @NotNull(message = "O código é obrigatório")
    private Long id;

    @NotEmpty(message = "O nome é obrigatório")
    private String nome;

    private Double valor;

    
}
