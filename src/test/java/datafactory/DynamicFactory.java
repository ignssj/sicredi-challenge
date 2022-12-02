package datafactory;

import com.github.javafaker.Faker;
import java.util.Locale;


public class DynamicFactory {


    public static Faker faker = new Faker(new Locale("pt-BR"));


    public static String retornaNomeAleatorio(){
        return faker.name().firstName();
    }

    public static String retornaEmailAleatorio(){
        return faker.internet().emailAddress();
    }

    public static String retornaCpf(){ // retorna um cpf de 10 digitos
        return faker.numerify("###########");
    }

    public static int retornaValorValido(){ // retorna um cpf de 10 digitos
        return Integer.parseInt(faker.numerify("2###"));
    }

    public static int retornaParcelasValidas(){ // retorna um cpf de 10 digitos
        return Integer.parseInt(faker.numerify("2#"));
    }
}
