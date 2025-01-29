pipeline {
    agent any
    environment {
        REPO_URL = 'git@github.com:maddySanthala/GitPract.git'
        CREDENTIALS_ID = 'DEFAULT'
    }
    parameters {
        choice(
            name: 'BRANCH',
            choices: [''], // This will be populated dynamically by Step 1
            description: 'Select a branch to build'
        )
    }
    stages {
        stage('Checkout Selected Branch') {
            when {
                expression { params.BRANCH?.trim() }
            }
            steps {
                checkout([
                    $class: 'GitSCM',
                    branches: [[name: "*/${params.BRANCH}"]],
                    userRemoteConfigs: [[
                        url: env.REPO_URL,
                        credentialsId: env.CREDENTIALS_ID
                    ]]
                ])
                echo "Checked out branch: ${params.BRANCH}"
            }
        }
    }
}
