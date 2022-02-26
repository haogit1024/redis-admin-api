#!/usr/bin/env groovy
void sleep(int waitTime) {
    // 等上一个阶段释放内存
    Thread.sleep(waitTime);
}
boolean isService(String url) {
    def get = new URL(url).openConnection();
    try {
        def httpStatus = get.getResponseCode();
        println('httpStatus: ' + httpStatus)
        return httpStatus == 200 || httpStatus == 405 || httpStatus == 500
    } catch(Exception e) {
        return false;
    }
}
void confirmServer(String url, int waitTime) {
    // 过期秒数 120 秒
    int timeOut = 120 * 1000;
    int sum = waitTime;
    // 先等待
    if (waitTime > 0) {
        Thread.sleep(waitTime);
    }
    while (sum <= timeOut) {
        boolean isRun = isService(url);
        if (isRun) {
            println('http server run success');
            return;
        }
        println('response error sleep 5s');
        int sleepTime = 2 * 1000
        Thread.sleep(sleepTime)
        sum += sleepTime
    }
    throw new RuntimeException("http server response time out");
}
pipeline{
    agent any
    environment {
        tomcat_prod = 8080
        tomcat_home = '/opt/tomcat'
        git_branch='dev'
        profile_active='dev'
    }
    stages {
        stage('Git Pull') {
            steps {
                git branch: 'master', credentialsId: 'git-ssh', url: 'git@github.com:haogit1024/redis-admin-api.git'
            }
        }
        stage("Stop Tomcat") {
            steps {
                sh "${tomcat_home}/bin/shutdown.sh"
                sleep(3)
                sh"""
                tomcat_pid=\$(ps -ef | grep ${tomcat_home} | grep -v grep | awk '{print \$2}')
                    echo 'tomcat_pid: '
                    echo \${tomat_pid}
                    for id in \${tomcat_pid}
                        do
                            kill \$id
                            echo "killed \${id}"
                        done
                """
            }
        }
        stage('Build') {
            steps {
                echo '='*50 + 'Build' + '='*50
                sh"""
                mvn clean package -Pdev -Dmaven.test.skip=true
                cd ./admin-api/target
                ls
                """
            }
        }
        stage('Deploy') {
            steps{
                echo '='*50 + 'Deploy' + '='*50
                sh "pwd"
                sh"""
                cp -rf admin-api/target/admin-api.war ${tomcat_home}/webapps/
                """
                sh'''
                ${tomcat_home}/bin/startup.sh
                '''
            }
        }
        stage('Confirm Deploy') {
            steps {
                echo '=' * 50 + 'Confirm Deploy' + '=' * 50
                confirmServer("http://localhost:8080/admin-api/sysUser/login", 5)
            }
        }
    }
}