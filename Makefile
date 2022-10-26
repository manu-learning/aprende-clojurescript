NAMESPACE=learn-cljs
DIR_PROJECTS=projects
PROJECT_DEFAULT=weather
NAME=$(NAMESPACE)/$(PROJECT_DEFAULT)
TEMPLATE=figwheel-main

new:
	cd $(DIR_PROJECTS) && \
	clj -X:project/new :template $(TEMPLATE) :name $(NAME) :args '["+deps" "--reagent"]'

build:
	cd $(DIR_PROJECTS)/$(PROJECT_DEFAULT) && \
	clj -A:fig:build

.PHONY: new build
