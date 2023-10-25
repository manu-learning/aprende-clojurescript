# definimos funciones propias
# - para evitar lógica repetida
# - las llamadas deben ser de la forma $(call nombre-funcion,param1,param2,...)
# - obtenemos los parámetros en el mismo orden que se pasaron $(1) $(2) ..
#
define lein-create-project
	cd $(DIR_PROJECTS) && \
	lein new $(TEMPLATE_REAGENT) \
		$(subst create-,,$(1)) \
		+shadow-cljs && \
	npm install
endef

define npm-watch-project
	cd $(DIR_PROJECTS)/$(subst watch-,,$(1)) && \
	npm install && \
	npx shadow-cljs watch app
endef

# función primitiva subst de GNU Make
# - Ej. $(subt patron,nuevaCadena,texto)
define figwheel-create-project
	@echo "Creando proyecto de ClojureScript con figwheel.." \
	&& cd $(DIR_PROJECTS) \
	&& clj -X:project/new \
			:template $(TEMPLATE) \
			:name $(NAMESPACE)/$(subst app-,,$(1)) \
			:args '["+deps" "--reagent"]'
endef

define figwheel-build
	@echo "Iniciando proyecto de ClojureScript con figwheel.." \
	&& cd $(DIR_PROJECTS)/$(subst app-,,$(1)) \
	&& clj -M:fig:build
#	clj -R:nrepl -m nrepl.cmdline --middleware "[cider.piggieback/wrap-cljs-repl]"
endef
