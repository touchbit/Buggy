ifneq ($(MAKECMDGOALS),$(findstring $(MAKECMDGOALS),build-doc-image run-doc-image version))
    VERSION := $(wordlist 2,$(words $(MAKECMDGOALS)),$(MAKECMDGOALS))
else
	ifneq (version,$(firstword $(MAKECMDGOALS)))
		VERSION := latest
	endif
endif
$(eval $(VERSION):;@:)

clean:
	mvn clean

install: clean
	mvn install -DskipTests

test: clean
	mvn test

deploy:
	mvn deploy -DskipTests

version:
	mvn versions:set -DnewVersion=${VERSION}

ver: version

purge:
	mvn dependency:purge-local-repository@local-buggy

clean-doc:
	rm -rf ./site

build-doc: clean-doc
	sphinx-build -W -b html ./docs ./site

build-doc-image:
	docker build --no-cache -t buggy/doc:${VERSION} -f ./docs/Dockerfile .

run-doc-image:
	docker run -p 8080:80 buggy/doc:${VERSION}

deploy-doc-image: build-doc
	echo ToDo gitlab registry