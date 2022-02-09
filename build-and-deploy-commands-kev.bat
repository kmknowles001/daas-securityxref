-- register a custom jar file into local maven repo.

mvn install:install-file \
   -Dfile=C:\Users\kmkno\IdeaProjects\securityxref-common\out\artifacts\securityxref_common_jar\securityxref-common.jar
   -DgroupId=daas-securityxref \
   -DartifactId=securityxref-common \
   -Dversion=1 \
   -Dpackaging=JAR \
   -DgeneratePom=true

mvn install:install-file -Dfile=C:\Users\kmkno\IdeaProjects\securityxref-common\out\artifacts\securityxref_common_jar\securityxref-common.jar -DgroupId=daas-securityxref -DartifactId=securityxref-common -Dversion=1 -Dpackaging=JAR  -DgeneratePom=true

-- maven build of jar
mvn package


-- docker build
docker build -t daas-securityxref .

docker tag daas-securityxref eu.gcr.io/data-services-337013/daas-securityxref


-- setup gcloud with docker
gcloud auth configure-docker