package template;

import java.util.ArrayList;
import java.util.Random;


public class TemplateRestricoes extends TemplateBase {
    public static ArrayList<String> cpfRestricoes;

public static String retornaCpfSemRestricao(){ // retorna um cpf de 10 digitos
    Random gerador = new Random();
    String cpf = "";
    for(int i=0;i<10;i++){
        cpf = cpf+Integer.toString(gerador.nextInt(9));
    }
    while(cpfRestricoes.contains(cpf)){ // garanto que o cpf não é restrito
        cpf = retornaCpfSemRestricao();
    }
    return cpf;
}

public static String retornaCpfComRestricao(){ // retorna um aleatorio dentro da lista
    Random gerador = new Random();
    return cpfRestricoes.get(gerador.nextInt(9));
}

}
