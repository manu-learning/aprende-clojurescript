NAMESPACE=learn-cljs
DIR_PROJECTS=projects
TEMPLATE=figwheel-main

# macro automática $@
# - nos anticipamos si luego cambiamos el target
create-weather-app:
	$(call figwheel-create-project,$@)
#	$(call figwheel-create-project,weather-app)

build-weather-app:
	$(call figwheel-build,$@)

create-trello-app:
	$(call figwheel-create-project,$@)

build-trello-app:
	$(call figwheel-build,$@)

create-startrek-game:
	$(call figwheel-create-project,$@)

build-startrek-game:
	$(call figwheel-build,$@)

# definimos funciones propias
# - para evitar lógica repetida
# - las llamadas deben ser de la forma $(call nombre-funcion,param1,param2,...)
# - obtenemos los parámetros en el mismo orden que se pasaron $(1) $(2) ..
#
# función primitiva subst de GNU Make
# - Ej. $(subt patron,nuevaCadena,texto)
define figwheel-create-project
	cd $(DIR_PROJECTS) && \
	clj -X:project/new \
		:template $(TEMPLATE) \
		:name $(NAMESPACE)/$(subst create-,,$(1)) \
		:args '["+deps" "--reagent"]'
endef

define figwheel-build
	cd $(DIR_PROJECTS)/$(subst build-,,$(1)) && \
	clj -M:fig:build
endef

# TODO: feature
test:
	read -p "Ingresar el nombre del proyecto: " nombre;\
	echo "proyecto ingresado: " $$nombre

.PHONY: test create-weather-app build-weather-app create-trello-app build-trello-app create-startrek-game build-startrek-game
