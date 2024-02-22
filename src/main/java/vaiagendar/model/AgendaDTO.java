package vaiagendar.model;

import java.util.Date;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class AgendaDTO {
    
    // @NotNull(message = "O código é obrigatório")
    private Long id;

    @NotEmpty(message = "O nome é obrigatório")
    private String nome;

    @NotEmpty(message = "O whatsapp é obrigatório")
    private String whatsapp;

    @NotNull(message = "O serviço é obrigatório")
    private Long servico;

    @NotNull(message = "A data e hora é obrigatório")
    private Date dataHora;

    private String mensagem;
}
