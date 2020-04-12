pipeline {
  agent {
    docker {
      image 'windsekirun/jenkins-android-docker:1.1.1'
    }
  }
  options {
    // Stop the build early in case of compile or test failures
    skipStagesAfterUnstable()
  }
  stages {
    stage ('Prepare'){
      steps {
        sh 'chmod +x ./gradlew'
      }
    }
    stage('Compile') {
      steps {
        // Compile the app and its dependencies
        sh './gradlew compileDebugSources'
      }
    }
    stage('Build APK') {
      steps {
        // Finish building and packaging the APK
        sh './gradlew assembleDebug'
      }
    }
  }
}
