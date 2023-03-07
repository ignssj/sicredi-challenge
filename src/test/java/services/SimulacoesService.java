package services;

import datafactory.DynamicFactory;
import models.Simulacao;
import template.TemplateBase;


public class SimulacoesService extends TemplateBase {

    public static Simulacao retornaSimulacao(){
        Simulacao simulacao = new Simulacao(DynamicFactory.retornaNomeAleatorio(), DynamicFactory.retornaCpf(),DynamicFactory.retornaEmailAleatorio(),DynamicFactory.retornaValorValido(),DynamicFactory.retornaParcelasValidas(),true);
        return simulacao;
    }

    public static Simulacao retornaSimulacaoVazia(){
        Simulacao simulacao = new Simulacao();
        return simulacao;
    }
}
