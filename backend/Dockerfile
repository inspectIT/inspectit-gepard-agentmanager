# ---- Build Stage ----
FROM gradle:jdk21 as build

WORKDIR /home/gradle/project

# Copy the Gradle wrapper files
COPY ../gradle /home/gradle/project/gradle
COPY ../gradlew /home/gradle/project/
COPY ../build.gradle /home/gradle/project/
COPY ../settings.gradle /home/gradle/project/

# Copy server source code
COPY backend /home/gradle/project/backend

# Build the application
RUN ./gradlew build -x test

# ---- Runtime Stage ----
# We use Temurin as it is the default at VHV
FROM eclipse-temurin:21-jre-jammy

# In order to use a postgres database, we need to set the active profile
# ENV SPRING_PROFILES_ACTIVE=postgres

# Copy the build from the server subproject to the runtime image
# The jar already contains the static export from the ui subproject
COPY --from=build /home/gradle/project/backend/build/libs/*.jar app.jar

# Expose the port on which the app will run
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "/app.jar"]



