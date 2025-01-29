pipeline{
    agent any
    options{
        timeout(time: 1, unit: 'SECONDS')
    }
    parameters{
                gitParameter(
                name: 'BRANCH', defaultValue: 'Release',
                useRepository: "git@github.com:maddySanthala/simple-java-maven-app.git",
                description: 'Select branch to build ', branch: 'master',
                branchFilter: 'origin.*/(.*)', tagFilter: '*',
                type: 'GitParameterDefinition', quickFilterEnabled: true,
                selectedValue: 'NONE', sortMode: 'NONE'
            )
        }
    }
    stages{
        stage('print hello')
        {
            steps{
                sh 'echo "hello world"'
                 }
        }
    }