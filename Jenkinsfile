pipeline {
    agent any

    environment {
        // Application & Docker image settings
        APP_NAME        = 'student-management'
        IMAGE_NAME      = 'student-management'
        IMAGE_TAG       = "${BUILD_NUMBER}"
        CONTAINER_NAME  = 'student-management-app'
        HOST_PORT       = '8080'
        CONTAINER_PORT  = '8080'
    }

    tools {
        maven 'Maven-3.9'   // Must match the Maven tool name configured in Jenkins
        jdk   'JDK-17'      // Must match the JDK tool name configured in Jenkins
    }

    stages {

        // ---------------------------------------------------------------
        stage('Checkout') {
        // ---------------------------------------------------------------
            steps {
                echo '====== Stage 1: Checking out source code ======'
                checkout scm
                sh 'git log --oneline -5'
            }
        }

        // ---------------------------------------------------------------
        stage('Build & Test') {
        // ---------------------------------------------------------------
            steps {
                echo '====== Stage 2: Running Maven Build and Unit Tests ======'
                sh 'mvn clean test -B'
            }
            post {
                always {
                    // Publish JUnit test results in Jenkins UI
                    junit 'target/surefire-reports/*.xml'
                }
                success {
                    echo 'All tests passed!'
                }
                failure {
                    echo 'Tests failed! Check test reports.'
                }
            }
        }

        // ---------------------------------------------------------------
        stage('Package') {
        // ---------------------------------------------------------------
            steps {
                echo '====== Stage 3: Packaging Application as JAR ======'
                sh 'mvn package -DskipTests -B'
                // Archive the JAR artifact
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }

        // ---------------------------------------------------------------
        stage('Build Docker Image') {
        // ---------------------------------------------------------------
            steps {
                echo '====== Stage 4: Building Docker Image ======'
                sh """
                    docker build -t ${IMAGE_NAME}:${IMAGE_TAG} .
                    docker tag ${IMAGE_NAME}:${IMAGE_TAG} ${IMAGE_NAME}:latest
                    docker images ${IMAGE_NAME}
                """
            }
        }

        // ---------------------------------------------------------------
        stage('Deploy Container') {
        // ---------------------------------------------------------------
            steps {
                echo '====== Stage 5: Deploying Docker Container ======'
                sh """
                    # Stop and remove existing container if running
                    if [ \$(docker ps -aq -f name=${CONTAINER_NAME}) ]; then
                        echo "Stopping existing container: ${CONTAINER_NAME}"
                        docker stop ${CONTAINER_NAME} || true
                        docker rm   ${CONTAINER_NAME} || true
                    fi

                    # Run the new container
                    docker run -d \\
                        --name ${CONTAINER_NAME} \\
                        -p ${HOST_PORT}:${CONTAINER_PORT} \\
                        --restart unless-stopped \\
                        ${IMAGE_NAME}:${IMAGE_TAG}

                    echo "Container started successfully!"
                    docker ps -f name=${CONTAINER_NAME}
                """
            }
        }

        // ---------------------------------------------------------------
        stage('Verify Deployment') {
        // ---------------------------------------------------------------
            steps {
                echo '====== Stage 6: Verifying Application is Running ======'
                sh """
                    # Wait for the app to start
                    sleep 15

                    # Hit the health endpoint
                    curl -f http://localhost:${HOST_PORT}/students/health || \
                        (echo "Health check failed!" && exit 1)

                    echo "Application is UP and running on port ${HOST_PORT}!"
                """
            }
        }
    }

    post {
        success {
            echo """
            ============================================
            Pipeline SUCCESSFUL!
            Application running at: http://localhost:${HOST_PORT}/students
            H2 Console at:          http://localhost:${HOST_PORT}/h2-console
            ============================================
            """
        }
        failure {
            echo 'Pipeline FAILED! Check the logs for details.'
        }
        always {
            echo "Build #${BUILD_NUMBER} completed with status: ${currentBuild.currentResult}"
        }
    }
}
