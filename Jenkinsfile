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
        return httpStatus == 200 || httpStatus == 405
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
        tomcat_prod = 8081
        tomcat_home = '/opt/tomcat8081'
    }
    stages {
        stage('Clone') {
            steps {
                git branch: 'master', credentialsId: '8cf884cb-95a1-40f5-a317-9b310d6826d4', url: 'https://code.aliyun.com/120715979/web-framework.git'
            }
        }
        stage("Stop Tomcat") {
            steps {
                sh "python3 /root/scripts/kill_port.py $tomcat_prod"
                sleep(2)
            }
        }
        stage('Build') {
            steps {
                echo '='*50 + 'Build' + '='*50
                sh"""
                mvn clean package -Dmaven.test.skip=true
                """
            }
        }
        stage('Deploy') {
            steps{
                sleep(2)
                echo '='*50 + 'Deploy' + '='*50
                sh "pwd"
                sh"""
                cp app-api/target/app-api.war $tomcat_home/webapps/
                """
                sh "sudo python3 $tomcat_home/webapps/restart_tomcat2.0.py $tomcat_prod"
            }
        }
        stage('Confirm server') {
            steps {
                echo '=' * 50 + 'confirm deploy' + '=' * 50
                confirmServer("http://xxxxxxxx/app-api/user/login", 5)
            }
        }
    }
}