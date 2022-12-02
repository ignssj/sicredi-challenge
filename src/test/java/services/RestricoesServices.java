package services;

import datafactory.DynamicFactory;
import lombok.Getter;
import lombok.Setter;
import template.TemplateBase;

import java.util.ArrayList;
import java.util.Random;

public class RestricoesServices extends TemplateBase {

    private static Random gerador = new Random();
    @Getter
    @Setter
    private static ArrayList<String> cpfRestricoes = new ArrayList<String>();

    public static String retornaCpfSemRestricao(){ // retorna um cpf de 10 digitos
        String cpf = DynamicFactory.faker.numerify("###########");
        if(cpfRestricoes.contains(cpf)){ // garanto que o cpf não é restrito
            cpf = retornaCpfSemRestricao();
        }
        return cpf;
    }

    public static String retornaCpfComRestricao(){ // retorna um aleatorio dentro da lista
        return cpfRestricoes.get(gerador.nextInt(9));
    }

}