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
        sh "docker build -t heartocean/docom:kj${BUILD_NUMBER} -f pro1_dockerfile_for_kub ."
      }
    }
    stage('dockerImagePush'){
      steps{
        withCredentials([string(credentialsId: 'docker_pass', variable: 'heartocean')]) {
          sh "docker login -u heartocean -p ${heartocean}"
        }
        sh "docker push heartocean/docom:kj${BUILD_NUMBER}"
      }
    }
    stage('modifyFile'){
      steps{
        sh "sed -i '20s/tag/kj${BUILD_NUMBER}/g' pro1_webapp1.yaml"
      }
    }
    stage('deployInK8s'){
      steps{
          sh 'kubectl apply -f pro1_webapp1.yaml -f pro1_service1.yaml -f pro1_ingress1.yaml'
      }
    }
  }
}
