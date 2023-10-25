##@ Experimentos Namespaces de Clojure
.PHONY: create-operaciones-asincronicas
# TODO: porque utilizabamos lein en vez de figwheel
create-operaciones-asincronicas: ## clojure.core.async
	$(call lein-create-project,$@)

##@ Aplicaciones con API Rest
# macro automática $@
# - nos anticipamos si luego cambiamos el target
.PHONY: app-weather-app app-trello-app
app-weather-app: ##
ifeq ("$(wildcard $(DIR_PROJECTS)/weather-app)", "")
	$(call figwheel-create-project,$@)
#	$(call figwheel-create-project,weather-app)
else
	$(call figwheel-build,$@)
endif

app-trello-app: ##
ifeq ("$(wildcard $(DIR_PROJECTS)/trello-app)", "")
	$(call figwheel-create-project,$@)
else
	$(call figwheel-build,$@)
endif

##@ Pequeñas Aplicaciones
.PHONY: app-simple-notes app-hello-world app-temperature-converter app-shopping-cart app-todo-list app-counter app-salary-calculator app-lein-erp-system
app-simple-notes: ##
ifeq ("$(wildcard $(DIR_PROJECTS)/simple-notes)", "")
	$(call figwheel-create-project,$@)
else
	$(call figwheel-build,$@)
endif

app-hello-world: ##
ifeq ("$(wildcard $(DIR_PROJECTS)/hello-world)", "")
	$(call figwheel-create-project,$@)
else
	$(call figwheel-build,$@)
endif

app-temperature-converter: ##
ifeq ("$(wildcard $(DIR_PROJECTS)/temperature-converter)", "")
	$(call figwheel-create-project,$@)
else
	$(call figwheel-build,$@)
endif

app-shopping-cart: ##
ifeq ("$(wildcard $(DIR_PROJECTS)/shopping-cart)", "")
	$(call figwheel-create-project,$@)
else
	$(call figwheel-build,$@)
endif

app-todo-list: ##
ifeq ("$(wildcard $(DIR_PROJECTS)/todo-list)", "")
	$(call figwheel-create-project,$@)
else
	$(call figwheel-build,$@)
endif

app-counter: ##
ifeq ("$(wildcard $(DIR_PROJECTS)/counter)", "")
	$(call figwheel-create-project,$@)
else
	$(call figwheel-build,$@)
endif

app-salary-calculator: ##
ifeq ("$(wildcard $(DIR_PROJECTS)/salary-calculator)", "")
	$(call figwheel-create-project,$@)
else
	$(call figwheel-build,$@)
endif

# TODO: porque utilizabamos lein en vez de figwheel
app-erp-system: ##
	$(call lein-create-project,$@)

##@ Juegos interactivos
.PHONY: app-startrek-game app-vim-editor-race
app-startrek-game: ##
ifeq ("$(wildcard $(DIR_PROJECTS)/startrek-game)", "")
	$(call figwheel-create-project,$@)
else
	$(call figwheel-build,$@)
endif

# TODO: porque utilizabamos lein en vez de figwheel
app-vim-editor-race: ##
	$(call lein-create-project,$@)

##@ Otros
.PHONY: app-interoperability-with-js app-google-closure
app-interoperability-with-js: ##
ifeq ("$(wildcard $(DIR_PROJECTS)/interoperability-with-js)", "")
	$(call figwheel-create-project,$@)
else
	$(call figwheel-build,$@)
endif

app-google-closure: ##
ifeq ("$(wildcard $(DIR_PROJECTS)/google-closure)", "")
	$(call figwheel-create-project,$@)
else
	$(call figwheel-build,$@)
endif

