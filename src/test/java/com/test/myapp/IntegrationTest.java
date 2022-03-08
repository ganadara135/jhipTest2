package com.test.myapp;

import com.test.myapp.JhipTest2App;
import com.test.myapp.MongoDbTestContainerExtension;
import com.test.myapp.RedisTestContainerExtension;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = JhipTest2App.class)
@ExtendWith(RedisTestContainerExtension.class)
@ExtendWith(MongoDbTestContainerExtension.class)
public @interface IntegrationTest {
}
