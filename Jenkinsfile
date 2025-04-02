
pipeline {
  parameters {
    string(name: 'PROJECT_NAME', defaultValue: 'gateway', description: '项目名称')
    string(name: 'VERSION', defaultValue: '0.0.1-SNAPSHOT', description: '版本号')
    string(name: 'DOCKERFILE_PATH', defaultValue: 'Dockerfile', description: 'Dockerfile路径')
  }
  agent {
    kubernetes {
      yaml '''
        spec:
          containers:
          - name: jnlp
            image: registry-intl-vpc.cn-hongkong.aliyuncs.com/my-link/test:jenkins-inbound-agent-buildah-latest
            securityContext:
              privileged: true
              runAsUser: 0
            volumeMounts:
              - name: docker-config
                mountPath: /workspace/docker-config/
              - name: workspace-volume
                mountPath: /home/jenkins/agent
          - name: maven
            image: maven:3.9.9-amazoncorretto-21-alpine
            command: ['tail', '-f', '/dev/null']
            securityContext:
              privileged: true
              runAsUser: 0
            volumeMounts:
              - name: workspace-volume
                mountPath: /home/jenkins/agent
              - name: builder-pvc
                mountPath: /data
                subPath: jenkins_runtime
              - name: mavenlib
                mountPath: /root/.m2
                subPath: mavenlib
          restartPolicy: Never
          imagePullSecrets:
            - name: docker-repository
          volumes:
            - emptyDir:
              name: "workspace-volume"
            - name: mavenlib
              persistentVolumeClaim:
                claimName: jenkins
            - name: builder-pvc
              persistentVolumeClaim:
                claimName: devops
            - name: docker-config
              secret:
                secretName: docker-repository
                items:
                 - key: .dockerconfigjson
                   path: auth.json
      '''
    }


  }
  stages {
    stage('Checkout') {
      steps {
        container('jnlp') {
          checkout([
            $class: 'GitSCM',
            branches: [[name: env.BRANCH_NAME]],
            extensions: [],
            userRemoteConfigs: [[
              url: 'https://github.com/zhoucheng45/spring-test.git'
            ]]
          ])
        }
      }
    }
    stage('Maven Build') {
      steps {
        container('maven') {
          sh """
            pwd
            ls -al
            mvn clean package -DskipTests=true
          """
        }
      }
    }
    stage('Build & Push Image') {
        steps {
          container('jnlp') {
            script {
                // 获取当前分支名称并清理特殊字符
                def branchName = env.BRANCH_NAME.replace('/', '-')
                // 获取完整提交 ID（40 位）
                def shortCommitId = sh(
                  returnStdout: true,
                  script: 'git rev-parse --short HEAD'
                ).trim()
                // 动态定义镜像标签
                def imageTag = "registry-intl-vpc.cn-hongkong.aliyuncs.com/my-link/test:${params.PROJECT_NAME}-${branchName}-${shortCommitId}"

                sh """
                  pwd
                  ls -al
                  cat /workspace/docker-config/auth.json
                  buildah bud --build-arg PROJECT_NAME=${params.PROJECT_NAME} --build-arg VERSION=${params.VERSION} -t app:${branchName} -f Dockerfile .
                  buildah push --authfile /workspace/docker-config/auth.json localhost/app:${branchName} ${imageTag}
                """
              }
          }
        }
    }
  }
}


