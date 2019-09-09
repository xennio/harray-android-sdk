#!/usr/bin/env groovy

node {
    def gradleHome = tool "gradle541"
    stage('checkout') {
        checkout scm
    }
    stage('build') {
        sh "${gradleHome}/bin/gradle build"
    }
}