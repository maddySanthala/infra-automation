def call(String GIT_REPO) {
    pipeline {
        agent any
        environment {
            access_key = credentials('my-ssh-key')
        }
        options {
            timeout(time: 1, unit: 'HOURS')
        }
        parameters {
            gitParameter(
                name: 'BRANCH',
                defaultValue: 'main',
                useRepository: "https://github.com/maddySanthala/${GIT_REPO}.git",
                description: 'Select branch to build',
                branchFilter: 'origin/(.*)',
                tagFilter: '*'
            )
        }
        stages {
            stage('Checkout Infra Code') {
                steps {
                    checkout scmGit(
                        branches: [[name: '*/shared_libraries']],
                        extensions: [[$class: 'RelativeTargetDirectory', relativeTargetDir: 'my-infra-automation']],
                        userRemoteConfigs: [[
                            credentialsId: "${CREDENTIAL_ID}",
                            url: 'https://github.com/maddySanthala/infra-automation.git'
                        ]]
                    )
                }
            }
            stage('Checkout Source Code') {
                steps {
                    checkout scmGit(
                        branches: [[name: "${BRANCH}"]],
                        userRemoteConfigs: [[
                            credentialsId: "${CREDENTIAL_ID}",
                            url: "https://github.com/maddySanthala/${GIT_REPO}.git"
                        ]]
                    )
                }
            }
            stage('Maven Compile') {
                steps {
                    echo 'Building Maven project...'
                    sh 'mvn clean package'
                }
            }
            stage('Run Tests') {
                steps {
                    echo 'Running tests...'
                    sh 'mvn test'
                }
            }
        }
    }
}
