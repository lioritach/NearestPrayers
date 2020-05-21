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
        sh 'ls -l'
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
     stage('Tests') {
      //Start all the existing tests in the test package 
          steps { 
            sh './gradlew test --rerun-tasks'
      }         
    }
  }
    post {
      always{
        sh 'find . -name "TEST-*.xml" -exec touch {} \\;'
        junit '**/*.xml'
      }
      //Send to mail the status of the pipeline
      mail to: 'lioritach1@gmail.com',
      subject: "The status of your last pipeline build is: ${currentBuild.fullDisplayName}",
      body: "${env.BUILD_URL} has result ${currentBuild.result} and ${BUILD_URL}/consoleText"
    }
}
