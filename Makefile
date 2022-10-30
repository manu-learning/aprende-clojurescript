NAMESPACE=learn-cljs
DIR_PROJECTS=projects
DEFAULT_PROJECT=weather

NAME=$(NAMESPACE)/$(PROJECT_DEFAULT)
TEMPLATE=figwheel-main

new figwheel-new:
	cd $(DIR_PROJECTS) && \
	clj -X:project/new :template $(TEMPLATE) :name $(NAMESPACE)/$(DEFAULT_PROJECT) :args '["+deps" "--reagent"]'

build figwheel-build:
	cd $(DIR_PROJECTS)/$(DEFAULT_PROJECT) && \
	clj -M:fig:build

.PHONY: new figwheel-new build figwheel-build
