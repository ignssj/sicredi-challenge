package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

@AllArgsConstructor
@Getter
@NoArgsConstructor
@ToString(includeFieldNames = true)
public class Simulacao {
    private String nome;
    @Setter
    private String cpf;
    private String email;
    @Setter
    private float valor;
    @Setter
    private int parcelas;
    private boolean seguro;
}
