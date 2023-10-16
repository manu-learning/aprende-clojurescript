##@ Experimentos Namespaces de Clojure
create-operaciones-asincronicas: ## clojure.core.async
	$(call lein-create-project,$@)

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

##@ Pequeñas Aplicaciones
create-temperature-converter: ##
	$(call figwheel-create-project,$@)

create-shopping-cart: ##
	$(call figwheel-create-project,$@)

create-todo-list: ##
	$(call figwheel-create-project,$@)

create-counter: ##
	$(call figwheel-create-project,$@)

create-salary-calculator: ##
	$(call figwheel-create-project,$@)

create-erp-system: ##
	$(call lein-create-project,$@)

build-temperature-converter: ##
	$(call figwheel-build,$@)

build-shopping-cart: ##
	$(call figwheel-build,$@)

build-todo-list: ##
	$(call figwheel-build,$@)

build-counter: ##
	$(call figwheel-build,$@)

build-salary-calculator: ##
	$(call figwheel-build,$@)

##@ Juegos interactivos
create-startrek-game: ##
	$(call figwheel-create-project,$@)

create-vim-editor-race: ##
	$(call lein-create-project,$@)

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

.PHONY: create-weather-app build-weather-app create-trello-app build-trello-app create-startrek-game build-startrek-game create-interoperability-with-js build-interoperability-with-js
