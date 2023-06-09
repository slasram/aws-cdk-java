package fr.sisram.cdk.infrastructure;

import fr.sisram.cdk.infrastructure.stack.PingLambdaStack;
import software.amazon.awscdk.App;

public class InfrastructureApp {
    public static void main(final String[] args) {
        App app = new App();
        String accountId = app.getAccount();
        System.out.println("AWS Account " + accountId);
        String region = app.getRegion();
        System.out.println("AWS Region" + region);
        declareStacks(accountId, region, app);
        app.synth();
    }

    public static void declareStacks(String accountId, String region, App app) {
        /**
         // 1 - Build environment
         Environment environment = StackUtils.environmentBuilder(accountId, region);
         // 2 - Configuoyre VPC
         VpcStack awsVpcStack = new VpcStack(app, "VPC", StackProps.builder().env(environment).build());
         // 3 - Configure RDS PostGres
         PostgresRdsStack awsRdsStack = new PostgresRdsStack(app, "RDS", StackProps.builder().env(environment).build(), awsVpcStack.getVpc());
         awsRdsStack.addDependency(awsVpcStack);
         // 4 - Configure Lambda
         */
        PingLambdaStack pingLambdaStack = new PingLambdaStack(app, "lambdaping2");

    }
}

