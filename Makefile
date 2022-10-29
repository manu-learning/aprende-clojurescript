NAMESPACE=learn-cljs
DIR_PROJECTS=projects
DEFAULT_PROJECT=weather

NAME=$(NAMESPACE)/$(PROJECT_DEFAULT)
TEMPLATE=figwheel-main

fn figwheel-new:
	cd $(DIR_PROJECTS) && \
	clj -X:project/new :template $(TEMPLATE) :name $(NAMESPACE)/$(DEFAULT_PROJECT) :args '["+deps" "--reagent"]'

fb figwheel-build:
	cd $(DIR_PROJECTS)/$(DEFAULT_PROJECT) && \
	clj -M:fig:build

.PHONY: fn figwheel-new fb figwheel-build
