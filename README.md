# Gradle Version Plugin (gradle-version-plugin)
A Gradle plugin to manage project versions easily and automatically. 
This plugin allows you to update the project version, increment major, minor, and patch versions, and print the current project version.
It is designed to be used with GitHub Actions to automatically generate pull requests after version updates.

## Features

- **Version Update**: Allows updating the project version to a specified one.
- **Major Increment**: Increments the major version of the project.
- **Minor Increment**: Increments the minor version of the project.
- **Patch Increment**: Increments the patch version of the project.
- **Print Version**: Prints the current project version.

## Requirements

- Gradle 8.5 or higher
- Java 21 or higher

## Installation

Add the following snippet to your project's `build.gradle` file:

```groovy
plugins {
    id 'io.github.ultimaphoenix.gradleversionplugin' version '1.0.0'
}
```
### Update Version

To update the project version to a specified one, run the following command:

```sh
./gradlew updateVersion -PnewVersion=<new_version>
```

Example:
```sh
./gradlew updateVersion -PnewVersion=2.0.0
```

Increment Major Version
To increment the major version of the project, run the following command:
```sh
./gradlew bumpMajor
```

Increment Minor Version
To increment the minor version of the project, run the following command:
```sh
./gradlew bumpMinor
```

Increment Patch Version
To increment the patch version of the project, run the following command:
```sh
./gradlew bumpPatch
```

Print Current Version
To print the current project version, run the following command:
```sh
./gradlew printVersion
```

GitHub Actions Integration
This plugin is intended to be used with GitHub Actions to automatically generate pull requests after version updates. Below is an example of a GitHub Actions workflow that uses this plugin and allows you to select the type of version bump (Major, Minor, Patch) manually:
```yaml
name: 'Release new Version'

on:
    workflow_dispatch:
    inputs:
        bump_type:
            description: 'Select the type of version bump'
            required: true
            default: 'patch'
            type: choice
            options:
                - major
                - minor
                - patch

jobs:
    bump-version:
    runs-on: ubuntu-latest
    steps:
      - name: Checkout code
        uses: actions/checkout@@v4
      - name: Set up JDK 21
        uses: actions/setup-java@v4
        with:
          java-version: 21
      - name: Set up Gradle
        uses: gradle/actions/setup-gradle@v3
        with:
          gradle-version: 8.5

      - name: Bump version
        run: |
          case ${{ github.event.inputs.bump_type }} in
            major)
              ./gradlew bumpMajor
              ;;
            minor)
              ./gradlew bumpMinor
              ;;
            patch)
              ./gradlew bumpPatch
              ;;
            esac
    
      - name: Commit and push changes
        run: |
          git config --global user.name 'github-actions[bot]'
          git config --global user.email 'github-actions[bot]@users.noreply.github.com'
          git add build.gradle
          git commit -m 'Bump version'
          git push

      - name: Create Pull Request
        run: |
          gh pr create --title "Bump version" --body "This PR bumps the project version." --base main --head version-bump
```

# Contributing
Contributions are welcome! To contribute, follow these steps:
1. Fork the repository.
2. Create a branch for your feature (git checkout -b feature/new-feature).
3. Commit your changes (git commit -m 'Add some new-feature').
4. Push the branch (git push origin feature/new-feature).
Open a Pull Request.

# License
Distributed under the Apache 2.0 License. See [LICENSE](https://github.com/UltimaPhoenix/gradle-version-plugin?tab=Apache-2.0-1-ov-file#readme) for more information.