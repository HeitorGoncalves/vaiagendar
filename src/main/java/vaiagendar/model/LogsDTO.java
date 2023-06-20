package vaiagendar.model;

import java.util.Date;

import lombok.Data;

@Data
public class LogsDTO {

    private Long id;  

    private String usuario;

    private Date updated;

    private Long cadastroId;

    private String cadastro;

    private String acao;

    private String conteudo;
    
}
