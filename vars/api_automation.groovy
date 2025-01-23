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
                type: 'Branch',
                defaultValue: 'main',
                useRepository: "git@github.com:maddySanthala/${GIT_REPO}.git",
                description: 'Select branch to build',
                branchFilter: 'origin/(.*)',
                tagFilter: '*'
            )
        }
        stages {
            stage('Checkout Infra Code') {
                steps {
                    script {
                        checkout([
                            $class: 'GitSCM',
                            branches: [[name: '*/shared_libraries']],
                            extensions: [[$class: 'RelativeTargetDirectory', relativeTargetDir: 'infra-automation']],
                            userRemoteConfigs: [[
                                credentialsId: 'my-ssh-key', // Replace with actual credential ID
                                url: 'git@github.com:maddySanthala/infra-automation.git'
                            ]]
                        ])
                    }
                }
            }
            stage('Checkout Source Code') {
                steps {
                    script {
                        checkout([
                            $class: 'GitSCM',
                            branches: [[name: "${params.BRANCH}"]],
                            userRemoteConfigs: [[
                                credentialsId: 'my-ssh-key', // Replace with actual credential ID
                                url: "git@github.com:maddySanthala/${GIT_REPO}.git"
                            ]]
                        ])
                    }
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
