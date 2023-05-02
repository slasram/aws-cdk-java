package fr.sisram.cdk.infrastructure.stack.spi;

import software.amazon.awscdk.Duration;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.services.apigateway.*;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.FunctionProps;
import software.amazon.awscdk.services.lambda.Runtime;
import software.constructs.Construct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class LambdaStack extends Stack {

    public LambdaStack(final Construct parent, final String name) {
        super(parent, name);

        Map<String, String> lambdaEnvMap = new HashMap<>();

        Function streamLambdaHandler = new Function(this, "getOneItemFunction",
                getLambdaFunctionProps(lambdaEnvMap, "my.service.StreamLambdaHandler",
                        "", 30, 1024));

        RestApi api = new RestApi(this, "itemsApi",
                RestApiProps.builder().restApiName("Items Service").build());
        api.
        IResource items = api.getRoot().addResource("v1");

        Integration getAllIntegration = new LambdaIntegration(streamLambdaHandler);
        items.addMethod("GET", getAllIntegration);
        items.addMethod("POST", getAllIntegration);
        items.addMethod("PUT", getAllIntegration);
        items.addMethod("DELETE", getAllIntegration);
        addCorsOptions(items);
    }

    private void addCorsOptions(IResource item) {
        List<MethodResponse> methoedResponses = new ArrayList<>();

        Map<String, Boolean> responseParameters = new HashMap<>();
        responseParameters.put("method.response.header.Access-Control-Allow-Headers", Boolean.TRUE);
        responseParameters.put("method.response.header.Access-Control-Allow-Methods", Boolean.TRUE);
        responseParameters.put("method.response.header.Access-Control-Allow-Credentials", Boolean.TRUE);
        responseParameters.put("method.response.header.Access-Control-Allow-Origin", Boolean.TRUE);
        methoedResponses.add(MethodResponse.builder()
                .responseParameters(responseParameters)
                .statusCode("200")
                .build());
        MethodOptions methodOptions = MethodOptions.builder()
                .methodResponses(methoedResponses)
                .build();

        Map<String, String> requestTemplate = new HashMap<>();
        requestTemplate.put("application/json", "{\"statusCode\": 200}");
        List<IntegrationResponse> integrationResponses = new ArrayList<>();

        Map<String, String> integrationResponseParameters = new HashMap<>();
        integrationResponseParameters.put("method.response.header.Access-Control-Allow-Headers", "'Content-Type,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token,X-Amz-User-Agent'");
        integrationResponseParameters.put("method.response.header.Access-Control-Allow-Origin", "'*'");
        integrationResponseParameters.put("method.response.header.Access-Control-Allow-Credentials", "'false'");
        integrationResponseParameters.put("method.response.header.Access-Control-Allow-Methods", "'OPTIONS,GET,PUT,POST,DELETE'");
        integrationResponses.add(IntegrationResponse.builder()
                .responseParameters(integrationResponseParameters)
                .statusCode("200")
                .build());
        Integration methodIntegration = MockIntegration.Builder.create()
                .integrationResponses(integrationResponses)
                .passthroughBehavior(PassthroughBehavior.NEVER)
                .requestTemplates(requestTemplate)
                .build();

        item.addMethod("OPTIONS", methodIntegration, methodOptions);
    }

    private FunctionProps getLambdaFunctionProps(Map<String, String> lambdaEnvMap,
                                                 String handler,
                                                 String codePath,
                                                 int seconds,
                                                 int memorySize) {
        return FunctionProps.builder()
                .code(Code.fromAsset("/dev_env/aws-service-lambda-springboot-0.0.1-lambda-package.zip"))
                .handler(handler)
                .runtime(Runtime.JAVA_11)
                .environment(lambdaEnvMap)
                .timeout(Duration.seconds(seconds))
                .memorySize(memorySize)
                .build();
    }

}
