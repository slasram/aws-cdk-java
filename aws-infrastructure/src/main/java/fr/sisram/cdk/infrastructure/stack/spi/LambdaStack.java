package fr.sisram.cdk.infrastructure.stack.spi;

import software.amazon.awscdk.Duration;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.services.apigateway.*;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.FunctionProps;
import software.amazon.awscdk.services.lambda.Runtime;
import software.constructs.Construct;

import java.util.*;

public abstract class LambdaStack extends Stack {

    LambdaRestApi lambdaRestApi;

    public LambdaStack(final Construct parent, final String name) {
        super(parent, name);

        Map<String, String> lambdaEnvMap = new HashMap<>();

        Function streamLambdaHandler = new Function(this, "pets",
                getLambdaFunctionProps(lambdaEnvMap, "my.service.StreamLambdaHandler",
                        "", 30, 1024));

        lambdaRestApi = LambdaRestApi.Builder.create(this, "lambda-pets-2").handler(streamLambdaHandler)
                .integrationOptions(LambdaIntegrationOptions.builder()
                        .contentHandling(ContentHandling.CONVERT_TO_TEXT)

                        .passthroughBehavior(PassthroughBehavior.WHEN_NO_MATCH).build())
                .build();


        RestApi restApi = RestApi.Builder.create(this, "rest-api-test-2").build();

        addLambdaAsResource(restApi, "hello");
    }

    public void addLambdaAsResource(RestApi restApi, String path) {

        IResource lambdaResource = restApi.getRoot().addResource(path);
        Method method = lambdaResource.addMethod("ANY", HttpIntegration.Builder.create(lambdaRestApi.getUrl()).proxy(false).httpMethod("ANY").build());
        method.getResource().addProxy(ProxyResourceOptions.builder()
                .defaultMethodOptions(MethodOptions.builder()

                        .requestParameters(new HashMap<String, Boolean>() {{
                    put("method.request.path.proxy", true);
                }}).build())
                .defaultIntegration(HttpIntegration.Builder.create(lambdaRestApi.getUrl() + "{proxy}").httpMethod("ANY")
                        .options(IntegrationOptions.builder().cacheKeyParameters(Arrays.asList("method.request.path.proxy"))

                                .requestParameters(new HashMap<String, String>() {{
                                    put("integration.request.path.proxy", "method.request.path.proxy");
                                }}).build()).proxy(true).build())
                .build());
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

    public LambdaRestApi getLambdaRestApi() {
        return this.lambdaRestApi;
    }

}
