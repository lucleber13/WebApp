# Setup Project Configuration

This topic describes how to configure a new project with React using Vite.js.

## Prerequisites

Before you start, make sure you have the following installed:

* ESLint
* Prettier

## Steps

1. **Create a new project using Vite.js.**
2. **Install the following dependencies:**
   ``` Bash
          npm install --save-dev prettier 
          npm install --save-dev eslint
          npm install --save-dev eslint-config-prettier
          npm install --save-dev eslint-plugin-react
          npm install --save-dev eslint-plugin-jsx-a11y
      ```

    * `prettier:` Formats your code automatically.
    * `eslint:` Analyzes your code and enforces best practices.
    * `eslint-config-prettier:` Disables ESLint rules that conflict with Prettier.
    * `eslint-plugin-react:` Provides additional React-specific rules.
    * `eslint-plugin-jsx-a11y:` Provides additional accessibility rules.

3. **Configure Prettier**
    * Create a `.prettierrc` file in the root directory of your project.
    * Add the following configuration:
        ``` JSON
        {
            "trailingComma": "all",
            "tabWidth": 2,
            "printWidth": 80,
            "semi": false,
            "jsxSingleQuote": true,
            "singleQuote": true
        }
        ```
4. **Configuring the Prettier extension in your code editor.**
    1. **_On Vs Code configuration:_**
        * Open VS Code settings by going to `File | Preferences | Settings` on Windows
          or `Code | Preferences | Settings` on macOS.
        * Search for `editor format on save` and enable it.
        * Search for `editor default formatter` and select `Prettier - Code formatter` from the dropdown list.
    2. **_On WebStorm configuration:_**
        * Open WebStorm settings by going to `File | Settings` on Windows or `WebStorm | Preferences` on macOS.
        * Open `Tools | Actions On Save` and thick in the `Reformat code`and `Run Prettier` checkboxes.
        * Open `Languages & Frameworks | JavaScript | Prettier` and select the `Automatic Prettier Configuration`
          checkboxes.
        * Click `Apply` and `OK` to save the changes.
5. **Creating Prettier `ignore` file.**
    * Create a `.prettierignore.json` file in the root directory of your project.
    * Add the following configuration:
        ``` 
           dist/
        ```
6. **Configuring ESLint**
    * Delete the automatically created `.eslintrc.cjs` file.
    * Create a `.eslintrc.json` file in the root directory of your project.
    * Add the following configuration:
        ``` JSON
        {
            "root": true,
            "env": {
                "browser": true
            },
           "parserOptions": {
                "ecmaVersion": "latest",
                "sourceType": "module"
            },
              "extends": [
                    "eslint:recommended",
                    "plugin:react/recommended",
                    "plugin:jsx-a11y/recommended",
                    "plugin:react/jsx-runtime",
                    "prettier"
                ],
           "settings": {
                "react": {
                    "version": "detect"
                }
            },
           "overrides": [
                {
                    "files": ["*.js", "*.jsx"]
                }
            ]
        }
        ```
7. **Create a new file named `.eslintignore` in the root directory of your project.**
    * Add the following configuration:
        ``` 
           dist/
           vite.config.js
        ```

8. **Save the files and run in the terminal to run linter.**
    ``` Bash
        npx eslint src --fix
    ```

9. **Adding a new script to run the linter.**
    * Run the following command in the terminal:
        ``` Bash
        npm pkg set scripts.lint="eslint src"
        ```
    * Run the following command to run the linter:
        ``` Bash
        npm run lint
        ```
