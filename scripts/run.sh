#!/bin/bash

# Caminho para todas as bibliotecas necessárias
CLASSPATH="./out:./libs/amqp-client-5.20.0.jar:./libs/slf4j-api-1.7.36.jar:./libs/slf4j-simple-1.7.36.jar"

# Caminho para o diretório onde estão os arquivos compilados (.class)
OUT_DIR="../out"

# Executa a aplicação
if [ "$1" == "AppBolsaDeValores" ]; then
    java -cp "$CLASSPATH:$OUT_DIR" app.AppBolsaDeValores
elif [ "$1" == "AppBroker" ]; then
    java -cp "$CLASSPATH:$OUT_DIR" app.AppBroker
else
    echo "Uso: $0 {AppBolsaDeValores|AppBroker}"
fi
