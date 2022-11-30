-include utils.mk

##@ Aplicaciones con API Rest
# macro automática $@
# - nos anticipamos si luego cambiamos el target
create-weather-app: ##
	$(call figwheel-create-project,$@)
#	$(call figwheel-create-project,weather-app)

create-trello-app: ##
	$(call figwheel-create-project,$@)

build-weather-app: ##
	$(call figwheel-build,$@)

build-trello-app: ##
	$(call figwheel-build,$@)

##@ Juegos interactivos
create-startrek-game: ##
	$(call figwheel-create-project,$@)

build-startrek-game: ##
	$(call figwheel-build,$@)

##@ Otros
create-interoperability-with-js: ##
	$(call figwheel-create-project,$@)

create-google-closure: ##
	$(call figwheel-create-project,$@)

build-interoperability-with-js: ##
	$(call figwheel-build,$@)

build-google-closure: ##
	$(call figwheel-build,$@)

##@ Utilidades
h help: ## Mostrar menú de ayuda
	@awk 'BEGIN {FS = ":.*##"; printf "\nOpciones para usar:\n  make \033[36m\033[0m\n"} /^[$$()% 0-9a-zA-Z_-]+:.*?##/ { printf "  \033[36m%-15s\033[0m %s\n", $$1, $$2 } /^##@/ { printf "\n\033[1m%s\033[0m\n", substr($$0, 5) } ' $(MAKEFILE_LIST)

# TODO: feature
test:
	read -p "Ingresar el nombre del proyecto: " nombre;\
	echo "proyecto ingresado: " $$nombre

.PHONY: h help test create-weather-app build-weather-app create-trello-app build-trello-app create-startrek-game build-startrek-game create-interoperability-with-js build-interoperability-with-js
