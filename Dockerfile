FROM eclipse-temurin:21-jdk AS build

# Copiar el código fuente a la imagen y establecer el directorio de trabajo
COPY . /app
WORKDIR /app

# Conceder permisos para ejecutar el archivo mvnw y ejecutar Maven para empaquetar la aplicación
RUN chmod +x mvnw
RUN ./mvnw package -DskipTests

# Mover el archivo JAR generado a la ubicación deseada
RUN mv -f target/*.jar app.jar

# Etapa final: JRE para ejecutar la aplicación
FROM eclipse-temurin:21-jre

# Establecer la variable de entorno del puerto
ARG PORT
ENV PORT=${PORT}

# Copiar el archivo JAR desde la etapa de construcción
COPY --from=build /app/app.jar .

# Crear un nuevo usuario para ejecutar la aplicación
RUN useradd runtime
USER runtime

# Ejecutar la aplicación Java con el archivo JAR
ENTRYPOINT [ "java", "-Dserver.port=${PORT}", "-jar", "app.jar"]
