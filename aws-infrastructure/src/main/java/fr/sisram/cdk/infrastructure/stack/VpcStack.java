package fr.sisram.cdk.infrastructure.stack;

import lombok.Getter;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.ec2.Vpc;
import software.constructs.Construct;
@Getter
public class VpcStack extends Stack {

    private Vpc vpc;

    public VpcStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);
        vpc = Vpc.Builder.create(this, "VPC")
                .vpcName("VPC")
                .maxAzs(4)
                .build();
    }

}