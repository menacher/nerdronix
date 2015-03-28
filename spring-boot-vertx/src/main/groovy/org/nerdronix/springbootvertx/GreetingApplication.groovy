package org.nerdronix.springbootvertx

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.PropertySource
import org.springframework.core.env.Environment
import org.vertx.groovy.core.Vertx
import org.vertx.groovy.core.eventbus.EventBus
import org.vertx.groovy.core.http.HttpClient
import org.vertx.java.spi.cluster.impl.hazelcast.HazelcastClusterManagerFactory

@SpringBootApplication
@PropertySource(value="classpath:vertx.properties")
class GreetingApplication {

	public static void main(String... args) {
		System.setProperty("vertx.clusterManagerFactory", HazelcastClusterManagerFactory.class.getCanonicalName())
		SpringApplication.run(GreetingApplication, args)
	}
	
	private static final String VERTX_HOSTNAME = "vertx.hostname";

	@Autowired
	Environment environment

	@Bean
	public Vertx vertx(){
		Vertx vertx = Vertx.newVertx(environment.getProperty(VERTX_HOSTNAME));
	}

	@Bean
	public EventBus eventBus() {
		return vertx().getEventBus();
	}

	@Bean
	public HttpClient httpClient() {
		vertx().createHttpClient(host:'jsonplaceholder.typicode.com', keepAlive: true);
	}

}
