package hello;

import io.baratine.service.*;
import io.baratine.vault.*;
import io.baratine.web.*;

@Asset
@Service
public class Hello
{
  private String greeting = "Hello, world";

  @Get
  public void hello(Result<Hello> result)
  {
    result.ok(this);
  }

  @Post
  @Modify
  public void update(@Body Hello hello, Result<String> result)
  {
    this.greeting = hello.greeting;

    result.ok("ok");
  }

  public static void main(String []args)
  {
    Web.include(Hello.class);

    Web.go(args);
  }
}