#!/usr/bin/env groovy

node {
    stage('checkout') {
        checkout scm
    }
    stage('build') {
        sh "./gradlew clean build"
    }
}