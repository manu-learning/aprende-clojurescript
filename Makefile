include config.cfg
include utils.mk
include building.mk
include helper.mk

# TODO: feature
test:
	read -p "Ingresar el nombre del proyecto: " nombre;\
	echo "proyecto ingresado: " $$nombre

.PHONY: test
