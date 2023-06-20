package vaiagendar.model;

import lombok.Data;

@Data
public class SearchLogsDTO {
    
    private Long id;  

    private String usuario;

    private Long cadastroId;

    private String cadastro;

    private String acao;

    private String conteudo;

}
