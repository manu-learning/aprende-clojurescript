NAMESPACE=learn-cljs
DIR_PROJECTS=projects
TEMPLATE=figwheel-main

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
