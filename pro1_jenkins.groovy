pipeline{
  agent any
  tools{
    git 'Default'
  }
  triggers{
      githubPush()
  }
  stages{
    stage('pullSCM'){
      steps{
        git credentialsId: 'trainyou credentials', url: 'https://github.com/trainyou/maven-web-application.git'
      }
    }
    stage('dockerImage'){
      steps{
        sh "docker build -t heartocean/docom:kj${BUILD_NUMBER} -f dockerfile_for_kub ."
      }
    }
    stage('dockerImagePush'){
      steps{
        withCredentials([string(credentialsId: 'docker_pass', variable: 'heartocean')]) {
          sh "docker login -u heartocean -p ${docker_pass}"
        }
        sh "docker push heartocean/docom:kj${BUILD_NUMBER}"
      }
    }
    stage('deployInK8s'){
      steps{
          sh 'kubectl apply -f webapp1.yaml -f service1.yaml -f ingress1.yaml'
      }
    }
  }
}
