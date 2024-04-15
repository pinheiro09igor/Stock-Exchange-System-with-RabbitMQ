#!/bin/bash

# O caminho para todas as bibliotecas necessárias
CLASSPATH="./libs/amqp-client-5.20.0.jar:./libs/slf4j-api-1.7.36.jar:./libs/slf4j-simple-1.7.36.jar"

# O caminho do diretório de origem
SRC_DIR="./src"

# O diretório de destino
DEST_DIR="./out"

# Cria o diretório de destino se ele não existir
mkdir -p "$DEST_DIR"

# Compila todos os arquivos .java no diretório src com o caminho de classe definido e saída para DEST_DIR
javac -cp "$CLASSPATH" -d "$DEST_DIR" $(find "$SRC_DIR" -name "*.java")

# Verifica se a compilação foi bem-sucedida
if [ $? -eq 0 ]; then
    echo "Compilação bem-sucedida. Arquivos compilados em $DEST_DIR"
else
    echo "Erro durante a compilação."
fi
