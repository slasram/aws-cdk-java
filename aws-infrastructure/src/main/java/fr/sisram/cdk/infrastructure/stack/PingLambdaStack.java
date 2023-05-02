package fr.sisram.cdk.infrastructure.stack;

import fr.sisram.cdk.infrastructure.stack.spi.LambdaStack;
import software.constructs.Construct;

public class PingLambdaStack extends LambdaStack {
    public PingLambdaStack(Construct parent, String name) {
        super(parent, name);
    }
}
