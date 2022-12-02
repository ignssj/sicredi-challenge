![logo](https://gitlab.com/personal1938/assets/-/raw/main/sprint3/Challenge_-_Sprint_6.png)

Este é o projeto de API que deve ser automatizado para o desafio da Sprint 6 do Programa de Bolsas Compass.uol - Automação em Java.

## Como clonar o projeto

```bash
# cd diretorio-destino
git clone https://gitlab.com/compassouol-istudio-qa/pbjavat01/challenges06/challengeignacio
```

##  Requisitos
 * Java 8+ JDK deve estar instalado
 * Maven deve estar instalado e configurado no path da aplicação
 
## Como executar a aplicação 

Na raiz do projeto, através de seu Prompt de Commando/Terminal/Console execute o comando 

```bash
mvn clean spring-boot:run
```

A aplicação estará disponível através da URL [http://localhost:8080](http://localhost:8080)

## Como executar os testes 

Na raiz do projeto, através de seu Prompt de Commando/Terminal/Console execute o comando 

```bash
mvn clean test -Denv=local
```

## Como gerar report dos testes

Na raiz do projeto, através de seu Prompt de Commando/Terminal/Console execute o comando 

```bash
# Gera os reports
mvn allure:report
# Levanta o servidor Allure
mvn allure:serve
```

# Agradecimentos
Agradeço à Compass.uol por todo o conteúdo disponibilizado e pela oportunidade de participar do Programa de Bolsas.

# Autor
 - Ignacio Pineiro Garcia  
https://github.com/ignssj
