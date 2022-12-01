package datafactory;

import com.github.javafaker.Faker;
import lombok.Getter;
import lombok.Setter;
import models.Simulacao;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class DynamicFactory {
    private static Random gerador = new Random();

    private static Faker faker = new Faker(new Locale("pt-BR"));
    @Getter
    @Setter
    private static ArrayList<String> cpfRestricoes = new ArrayList<String>();

    public static String retornaCpfSemRestricao(){ // retorna um cpf de 10 digitos
        String cpf = "";
        for(int i=0;i<10;i++){
            cpf = cpf+Integer.toString(gerador.nextInt(9));
        }
        if(cpfRestricoes.contains(cpf)){ // garanto que o cpf não é restrito
            cpf = retornaCpfSemRestricao();
        }
        return cpf;
    }

    public static String retornaCpfComRestricao(){ // retorna um aleatorio dentro da lista
        return cpfRestricoes.get(gerador.nextInt(9));
    }

    public static String retornaCpf(){ // retorna um cpf de 10 digitos
        String cpf = "";
        for(int i=0;i<10;i++){
            cpf = cpf+Integer.toString(gerador.nextInt(9));
        }
        return cpf;
    }

    public static Simulacao retornaSimulacao(){
        Simulacao simulacao = new Simulacao(faker.name().firstName(), DynamicFactory.retornaCpf(),faker.internet().emailAddress(),Integer.parseInt(faker.numerify("2###")),Integer.parseInt(faker.numerify("3#")),true);
        return simulacao;
    }

    public static Simulacao retornaSimulacaoVazia(){
        Simulacao simulacao = new Simulacao();
        return simulacao;
    }
}
