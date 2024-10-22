# Contributing

## IDE

We recommend using [IntelliJ](https://www.jetbrains.com/idea/download/#section=windows) as IDE for contributing.

## Dependencies

We integrate our [inspectit-gepard-config](https://github.com/inspectIT/inspectit-gepard-config) model as dependency. We download the dependency
from GitHub Packages, which requires authentication. To set up your authentication, follow these steps:

1. Create a `gradle.properties` file in `%userprofile%\.gradle`
2. Create a [(classic) personal access token (PAT)](https://github.com/settings/tokens) with `read:packages` permissions.
3. Paste the following content into your `gradle.properties`:

```
gpr.inspectit.gepard.user=<YOUR_GITHUB_USERNAME>
gpr.inspectit.gepard.token=<YOUR_GITHUB_ACCESS_TOKEN>
```

You can find more information here as well: https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-gradle-registry


## Formatting

We have [spotless](https://github.com/diffplug/spotless) configured to format the code. You can run the following commands:

- `./gradlew spotlessCheck` to validate the formatting of the code.
- `./gradlew spotlessApply` to format the code.

Be aware that the CI will fail if the code is not formatted correctly, as `spotlessCheck` is part of the build process.


## Building

### Prerequisites
Please make sure you have the following tools installed on your machine:
- A big cup of coffee
- A Java Development Kit (JDK) with version 21 or higher
- A working internet connection (for downloading dependencies)
- Docker (optional, for running the application in a container)

Thats it for now.

### Backend
To build the backend, simply run the following command in the root directory of the project:
```shell
./gradlew 
```
This will generate the backend jar file in the `server/build/libs` directory.

> [!TIP]  
> You might have installed an auto-formatter in your IDE. It may break the installed spotless code-style. In this case, the build will not be successfull. Please run ```./gradlew potlessApply``` to fix this issue.

### Frontend
To build the frontend, navigate to the `frontend/src` directory and run the following commands:
```shell
    npm install
    npm run build
```
This will generate an entry point that launches a ready-to-run node server in the `frontend/src/.output` directory.

You could also run the frontend in development mode by running:
```shell
    npm run dev
```
