# CDK Java Sample

This is my personal CDK Sample using Java Language. It is built on the base of the example provided by AWS
The `cdk.json` file tells the CDK Toolkit how to execute your app.

It is a [Maven](https://maven.apache.org/) based project, so you can open this project with any Maven compatible Java
IDE to build and run tests.

## Pre-requisites :

1 - AWS CLI
2 - CDK

## Useful commands

* `mvn package`     compile and run tests
* `cdk ls`          list all stacks in the app
* `cdk synth`       emits the synthesized CloudFormation template
* `cdk deploy`      deploy this stack to your default AWS account/region
* `cdk diff`        compare deployed stack with current state
* `cdk docs`        open CDK documentation

## Steps :

# 1 - Configure SSO Connection
On the command line:
!! IN PROGRESS!!
* aws configure sso
* aws login sso --sso-session <<sso_configured_sso>>

# 2 - Bootstrap the region
* cdk bootstrap aws://<<aws_account_id>>/<<region>>

# 3 - Deploy your infrastructure
* cdk deploy --all --verbose --parameters databaseUsername=<<db_username>> -- parameters databasePassword=<<db_password>>