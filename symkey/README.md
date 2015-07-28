# Projecto de Sistemas DistribuÃ­dos #

## Primeira entrega ##

Grupo de SD 39

76465 Alexandre Ferreira - hellnukes@gmail.com

76370 Ricardo Abreu - ricardojla_17@hotmail.com

64057 Pedro Oliveira - 	pe.m.oliveira@gmail.com


RepositÃ³rio:
[tecnico-softeng-distsys-2015/T_26_39_42-project](https://github.com/tecnico-softeng-distsys-2015/T_26_39_42-project/)


-------------------------------------------------------------------------------

## ServiÃ§o SD-ID

### InstruÃ§Ãµes de instalaÃ§Ã£o 

[0] Iniciar sistema operativo linux


[1] Iniciar servidores de apoio

JUDDI:
> startup.sh

[2] Criar pasta temporÃ¡ria

> cd ...
> mkdir ...

[3] Obter versÃ£o entregue

> git clone https://github.com/tecnico-softeng-distsys-2015/T_26_39_42-project/ 


[4] Construir e executar **servidor**

> cd T_26_39_42-project/T_26_39_42-project/sd-id/id-ws
> mvn clean
> mvn generate-sources && mvn compile && mvn exec:java


-------------------------------------------------------------------------------

### InstruÃ§Ãµes de teste: ###
*(Como verificar que todas as funcionalidades estÃ£o a funcionar correctamente)*


[1] Executar testes a funcionalidade
> mvn test


-------------------------------------------------------------------------------
**FIM**