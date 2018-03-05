# Proxy Integration Handler

The idea behind this project is to provide better/easier interface for your AWS Lambda endpoint handlers.
It does nice object serialization/deserialization for you and relieves the headache of using Input and Ouput streams.
It also uses AWS X-Ray internally and does some basic error handling for you.

### Installing

To be defined when published into a public repo

## Example usage

```
public class ExampleHandler extends ProxyRequestHandler<User, QueryStringType> {

    static final Logger log = LogManager.getLogger(AddPreRegUser.class);
    private final AmazonDynamoDB dynamoDBClient = AmazonDynamoDBClientBuilder.standard().build();

    @Nonnull
    @Override
    public ApiGatewayResponse handle(@Nonnull final ProxyGatewayRequest<User, Object> request,
                                     @Nullable final Context context,
                                     @Nonnull final Segment segment) {
        //You can access request body or query string as POJO
        User user = request.getBody(); 
        if (user.getEmail() == null) {
            final String msg = "email can not be empty";
            log.warn("Bad request");
            return badRequest(new ApiError("https://github.com/eMarketeerSE/user-service", msg));
            //Any object can be passed as response body
        }
        ...
        //There are also some nice methods for typical responses
        return ok(user);
    }

    @Nonnull
    @Override
    public Type bodyType() {
        return User.class;
    }

    @Nonnull
    @Override
    public Type queryStringType() {
        return QueryStringType.class;
    }
}
```

## Authors

See the list of [contributors](https://github.com/your/project/contributors) who participated in this project.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details
