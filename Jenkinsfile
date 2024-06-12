
pipeline {
    agent any  // Use any available agent

    stages {
        stage('Checkout Code') {
            steps {
                git branch: 'development', // Replace 'main' with your branch name
                    credentialsId: 'git-hub-credentials', // Replace with your Git credentials ID (if needed)
                    url: 'https://github.com/VFALAN/msc-ms-users.git' // Replace with your Git repository URL
            }
        }
        stage('Build Project') {
            steps {
                bat 'mvn clean package' // Adjust command for your build tool (e.g., Gradle: ./gradlew clean build)
            }
        }
        stage('Build Docker Image') {
            steps {
                script {
                    docker.withRegistry('https://registry.hub.docker.com','dcoker-credentials'){
                        def dockerImageBuild = docker.build("vifa951002/msc-ms-users")
                        dockerImageBuild.push()
                    }
                }

            }
        }
        /* stage('Deploy App'){
                steps{
               withCredentials(bindings: [
                                     string(credentialsId: 'k8s-token', variable: 'api_token')
                                     ]) {
                                     bat 'dir'
                           bat 'kubectl --token $api_token --server http://127.0.0.1:51416 apply -f DeployK8s.yaml --validate=false'
                         }
        }


    } */
}
}