#!/bin/bash

# ============================================
# Afetto — Infraestrutura Sprint 3 (ACR + ACI)
# FIAP 2026 — DevOps Tools & Cloud Computing
# ============================================
#
# Este script documenta TODOS os comandos utilizados para provisionar
# a infraestrutura da Sprint 3 do projeto Afetto.
#
# Pré-requisito: az login já realizado no Cloud Shell / terminal local.
#
# NOTA (Git Bash / Windows): se estiver rodando via Git Bash, prefixe os
# comandos que usam "--scope /subscriptions/..." com MSYS_NO_PATHCONV=1,
# pois o Git Bash converte automaticamente caminhos Unix para Windows,
# corrompendo o parâmetro. Exemplo já incluso abaixo.

set -e  # Interrompe a execução se algum comando falhar

# --- Variáveis ---
RESOURCE_GROUP="rg-afetto-devops"
LOCATION="eastus2"
ACR_NAME="afettodevops"
KEYVAULT_NAME="kv-afetto-devops"

MYSQL_CONTAINER="mysql-afetto"
MYSQL_DNS_LABEL="mysql-afetto-rm564063"
MYSQL_DATABASE="afettodb"

API_CONTAINER="afetto-api"
API_DNS_LABEL="afetto-api-rm564063"
API_IMAGE_TAG="v3.2"

# ============================================
# 1. DESCOBRIR REGIÃO LIBERADA NA ASSINATURA
# ============================================
echo "[1] Verificando regiões liberadas na assinatura..."
az policy assignment list -o table
# Procurar linha "Allowed resource deployment regions" e confirmar LOCATION acima

# ============================================
# 2. RESOURCE GROUP
# ============================================
echo "[2] Criando Resource Group..."
az group create \
  --name $RESOURCE_GROUP \
  --location $LOCATION

# ============================================
# 3. AZURE CONTAINER REGISTRY (ACR)
# ============================================
echo "[3] Criando ACR..."
az provider register --namespace Microsoft.ContainerRegistry

az acr create \
  --resource-group $RESOURCE_GROUP \
  --name $ACR_NAME \
  --sku Standard \
  --location $LOCATION \
  --public-network-enabled true \
  --admin-enabled true

# --- Build local e push da imagem da API (rodar no diretório do projeto) ---
# git clone https://github.com/iagoliziero/api-afetto-v2.git
# cd api-afetto-v2
# docker build -t afetto-api .
# az acr login --name $ACR_NAME
# docker tag afetto-api $ACR_NAME.azurecr.io/afetto-api:$API_IMAGE_TAG
# docker push $ACR_NAME.azurecr.io/afetto-api:$API_IMAGE_TAG

# --- Importar imagem do MySQL do Docker Hub direto para o ACR ---
# (evita rate limit do Docker Hub ao subir o ACI)
echo "[3.1] Importando imagem do MySQL para o ACR..."
az acr import \
  --name $ACR_NAME \
  --source docker.io/library/mysql:8.0 \
  --image mysql:8.0

# Verificar imagens no ACR
az acr repository list --name $ACR_NAME --output table

# ============================================
# 4. KEY VAULT (segredos)
# ============================================
echo "[4] Criando Key Vault..."
az provider register --namespace Microsoft.KeyVault

az keyvault create \
  --name $KEYVAULT_NAME \
  --resource-group $RESOURCE_GROUP \
  --location $LOCATION

# Dar permissão de administrador do vault a si mesmo (RBAC)
# Prefixo MSYS_NO_PATHCONV=1 necessário apenas em Git Bash / Windows
MSYS_NO_PATHCONV=1 az role assignment create \
  --assignee $(az account show --query user.name -o tsv) \
  --role "Key Vault Administrator" \
  --scope /subscriptions/$(az account show --query id -o tsv)/resourceGroups/$RESOURCE_GROUP/providers/Microsoft.KeyVault/vaults/$KEYVAULT_NAME

# Aguardar ~15 segundos para a permissão propagar

# Guardar senha do banco
az keyvault secret set --vault-name $KEYVAULT_NAME --name oracle-password --value "Afetto@2tdspw"

# Guardar credenciais do ACR (para os ACIs autenticarem no registro)
ACR_USERNAME=$(az acr credential show --name $ACR_NAME --query username -o tsv)
ACR_PASSWORD=$(az acr credential show --name $ACR_NAME --query passwords[0].value -o tsv)
az keyvault secret set --vault-name $KEYVAULT_NAME --name acr-username --value "$ACR_USERNAME"
az keyvault secret set --vault-name $KEYVAULT_NAME --name acr-password --value "$ACR_PASSWORD"

# ============================================
# 5. CONTAINER MYSQL (ACI)
# ============================================
echo "[5] Criando container do MySQL..."
az provider register --namespace Microsoft.ContainerInstance

# NOTA: rodamos SEM volume Azure Files. Testamos persistência via Azure
# File Share (CIFS) tanto para Oracle quanto para MySQL, e ambos falharam
# (ExitCode 198 e ExitCode 1, respectivamente) porque o protocolo CIFS não
# suporta as operações de chown/chmod que esses bancos fazem na
# inicialização da pasta de dados — uma limitação documentada do próprio
# protocolo (chmod/chown são descartados silenciosamente em CIFS), não um
# erro de configuração. A Sprint 3 não exige volume nomeado, apenas banco
# containerizado funcionando, então os dados persistem durante o ciclo de
# vida do container — suficiente para demonstrar o CRUD via SELECT ao vivo.

az container create \
  --resource-group $RESOURCE_GROUP \
  --name $MYSQL_CONTAINER \
  --location $LOCATION \
  --image $ACR_NAME.azurecr.io/mysql:8.0 \
  --cpu 1 \
  --memory 1.5 \
  --os-type Linux \
  --dns-name-label $MYSQL_DNS_LABEL \
  --ports 3306 \
  --registry-login-server $ACR_NAME.azurecr.io \
  --registry-username $(az keyvault secret show --vault-name $KEYVAULT_NAME --name acr-username --query value -o tsv) \
  --registry-password $(az keyvault secret show --vault-name $KEYVAULT_NAME --name acr-password --query value -o tsv) \
  --environment-variables \
    MYSQL_ROOT_PASSWORD=$(az keyvault secret show --vault-name $KEYVAULT_NAME --name oracle-password --query value -o tsv) \
    MYSQL_DATABASE=$MYSQL_DATABASE \
  --restart-policy Always

# Aguardar ~30 segundos para o MySQL inicializar completamente
# az container logs --resource-group $RESOURCE_GROUP --name $MYSQL_CONTAINER
# Procurar por: "ready for connections"

# Pegar o IP do MySQL para usar na criação do container da API
MYSQL_IP=$(az container show \
  --resource-group $RESOURCE_GROUP \
  --name $MYSQL_CONTAINER \
  --query ipAddress.ip \
  --output tsv)

echo "IP do MySQL: $MYSQL_IP"

# ============================================
# 6. CONTAINER DA API (ACI)
# ============================================
echo "[6] Criando container da API..."
az container create \
  --resource-group $RESOURCE_GROUP \
  --name $API_CONTAINER \
  --location $LOCATION \
  --image $ACR_NAME.azurecr.io/afetto-api:$API_IMAGE_TAG \
  --cpu 1 \
  --memory 1 \
  --os-type Linux \
  --dns-name-label $API_DNS_LABEL \
  --ports 8080 \
  --registry-login-server $ACR_NAME.azurecr.io \
  --registry-username $(az keyvault secret show --vault-name $KEYVAULT_NAME --name acr-username --query value -o tsv) \
  --registry-password $(az keyvault secret show --vault-name $KEYVAULT_NAME --name acr-password --query value -o tsv) \
  --environment-variables \
    SPRING_DATASOURCE_URL="jdbc:mysql://$MYSQL_IP:3306/$MYSQL_DATABASE" \
    SPRING_DATASOURCE_USERNAME="root" \
    SPRING_DATASOURCE_PASSWORD=$(az keyvault secret show --vault-name $KEYVAULT_NAME --name oracle-password --query value -o tsv) \
  --restart-policy Always

# Aguardar ~25 segundos para a API inicializar (Spring Boot leva ~22s)
# az container logs --resource-group $RESOURCE_GROUP --name $API_CONTAINER
# Procurar por: "Started JavaAfettoApplication in XX seconds"

# ============================================
# 7. TESTAR E CONFERIR
# ============================================
echo "[7] Testando a solução..."
az container list --resource-group $RESOURCE_GROUP --output table

API_FQDN=$(az container show \
  --resource-group $RESOURCE_GROUP \
  --name $API_CONTAINER \
  --query ipAddress.fqdn \
  --output tsv)

echo "API disponível em: http://$API_FQDN:8080"

# Criar um usuário (tutor)
# curl -X POST http://$API_FQDN:8080/usuario \
#   -H "Content-Type: application/json" \
#   -d '{"nome":"Teste","cpf":"12345678900","dataNascimento":"2000-01-01","email":"teste@afetto.com","senha":"Senha@123","telefone":"11999999999"}'

# Login (salva cookie de sessão — a API usa autenticação por sessão JSESSIONID)
# curl -c cookies.txt -X POST http://$API_FQDN:8080/login \
#   -H "Content-Type: application/json" \
#   -d '{"email":"teste@afetto.com","senha":"Senha@123"}'

# CRUD do Pet (usando a sessão salva)
# curl -b cookies.txt http://$API_FQDN:8080/pet
# curl -b cookies.txt -X POST http://$API_FQDN:8080/pet -H "Content-Type: application/json" -d '{...}'
# curl -b cookies.txt -X PUT http://$API_FQDN:8080/pet/{id} -H "Content-Type: application/json" -d '{...}'
# curl -b cookies.txt -X DELETE http://$API_FQDN:8080/pet/{id}

# CRUD da Vacina (mesma lógica, 2ª tabela relacionada ao Pet)
# curl -b cookies.txt http://$API_FQDN:8080/vacina
# curl -b cookies.txt -X POST http://$API_FQDN:8080/vacina -H "Content-Type: application/json" -d '{...}'
# curl -b cookies.txt -X PUT http://$API_FQDN:8080/vacina/{id} -H "Content-Type: application/json" -d '{...}'
# curl -b cookies.txt -X DELETE http://$API_FQDN:8080/vacina/{id}

# Confirmar persistência direto no banco (SELECT via mysql client)
# az container exec --resource-group $RESOURCE_GROUP --name $MYSQL_CONTAINER --exec-command "mysql -uroot -pAfetto@2tdspw afettodb"
# Dentro do mysql: SELECT nome, especie, raca FROM TBL_PET;
# Dentro do mysql: SELECT nome_vacina, fabricante FROM TBL_VACINA;

# ============================================
# 8. LIMPAR O AMBIENTE (rodar ao final da entrega)
# ============================================
# az group delete --name $RESOURCE_GROUP --yes --no-wait
