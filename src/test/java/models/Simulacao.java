package models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Simulacao {
    private String nome;
    private String cpf;
    private String email;
    private float valor;
    private int parcelas;
    private boolean seguro;
}
