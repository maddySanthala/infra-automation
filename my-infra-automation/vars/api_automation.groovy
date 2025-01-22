def call(String GIT_REPO)
pipeline{
    agent any
    environment{
        access_key = credentials('my-ssh-key')
    }
    options{
        timeout(time: 1, unit: 'HOURS')
    }
    parametres{
        gitParameter(
        name: 'BRANCH', 
        defaultValue: 'main', 
        useRepository: 'https://github.com/your-repo.git',
        description: 'Select branch to build',
        branchFilter: 'origin/(.*)',
        tagFilter: '*',
        type: 'GitParameterDefinition'
)

    }
}