package fr.sisram.cdk.infrastructure.util;

import lombok.experimental.UtilityClass;
import software.amazon.awscdk.CfnOutput;
import software.amazon.awscdk.CfnParameter;
import software.amazon.awscdk.Environment;
import software.constructs.Construct;
@UtilityClass
public class StackUtils {

    public static Environment environmentBuilder(String accountId, String region) {
        return Environment.builder() .account(accountId).region(region).build();
    }

    public static CfnParameter getCfnParameter(Construct scope, String parameterKey, String description, String type) {
        return CfnParameter.Builder.create(scope, parameterKey).type(type).description(description).build();
    }

    public static CfnOutput printCfnParameter(Construct scope, String exportName, String value) {
        return CfnOutput.Builder.create(scope, exportName).exportName(exportName).value(value).build();
    }

}
