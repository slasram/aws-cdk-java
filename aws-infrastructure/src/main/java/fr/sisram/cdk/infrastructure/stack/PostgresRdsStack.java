package fr.sisram.cdk.infrastructure.stack;

import fr.sisram.cdk.infrastructure.util.StackUtils;
import software.amazon.awscdk.*;
import software.amazon.awscdk.services.ec2.*;
import software.amazon.awscdk.services.rds.*;
import software.constructs.Construct;

import java.util.Collections;

public class PostgresRdsStack extends Stack {
    public PostgresRdsStack(final Construct scope, final String id, final StackProps props, Vpc vpc) {
        super(scope, id, props);

        // The code that defines your stack goes here
        CfnParameter databaseUsername = StackUtils
                .getCfnParameter(this, "databaseUsername", "The RDS instance username", "String");

        CfnParameter databasePass = StackUtils
                .getCfnParameter(this, "databasePassword", "The RDS instance password", "String");

        ISecurityGroup iSecurityGroup = SecurityGroup.fromSecurityGroupId(this, id, vpc.getVpcDefaultSecurityGroup());
        iSecurityGroup.addIngressRule(Peer.anyIpv4(), Port.tcp(3306));

        DatabaseInstance databaseInstance = DatabaseInstance.Builder
                .create(this, "rds-postgres")
                .instanceIdentifier("rds-postgres")
                .engine(DatabaseInstanceEngine.postgres(PostgresInstanceEngineProps.builder()
                        .version(PostgresEngineVersion.VER_15_2)
                        .build()))
                .vpc(vpc)
                .credentials(Credentials.fromUsername(databaseUsername.getValueAsString(), CredentialsFromUsernameOptions.builder()
                        .password(SecretValue.plainText(databasePass.getValueAsString()))
                        .build()))
                .instanceType(InstanceType.of(InstanceClass.T3, InstanceSize.MICRO))
                .multiAz(false)
                .allocatedStorage(10)
                .securityGroups(Collections.singletonList(iSecurityGroup))
                .vpcSubnets(SubnetSelection.builder()
                        .subnets(vpc.getPrivateSubnets())
                        .build())
                .build();


        StackUtils.printCfnParameter(this, "rds-endpoint", databaseInstance.getDbInstanceEndpointAddress());
        StackUtils.printCfnParameter(this, "rds-username", databaseUsername.getValueAsString());
        StackUtils.printCfnParameter(this, "rds-password", databasePass.getValueAsString());
    }
}
